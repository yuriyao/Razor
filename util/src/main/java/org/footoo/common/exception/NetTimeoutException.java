/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.exception;

/**
 * 网络超时异常
 * 
 * @author fengjing.yfj
 * @version $Id: NetTimeoutException.java, v 0.1 2014年2月12日 下午7:05:05 fengjing.yfj Exp $
 */
public class NetTimeoutException extends DistributeCommonException {

    /** 序列号 */
    private static final long serialVersionUID = 766325817874964665L;

    /**
     * 
     */
    public NetTimeoutException() {
        super();
    }

    /**
     * @param e
     * @param info
     */
    public NetTimeoutException(Exception e, String info) {
        super(e, info);
    }

    /**
     * @param e
     */
    public NetTimeoutException(Exception e) {
        super(e);
    }

    /**
     * @param info
     */
    public NetTimeoutException(String info) {
        super(info);
    }

}
