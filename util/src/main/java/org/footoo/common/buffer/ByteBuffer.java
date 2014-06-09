/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.buffer;

/**
 * 存放byte的可变长缓存
 * 
 * @author jeff
 * @version $Id: ByteBuffer.java, v 0.1 2014年4月14日 下午12:43:03 jeff Exp $
 */
public class ByteBuffer {
    /** 缓存 */
    private byte[] buffer;
    /** 使用的大小 */
    private int    len;

    public ByteBuffer() {
        buffer = new byte[8];
        len = 0;
    }

    public ByteBuffer(int initCap) {
        buffer = new byte[initCap];
        len = 0;
    }

    /**
     * 添加string
     * 
     * @param str
     */
    public void append(String str) {
        append(str.getBytes());
    }

    /**
     * 添加数据
     * 
     * @param bytes
     * @param from
     * @param len
     */
    public void append(byte[] bytes, int from, int len) {
        //超出容量
        if (this.len + len > buffer.length) {
            //计算新的长度
            int newLen = this.len + len;
            //大于4k，1.5倍增加
            if (newLen >= 4096) {
                newLen *= 1.5;
            } else {
                newLen *= 2;
            }
            byte[] other = new byte[newLen];
            //拷贝原先的数据
            System.arraycopy(buffer, 0, other, 0, this.len);
            buffer = other;
        }
        //添加数据
        System.arraycopy(bytes, from, buffer, this.len, len);
        this.len += len;
    }

    public void append(byte[] bytes) {
        append(bytes, 0, bytes.length);
    }

    /**
     * 获取所有的byte
     * 
     * @return
     */
    public byte[] toByteArray() {
        byte[] result = new byte[len];
        System.arraycopy(buffer, 0, result, 0, len);
        return result;
    }

}
