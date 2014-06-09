/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.net.netty;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

/**
 * 服务端的piple factory
 * 
 * @author fengjing.yfj
 * @version $Id: NettyServerChannelPipleFactory.java, v 0.1 2014年2月15日 下午8:01:20 fengjing.yfj Exp $
 */
public class NettyServerChannelPipleFactory implements ChannelPipelineFactory {
    private NettyServerConnectionImpl nettyServerConnection;

    public NettyServerChannelPipleFactory(NettyServerConnectionImpl nettyServerConnection) {
        this.nettyServerConnection = nettyServerConnection;
    }

    /** 
     * @see org.jboss.netty.channel.ChannelPipelineFactory#getPipeline()
     */
    @Override
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = Channels.pipeline();
        pipeline.addLast("encoder", new CommandPackageEncoder(nettyServerConnection));
        pipeline.addLast("decoder", new CommandPackageDecoder(nettyServerConnection));
        pipeline.addLast("handler", new NettyServerChannelHandler(nettyServerConnection));

        return pipeline;
    }

}
