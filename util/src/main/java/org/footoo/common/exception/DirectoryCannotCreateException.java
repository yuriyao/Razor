/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.exception;

/**
 * 目录无法创建的异常
 * 
 * @author fengjing.yfj
 * @version $Id: DirectoryCannotCreateException.java, v 0.1 2014年2月13日 下午2:24:53 fengjing.yfj Exp $
 */
public class DirectoryCannotCreateException extends Exception {

    /** 序列号 */
    private static final long serialVersionUID = -8685603220332845548L;

    /**
     * 
     */
    public DirectoryCannotCreateException() {
        super();
    }

    /**
     * @param message
     * @param cause
     */
    public DirectoryCannotCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public DirectoryCannotCreateException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public DirectoryCannotCreateException(Throwable cause) {
        super(cause);
    }

}
