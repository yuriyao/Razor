/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.exception;

/**
 * bit map的索引越界的异常
 * 
 * @author jeff
 * @version $Id: BitMapOutOfIndexException.java, v 0.1 2014年3月6日 下午10:08:01 jeff Exp $
 */
public class BitMapOutOfIndexException extends ValueFileSystemException {

    /**  */
    private static final long serialVersionUID = -7625282343580151930L;

    /**
     * 
     */
    public BitMapOutOfIndexException() {
    }

    /**
     * @param message
     */
    public BitMapOutOfIndexException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public BitMapOutOfIndexException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public BitMapOutOfIndexException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public BitMapOutOfIndexException(String message, Throwable cause, boolean enableSuppression,
                                     boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
