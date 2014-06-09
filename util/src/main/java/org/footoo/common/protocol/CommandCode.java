/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.protocol;

/**
 * 命令码
 * 
 * @author fengjing.yfj
 * @version $Id: CommandCode.java, v 0.1 2014年2月12日 上午10:33:32 fengjing.yfj Exp $
 */
public enum CommandCode {
    /** 心跳包 */
    HEART_BEAT(0x0001, "心跳包"),
    /** ECHO */
    ECHO(0x0002, "echo"),

    /******配置服务器 **/
    /** 获取存储服务器集群主服务器列表 */
    GET_CLUSTER_MASTERS(0x0101, "获取存储服务器集群主服务器列表"),
    /** 获取对应hash值集群的主服务器 */
    GET_CLUSTER_MASTER(0x0102, "获取对应hash值集群的主服务器"),
    /** 注册存储服务器集群成员 */
    REGISTER_CLUSER_MEMBER(0x0103, "注册存储服务器集群成员"),
    /** 获取存储集群的数量 */
    GET_CLUSTER_NUMER(0x0104, "获取存储集群的数量"),

    /***** 存储服务器 *********/
    /** 创建事务 */
    @Deprecated
    CREATE_TRANSACTION(0x0201, "创建事务"),
    /** 操作存储引擎 */
    @Deprecated
    OPERATE_STORE_ENGINE(0x0202, "操作存储引擎"),
    /** 完成事务 */
    @Deprecated
    FINISH_TRANSACTION(0x0203, "完成事务"),
    /** 事务准备 */
    @Deprecated
    PREPARE_TRANSACTION(0x0204, "事务准备"),
    /** 操作事务引擎,对于最新的API，只使用这一个请求号，上面的4个已经废掉了 */
    OPERATE_TRANSACTION_ENGINE(0x0205, "操作事务引擎"),

    /********* 主服务器 **********/
    /** 创建分布式事务，这个一般会是分布式事务 */
    CREEATE_DISTRIBUTE_TRANSACTION(0x0301, "创建分布式事务"),
    /** 进行分布式存储 */
    DISTRIBUTE_STORAGE(0x0302, "进行分布式存储"),
    /** 事务准备 */
    DISTRIBUTE_TRANSACTION_PREPARE(0x0303, "分布式事务准备"),
    /** 分布式事务提交 */
    DISTRIBUTE_TRANSACTION_COMMIT(0x0304, "分布式事务提交");

    /** 命令码 */
    private final int    code;

    /** 命令说明 */
    private final String info;

    /**
     * 默认构造器
     * 
     * @param code 命令码
     * @param info 命令码说明
     */
    private CommandCode(int code, String info) {
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
