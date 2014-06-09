/**
 * IBMTC HIT 
 * Copyright (c) 2014-2014 All Rights Reserved.
 */
package org.footoo.common.tools;

/**
 * 字符串工具集
 * 
 * @author jeff
 * @version $Id: StringUtil.java, v 0.1 2014年3月24日 下午3:30:49 jeff Exp $
 */
public abstract class StringUtil {
    /**
     * 字符串是否为空白字符串
     * <ul>
     *  <li>null</li>
     *  <li>""</li>
     *  <li>都是' ', \t, \r, \n</li>
     * </ul>
     * 
     * @param str
     * @return
     */
    public static final boolean isBlank(String str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        for (char c : str.toCharArray()) {
            if (!isBlank(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 字符串是否是数字串
     * 
     * @param str
     * @return
     */
    public static final boolean isNumber(String str) {
        if (isBlank(str)) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!isNumber(c)) {
                return false;
            }
        }
        return true;

    }

    /**
     * 是否是16进制字符串
     * 
     * @param str
     * @return
     */
    public static final boolean isHexNumber(String str) {
        if (isBlank(str)) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!isHexNumber(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检测字符是否为十六进制数
     * 
     * @param c
     * @return
     */
    public static final boolean isHexNumber(char c) {
        if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F')) {
            return true;
        }
        return false;
    }

    /**
     * 字符是否是数字
     * 
     * @param c
     * @return
     */
    public static final boolean isNumber(char c) {
        if (c >= '0' && c <= '9') {
            return true;
        }
        return false;
    }

    /**
     * 校验字符是否为空白字符
     * 
     * @param c
     * @return
     */
    public static final boolean isBlank(char c) {
        if (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
            return true;
        }
        return false;
    }
}
