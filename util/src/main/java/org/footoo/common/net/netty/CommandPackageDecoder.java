/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.net.netty;

import org.footoo.common.protocol.CommandPackage;
import org.footoo.common.tools.JsonSerializable;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

/**
 * package的协议解析工具
 * 每一个包由一个int长度和CommandPackage的序列化字符串组成
 * 
 * @author fengjing.yfj
 * @version $Id: CommandPackageDecoder.java, v 0.1 2014年2月14日 下午3:46:43 fengjing.yfj Exp $
 */
public class CommandPackageDecoder extends FrameDecoder {
    private final NettyConnection nettyConnection;

    public CommandPackageDecoder(NettyConnection nettyConnection) {
        this.nettyConnection = nettyConnection;
    }

    /** 
     * @see org.jboss.netty.handler.codec.frame.FrameDecoder#decode(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.Channel, org.jboss.netty.buffer.ChannelBuffer)
     */
    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer)
                                                                                             throws Exception {
        //长度信息还没有接收到
        if (buffer.readableBytes() < 4) {
            return null;
        }

        //已经接收到长度
        int len = buffer.getInt(buffer.readerIndex());

        //调用同步函数,这个是被多次调用的，需要被调用者进行去重
        nettyConnection.invokeRecvNewPackageSync(len);

        //看看完整的包是否已经获取到
        if (buffer.readableBytes() < len + 4) {
            return null;
        }
        //完整的一个包来了
        //跳过长度
        buffer.skipBytes(4);
        assert (len > 0);
        //解析包
        byte[] datas = new byte[len];
        buffer.readBytes(datas);
        CommandPackage commandPackage = JsonSerializable.deserialize(new String(datas),
            CommandPackage.class);
        //调用同步方法
        nettyConnection.invokeRecvFullPackageSync(commandPackage);

        //解析出来了一个包
        return commandPackage;
    }
}
