/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.net.netty;

import org.footoo.common.protocol.CommandPackage;

/**
 * Netty的一个连接,可以用于服务端也可以用于客户端
 * 一个peer（相同端口)到另一个peer（相同端口)应该只使用一个连接
 * 
 * @author fengjing.yfj
 * @version $Id: NettyConnection.java, v 0.1 2014年2月14日 上午10:51:49 fengjing.yfj Exp $
 */
public interface NettyConnection {

    /**
     * 注册监听器
     * 
     * @param listener
     */
    public void registerNettyListener(NettyListener listener);

    /**
     * 注销监听器
     * 
     * @param listener
     */
    public void unregisterNettyListener(NettyListener listener);

    /**
     * 发送一个报文后调用的同步函数
     * 
     * @param commandPackage
     */
    public void invokeAfterPackageSendRecv(CommandPackage commandPackage);

    /**
     * 调用接受到新的包时的回调函数
     * 注意，只要一有包含长度的报文到达，就会调用这个函数
     * 所以这个函数必须使用一个变量进行跟踪
     * 
     * @param len
     */
    public void invokeRecvNewPackageSync(int len);

    /**
     * 接收到一个完整的包，调用的回调函数
     * 
     * @param commandPackage
     */
    public void invokeRecvFullPackageSync(CommandPackage commandPackage);

    /**
     * 发送一个报文前调用的同步函数
     * 
     * @param commandPackage
     */
    public void invokeBeforePackageSendRecv(CommandPackage commandPackage);

}
