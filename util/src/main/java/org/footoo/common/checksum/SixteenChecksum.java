/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.checksum;

import java.util.List;

/**
 * 16位checksum
 * <ul>
 * <li>如果需要将校验和放在校验数据之中，必须是双字节对齐，否则检测时不会等于0</li>
 * </ul>
 * 
 * @author jeff
 * @version $Id: SixteenChecksum.java, v 0.1 2014年5月16日 下午10:10:51 jeff Exp $
 */
public class SixteenChecksum {
    /**
     * 计算校验和
     * 
     * @param bytesList 所有的byte数组列表逻辑上组成一个大的byte数组
     * @return 16位校验和
     */
    public static short checksum(List<byte[]> bytesList) {
        boolean needLast = false;
        int tmp = 0;
        int result = 0;
        for (byte[] bytes : bytesList) {
            for (byte b : bytes) {
                //可以合成一个short
                if (needLast) {
                    tmp = (tmp << 8) | (b & 0xFF);
                    result += tmp;
                    needLast = false;
                } else {
                    tmp = b & 0xFF;
                    needLast = true;
                }
            }
        }
        if (needLast) {
            result += tmp;
        }

        while ((result >>> 16) != 0) {
            result = (result & 0xFFFF) + (result >>> 16);
        }
        return (short) ~result;
    }

    /**
     * 计算十六进制校验和
     * 
     * @param bytes
     * @param from
     * @param length
     * @return
     */
    public static short checksum(byte[] bytes, int from, int length) {
        boolean needLast = false;
        int tmp = 0;
        int result = 0;

        for (int i = from; i < from + length; i++) {
            int b = bytes[i] & 0xFF;
            //可以合成一个short
            if (needLast) {
                tmp = (tmp << 8) | b;
                result += tmp;
                needLast = false;
            } else {
                tmp = b;
                needLast = true;
            }
        }

        if (needLast) {
            result += tmp;
        }

        while ((result >>> 16) != 0) {
            result = (result & 0xFFFF) + (result >>> 16);
        }
        return (short) ~result;
    }
}
