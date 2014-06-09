/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.net.netty;

import org.footoo.common.protocol.CommandPackage;
import org.footoo.common.tools.JsonSerializable;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

/**
 * 传输报文的编码,编码方法 commandPackage序列化数据的长度 + commadPackage的序列化数据
 * 
 * @author fengjing.yfj
 * @version $Id: CommandPackageEncoder.java, v 0.1 2014年2月14日 下午4:41:39 fengjing.yfj Exp $
 */
public class CommandPackageEncoder extends OneToOneEncoder {

    private final NettyConnection nettyConnection;

    public CommandPackageEncoder(NettyConnection nettyConnection) {
        this.nettyConnection = nettyConnection;
    }

    /** 
     * @see org.jboss.netty.handler.codec.oneone.OneToOneEncoder#encode(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.Channel, java.lang.Object)
     */
    @Override
    protected Object encode(ChannelHandlerContext ctx, Channel channel, Object obj)
                                                                                   throws Exception {
        //类型不对
        if (!(obj instanceof CommandPackage)) {
            return obj;
        }
        //序列化报文
        String datas = JsonSerializable.serialize(obj);
        byte array[] = datas.getBytes();
        //进行编码
        ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
        buffer.writeInt(array.length);
        buffer.writeBytes(array);
        //调用同步函数
        nettyConnection.invokeBeforePackageSendRecv((CommandPackage) obj);

        return buffer;
    }

}
