/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.net.netty;

import org.jboss.netty.channel.Channel;

/**
 * netty的server的连接
 * 
 * @author fengjing.yfj
 * @version $Id: NettyServerConnection.java, v 0.1 2014年2月15日 下午8:17:37 fengjing.yfj Exp $
 */
public interface NettyServerConnection extends NettyConnection {

    /**
     * 调用新的连接建立的同步函数
     * 
     * @param clientAddr
     * @param clientPort
     * @param channel
     */
    public void invokeConnectionComingSync(String clientAddr, int clientPort, Channel channel);
}
