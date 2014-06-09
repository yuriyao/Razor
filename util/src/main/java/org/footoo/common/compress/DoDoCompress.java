/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.compress;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import org.footoo.common.buffer.BitBuffer;
import org.footoo.common.buffer.ByteBuffer;
import org.footoo.common.log.Logger;
import org.footoo.common.log.LoggerFactory;
import org.footoo.common.tools.ByteUtil;

/**
 * LZ系列的压缩算法<br>
 * 用于压缩K级别的数据，主要是网络传输的数据<br>
 * 当然这也可以压缩比较大的数据，只是压缩后的效果可能不是太理想<br>
 * <ul>
 *  <li>原数据和压缩数据的差别用一个bit表示</li>
 *  <li>所有数据划分为1M的数据块分别压缩,所以偏移位置可以用20位bit表示</li>
 *  <li>长度使用5bit表示,并且只有大于等于4的长度的数据才会被压缩，所以可以表示的范围是[4, 36)</li>
 *  <li>字典的长度固定为4,并且保存后出现的</li>
 *  <li>所有的数据都采用的大端模式</li>
 * </ul>
 * 
 * @author jeff
 * @version $Id: SimpleLZCompress.java, v 0.1 2014年6月8日 上午11:15:42 jeff Exp $
 */
public class DoDoCompress implements Compress {
    /** 数据分段的大小 */
    private static final int    FRAGMENT_LENGTH = 1 * 1024 * 1024;
    /** 偏移的bit位数 */
    private static final int    OFFSET_BIT_LEN  = 11;
    /** 长度的bit位数 */
    private static final int    LENGTH_BIT_LEN  = 5;
    /** 压缩字典存放的数据的长度 */
    private static final int    DICT_ENTRY_LEN  = 4;
    /** 最大的长度，不包括 */
    private static final int    LENGTH_MAX_LEN  = 36;
    /** 压缩数据标志 */
    private static final byte[] COMPRESS_MARK   = new byte[] { 0x01 };
    /** 元数据标志 */
    private static final byte[] NORMAL_MARK     = new byte[] { 0x00 };
    /** 日志工具 */
    private static final Logger logger          = LoggerFactory.getLogger(DoDoCompress.class);

    /** 
     * @see org.footoo.common.compress.Compress#compress(byte[])
     */
    @Override
    public byte[] compress(byte[] data) {
        return compress(data, 0, data.length);
    }

    /** 
     * @see org.footoo.common.compress.Compress#compress(byte[], int, int)
     */
    @Override
    public byte[] compress(byte[] data, int from, int len) {
        BitBuffer bitBuffer = new BitBuffer();
        FragmentInfo fragmentInfo = new FragmentInfo(data, from, len);
        //处理数据的缓存
        byte[] buffer = new byte[LENGTH_MAX_LEN];
        //缓存的长度
        int bufferLen = 0;

        while (true) {
            //缓存上一次匹配的位置
            int formerPosition = -1;
            bufferLen = 0;
            while (fragmentInfo.hasNext()) {
                byte b = fragmentInfo.nextByte();
                buffer[bufferLen++] = b;
                //可以进行头的匹配
                if (bufferLen >= DICT_ENTRY_LEN) {
                    int position = fragmentInfo.getPosition(buffer, bufferLen);
                    //匹配，并且相同的
                    if (position >= 0 && bufferLen >= LENGTH_MAX_LEN - 1) {
                        writeCompress(bitBuffer, position, bufferLen);
                        //设置上一次匹配位置
                        formerPosition = -1;
                        bufferLen = 0;
                        //生成字典
                        fragmentInfo.generateToCurrentPoint();
                    } else if (position < 0) {
                        //前面有匹配上的数据
                        if (bufferLen > DICT_ENTRY_LEN) {
                            writeCompress(bitBuffer, formerPosition, bufferLen - 1);
                            buffer[0] = b;
                            bufferLen = 1;
                            //生成字典
                            fragmentInfo.generateBeforeCurrentPoint();
                        } else {
                            writeByte(bitBuffer, buffer[0]);
                            for (int i = 1; i < bufferLen; i++) {
                                buffer[i - 1] = buffer[i];
                            }
                            bufferLen--;
                            //生成字典
                            fragmentInfo.generateOneEntry();
                        }
                    } else {
                        formerPosition = position;
                    }
                }
            }
            //还有残余的数据
            if (bufferLen > 0 && bufferLen < DICT_ENTRY_LEN) {
                for (int i = 0; i < bufferLen; i++) {
                    writeByte(bitBuffer, buffer[i]);
                }
            } else if (bufferLen >= DICT_ENTRY_LEN) {
                writeCompress(bitBuffer, formerPosition, bufferLen);
            }

            //所有数据块处理完毕
            if (!fragmentInfo.hasNextFragment()) {
                break;
            }
            //处理下一个数据块
            fragmentInfo = fragmentInfo.nextFragment();
        }

        return bitBuffer.toBytes();
    }

    /**
     * 写入压缩数据信息
     * 
     * @param bitBuffer
     * @param offset 在数据块中的偏移
     * @param len 匹配的长度
     */
    public void writeCompress(BitBuffer bitBuffer, int offset, int len) {
        //写入压缩标志
        bitBuffer.appendBits(COMPRESS_MARK, 7, 1);
        //写入偏移
        bitBuffer.appendBits(ByteUtil.toBytes(offset), 32 - OFFSET_BIT_LEN, OFFSET_BIT_LEN);
        //写入长度
        bitBuffer.appendBits(ByteUtil.toBytes(len - DICT_ENTRY_LEN), 32 - LENGTH_BIT_LEN,
            LENGTH_BIT_LEN);
    }

    /**
     * 写入原数据的信息
     * 
     * @param bitBuffer
     * @param b
     */
    public void writeByte(BitBuffer bitBuffer, byte b) {
        //写入原数据的信息
        bitBuffer.appendBits(NORMAL_MARK, 7, 1);
        //写入数据
        bitBuffer.appendBits(new byte[] { b }, 0, 8);
    }

    /** 
     * @throws InvalidCompressedDataException 
     * @see org.footoo.common.compress.Compress#uncompress(byte[])
     */
    @Override
    public byte[] uncompress(byte[] compressedData) throws InvalidCompressedDataException {
        BitsSequence bitsSequence = new BitsSequence(compressedData);
        byte[] buffer = new byte[FRAGMENT_LENGTH];
        Arrays.fill(buffer, (byte) 0x48);

        //
        ByteBuffer result = new ByteBuffer();
        while (true) {
            int position = 0;
            //开始处理一个数据块
            while (position < FRAGMENT_LENGTH) {
                if (bitsSequence.remains() <= 0) {
                    break;
                }
                //读取压缩标志
                if (bitsSequence.nextBit() != 0) {//压缩数据
                    //长度不够了
                    if (bitsSequence.remains() < OFFSET_BIT_LEN + LENGTH_BIT_LEN) {
                        break;
                    }
                    int offset = ByteUtil.toShort(bitsSequence.getBits(OFFSET_BIT_LEN), 0);
                    int len = (bitsSequence.getBits(LENGTH_BIT_LEN)[0] & 0xFF) + DICT_ENTRY_LEN;
                    //数据过长，不合法
                    if (position + len > FRAGMENT_LENGTH) {
                        throw new InvalidCompressedDataException();
                    }
                    logger.debug("offset=[" + offset + "],len=[" + len + "], position=[" + position
                                 + "]");
                    //

                    System.arraycopy(buffer, offset, buffer, position, len);

                    position += len;

                } else {//这是原数据
                    //长度不够了
                    if (bitsSequence.remains() < 8) {
                        break;
                    }
                    //获取原数据
                    byte[] formerData = bitsSequence.getBits(8);
                    buffer[position++] = formerData[0];
                }
            }//一个数据块处理完毕
            result.append(buffer, 0, position);
            //没有数据了
            if (bitsSequence.remains() < 8) {
                break;
            }
        }

        return result.toByteArray();
    }

    /**
     * 用于压缩的一个数据块的信息
     * 
     * @author jeff
     * @version $Id: DoDoCompress.java, v 0.1 2014年6月8日 下午12:17:12 jeff Exp $
     */
    private class FragmentInfo {
        /** 数据 */
        private byte[]                                        bytes;
        /** 当前数据块的开始位置 */
        private int                                           begin;
        /** 当前数据块的结束位置,不包括 */
        private int                                           to;
        /** 当前的读指针 */
        private int                                           point;
        /** 压缩字典，value为在整个压缩数据中的位置，可以快速定位 */
        private ConcurrentHashMap<CompressDictEntry, Integer> compressDict;
        /** 上一次生成字典的位置 */
        private int                                           formerDictPosition;
        /** bytes的长度 */
        private int                                           capacity;

        public FragmentInfo(byte[] bytes, int from, int bytesLen) {
            this.bytes = bytes;
            this.begin = from;
            this.to = bytesLen;
            this.to = this.to < (begin + FRAGMENT_LENGTH) ? this.to : (begin + FRAGMENT_LENGTH);
            this.point = from;
            compressDict = new ConcurrentHashMap<>();
            formerDictPosition = from;
            this.capacity = bytesLen;

        }

        /**
         * 获取数据在数据块中的位置
         * 
         * @param buffer
         * @param len
         * @return 如果存在，返回在数据块中的位置；否则返回-1
         */
        public int getPosition(byte[] buffer, int len) {
            assert (len >= DICT_ENTRY_LEN);
            CompressDictEntry compressDictEntry = new CompressDictEntry(buffer, 0);
            Integer position = compressDict.get(compressDictEntry);
            //没有相同的头
            if (position == null) {
                return -1;
            }
            if (position + len > formerDictPosition) {
                return -1;
            }

            //相同
            if (ByteUtil.bytesEqual(bytes, position, len, buffer, 0, len)) {
                return position - begin;
            }
            return -1;

        }

        /**
         * 当前数据块时候还可以读取
         * 
         * @return
         */
        public boolean hasNext() {
            return point < to;
        }

        /**
         * 读取当前数据块下一个的数据
         * 
         * @return
         */
        public byte nextByte() {
            //越界
            if (!hasNext()) {
                throw new IndexOutOfBoundsException();
            }

            return bytes[point++];
        }

        /**
         * 从上一次生成字典的位置再生成一个字典块
         */
        public void generateOneEntry() {

            if (formerDictPosition <= point) {
                //还不不能建立字典
                if (formerDictPosition < begin + DICT_ENTRY_LEN - 1) {
                    formerDictPosition++;
                    return;
                } else {
                    CompressDictEntry dictEntry = new CompressDictEntry(bytes, formerDictPosition
                                                                               - DICT_ENTRY_LEN + 1);
                    compressDict.put(dictEntry, formerDictPosition - DICT_ENTRY_LEN + 1);
                    formerDictPosition++;
                }
            }
            /*if (formerDictPosition + DICT_ENTRY_LEN <= point) {
                CompressDictEntry dictEntry = new CompressDictEntry(bytes, formerDictPosition);
                compressDict.put(dictEntry, formerDictPosition);
                formerDictPosition++;
            }*/
        }

        /**
         * 生成字典，一直到当前读取的位置
         */
        public void generateToCurrentPoint() {
            /*for (int i = formerDictPosition; i < point - DICT_ENTRY_LEN; i++) {
                CompressDictEntry dictEntry = new CompressDictEntry(bytes, i);
                compressDict.put(dictEntry, i);
                formerDictPosition++;
            }*/

            for (int i = formerDictPosition; i < point; i++) {
                generateOneEntry();
            }

        }

        /**
         * 生成字典，一直到当前位置的前一个位置
         */
        public void generateBeforeCurrentPoint() {
            /*for (int i = formerDictPosition; i < point - DICT_ENTRY_LEN - 1; i++) {
                CompressDictEntry dictEntry = new CompressDictEntry(bytes, i);
                compressDict.put(dictEntry, i);
                formerDictPosition++;
            }*/
            for (int i = formerDictPosition; i < point - DICT_ENTRY_LEN - 1; i++) {
                generateOneEntry();
            }
        }

        /**
         * bytes是否可以生成下一个数据块
         * 
         * @return
         */
        public boolean hasNextFragment() {
            return to < capacity;
        }

        /**
         * bytes生成的下一个数据块
         * 
         * @return
         */
        public FragmentInfo nextFragment() {
            if (!hasNextFragment()) {
                throw new IndexOutOfBoundsException();
            }
            return new FragmentInfo(bytes, to, capacity);
        }
    }

    /**
     * 压缩字典的项
     * 
     * @author jeff
     * @version $Id: DoDoCompress.java, v 0.1 2014年6月8日 下午12:48:59 jeff Exp $
     */
    private class CompressDictEntry {
        /** 字典的数据 */
        private byte[] bytes;

        @SuppressWarnings("unused")
        public CompressDictEntry(byte[] bytes) {
            assert (bytes.length == DICT_ENTRY_LEN);
            this.bytes = bytes;
        }

        public CompressDictEntry(byte[] bytes, int from) {
            this.bytes = new byte[DICT_ENTRY_LEN];
            System.arraycopy(bytes, from, this.bytes, 0, DICT_ENTRY_LEN);
        }

        /** 
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + Arrays.hashCode(bytes);
            return result;
        }

        /** 
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof CompressDictEntry)) {
                return false;
            }
            CompressDictEntry other = (CompressDictEntry) obj;
            if (!getOuterType().equals(other.getOuterType())) {
                return false;
            }
            if (!Arrays.equals(bytes, other.bytes)) {
                return false;
            }
            return true;
        }

        private DoDoCompress getOuterType() {
            return DoDoCompress.this;
        }

    }

    /**
     * bit数组
     * 
     * @author jeff
     * @version $Id: DoDoCompress.java, v 0.1 2014年6月8日 下午5:33:38 jeff Exp $
     */
    private static class BitsSequence {
        /** 字节数组 */
        private byte[]              bytes;
        /** 当前读取到的位置 */
        private int                 position;
        /** 填充字节 */
        private static final byte[] FILL_BYTES = new byte[] { 0x00 };

        public BitsSequence(byte[] bytes) {
            this.bytes = bytes;
            position = 0;
        }

        /**
         * 获取长度为length的bit数组
         * 
         * @param length
         * @return
         */
        public byte[] getBits(int length) {
            //越界
            if (remains() < length) {
                throw new IndexOutOfBoundsException();
            }
            BitBuffer bitBuffer = new BitBuffer();
            //先高位填充0
            if (length % 8 != 0) {
                bitBuffer.appendBits(FILL_BYTES, 0, 8 - length % 8);
            }
            bitBuffer.appendBits(bytes, position, length);
            position += length;
            return bitBuffer.toBytes();
        }

        /**
         * 下一个bit
         * 
         * @return
         */
        public byte nextBit() {
            byte result = 0;
            if (remains() < 1) {
                throw new IndexOutOfBoundsException();
            }
            byte b = bytes[position / 8];
            if ((b & (1 << (7 - position % 8))) != 0) {
                result = 0x01;
            } else {
                result = 0x00;
            }
            position++;
            return result;
        }

        /**
         * 检测剩余的bit数量
         * 
         * @return
         */
        public int remains() {
            return ((bytes.length << 3) - position);
        }

    }

}
