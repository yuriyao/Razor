/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.protocol;

/**
 * 命令处理后的错误码
 * 
 * @author fengjing.yfj
 * @version $Id: CommandErrorCode.java, v 0.1 2014年2月12日 上午10:33:49 fengjing.yfj Exp $
 */
public enum CommandErrorCode {
    /*** 通用 **/

    /** 处理成功 */
    OK(0x0000, "处理成功"),
    /** 服务端内部错误 */
    SERVER_INNER_ERROR(0x0001, "服务端内部错误"),
    /** 参数不合法 */
    INVALID_PARMAS(0x0002, "参数不合法"),
    /** 无法识别的错误码 */
    UNKNOWN_COMMAND(0x0003, "无法识别的错误码"),
    /** 运行时错误 */
    RUNNTIME_ERROR(0x0004, "运行时错误"),

    /******** 存储引擎 */
    /** 创建事务失败 */
    CREATE_TRANSACTION_FAIL(0x0201, "创建事务失败"),
    /** 不合法的分布式事务号 */
    INVALID_XID(0x0202, "不合法的分布式事务号"),
    /** 存储引擎异常 */
    STORAGE_ENGINE_EXCEPTION(0x0203, "存储引擎异常"),

    ;

    /** 错误码 */
    private final int    code;

    /** 错误说明 */
    private final String info;

    /**
     * 默认构造器
     * 
     * @param code 错误码
     * @param info 错误码说明
     */
    private CommandErrorCode(int code, String info) {
        this.code = code;
        this.info = info;
    }

    /**
     * Getter method for property <tt>code</tt>.
     * 
     * @return property value of code
     */
    public int getCode() {
        return code;
    }

    /**
     * Getter method for property <tt>info</tt>.
     * 
     * @return property value of info
     */
    public String getInfo() {
        return info;
    }

}
