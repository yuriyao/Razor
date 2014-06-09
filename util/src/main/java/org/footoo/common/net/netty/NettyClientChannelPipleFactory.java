/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.net.netty;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

/**
 * 客户端的pipeline的工厂方法
 * 
 * @author fengjing.yfj
 * @version $Id: NettyChannelPipleFactory.java, v 0.1 2014年2月14日 下午4:50:46 fengjing.yfj Exp $
 */
public class NettyClientChannelPipleFactory implements ChannelPipelineFactory {
    /** 连接 */
    private final NettyClientConnectionImpl nettyConnectionImpl;

    public NettyClientChannelPipleFactory(NettyClientConnectionImpl nettyConnectionImpl) {
        this.nettyConnectionImpl = nettyConnectionImpl;
    }

    /** 
     * @see org.jboss.netty.channel.ChannelPipelineFactory#getPipeline()
     */
    @Override
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline channelPipeline = Channels.pipeline();
        //添加工具链
        channelPipeline.addLast("decoder", new CommandPackageDecoder(nettyConnectionImpl));
        channelPipeline.addLast("encoder", new CommandPackageEncoder(nettyConnectionImpl));
        channelPipeline.addLast("handler", new NettyClientChannelHandler(nettyConnectionImpl));
        return channelPipeline;
    }

}
