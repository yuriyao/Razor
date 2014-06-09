/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.file;

import java.nio.MappedByteBuffer;

import org.footoo.common.log.Logger;
import org.footoo.common.log.LoggerFactory;

/**
 * 用于连续搜索的bitmap
 * <ul>
 * <li>约定：</li>
 * <li>字节的高位为开始，低位为结尾</li>
 * </ul>
 * 
 * @author jeff
 * @version $Id: ContinuesBitmap.java, v 0.1 2014年5月15日 上午10:14:30 jeff Exp $
 */
public class ContinuesBitmap {
    /** 位映射的内存映射 */
    private final MappedByteBuffer bitmap;
    /** 是否折返查找 */
    private boolean                willBack = true;
    /** 日志 */
    private static final Logger    logger   = LoggerFactory.getLogger(ContinuesBitmap.class);

    public ContinuesBitmap(MappedByteBuffer bitmap) {
        this.bitmap = bitmap;
    }

    /**
     * 获取连续的长度为len的0bit位，并且设置这些位为1
     * 
     * @param len
     * @return 返回开始的bit的索引，如果没有找到，返回-1
     */
    public int getAndSetWithLen(int len) {
        /** 参数检查 */
        if (len <= 0) {
            return -1;
        }
        //防止竞争
        synchronized (this) {
            //记录起始搜索的位置，防止折返查找时，无法停止
            int position = bitmap.position();
            //还可以查找
            int index = getContinues(bitmap.remaining(), len);
            if (index != -1) {
                setContinues(index, len);
                return index;
            }
            //不能进行折返查找
            if (!willBack) {
                return -1;
            }
            //折返到头部
            bitmap.position(0);
            index = getContinues(position, len);
            if (index != -1) {
                setContinues(index, len);
            }
            return index;
        }
    }

    /**
     * 获取连续的长度为len的bit0,并计算开始索引
     * 
     * @param offset
     * @param remaining
     * @param len
     * @return
     */
    private int getContinues(int remaining, int len) {

        while (remaining > 0) {
            int offset = bitmap.position();
            //短的连续0bit搜索
            if (len <= 8) {
                int begin = getContinuesZerobegin(bitmap.get(), len);
                //还可以用来寻找一遍
                bitmap.position(bitmap.position() - 1);
                if (begin != -1) {
                    int index = (offset << 3) + begin;
                    return index;
                }
            }

            int endNumber = endZeroNumber(bitmap.get());
            if (endNumber == 0) {
                remaining--;
                continue;
            }
            if (isContinuesZero(len - endNumber, remaining - 1)) {
                int index = (offset << 3) + 8 - endNumber;

                bitmap.position(bitmap.position() - 1);
                return index;
            }
            bitmap.position(bitmap.position() - 1);
            remaining -= bitmap.position() - offset;
        }
        return -1;
    }

    /**
     * 以当前位置开始的len长度是否都是0bit
     * 
     * @param len
     * @param remain
     * @return
     */
    private boolean isContinuesZero(int len, int remain) {
        if (len > (remain << 3)) {
            bitmap.position(bitmap.position() + remain);
            return false;
        }
        while (len >= 8) {
            if (bitmap.get() != 0) {
                return false;
            }
            len -= 8;
        }
        if (len > 0) {
            if (isBeginWithZeroNumber(bitmap.get(), len)) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }

    }

    /**
     * 将index开始的len个连续bit置1
     * 
     * @param index
     * @param len
     */
    public void setContinues(int index, int len) {
        synchronized (this) {
            //记录原位置
            int position = bitmap.position();
            //请求越界
            if (index + len > bitmap.limit() * 8) {
                logger.warn("无法给开始位置为[" + index + "],长度为[" + len + "]的bit置1,因为越界");
                return;
            }
            //
            bitmap.position(index / 8);
            //计算需要读取的字节长度
            //8字节对齐
            int end = (index + len + 7) & ~0x7;
            int byteLen = end / 8 - index / 8;
            byte[] bytes = new byte[byteLen];
            //读取需要进行置位的字节数组
            bitmap.get(bytes);
            //置位
            set(bytes, index % 8, len);
            //写回位映射表
            bitmap.position(index / 8);
            bitmap.put(bytes);
            //恢复“现场”
            bitmap.position(position);
        }

    }

    /**
     * 给bytes数组置位，开始位置为index，长度为len
     * 
     * @param bytes
     * @param index
     * @param len
     */
    private void set(byte[] bytes, int index, int len) {
        assert (index <= 7);
        int currentNumber = 8 - index % 8;
        currentNumber = currentNumber > len ? len : currentNumber;
        //计算掩码
        int mask = ((1 << currentNumber) - 1);
        mask <<= 8 - index - currentNumber;
        bytes[0] |= mask;
        //置中间的位
        len -= currentNumber;
        int i = 1;
        while (len > 8) {
            bytes[i++] = (byte) 0xFF;
            len -= 8;
        }
        //置末尾的位
        if (len > 0) {
            mask = (1 << len) - 1;
            mask <<= 8 - len;
            bytes[i] |= mask;
        }
    }

    /**
     * 将index开始的len个连续bit置0
     * 
     * @param index
     * @param len
     */
    public void clearContinues(int index, int len) {
        synchronized (this) {
            //记录原位置
            int position = bitmap.position();
            //请求越界
            if (index + len > bitmap.limit() * 8) {
                logger.warn("无法给开始位置为[" + index + "],长度为[" + len + "]的bit置0,因为越界");
                return;
            }
            //
            bitmap.position(index / 8);
            //计算需要读取的字节长度
            //8字节对齐
            int end = (index + len + 7) & ~0x7;
            int byteLen = end / 8 - index / 8;
            byte[] bytes = new byte[byteLen];
            //读取需要进行置位的字节数组
            bitmap.get(bytes);
            //置位
            clear(bytes, index % 8, len);
            //写回位映射表
            bitmap.position(index / 8);
            bitmap.put(bytes);
            //恢复“现场”
            bitmap.position(position);
        }
    }

    /**
     * 给bytes数组代表的bitmap清0,开始位置为index, 长度为len
     * 
     * @param bytes
     * @param index
     * @param len
     */
    private void clear(byte[] bytes, int index, int len) {
        assert (index <= 7);
        int currentNumber = 8 - index % 8;
        currentNumber = currentNumber > len ? len : currentNumber;
        //计算掩码
        int mask = ((1 << currentNumber) - 1);
        mask <<= 8 - index - currentNumber;
        bytes[0] &= ~mask;
        //置中间的位
        len -= currentNumber;
        int i = 1;
        while (len > 8) {
            bytes[i++] = (byte) 0;
            len -= 8;
        }
        //置末尾的位
        if (len > 0) {
            mask = (1 << len) - 1;
            mask <<= 8 - len;
            bytes[i] &= ~mask;
        }
    }

    /**
     * 检测b开始连续的0的数量
     * 
     * @param b
     * @return
     */
    public int beginZeroNumber(byte b) {
        int bi = ((int) b) & 0xFF;
        for (int i = 0; i < 8; i++) {
            if ((beginZero[i] & bi) != 0) {
                return i;
            }
        }
        return 8;
    }

    /**
     * 检测b末尾连续的0的数量
     * 
     * @param b
     * @return
     */
    public int endZeroNumber(byte b) {
        int bi = ((int) b) & 0xFF;
        for (int i = 0; i < 8; i++) {
            if ((endZero[i] & bi) != 0) {
                return i;
            }
        }
        return 8;
    }

    /**
     * 检测字节b是否开始连续至少有number个0bit
     * 
     * @param b
     * @param number
     * @return
     */
    public boolean isBeginWithZeroNumber(byte b, int number) {
        int bi = ((int) b) & 0xFF;
        if (number > 8 || number < 0) {
            return false;
        }
        return ((beginZero[number - 1] & bi) == 0);
    }

    /**
     * 检测b末尾是否至少有number个0
     * 
     * @param b
     * @param number
     * @return
     */
    public boolean isEndWithZeroNumber(byte b, int number) {
        int bi = ((int) b) & 0xFF;
        if (number > 8 || number < 0) {
            return false;
        }
        return ((endZero[number] & bi) == 0);
    }

    /**
     * 检测b是否包含至少number个连续0bit
     * 
     * @param b
     * @param number
     * @return
     */
    public boolean isContainWithZeroNumber(byte b, int number) {
        int bi = ((int) b) & 0xFF;
        if (number > 8 || number <= 0) {
            return false;
        }
        for (int i = 0; i <= 8 - number; i++) {
            if (((containZero[i] >>> i) & bi) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取连续number个0bit开始的位置
     * 
     * @param b
     * @param number
     * @return
     */
    public int getContinuesZerobegin(byte b, int number) {
        int bi = ((int) b) & 0xFF;
        if (number > 8 || number <= 0) {
            return -1;
        }
        for (int i = 0; i <= 8 - number; i++) {
            if (((containZero[number - 1] >>> i) & bi) == 0) {
                return i;
            }
        }
        return -1;
    }

    /** 用于检测开始连续为0的比特数量的与数据 */
    private static int[] beginZero   = new int[] { ~0x7F, ~0x3F, ~0x1F, ~0x0F, ~0x07, ~0x03, ~0x01,
            ~0x00                   };
    /** 检测末尾连续为0的比特数量的与操作数 */
    private static int[] endZero     = new int[] { ~0xFE, ~0xFC, ~0xF8, ~0xF0, ~0xE0, ~0xC0, ~0x80,
            ~0x00                   };
    /** 用于检测任意位置的连续0的与操作数 */
    private static int[] containZero = new int[] { ~0x7F & 0xFF, ~0x3F & 0xFF, ~0x1F & 0xFF,
            ~0x0F & 0xFF, ~0x07 & 0xFF, ~0x03 & 0xFF, ~0x01 & 0xFF, ~0x00 & 0xFF };

}
