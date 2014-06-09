/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.file;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.footoo.common.exception.BitMapOutOfIndexException;

/**
 * 位映射
 * 
 * @author jeff
 * @version $Id: ValueFileSystemBitMap.java, v 0.1 2014年3月6日 上午10:02:47 jeff Exp $
 */
public class ValueFileSystemBitMap {
    /** 保存bitmap的byteBuffer */
    private ByteBuffer byteBuffer;

    /** 开始的查找的索引 */
    private long       beginIndex;

    /** 锁 */
    private Object     lock = new Object();

    /**
     * 
     * 
     * @param byteBuffer 包含所有映射位索引的mappedByteBuffer
     * @param beginIndex bitmap开始的索引
     */
    public ValueFileSystemBitMap(ByteBuffer byteBuffer, long beginIndex) {
        this.byteBuffer = byteBuffer;
        this.beginIndex = beginIndex;
        byteBuffer.position((int) beginIndex);
    }

    /**
     * 寻找number块数据块
     * 
     * @param number
     * @return 找到的块对应的索引数组，如果返回null,说明空间不足，可以进行申请新的数据空间了
     */
    public long[] findBlocks(int number) {
        if (number <= 0 || number > byteBuffer.limit() * 8) {
            return null;
        }

        long[] result = new long[number];

        //开始找
        synchronized (lock) {
            int remain = 0;
            //开始查找的位置
            int savePoint = byteBuffer.position();
            while ((remain = byteBuffer.remaining()) > 0) {
                byte bytes[];

                if (remain <= 8) {
                    bytes = new byte[remain];
                } else {
                    bytes = new byte[8];
                }
                byteBuffer.get(bytes);

                int[] res = findAndSetBytes(bytes, number);
                for (int i : res) {
                    result[--number] = ((byteBuffer.position() - bytes.length - beginIndex) << 3)
                                       + i;
                }
                //写入占位的bit
                if (res.length > 0) {
                    //置位
                    byteBuffer.position(byteBuffer.position() - bytes.length);
                    byteBuffer.put(bytes);
                    if (number == 0) {
                        byteBuffer.position(byteBuffer.position() - bytes.length);
                        break;
                    }
                }
            }

            //重头找一遍
            if (number > 0) {
                byteBuffer.position((int) beginIndex);
            } /*else {
                long possible = byteBuffer.position() - 8;
                possible = possible > savePoint ? possible : savePoint;
                byteBuffer.position((int) possible);
              }*/
            //while (number > 0) {
            while ((remain = savePoint - byteBuffer.position()) > 0) {
                byte bytes[];

                if (remain <= 8) {
                    bytes = new byte[remain];
                } else {
                    bytes = new byte[8];
                }
                byteBuffer.get(bytes);

                int[] res = findAndSetBytes(bytes, number);
                for (int i : res) {
                    result[--number] = ((byteBuffer.position() - bytes.length - beginIndex) << 3)
                                       + i;
                }
                //写入占位的bit
                if (res.length > 0) {
                    //置这一位
                    byteBuffer.position(byteBuffer.position() - bytes.length);
                    byteBuffer.put(bytes);
                    if (number == 0) {
                        byteBuffer.position(byteBuffer.position() - bytes.length);
                        break;
                    }
                }

            }
            //}
            //还有没有找到的，说明大文件没有空闲了
            if (number > 0) {
                //释放这些位置
                try {
                    release(Arrays.copyOfRange(result, number, result.length));
                } catch (BitMapOutOfIndexException e) {
                    //这边应该不会出现这种异常的
                    ;
                }
                return null;
            }

            //排一下序， 保证磁盘访问是顺序进行的，减少寻道时间（假设大文件的存储是连续的)
            Arrays.sort(result);

            return result;

        }

    }

    private int[] findAndSetBytes(byte[] bytes, int number) {
        assert (bytes.length <= 8);
        long l = 0;
        List<Integer> res = new ArrayList<>();
        int is[] = null;

        //转换为long
        for (int i = 0; i < bytes.length; i++) {
            l |= ((long) bytes[i] & 0xFF) << (i << 3);
        }
        //开找
        for (int i = 0; i < number; i++) {
            int index = findLong(l, 0, bytes.length << 3);
            if (index != -1) {
                res.add(index);
                l |= 1L << index;
            } else {
                break;
            }
        }

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) ((l >>> (i * 8)) & 0xFF);
        }

        //转换结果
        is = new int[res.size()];
        for (int i = 0; i < res.size(); i++) {
            is[i] = res.get(i);
        }
        return is;
    }

    /**
     * 释放索引，其实就是释放这些位置所占用的数据
     * 
     * @param indexs 索引
     * @throws BitMapOutOfIndexException 
     */
    public void release(long indexs[]) throws BitMapOutOfIndexException {
        Arrays.sort(indexs);

        synchronized (lock) {
            for (long index : indexs) {
                if (index < 0 || index >= (byteBuffer.limit() - beginIndex) << 3) {
                    throw new BitMapOutOfIndexException();
                }
            }

            for (long index : indexs) {
                byteBuffer.position((int) (beginIndex + index / 8));

                byte b = byteBuffer.get();
                b &= (~(1 << (index % 8)));

                byteBuffer.position(byteBuffer.position() - 1);
                byteBuffer.put(b);
            }
        }
    }

    /**
     * 从[begin, end)范围中找到一个bit0位
     * 采用的算法是二分搜索
     * 
     * @param number
     * @param begin
     * @param end 
     * @return
     */
    public int findLong(long number, int begin, int end) {
        //找到一个
        if (end - begin == 1) {
            if ((~number & (1l << begin)) != 0) {
                return begin;
            }
            return -1;
        }
        //计算掩码
        long mask = (1l << ((end - begin) / 2)) - 1;
        mask <<= begin;
        //
        if ((~number & mask) != 0) {
            return findLong(number, begin, (end + begin) >>> 1);
        } else {
            return findLong(number, (end + begin) >>> 1, end);
        }
    }
}
