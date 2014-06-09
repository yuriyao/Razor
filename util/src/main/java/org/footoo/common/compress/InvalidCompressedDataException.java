/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.compress;

/**
 * 非法的压缩数据
 * 
 * @author jeff
 * @version $Id: InvalidCompressedDataException.java, v 0.1 2014年6月8日 下午6:28:07 jeff Exp $
 */
public class InvalidCompressedDataException extends Exception {

    /**  */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public InvalidCompressedDataException() {
        super();
    }

    /**
     * @param message
     * @param cause
     */
    public InvalidCompressedDataException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public InvalidCompressedDataException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public InvalidCompressedDataException(Throwable cause) {
        super(cause);
    }

}
