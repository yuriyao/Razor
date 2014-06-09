/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.tools;

import java.util.List;

/**
 * UUID工具
 * 
 * @author jeff
 * @version $Id: UUIDUtil.java, v 0.1 2014年4月26日 下午2:49:35 jeff Exp $
 */
public abstract class UUIDUtil {
    /**
     * 生成64字节的UUID
     * <ul>
     *  <li>6字节的网卡号</li>
     *  <li>8字节日期信息</li>
     *  <li>16字节的随机数信息</li>
     *  <li>30字节0</li>
     *  <li>4字节末尾序号</li>
     * </ul>
     * 
     * @param tail 末尾数字
     * @return
     */
    public static byte[] generateUUID64(int tail) {
        byte[] bytes = new byte[64];

        //6字节MAC
        System.arraycopy(generateSixByteDefaultMac(), 0, bytes, 0, 6);
        //8字节日期信息
        System.arraycopy(ByteUtil.toBytes(System.currentTimeMillis()), 0, bytes, 6, 8);
        //16字节随机数信息
        System.arraycopy(RandomSequenceUtil.generateRandomSequence(16), 0, bytes, 14, 16);
        //30字节0,不用填
        //末尾序号
        System.arraycopy(ByteUtil.toBytes(tail), 0, bytes, 60, 4);

        return bytes;
    }

    /**
     * 生成6字节的序列，默认是网卡号
     * 
     * @return
     */
    public static byte[] generateSixByteDefaultMac() {

        List<byte[]> macs = SystemInfoTool.getMacs();
        //存在mac地址
        if (!CollectionUtils.isEmpty(macs)) {
            byte[] bytes = new byte[6];
            byte[] mac = macs.get(0);

            int len = mac.length > 6 ? 6 : mac.length;

            System.arraycopy(mac, 0, bytes, 0, len);

            return bytes;
        }
        //不存在mac地址
        return RandomSequenceUtil.generateRandomSequence(6);
    }
}
