/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.xopen;

import org.footoo.common.tools.ByteUtil;
import org.footoo.common.tools.UUIDUtil;

/**
 * xid生成器
 * 
 * @author jeff
 * @version $Id: XidGenerator.java, v 0.1 2014年2月26日 下午2:26:34 jeff Exp $
 */
public abstract class XidGenerator {

    /**
     * 生成全局事务号
     * 
     * @return
     */
    public static final byte[] generateGlobalXid() {
        return UUIDUtil.generateUUID64(0);
    }

    /**
     * 生成分支事务号
     * 
     * @param globalXid
     * @param index
     * @return
     */
    public static byte[] generateBranchId(byte[] globalXid, int index) {
        byte[] bytes = globalXid.clone();
        System.arraycopy(ByteUtil.toBytes(index), 0, bytes, bytes.length - 4, 4);
        return bytes;

    }
}
