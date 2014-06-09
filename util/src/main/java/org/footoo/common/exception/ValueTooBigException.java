/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.exception;

/**
 * value太大异常，超过文件系统的限制了
 * 
 * @author jeff
 * @version $Id: ValueTooBigException.java, v 0.1 2014年3月6日 下午10:47:59 jeff Exp $
 */
public class ValueTooBigException extends ValueFileSystemException {

    /**  */
    private static final long serialVersionUID = 189461850845470960L;

    /**
     * 
     */
    public ValueTooBigException() {
    }

    /**
     * @param message
     */
    public ValueTooBigException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ValueTooBigException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public ValueTooBigException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public ValueTooBigException(String message, Throwable cause, boolean enableSuppression,
                                boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
