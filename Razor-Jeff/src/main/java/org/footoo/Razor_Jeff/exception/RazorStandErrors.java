/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.Razor_Jeff.exception;

/**
 * 标准错误码的实现
 * 
 * @author jeff
 * @version $Id: RazorStandErrors.java, v 0.1 2014年6月10日 上午10:03:57 jeff Exp $
 */
public class RazorStandErrors implements RazorStandError {

    /** 错误码 */
    private final int    code;
    /** 错误信息 */
    private final String info;

    public RazorStandErrors(int code, String info) {
        this.code = code;
        this.info = info;
    }

    /** 
     * @see org.footoo.Razor_Jeff.exception.RazorStandError#getCode()
     */
    @Override
    public int getCode() {
        return code;
    }

    /** 
     * @see org.footoo.Razor_Jeff.exception.RazorStandError#getInfo()
     */
    @Override
    public String getInfo() {
        return info;
    }

}
