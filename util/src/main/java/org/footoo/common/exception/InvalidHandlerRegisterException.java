/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.exception;

/**
 * 不合法的注册信息
 * 
 * @author fengjing.yfj
 * @version $Id: InvalidHandlerRegisterException.java, v 0.1 2014年2月15日 下午5:54:24 fengjing.yfj Exp $
 */
public class InvalidHandlerRegisterException extends DistributeCommonException {

    /** 序列号 */
    private static final long serialVersionUID = 1649328189950943687L;

    /**
     * 
     */
    public InvalidHandlerRegisterException() {
        super();
    }

    /**
     * @param info
     */
    public InvalidHandlerRegisterException(String info) {
        super(info);
    }

    /**
     * @param throwable
     * @param info
     */
    public InvalidHandlerRegisterException(Throwable throwable, String info) {
        super(throwable, info);
    }

    /**
     * @param cause
     */
    public InvalidHandlerRegisterException(Throwable cause) {
        super(cause);
    }

}
