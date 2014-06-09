/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.exception;

/**
 * value文件系统的异常
 * 
 * @author jeff
 * @version $Id: ValueFileSystemException.java, v 0.1 2014年3月6日 下午10:46:48 jeff Exp $
 */
public class ValueFileSystemException extends Exception {

    /**  */
    private static final long serialVersionUID = 428378958116375533L;

    /**
     * 
     */
    public ValueFileSystemException() {
    }

    /**
     * @param message
     */
    public ValueFileSystemException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ValueFileSystemException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public ValueFileSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public ValueFileSystemException(String message, Throwable cause, boolean enableSuppression,
                                    boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
