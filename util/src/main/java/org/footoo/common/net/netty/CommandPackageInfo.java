/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.net.netty;

import org.footoo.common.protocol.CommandPackage;
import org.jboss.netty.channel.Channel;

/**
 * 
 * @author jeff
 * @version $Id: CommandPackageInfo.java, v 0.1 2014年3月16日 下午5:42:41 jeff Exp $
 */
public class CommandPackageInfo {
    /** 数据包 */
    private CommandPackage commandPackage;
    /** 通讯信道 */
    private Channel        channel;

    public CommandPackageInfo() {

    }

    public CommandPackageInfo(CommandPackage commandPackage, Channel channel) {
        this.commandPackage = commandPackage;
        this.channel = channel;
    }

    /**
     * @return the commandPackage
     */
    public CommandPackage getCommandPackage() {
        return commandPackage;
    }

    /**
     * @param commandPackage
     *            the commandPackage to set
     */
    public void setCommandPackage(CommandPackage commandPackage) {
        this.commandPackage = commandPackage;
    }

    /**
     * @return the channel
     */
    public Channel getChannel() {
        return channel;
    }

    /**
     * @param channel
     *            the channel to set
     */
    public void setChannel(Channel channel) {
        this.channel = channel;
    }

}
