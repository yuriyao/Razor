/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.net;

import org.footoo.common.exception.NetException;
import org.footoo.common.exception.NetTimeoutException;
import org.footoo.common.protocol.CommandPackage;

/**
 * 客户端接口，这个接口的实现必须是线程安全的
 * 
 * @author fengjing.yfj
 * @version $Id: RemoteClient.java, v 0.1 2014年2月12日 下午6:52:08 fengjing.yfj Exp $
 */
public interface RemoteClient {

    /**
     * 同步的方式调用请求
     * 
     * @param commandPackage 请求包
     * @param timeoutms 超时时间（ms)
     * @return 处理的结果的包
     * @throws NetTimeoutException
     * @throws NetException
     */
    public CommandPackage invokeCommandSync(CommandPackage commandPackage, int timeoutms)
                                                                                         throws NetTimeoutException,
                                                                                         NetException;

    /**
     * 异步的方式调用请求
     * 
     * @param commandPackage 请求包
     * @param timeoutms 超时时间(ms)
     * @param callback 回调函数
     */
    public void invokeCommandAsync(CommandPackage commandPackage, int timeoutms,
                                   CommandInvokedCallback callback);

    /**
     * 同步的方式发送响应报文
     * 
     * @param responsePackage 响应报文
     * @param timeoutms 超时时间（ms)
     * @throws NetTimeoutException
     * @throws NetException
     */
    public void sendResponseSync(CommandPackage responsePackage, int timeoutms)
                                                                               throws NetTimeoutException,
                                                                               NetException;

    /**
     * 异步发送响应报文
     * 
     * @param responsePackage 响应报文
     * @param timeoutms 超时时间(ms)
     * @param callback 发送结束的回调函数
     */
    public void sendResponseAsync(CommandPackage responsePackage, int timeoutms,
                                  SendedCallback callback);

    /**
     * 启动客户端
     */
    public void start();

    /**
     * 关闭客户端
     */
    public void shutdown();

}
