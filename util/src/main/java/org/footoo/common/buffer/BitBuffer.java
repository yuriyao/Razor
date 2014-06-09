/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.buffer;

/**
 * 位缓存<br>
 * 实际上是由字节组成
 * 
 * @author jeff
 * @version $Id: BitBuffer.java, v 0.1 2014年6月6日 下午12:14:06 jeff Exp $
 */
public class BitBuffer {
    /** 字节缓存 */
    private byte[]          buffer;
    /** 比特长度 */
    private int             bitsLen;
    /** 初始的尺寸 */
    public static final int INIT_SIZE = 16;

    public BitBuffer() {
        buffer = new byte[INIT_SIZE];
        bitsLen = 0;
    }

    /**
     * 添加bit数组
     * 
     * @param bits bit缓存
     * @param from bit的开始位置
     * @param len bit的长度
     */
    public void appendBits(byte[] bits, int from, int len) {
        //检查是否存储空间已满
        if (bitsLen >= buffer.length << 3) {
            enlarge();
        }
        //计算在当前写入位置的byte里bit剩余空间位数
        int canWrite = 8 - bitsLen % 8;
        //写入的缓冲区字节位置
        int bufferPosition = bitsLen / 8;
        //能够写入的"bit"的零头
        int canWritebitesLenitesLen1 = 8 - from % 8;
        //
        int canWritebitesLenitesLen = canWritebitesLenitesLen1 < len ? canWritebitesLenitesLen1
            : len;
        //写入的完整的字节
        int b = ((int) bits[from / 8]) & 0xFF;
        //计算实际的写入bit长度
        int actualLen = canWrite < canWritebitesLenitesLen ? canWrite : canWritebitesLenitesLen;
        //计算出干净的可以直接|的数据
        b = (b >>> (canWritebitesLenitesLen1 - actualLen)) & ((1 << actualLen) - 1);
        //
        b = (((int) buffer[bufferPosition]) & 0xFF) | (b << (canWrite - actualLen));
        buffer[bufferPosition] = (byte) b;
        //
        bitsLen += actualLen;
        //还有数据没有写
        if (len - actualLen > 0) {
            appendBits(bits, from + actualLen, len - actualLen);
        }

    }

    /**
     * 扩大尺寸<br>
     * <ul>
     *  <li>小于1M时2倍扩展</li>
     *  <li>大于1M增加1/2
     * </ul>
     * 
     */
    private void enlarge() {
        int newSize = buffer.length * 2;
        if (buffer.length > 1 * 1024 * 1024) {
            newSize = buffer.length * 3 / 2;
        }
        //申请新的空间
        byte[] newBuffer = new byte[newSize];
        //拷贝原有数据
        System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
        //
        buffer = newBuffer;
    }

    /**
     * 获取实际的数据
     * 
     * @return
     */
    public byte[] toBytes() {
        int len = (bitsLen + 7) / 8;
        byte[] ret = new byte[len];
        System.arraycopy(buffer, 0, ret, 0, len);
        return ret;
    }
}
