/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.footoo.common.exception.InvalidMappedFileException;

/**
 * 单一的内存映射文件
 * 
 * @author jeff
 * @version $Id: MappedFile.java, v 0.1 2014年2月24日 下午5:03:15 jeff Exp $
 */
public class MappedFile {
    /** 映射的文件 */
    private final MappedByteBuffer byteBuffer;

    /** 映射文件的大小 */
    private final long             size;

    /** 开始的偏移，在多个文件组成的文件系统中才有意义的 */
    private long                   offset = 0;

    /** 读写锁 */
    private final ReadWriteLock    lock   = new ReentrantReadWriteLock();

    /** 文件名 */
    private final String           fileName;

    /**
     * 构造函数
     * 
     * @param file
     * @throws IOException 
     */
    public MappedFile(File file) throws IOException {
        this(file, -1);
    }

    public MappedFile(String fileName) throws IOException {
        this(new File(fileName));
    }

    public MappedFile(String fileName, long size) throws IOException {
        this(new File(fileName), size);
    }

    public MappedFile(File file, long size) throws IOException {
        //文件还不存在
        if (!file.exists()) {
            if (size < 0) {
                throw new InvalidMappedFileException("无法创建size=[" + size + "]的映射文件");
            }
            this.size = size;

        } else {//文件已经存在
            this.size = file.length();
        }
        //设置文件名
        fileName = file.getPath();
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        FileChannel channel = randomAccessFile.getChannel();
        byteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, this.size);
        randomAccessFile.close();
    }

    /**
     * 拷贝一个新的副本，共享内存
     * 
     * @return
     */
    public ByteBuffer dumplicate() {
        lock.readLock().lock();
        ByteBuffer buffer = this.byteBuffer.duplicate();
        lock.readLock().unlock();
        return buffer;
    }

    /**
     * 刷新到磁盘
     */
    public void flush() {
        this.byteBuffer.force();
    }

    /**
     * Getter method for property <tt>byteBuffer</tt>.
     * 
     * @return property value of byteBuffer
     */
    public MappedByteBuffer getByteBuffer() {
        return byteBuffer;
    }

    /**
     * Getter method for property <tt>size</tt>.
     * 
     * @return property value of size
     */
    public long getSize() {
        return size;
    }

    /**
     * Getter method for property <tt>offset</tt>.
     * 
     * @return property value of offset
     */
    public long getOffset() {
        return offset;
    }

    /**
     * Getter method for property <tt>fileName</tt>.
     * 
     * @return property value of fileName
     */
    public final String getFileName() {
        return fileName;
    }

    /**
     * Setter method for property <tt>offset</tt>.
     * 
     * @param offset value to be assigned to property offset
     */
    public void setOffset(long offset) {
        this.offset = offset;
    }

    /**
     * Getter method for property <tt>lock</tt>.
     * 
     * @return property value of lock
     */
    public ReadWriteLock getLock() {
        return lock;
    }

    public int hashCode() {
        return fileName.hashCode();
    }

    /** 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MappedFile)) {
            return false;
        }
        return this.fileName.equals(((MappedFile) other).fileName);
    }
}
