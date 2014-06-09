/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.compress;

/**
 * 压缩算法
 * 
 * @author jeff
 * @version $Id: Compress.java, v 0.1 2014年5月16日 下午9:36:31 jeff Exp $
 */
public interface Compress {

    /**
     * 压缩
     * 
     * @param data 原数据
     * @return 压缩后的数据
     */
    public byte[] compress(byte[] data);

    /**
     * 压缩
     * 
     * @param data 
     * @param from
     * @param len
     * @return
     */
    public byte[] compress(byte[] data, int from, int len);

    /**
     * 解压缩
     * 
     * @param compressedData 压缩后的数据
     * @return 解压缩后的数据
     */
    public byte[] uncompress(byte[] compressedData) throws InvalidCompressedDataException;
}
