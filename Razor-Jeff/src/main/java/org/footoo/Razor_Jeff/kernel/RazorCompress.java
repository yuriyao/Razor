/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.Razor_Jeff.kernel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.footoo.Razor_Jeff.exception.RazorException;
import org.footoo.Razor_Jeff.exception.RazorStandError;
import org.footoo.Razor_Jeff.file.FileStreamOperator;
import org.footoo.Razor_Jeff.kernel.callbacks.CompressProcessListener;
import org.footoo.Razor_Jeff.kernel.callbacks.CompressStateListener;
import org.footoo.common.compress.Compress;
import org.footoo.common.compress.DoDoCompress;
import org.footoo.common.compress.InvalidCompressedDataException;
import org.footoo.common.exception.DirectoryCannotCreateException;
import org.footoo.common.log.Logger;
import org.footoo.common.log.LoggerFactory;
import org.footoo.common.tools.ByteUtil;
import org.footoo.common.tools.SystemInfoTool;

/**
 * 压缩工具的核心工具<br>
 * 进行数据的压缩
 * 
 * @author jeff
 * @version $Id: RazorCompress.java, v 0.1 2014年6月9日 下午3:54:31 jeff Exp $
 */
public abstract class RazorCompress implements CompressProcessListener, CompressStateListener {
    /** 日志工具 */
    private static final Logger logger             = LoggerFactory.getLogger(RazorCompress.class);
    /** 魔数 */
    private static final short  MAGIC_NUMBER       = ByteUtil.toShort("je".getBytes(), 0);
    /** 版本号(0.5=500, 1.0=1000, 1.12=1120....) */
    private static final int    VERSION            = 500;
    /** 最大的文件的大小 */
    private static final int    MAX_FILE_NAME_SIZE = 1024;

    /**
     * 将多个文件压缩成一个文件
     * 
     * @param files 文件列表
     * @param targetFileName 目标文件名
     * @throws RazorException 
     */
    public void compress(List<File> files, String targetFileName) throws RazorException {
        //开始压缩
        this.stateChanged(CompressStateListener.COMPRESS_START);
        //压缩文件列表不能为空
        if (files.isEmpty()) {
            this.stateChanged(CompressStateListener.COMPRESS_FAIL);
            throw new RazorException(RazorStandError.EMPTY_FILES);
        }

        //创建文件树
        this.stateChanged(CompressStateListener.SEARCHING_FILES);
        FileEntry root = establishFileTree(files);
        //统计文件的数量
        int normalFileNumber = computeNumber(root);
        logger.info(normalFileNumber);
        //开始进入压缩阶段
        //创建输出文件
        RandomAccessFile output = null;
        try {
            output = new RandomAccessFile(targetFileName, "rw");
        } catch (FileNotFoundException e) {
            logger.error(e, "创建输出文件失败");
            this.stateChanged(CompressStateListener.COMPRESS_FAIL);
            throw new RazorException(RazorStandError.CREATE_FILE_FAILED, e.getMessage());
        }
        try {
            //正在压缩
            this.stateChanged(CompressStateListener.COMPRESSING);
            //初始化文件系统
            initFS(output);
            //
            //dumpFiles(root);
            //压缩文件
            compressFiles(root, output, normalFileNumber);
            //写入文件信息
            //获取文件信息的写入的位置
            long offset = writeFileInfo(root.getInternelChild(), output);
            //写入第一个节点的位置
            output.writeLong(offset);
            logger.info("最后一个节点的位置:" + offset);
        } catch (RazorException e) {
            this.stateChanged(CompressStateListener.COMPRESS_FAIL);
            throw e;
        } catch (IOException e) {
            logger.error(e, "写入数据失败");
            throw new RazorException(RazorStandError.CANNOT_WRITE_FILE);
        } finally {
            close(output);
        }

    }

    /**
     * 将压缩文件解压到解压目录
     * 
     * @param compressedFileName
     * @param uncompressedDir
     * @throws RazorException 
     */
    public void uncompress(String compressedFileName, String uncompressedDir) throws RazorException {
        RandomAccessFile input = null;
        try {
            //打开文件
            input = new RandomAccessFile(compressedFileName, "r");
            //检查文件系统
            checkFS(input);
            //获取文件的大小
            long fileLength = input.length();
            //
            input.seek(fileLength - 8);
            //获取文件信息的开始节点
            long begin = input.readLong();
            //加载文件信息
            FileEntry fileEntry = loadFileInfos(input, begin);
            //创建root
            FileEntry root = new FileEntry(new File("/"));
            root.setInternelChild(fileEntry);
            //
            //dumpFiles(root);
            //生成完整的路进
            generateFullName(root, uncompressedDir);
            //解压文件
            //创建解压的根目录
            SystemInfoTool.createDirIfNotExist(uncompressedDir);
            uncompressFiles(root, input);
        } catch (FileNotFoundException e) {
            logger.error(e, "无法打开文件");
            throw new RazorException(RazorStandError.FILE_NOT_EXISTS, e.getMessage());
        } catch (IOException e) {
            logger.error("", e);
            throw new RazorException(RazorStandError.OPERATE_FILE_FAILED, e.getMessage());
        } catch (DirectoryCannotCreateException e) {
            logger.error(e, "创建目录失败");
            throw new RazorException(RazorStandError.CANNOT_CREATE_DIR);
        } finally {
            //关闭文件
            if (input != null) {
                close(input);
            }
        }
    }

    /**
     * 关闭文件
     * 
     * @param file
     */
    private void close(RandomAccessFile file) {
        try {
            file.close();
        } catch (IOException e) {
            logger.error(e, "关闭文件失败");
        }
    }

    /**
     * 检查是否是合法的文件
     * 
     * @param input
     * @throws RazorException 
     */
    private void checkFS(RandomAccessFile input) throws RazorException {
        try {
            input.seek(0);
            short magicNumber = input.readShort();
            int version = input.readInt();
            if (magicNumber != MAGIC_NUMBER || version != VERSION) {
                throw new RazorException(RazorStandError.INVALID_COMPRESSED_FILE);
            }
        } catch (IOException e) {
            logger.error(e, "操作文件发生异常");
            throw new RazorException(RazorStandError.OPERATE_FILE_FAILED);
        }
    }

    /**
     * 加载所有的文件信息
     * 
     * @param input
     * @param begin
     * @return
     * @throws RazorException
     */
    private FileEntry loadFileInfos(RandomAccessFile input, long begin) throws RazorException {
        try {
            input.seek(begin);

            //加载当前的FileEntry
            FileEntry fileEntry = loadFileEntry(input);
            //加载兄弟节点
            FileEntry slibingEntry = null;
            if (fileEntry.getSlibing() >= 0) {
                slibingEntry = loadFileInfos(input, fileEntry.getSlibing());
            }
            fileEntry.setInternelSlibing(slibingEntry);
            //加载孩子节点
            if (!fileEntry.isNormalFile()) {
                FileEntry childEntry = null;
                if (fileEntry.getChild() >= 0) {
                    childEntry = loadFileInfos(input, fileEntry.getChild());
                }
                fileEntry.setInternelChild(childEntry);
            }
            return fileEntry;

        } catch (IOException e) {
            logger.error(e, "操作文件发生异常");
            throw new RazorException(RazorStandError.OPERATE_FILE_FAILED, e.getMessage());
        }

    }

    /**
     * 为每一个节点生成完整的路径
     * 
     * @param root
     * @param baseDir
     */
    private void generateFullName(FileEntry root, String baseDir) {
        root.setFullFileName(baseDir);
        FileEntry parent = root;
        //广度遍历
        Queue<FileEntry> fileEntries = new LinkedList<>();
        fileEntries.add(parent);
        while (!fileEntries.isEmpty()) {
            parent = fileEntries.remove();
            FileEntry child = parent.getInternelChild();
            while (child != null) {
                //设置完整的路径名
                child.setFullFileName(SystemInfoTool.getPath(parent.getFullFileName(), new String(
                    child.getFileName())));
                fileEntries.add(child);
                child = child.getInternelSlibing();
            }
        }
    }

    /**
     * 加载input的当前位置的fileEntry
     * 
     * @param input
     * @return
     * @throws RazorException
     */
    private FileEntry loadFileEntry(RandomAccessFile input) throws RazorException {

        try {
            //读取文件名的长度
            int fileNameLen = input.readInt();
            if (fileNameLen < 0 || fileNameLen > MAX_FILE_NAME_SIZE) {
                logger.error("获得长度为[" + fileNameLen + "]的非法文件名");
                throw new RazorException(RazorStandError.INVALID_COMPRESSED_FILE);
            }
            //读取文件名
            byte[] bytes = new byte[fileNameLen];
            input.readFully(bytes);
            //读取Flag
            int flag = input.readInt();
            //读取兄弟文件的偏移
            long slibing = input.readLong();
            //读取子文件偏移
            long child = input.readLong();
            //创建FileEntry
            FileEntry fileEntry = new FileEntry(new File(new String(bytes)));
            fileEntry.setFlag(flag);
            fileEntry.setSlibing(slibing);
            fileEntry.setChild(child);
            //
            return fileEntry;
        } catch (IOException e) {
            logger.error(e, "");
            throw new RazorException(RazorStandError.OPERATE_FILE_FAILED, e.getMessage());
        }
    }

    /**
     * 获取文件指针的位置
     * 
     * @param output
     * @return
     * @throws RazorException
     */
    private long getFilePointer(RandomAccessFile output) throws RazorException {

        try {
            return output.getFilePointer();
        } catch (IOException e) {
            logger.error(e, "无法定位文件的偏移");

            throw new RazorException(RazorStandError.CANNOT_LOCATE_FILE_OFFSET);
        }
    }

    /**
     * 写入文件的信息<br>
     * 实际是一个后序遍历
     * 
     * @param fileEntry
     * @param offset
     * @return
     * @throws RazorException
     */
    private long writeFileInfo(FileEntry fileEntry, RandomAccessFile output) throws RazorException {
        //写入孩子节点
        if (fileEntry == null) {
            return -1;
        }
        //写入孩子节点
        FileEntry child = fileEntry.getInternelChild();
        if (child != null) {
            fileEntry.setChild(writeFileInfo(child, output));
        }
        //写入兄弟节点
        fileEntry.setSlibing(writeFileInfo(fileEntry.getInternelSlibing(), output));
        //获取当前节点的写入位置
        long offset = getFilePointer(output);
        //写入当前的节点
        try {
            output.write(fileEntry.toBytes());
        } catch (IOException e) {
            logger.error(e, "写入文件信息失败");
            throw new RazorException(RazorStandError.CANNOT_WRITE_FILE);
        }
        return offset;
    }

    /**
     * 计算普通文件的数量
     * 
     * @param root
     * @return
     * @throws RazorException 
     */
    private int computeNumber(FileEntry root) throws RazorException {
        ComputeNormalFilesNumber computeNormalFilesNumber = new ComputeNormalFilesNumber();
        wideSearch(root, computeNormalFilesNumber);
        return computeNormalFilesNumber.getNumber();
    }

    /**
     * 压缩所有的文件
     * 
     * @param root
     * @param output
     * @throws RazorException
     */
    private void compressFiles(FileEntry root, RandomAccessFile output, int totalNumber)
                                                                                        throws RazorException {
        wideSearch(root, new CompressFile(output, totalNumber));
    }

    /**
     * 解压所有的文件
     * 
     * @author jeff
     * @version $Id: RazorCompress.java, v 0.1 2014年6月10日 下午9:42:51 jeff Exp $
     */
    private class UncompressFiles implements WideSearchCallBack {
        /** 输入文件 */
        private RandomAccessFile in;

        public UncompressFiles(RandomAccessFile in) {
            this.in = in;
        }

        @Override
        public void findOne(FileEntry fileEntry) throws RazorException {
            //是目录
            if (!fileEntry.isNormalFile()) {
                try {
                    SystemInfoTool.createDirIfNotExist(fileEntry.getFullFileName());
                } catch (DirectoryCannotCreateException e) {
                    logger.error(e, "无法创建目录");
                    throw new RazorException(RazorStandError.CANNOT_CREATE_DIR);
                }
            }//是文件
            else {
                long child = fileEntry.getChild();
                if (child < 0) {
                    throw new RazorException(RazorStandError.INVALID_COMPRESSED_FILE);
                }
                //进行解压
                uncompress(in, child, fileEntry.getFullFileName());
            }
        }
    }

    /**
     * 解压所有文件
     * 
     * @param root
     * @param in
     * @throws RazorException
     */
    private void uncompressFiles(FileEntry root, RandomAccessFile in) throws RazorException {
        wideSearch(root, new UncompressFiles(in));
    }

    /**
     * 解压数据
     * 
     * @param input
     * @param begin
     * @param targetFileName
     * @throws RazorException
     */
    @SuppressWarnings("resource")
    private void uncompress(RandomAccessFile input, long begin, String targetFileName)
                                                                                      throws RazorException {
        Compress compress = new DoDoCompress();
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(targetFileName);
            input.seek(begin);
            int len = input.readInt();
            while (len > 0) {
                if (len > DoDoCompress.FRAGMENT_LENGTH * 2) {
                    logger.error("读到过长的压缩数据块:[" + len + "]");
                    throw new RazorException(RazorStandError.INVALID_COMPRESSED_FILE);
                }
                byte[] bytes = new byte[len];
                //读取数据
                input.readFully(bytes);
                try {
                    byte[] result = compress.uncompress(bytes);
                    outputStream.write(result);
                } catch (InvalidCompressedDataException e) {
                    logger.error(e, "解压失败");
                    throw new RazorException(RazorStandError.INVALID_COMPRESSED_FILE);
                }
                //读取下一块
                len = input.readInt();
            }
        } catch (IOException e) {
            logger.error("", e);
            throw new RazorException(RazorStandError.OPERATE_FILE_FAILED, e.getMessage());
        } finally {
            if (outputStream != null) {
                close(outputStream);
            }
        }
    }

    /**
     * 关闭输出流
     * 
     * @param out
     */
    private void close(OutputStream out) {
        try {
            out.close();
        } catch (IOException e) {
            logger.error(e, "关闭输出流失败");
        }
    }

    /**
     * 计算文件数量的回调函数
     * 
     * @author jeff
     * @version $Id: RazorCompress.java, v 0.1 2014年6月10日 下午6:02:58 jeff Exp $
     */
    private static class ComputeNormalFilesNumber implements WideSearchCallBack {
        /** 普通文件的数量 */
        private int number = 0;

        public ComputeNormalFilesNumber() {

        }

        @Override
        public void findOne(FileEntry fileEntry) {
            if (!fileEntry.getFile().isDirectory()) {
                number++;
            }
        }

        /**
         * Getter method for property <tt>number</tt>.
         * 
         * @return property value of number 0000 009f
         */
        public final int getNumber() {
            return number;
        }

    }

    /**
     * 进行文件的压缩
     * 
     * @author jeff
     * @version $Id: RazorCompress.java, v 0.1 2014年6月10日 下午6:09:57 jeff Exp $
     */
    private class CompressFile implements WideSearchCallBack {
        /** 输出文件 */
        private RandomAccessFile output;
        /** 处理进度 */
        private int              process;
        /** 总文件的数量 */
        private int              totalNumber;
        /** 处理的数量 */
        private int              number;

        public CompressFile(RandomAccessFile output, int totalNumber) {
            this.output = output;
            this.totalNumber = totalNumber;
            //避免被0除
            if (totalNumber == 0) {
                this.totalNumber = 1;
            }
        }

        @Override
        public void findOne(FileEntry fileEntry) throws RazorException {
            if (!fileEntry.getFile().isDirectory()) {
                //压缩文件
                compressFile(fileEntry, output);

                //跟踪文件的压缩进度
                number++;
                int newProcess = number * 100 / totalNumber;
                if (newProcess != process) {
                    processChanged(newProcess);
                    process = newProcess;
                }
            }
        }
    }

    /**
     * 用于调试，遍历所有的文件信息
     * 
     * @author jeff
     * @version $Id: RazorCompress.java, v 0.1 2014年6月10日 下午6:23:47 jeff Exp $
     */
    private class DumpFiles implements WideSearchCallBack {

        @Override
        public void findOne(FileEntry fileEntry) throws RazorException {
            logger.info(fileEntry.getFile().getName());
        }
    }

    /**
     * 遍历文件信息
     * 
     * @param root
     * @throws RazorException
     */
    @SuppressWarnings("unused")
    private void dumpFiles(FileEntry root) throws RazorException {
        wideSearch(root, new DumpFiles());
    }

    /**
     * 进行广度优先遍历
     * 
     * @param root
     * @param callBack
     * @throws RazorException 
     */
    private void wideSearch(FileEntry root, WideSearchCallBack callBack) throws RazorException {
        Queue<FileEntry> fileEntries = new LinkedList<>();
        addChild(root, fileEntries);
        while (!fileEntries.isEmpty()) {
            FileEntry entry = fileEntries.remove();
            callBack.findOne(entry);
            addChild(entry, fileEntries);
        }
    }

    /**
     * 向fileEntries中添加广度优先遍历获取的节点
     * 
     * @param parent
     * @param fileEntries
     */
    private void addChild(FileEntry parent, Queue<FileEntry> fileEntries) {
        FileEntry child = parent.getInternelChild();
        while (child != null) {
            fileEntries.add(child);
            child = child.getInternelSlibing();
        }
    }

    /**
     * 广度优先遍历的回调函数
     * 
     * @author jeff
     * @version $Id: RazorCompress.java, v 0.1 2014年6月10日 下午5:47:28 jeff Exp $
     */
    private static interface WideSearchCallBack {
        /**
         * 发现一个节点
         * 
         * @param fileEntry
         * @throws RazorException 
         */
        public void findOne(FileEntry fileEntry) throws RazorException;
    }

    /**
     * 压缩一个文件
     * 
     * @param fileEntry
     * @param output
     * @throws RazorException
     */
    private void compressFile(FileEntry fileEntry, RandomAccessFile output) throws RazorException {
        File file = fileEntry.getFile();
        long offset = 0;
        assert (!file.isDirectory());
        Compress compress = new DoDoCompress();
        //文件不存在
        if (!file.exists()) {
            logger.error("文件[" + file.getAbsolutePath() + "]不存在");
            throw new RazorException(RazorStandError.FILE_NOT_EXISTS);
        }
        //打开文件
        InputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            logger.error(e, "文件[" + file.getAbsolutePath() + "]不存在");
            throw new RazorException(RazorStandError.FILE_NOT_EXISTS);
        }
        //获取写入文件的偏移
        try {
            offset = output.getFilePointer();
        } catch (IOException e) {
            logger.error(e, "无法定位文件的偏移");
            //
            close(in);
            throw new RazorException(RazorStandError.CANNOT_LOCATE_FILE_OFFSET);

        }
        //压缩数据块
        try {
            while (true) {
                byte[] buffer = FileStreamOperator.readBytes(in, DoDoCompress.FRAGMENT_LENGTH);
                //写完了数据
                if (buffer == null || buffer.length == 0) {
                    output.writeInt(-1);
                    fileEntry.setChild(offset);
                    break;
                }
                byte[] result = compress.compress(buffer);
                //logger.info("压缩后的大小:" + result.length);
                //写入数据的长度
                output.writeInt(result.length);
                //写入压缩数据
                output.write(result);
                //没有读到满格的数据，说明已经读完
                if (buffer.length < DoDoCompress.FRAGMENT_LENGTH) {
                    output.writeInt(-1);
                    fileEntry.setChild(offset);
                    break;
                }
            }
        } catch (RazorException e) {
            throw e;
        } catch (IOException e) {
            logger.error(e, "输出数据发生异常");
            throw new RazorException(RazorStandError.CANNOT_WRITE_FILE, e.getMessage());
        } finally {
            close(in);
        }

    }

    /**
     * 关闭输入流
     * 
     * @param in
     */
    private void close(InputStream in) {
        try {
            in.close();
        } catch (IOException e) {
            logger.error(e, "关闭文件发生异常");
        }
    }

    /**
     * 初始化文件系统
     * 
     * @param output
     * @throws RazorException 
     */
    private void initFS(RandomAccessFile output) throws RazorException {

        try {
            //写入魔数
            output.writeShort(MAGIC_NUMBER);
            //写入版本号
            output.writeInt(VERSION);
        } catch (IOException e) {
            logger.error(e, "输出数据发生异常");
            throw new RazorException(RazorStandError.CANNOT_WRITE_FILE, e.getMessage());
        }
    }

    /**
     * 创建文件树<br>
     * 采用广度优先的搜索方法
     * 
     * @param files
     */
    private FileEntry establishFileTree(List<File> files) {
        //这个只是
        FileEntry root = new FileEntry(new File("/"));
        Queue<SearchEntry> entrys = new LinkedList<>();

        //
        root.setInternelChild(establishBrother(files));

        FileEntry tmp = root.getInternelChild();
        //将文件列表加入队列
        for (File file : files) {
            if (file.isDirectory()) {
                entrys.add(new SearchEntry(tmp, file));
                //tmp.setFlag(flag);
            } else {
                tmp.setFlag(FileEntry.NORMAL_FILE);
            }
            tmp = tmp.getInternelSlibing();
        }
        //进行广度优先的遍历
        while (!entrys.isEmpty()) {
            SearchEntry head = entrys.remove();
            File[] childs = head.getFile().listFiles();
            //空文件夹
            if (childs == null || childs.length == 0) {
                continue;
            }
            //生成文件列表
            files = new ArrayList<>();
            for (File file : childs) {
                files.add(file);
            }
            //
            head.getFileEntry().setInternelChild(establishBrother(files));
            tmp = head.getFileEntry().getInternelChild();
            for (File file : childs) {
                if (file.isDirectory()) {
                    entrys.add(new SearchEntry(tmp, file));
                } else {
                    tmp.setFlag(FileEntry.NORMAL_FILE);
                }

                tmp = tmp.getInternelSlibing();
            }
        }

        return root;
    }

    /**
     * 创建兄弟链表，并且返回第一个兄弟
     * 
     * @param files
     * @return
     */
    private FileEntry establishBrother(List<File> files) {
        if (files.isEmpty()) {
            return null;
        }
        FileEntry firstEntry = new FileEntry(files.get(0));
        FileEntry tmp = firstEntry;

        for (int i = 1; i < files.size(); i++) {
            FileEntry entry = new FileEntry(files.get(i));
            tmp.setInternelSlibing(entry);
            tmp = entry;
        }
        return firstEntry;
    }

    private static class SearchEntry {
        /** 文件项 */
        private FileEntry fileEntry;
        /** 文件 */
        private File      file;

        @SuppressWarnings("unused")
        public SearchEntry() {

        }

        public SearchEntry(FileEntry fileEntry, File file) {
            this.file = file;
            this.fileEntry = fileEntry;
        }

        /**
         * Getter method for property <tt>fileEntry</tt>.
         * 
         * @return property value of fileEntry
         */
        public final FileEntry getFileEntry() {
            return fileEntry;
        }

        /**
         * Setter method for property <tt>fileEntry</tt>.
         * 
         * @param fileEntry value to be assigned to property fileEntry
         */
        @SuppressWarnings("unused")
        public final void setFileEntry(FileEntry fileEntry) {
            this.fileEntry = fileEntry;
        }

        /**
         * Getter method for property <tt>file</tt>.
         * 
         * @return property value of file
         */
        public final File getFile() {
            return file;
        }

        /**
         * Setter method for property <tt>file</tt>.
         * 
         * @param file value to be assigned to property file
         */
        @SuppressWarnings("unused")
        public final void setFile(File file) {
            this.file = file;
        }

    }
}
