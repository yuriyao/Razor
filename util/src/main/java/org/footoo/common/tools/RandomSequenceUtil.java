/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.tools;

/**
 * 随即序列工具
 * 
 * @author jeff
 * @version $Id: RandomSequceUtil.java, v 0.1 2014年4月26日 下午3:44:06 jeff Exp $
 */
public abstract class RandomSequenceUtil {
    /**
     * 生成len长度的字节数组
     * 
     * @param len
     * @return
     */
    public static final byte[] generateRandomSequence(int len) {
        byte[] sequence = new byte[len];
        //
        for (int i = 0; i < len / 8; i++) {
            long l = (long) (Math.random() * Long.MAX_VALUE);
            System.arraycopy(ByteUtil.toBytes(l), 0, sequence, 8 * i, 8);
        }
        //生成无法对齐到long的字节
        int ugly = len / 8 * 8;
        for (int i = ugly; i < len; i++) {
            sequence[i] = generateRandomByte();
        }

        return sequence;
    }

    /**
     * 生成单字节随即数
     * 
     * @return
     */
    public static final byte generateRandomByte() {
        return (byte) (Math.random() * 0xFF);
    }
}
