/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.exception;

/**
 * 覆盖原有的注册信息的异常，只有当原有的注册信息和想要注册的信息不一致时会发生这个异常
 * 
 * @author fengjing.yfj
 * @version $Id: CoverRegisterException.java, v 0.1 2014年2月15日 下午5:48:02 fengjing.yfj Exp $
 */
public class CoverRegisterException extends DistributeCommonException {

    /** 序列号 */
    private static final long serialVersionUID = -5441517187814898353L;

    /**
     * 
     */
    public CoverRegisterException() {
        super();
    }

    /**
     * @param info
     */
    public CoverRegisterException(String info) {
        super(info);
    }

    /**
     * @param throwable
     * @param info
     */
    public CoverRegisterException(Throwable throwable, String info) {
        super(throwable, info);
    }

    /**
     * @param cause
     */
    public CoverRegisterException(Throwable cause) {
        super(cause);
    }

}
