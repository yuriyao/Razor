/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.tools;

/**
 * 字节工具
 * 
 * @author jeff
 * @version $Id: ByteUtil.java, v 0.1 2014年4月26日 下午2:56:52 jeff Exp $
 */
public abstract class ByteUtil {
    /**
     * 将字符数组转换为十六进制字符串   
     * 
     * @param bytes
     * @return
     */
    public static final String toHex(byte[] bytes) {
        StringBuffer buffer = new StringBuffer();
        for (byte b : bytes) {
            int index = b & 0xFF;
            buffer.append(HexDigits[index]);
        }
        return buffer.toString();
    }

    /**
     * 只转换from开始，长度为len的byte数组
     * 
     * @param bytes
     * @param from
     * @param len
     * @return
     */
    public static final String toHex(byte[] bytes, int from, int len) {
        StringBuffer buffer = new StringBuffer();
        for (int i = from; i < len + from; i++) {
            byte b = bytes[i];
            int index = b & 0xFF;
            buffer.append(HexDigits[index]);
        }
        return buffer.toString();
    }

    /**
     * 将int转换为4字节的byte数组
     * 
     * @param is
     * @return
     */
    public static final byte[] toBytes(int is) {
        byte[] bytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            bytes[3 - i] = (byte) ((is >>> (i << 3)) & 0xFF);
        }
        return bytes;
    }

    /**
     * 将long转换为8字节的byte数组
     * 
     * @param l
     * @return
     */
    public static final byte[] toBytes(long l) {
        byte[] bytes = new byte[8];
        for (int i = 0; i < 8; i++) {
            bytes[7 - i] = (byte) ((l >>> (i << 3)) & 0xFF);
        }
        return bytes;
    }

    /**
     * 将short转换位字节数组
     * 
     * @param s
     * @return
     */
    public static final byte[] toBytes(short s) {
        byte[] bytes = new byte[2];
        bytes[1] = (byte) (s & 0xFF);
        bytes[0] = (byte) (s >>> 8);
        return bytes;
    }

    /**
     * 将i转换为小端模式的字节数组
     * 
     * @param i
     * @return
     */
    public static final byte[] toLittleEndianBytes(int is) {
        byte[] bytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            bytes[i] = (byte) ((is >>> (i << 3)) & 0xFF);
        }
        return bytes;
    }

    /**
     * 转换为二进制
     * 
     * @param b
     * @return
     */
    public static final String toBitsString(byte b) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < 8; i++) {
            if ((b & BYTE_MASKs[i]) != 0) {
                buffer.append("1");
            } else {
                buffer.append("0");
            }
        }
        return buffer.toString();
    }

    /**
     * 转换为可读的二进制，相邻的byte用空格分割
     * 
     * @param bs
     * @return
     */
    public static final String toBitsString(byte[] bs) {
        StringBuffer buffer = new StringBuffer();
        for (byte b : bs) {
            buffer.append(toBitsString(b));
            buffer.append(" ");
        }
        return buffer.toString();
    }

    /**
     * 转换为可读的十六进制
     * 
     * @param bytes
     * @return
     */
    public static final String toReadableHex(byte[] bytes) {
        if (bytes.length <= 10) {
            return toHex(bytes);
        }

        //显示前5个字节和后五个字节
        StringBuffer buffer = new StringBuffer();
        buffer.append(toHex(bytes, 0, 5));
        buffer.append("...");
        buffer.append(toHex(bytes, bytes.length - 5, 5));

        return buffer.toString();
    }

    /**
     * byte数组转换为int，采用的都是大端模式
     * 
     * @param bytes
     * @param from
     * @return
     */
    public static int toInt(byte[] bytes, int from) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            int b = ((int) bytes[from + i]) & 0xFF;
            result = (result << 8) | b;
        }
        return result;
    }

    /**
     * 将字节数组转换为long
     * 
     * @param bytes
     * @param from
     * @return
     */
    public static long toLong(byte[] bytes, int from) {
        long result = 0;
        for (int i = 0; i < 8; i++) {
            long b = ((long) bytes[from + i]) & 0xFF;
            result = (result << 8) | b;
        }
        return result;
    }

    /**
     * 转换为short
     * 
     * @param bytes
     * @param from
     * @return
     */
    public static short toShort(byte[] bytes, int from) {
        short result = 0;
        for (int i = 0; i < 2; i++) {
            short b = (short) (((int) bytes[from + i]) & 0xFF);
            result = (short) ((result << 8) | b);
        }
        return result;

    }

    /**
     * 计算byte数组的hash值
     * 
     * @param bytes
     * @return
     */
    public static int bytesHash(byte[] bytes) {
        int ret = 0x12345678;
        for (byte b : bytes) {
            ret ^= (int) b;
        }
        return ret;
    }

    /**
     * 比较a和b的字节数组是否相等
     * 
     * @param a
     * @param aFrom
     * @param aLen
     * @param b
     * @param bFrom
     * @param bLen
     * @return
     */
    public static boolean bytesEqual(byte[] a, int aFrom, int aLen, byte[] b, int bFrom, int bLen) {
        if (aLen != bLen) {
            return false;
        }
        for (int i = 0; i < aLen; i++) {
            if (a[aFrom + i] != b[bFrom + i]) {
                return false;
            }
        }
        return true;
    }

    /** 十六进制数组 */
    public static String HexDigits[] = { "00", "01", "02", "03", "04", "05", "06", "07", "08",
            "09", "0a", "0b", "0c", "0d", "0e", "0f", "10", "11", "12", "13", "14", "15", "16",
            "17", "18", "19", "1a", "1b", "1c", "1d", "1e", "1f", "20", "21", "22", "23", "24",
            "25", "26", "27", "28", "29", "2a", "2b", "2c", "2d", "2e", "2f", "30", "31", "32",
            "33", "34", "35", "36", "37", "38", "39", "3a", "3b", "3c", "3d", "3e", "3f", "40",
            "41", "42", "43", "44", "45", "46", "47", "48", "49", "4a", "4b", "4c", "4d", "4e",
            "4f", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "5a", "5b", "5c",
            "5d", "5e", "5f", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "6a",
            "6b", "6c", "6d", "6e", "6f", "70", "71", "72", "73", "74", "75", "76", "77", "78",
            "79", "7a", "7b", "7c", "7d", "7e", "7f", "80", "81", "82", "83", "84", "85", "86",
            "87", "88", "89", "8a", "8b", "8c", "8d", "8e", "8f", "90", "91", "92", "93", "94",
            "95", "96", "97", "98", "99", "9a", "9b", "9c", "9d", "9e", "9f", "a0", "a1", "a2",
            "a3", "a4", "a5", "a6", "a7", "a8", "a9", "aa", "ab", "ac", "ad", "ae", "af", "b0",
            "b1", "b2", "b3", "b4", "b5", "b6", "b7", "b8", "b9", "ba", "bb", "bc", "bd", "be",
            "bf", "c0", "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9", "ca", "cb", "cc",
            "cd", "ce", "cf", "d0", "d1", "d2", "d3", "d4", "d5", "d6", "d7", "d8", "d9", "da",
            "db", "dc", "dd", "de", "df", "e0", "e1", "e2", "e3", "e4", "e5", "e6", "e7", "e8",
            "e9", "ea", "eb", "ec", "ed", "ee", "ef", "f0", "f1", "f2", "f3", "f4", "f5", "f6",
            "f7", "f8", "f9", "fa", "fb", "fc", "fd", "fe", "ff" };
    /** 二进制掩码 */
    public static int[]  BYTE_MASKs  = { 0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01 };
}
