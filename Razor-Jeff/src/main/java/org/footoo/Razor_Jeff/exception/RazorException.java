/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.Razor_Jeff.exception;

/**
 * 系统的异常
 * 
 * @author jeff
 * @version $Id: RazorException.java, v 0.1 2014年6月10日 上午10:08:49 jeff Exp $
 */
public class RazorException extends Exception {
    /**  */
    private static final long serialVersionUID = 1L;

    /** 异常的标准错误码 */
    private final int         code;

    public RazorException(RazorStandError standError) {
        super(standError.getInfo());
        this.code = standError.getCode();
    }

    public RazorException(RazorStandError standError, String info) {
        super(info);
        this.code = standError.getCode();
    }

    /**
     * Getter method for property <tt>code</tt>.
     * 
     * @return property value of code
     */
    public final int getCode() {
        return code;
    }
}
