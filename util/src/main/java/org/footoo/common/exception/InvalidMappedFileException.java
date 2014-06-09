/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.exception;

import java.io.IOException;

/**
 * 无法创建MappedFile异常
 * 
 * @author jeff
 * @version $Id: InvalidMappedFileException.java, v 0.1 2014年3月4日 下午5:08:46 jeff Exp $
 */
public class InvalidMappedFileException extends IOException {

    /**  */
    private static final long serialVersionUID = 6173384276340582801L;

    /**
     * 
     */
    public InvalidMappedFileException() {
    }

    /**
     * @param message
     */
    public InvalidMappedFileException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public InvalidMappedFileException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public InvalidMappedFileException(String message, Throwable cause) {
        super(message, cause);
    }

}
