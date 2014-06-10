/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.Razor_Jeff.kernel;

import java.io.File;

import org.footoo.common.buffer.ByteBuffer;
import org.footoo.common.tools.ByteUtil;

/**
 * 每一个目录项<br>
 * 完成文件系统的数据的序列化
 * 
 * @author jeff
 * @version $Id: FileEntry.java, v 0.1 2014年6月10日 上午10:12:48 jeff Exp $
 */
public class FileEntry {
    /** 普通文件 */
    public static final int NORMAL_FILE     = 0x01;

    /** 文件的标志信息 */
    private int             flag            = 0;
    /** 文件名 */
    private byte[]          fileName;
    /** 下一个兄弟文件的偏移 */
    private long            slibing         = -1;
    /** 子文件或者文件内容的偏移 */
    private long            child           = -1;
    /** 下一个兄弟节点,仅用于系统内部 */
    private FileEntry       internelSlibing = null;
    /** 子文件，对于普通文件，该项为NULL */
    private FileEntry       internelChild   = null;
    /** 对应的原文件 */
    private File            file;
    /** 完整的名称,只有解压的时候会用到 */
    private String          fullFileName;

    public FileEntry() {

    }

    public FileEntry(File file) {
        this.fileName = file.getName().getBytes();
        this.file = file;
    }

    /**
     * 计算文件项序列化后的长度
     * <ul>
     *  <li>文件名的长度信息的长度（int，4字节）</li>
     *  <li>文件名的长度，即fileName的长度</li>
     *  <li>文件的标志信息的长度（int，4字节）</li>
     *  <li>下一个兄弟文件的偏移(int, 8字节)</li>
     *  <li>子文件的偏移(int, 8字节)</li>
     * </ul>
     * 
     * @return
     */
    public int sizeof() {
        return fileName.length + 24;
    }

    /**
     * 检测当前文件是否是一般的文件
     * 
     * @return
     */
    public boolean isNormalFile() {
        return (flag & NORMAL_FILE) != 0;
    }

    /**
     * 文件项序列化
     * 
     * @return
     */
    public byte[] toBytes() {
        ByteBuffer byteBuffer = new ByteBuffer();
        //文件名的长度
        byteBuffer.append(ByteUtil.toBytes(fileName.length));
        //文件名 
        byteBuffer.append(fileName);
        //Flag
        byteBuffer.append(ByteUtil.toBytes(flag));
        //兄弟文件的偏移
        byteBuffer.append(ByteUtil.toBytes(slibing));
        //子文件的偏移
        byteBuffer.append(ByteUtil.toBytes(child));

        return byteBuffer.toByteArray();
    }

    /**
     * Getter method for property <tt>flag</tt>.
     * 
     * @return property value of flag
     */
    public final int getFlag() {
        return flag;
    }

    /**
     * Setter method for property <tt>flag</tt>.
     * 
     * @param flag value to be assigned to property flag
     */
    public final void setFlag(int flag) {
        this.flag = flag;
    }

    /**
     * Getter method for property <tt>slibing</tt>.
     * 
     * @return property value of slibing
     */
    public final long getSlibing() {
        return slibing;
    }

    /**
     * Setter method for property <tt>slibing</tt>.
     * 
     * @param slibing value to be assigned to property slibing
     */
    public final void setSlibing(long slibing) {
        this.slibing = slibing;
    }

    /**
     * Getter method for property <tt>child</tt>.
     * 
     * @return property value of child
     */
    public final long getChild() {
        return child;
    }

    /**
     * Setter method for property <tt>child</tt>.
     * 
     * @param child value to be assigned to property child
     */
    public final void setChild(long child) {
        this.child = child;
    }

    /**
     * Getter method for property <tt>fileName</tt>.
     * 
     * @return property value of fileName
     */
    public final byte[] getFileName() {
        return fileName;
    }

    /**
     * Getter method for property <tt>internelSlibing</tt>.
     * 
     * @return property value of internelSlibing
     */
    public final FileEntry getInternelSlibing() {
        return internelSlibing;
    }

    /**
     * Setter method for property <tt>internelSlibing</tt>.
     * 
     * @param internelSlibing value to be assigned to property internelSlibing
     */
    public final void setInternelSlibing(FileEntry internelSlibing) {
        this.internelSlibing = internelSlibing;
    }

    /**
     * Getter method for property <tt>internelChild</tt>.
     * 
     * @return property value of internelChild
     */
    public final FileEntry getInternelChild() {
        return internelChild;
    }

    /**
     * Setter method for property <tt>internelChild</tt>.
     * 
     * @param internelChild value to be assigned to property internelChild
     */
    public final void setInternelChild(FileEntry internelChild) {
        this.internelChild = internelChild;
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
    public final void setFile(File file) {
        this.file = file;
    }

    /**
     * Setter method for property <tt>fileName</tt>.
     * 
     * @param fileName value to be assigned to property fileName
     */
    public final void setFileName(byte[] fileName) {
        this.fileName = fileName;
    }

    /**
     * Getter method for property <tt>fullFileName</tt>.
     * 
     * @return property value of fullFileName
     */
    public final String getFullFileName() {
        return fullFileName;
    }

    /**
     * Setter method for property <tt>fullFileName</tt>.
     * 
     * @param fullFileName value to be assigned to property fullFileName
     */
    public final void setFullFileName(String fullFileName) {
        this.fullFileName = fullFileName;
    }

}
