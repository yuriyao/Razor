/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.net.netty;

import org.jboss.netty.channel.Channel;

/**
 * 服务端有连接请求的监听器
 * 
 * @author fengjing.yfj
 * @version $Id: NettyConnectionComing.java, v 0.1 2014年2月15日 下午5:24:18 fengjing.yfj Exp $
 */
public interface NettyConnectionComingListener extends NettyListener {
    /**
     * 连接正在建立
     * 
     * @param clientAddr 发起连接的客户端地址
     * @param clientPort 发起连接的客户端地址
     * @param channel 建立的channel
     */
    public void connecting(String clientAddr, int clientPort, Channel channel);
}
