/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.xopen;

import java.util.Arrays;

import javax.transaction.xa.Xid;

import org.footoo.common.tools.ByteUtil;

/**
 * 系统实现的XID
 * 
 * @author jeff
 * @version $Id: XidDoDo.java, v 0.1 2014年3月18日 上午10:14:58 jeff Exp $
 */
public class XidDoDo implements Xid {

    /** 全局事务号 */
    private byte[] globalTransactionId;
    /** 分支事务号 */
    private byte[] branchQualifier;
    /** 事务格式 */
    private int    formatId = 34527;

    public XidDoDo() {

    }

    /**
     * 拷贝构造函数
     * 
     * @param xid
     */
    public XidDoDo(Xid xid) {
        setGlobalTransactionId(xid.getGlobalTransactionId());
        setFormatId(xid.getFormatId());
        setBranchQualifier(xid.getBranchQualifier());
    }

    /**
     * 自动生成默认的xid
     * 
     * @param index 分支序号，全局的为0;否则为相应的分支号
     */
    public XidDoDo(int index) {
        globalTransactionId = XidGenerator.generateGlobalXid();
        branchQualifier = XidGenerator.generateBranchId(globalTransactionId, index);
    }

    @Override
    public byte[] getBranchQualifier() {
        return branchQualifier;
    }

    @Override
    public int getFormatId() {
        return formatId;
    }

    @Override
    public byte[] getGlobalTransactionId() {
        return globalTransactionId;
    }

    /**
     * Setter method for property <tt>globalTransactionId</tt>.
     * 
     * @param globalTransactionId value to be assigned to property globalTransactionId
     */
    public final void setGlobalTransactionId(byte[] globalTransactionId) {
        this.globalTransactionId = globalTransactionId;
    }

    /**
     * Setter method for property <tt>branchQualifier</tt>.
     * 
     * @param branchQualifier value to be assigned to property branchQualifier
     */
    public final void setBranchQualifier(byte[] branchQualifier) {
        this.branchQualifier = branchQualifier;
    }

    /**
     * Setter method for property <tt>formatId</tt>.
     * 
     * @param formatId value to be assigned to property formatId
     */
    public final void setFormatId(int formatId) {
        this.formatId = formatId;
    }

    public String toString() {
        return formatId + ":" + ByteUtil.toReadableHex(getGlobalTransactionId()) + ":"
               + ByteUtil.toReadableHex(getBranchQualifier());
    }

    /** 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return ByteUtil.bytesHash(this.branchQualifier)
               + ByteUtil.bytesHash(this.globalTransactionId) + formatId;
    }

    /** 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof XidDoDo) {
            XidDoDo otherXid = (XidDoDo) other;
            if (Arrays.equals(this.getBranchQualifier(), otherXid.getBranchQualifier())
                && Arrays.equals(this.getGlobalTransactionId(), otherXid.getGlobalTransactionId())
                && this.formatId == otherXid.getFormatId()) {
                return true;
            }
        }
        return false;
    }
}
