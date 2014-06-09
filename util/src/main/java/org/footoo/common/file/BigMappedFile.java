/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.file;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.footoo.common.exception.InvalidMappedFileException;
import org.footoo.common.log.Logger;
import org.footoo.common.log.LoggerFactory;

/**
 * 一个逻辑上巨大的文件，由多个MappedFile组成，并且所有的mappped在逻辑上必须是连续的
 * 所有的MappedFile必须是同样大的
 * 
 * @author jeff
 * @version $Id: BigMappedFile.java, v 0.1 2014年3月4日 下午5:21:57 jeff Exp $
 */
public class BigMappedFile {
    /** 所有文件 */
    private final List<SortableMappedFile<? extends SortableMappedFile<?>>> files          = new ArrayList<>();

    /** 逻辑文件的开始偏移 */
    private long                                                            startOffset    = 0;

    /** 读写锁 */
    private final ReadWriteLock                                             lock           = new ReentrantReadWriteLock();

    /** 日志 */
    private Logger                                                          logger         = LoggerFactory
                                                                                               .getLogger(BigMappedFile.class);

    /** 每一个mappedFile的大小 */
    private long                                                            mappedFileSize = 0;

    /**
     * 
     */
    public BigMappedFile() {
    }

    /**
     * 添加mappedFile
     * 
     * @param file
     * @throws InvalidMappedFileException 
     */
    public void addMappedFile(SortableMappedFile<? extends SortableMappedFile<?>> file)
                                                                                       throws InvalidMappedFileException {
        try {
            if (!fileIsValid(file)) {
                throw new InvalidMappedFileException("无法添加file,因为每一个mappedFile的大小必须相同");
            }
            lock.writeLock().lock();
            files.add(file);
            //Collections.sort;
        } catch (InvalidMappedFileException e) {
            throw e;
        } catch (Exception e) {
            logger.warn(e, "添加文件发生异常");
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 验证文件属性是否合法
     * 
     * @param file
     * @return
     */
    private boolean fileIsValid(SortableMappedFile<? extends SortableMappedFile<?>> file) {
        try {
            lock.writeLock().lock();
            if (!files.isEmpty()) {
                this.mappedFileSize = files.get(0).getSize();
            } else {
                this.mappedFileSize = file.getSize();
            }
            if (file.getSize() != this.mappedFileSize) {
                return false;
            }
            return true;

        } catch (Exception e) {

            logger.warn(e);
            return false;
        } finally {
            lock.writeLock().unlock();
        }

    }

    /**
     * 根据偏移获得相应的MappedFile
     * 
     * @param offset
     * @return 如果超出最大或者小于最小偏移，返回null
     */
    public MappedFile getMappedFileByOffset(long offset) {
        try {
            lock.readLock().lock();
            if (offset < this.startOffset || offset >= startOffset + files.size() * mappedFileSize) {
                return null;
            }
            return files.get((int) ((offset - startOffset) / mappedFileSize));
        } catch (Exception e) {
            return null;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * 通过文件索引获得文件
     * 
     * @param index
     * @return
     */
    public MappedFile getByIndex(int index) {
        try {
            return files.get(index);
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 获取控制的映射文件的数量
     * 
     * @return
     */
    public int getFileNumber() {
        return files.size();
    }

    /**
     * Getter method for property <tt>mappedFileSize</tt>.
     * 
     * @return property value of mappedFileSize
     */
    public long getMappedFileSize() {
        return mappedFileSize;
    }

    /**
     * Setter method for property <tt>mappedFileSize</tt>.
     * 
     * @param mappedFileSize value to be assigned to property mappedFileSize
     */
    public void setMappedFileSize(long mappedFileSize) {
        this.mappedFileSize = mappedFileSize;
    }

    /**
     * Getter method for property <tt>startOffset</tt>.
     * 
     * @return property value of startOffset
     */
    public long getStartOffset() {
        return startOffset;
    }

    /**
     * Setter method for property <tt>startOffset</tt>.
     * 
     * @param startOffset value to be assigned to property startOffset
     */
    public void setStartOffset(long startOffset) {
        this.startOffset = startOffset;
    }

}
