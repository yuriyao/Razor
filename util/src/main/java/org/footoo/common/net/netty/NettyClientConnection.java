/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.net.netty;

import org.footoo.common.exception.DistributeCommonException;
import org.footoo.common.exception.NetException;
import org.footoo.common.exception.NetTimeoutException;
import org.footoo.common.net.CommandInvokedCallback;
import org.footoo.common.net.SendedCallback;
import org.footoo.common.protocol.CommandPackage;

/**
 * 客户端连接
 * 
 * @author fengjing.yfj
 * @version $Id: NettyClientConnection.java, v 0.1 2014年2月15日 下午7:11:22 fengjing.yfj Exp $
 */
public interface NettyClientConnection extends NettyConnection {

    /**
     * 获取连接的目标的地址
     * 
     * @return 目标地址
     */
    public String getDestAddr();

    /**
     * 获取连接的目标端口
     * 
     * @return 目标端口
     */
    public int getPort();

    /**
     * 以同步的方式发送响应报文
     * 
     * @param commandPackage 响应报文
     * @param timeoutms 超时时间（ms)
     * @throws NetTimeoutException
     * @throws NetException
     * @throws DistributeCommonException 
     */
    public void sendResponseSync(CommandPackage commandPackage, int timeoutms)
                                                                              throws NetTimeoutException,
                                                                              NetException,
                                                                              DistributeCommonException;

    /**
     * 以异步方式发送响应报文
     * 
     * @param commandPackage 响应报文
     * @param callback 发送结束的回调函数
     * @throws NetException 
     */
    public void sendResponseAsync(CommandPackage commandPackage, SendedCallback callback)
                                                                                         throws NetException;

    /**
     * 同步的方式调用请求
     * 
     * @param commandPackage 请求包
     * @param timeoutms 超时时间（ms)
     * @return 处理的结果的包
     * @throws NetTimeoutException
     * @throws NetException
     * @throws DistributeCommonException 
     */
    public CommandPackage invokeCommandSync(CommandPackage commandPackage, int timeoutms)
                                                                                         throws NetTimeoutException,
                                                                                         NetException,
                                                                                         DistributeCommonException;

    /**
     * 异步的方式调用请求
     * 
     * @param commandPackage 请求包
     * @param callback 回调函数
     * @throws NetException 
     */
    public void invokeCommandAsync(CommandPackage commandPackage, CommandInvokedCallback callback)
                                                                                                  throws NetException;
}
