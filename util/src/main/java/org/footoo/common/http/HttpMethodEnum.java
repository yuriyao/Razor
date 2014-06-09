/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.http;

/**
 * 
 * @author jeff
 * @version $Id: HttpMethodEnum.java, v 0.1 2014年4月14日 下午5:00:32 jeff Exp $
 */
public enum HttpMethodEnum {
    /** GET */
    GET(0, "GET"),
    /** */
    POST(1, "POST"),
    /** */
    HEAD(2, "HEAD");

    /** 请求方法的代码 */
    private final int    code;
    /** 请求方法对应的请求头字符串 */
    private final String name;

    private HttpMethodEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Getter method for property <tt>code</tt>.
     * 
     * @return property value of code
     */
    public final int getCode() {
        return code;
    }

    /**
     * Getter method for property <tt>name</tt>.
     * 
     * @return property value of name
     */
    public final String getName() {
        return name;
    }

}
