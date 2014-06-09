/**
 * IBMTC HIT 
 * Copyright (c) 2014-2014 All Rights Reserved.
 */
package org.footoo.common.propertyConvetor;

/**
 * 属性转换的异常
 * 
 * @author jeff
 * @version $Id: PropertyConvetorException.java, v 0.1 2014年3月24日 下午6:50:31 jeff Exp $
 */
public class PropertyConvetorException extends Exception {

    /**  */
    private static final long serialVersionUID = -8494686195170997370L;

    /**
     * 
     */
    public PropertyConvetorException() {
    }

    /**
     * @param message
     */
    public PropertyConvetorException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public PropertyConvetorException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public PropertyConvetorException(String message, Throwable cause) {
        super(message, cause);
    }

}
