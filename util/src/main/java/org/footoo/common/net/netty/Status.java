/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.net.netty;

/**
 * 状态枚举
 * 
 * @author fengjing.yfj
 * @version $Id: Status.java, v 0.1 2014年2月14日 下午6:05:29 fengjing.yfj Exp $
 */
public enum Status {
    /** 正在运行 */
    RUNNING(0),
    /** 初始化 */
    INIT(1),
    /** 挂起 */
    SUSPEND(2),
    /** 关闭 */
    STOPPED(3),
    /** 正在启动 */
    STARTING(4), ;

    /** 状态码 */
    private final int code;

    private Status(int code) {
        this.code = code;
    }

    /**
     * Getter method for property <tt>code</tt>.
     * 
     * @return property value of code
     */
    public int getCode() {
        return code;
    }

}
