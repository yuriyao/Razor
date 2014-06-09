/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.config;

/**
 * dodo系统的配置信息
 * 
 * @author jeff
 * @version $Id: DoDoConfigureInfo.java, v 0.1 2014年4月1日 下午5:39:53 jeff Exp $
 */
public class DoDoConfigureInfo extends Configure {
    /** value文件系统的块的大小 */
    @ConfigureAttr(defaultValue = "8")
    private int valueBlockSize;

    /**
     * Getter method for property <tt>valueBlockSize</tt>.
     * 
     * @return property value of valueBlockSize
     */
    public final int getValueBlockSize() {
        return valueBlockSize;
    }

    /**
     * Setter method for property <tt>valueBlockSize</tt>.
     * 
     * @param valueBlockSize value to be assigned to property valueBlockSize
     */
    public final void setValueBlockSize(int valueBlockSize) {
        this.valueBlockSize = valueBlockSize;
    }

}
