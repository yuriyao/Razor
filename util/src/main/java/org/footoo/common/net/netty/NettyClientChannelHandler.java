/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.net.netty;

import org.footoo.common.protocol.CommandPackage;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * netty的客户端数据处理类
 * 
 * @author fengjing.yfj
 * @version $Id: NettyChannelHandler.java, v 0.1 2014年2月15日 下午4:46:56 fengjing.yfj Exp $
 */
public class NettyClientChannelHandler extends SimpleChannelHandler {

    private NettyClientConnectionImpl nettyConnectionImpl;

    public NettyClientChannelHandler(NettyClientConnectionImpl nettyConnectionImpl) {
        this.nettyConnectionImpl = nettyConnectionImpl;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        //获取的响应报文
        CommandPackage commandPackage = (CommandPackage) e.getMessage();
        //保存数据
        nettyConnectionImpl.saveResponsePackage(commandPackage);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        //throw new DistributeCommonException(e.getCause());
        e.getCause().printStackTrace();
    }

}
