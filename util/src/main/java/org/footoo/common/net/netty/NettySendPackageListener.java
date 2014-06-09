/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.net.netty;

import org.footoo.common.protocol.CommandPackage;

/**
 * 发送报文时的监听器
 * 
 * @author fengjing.yfj
 * @version $Id: NettySendPackageListener.java, v 0.1 2014年2月14日 上午11:12:14 fengjing.yfj Exp $
 */
public interface NettySendPackageListener extends NettyListener {

    /**
     * 在package被发送之前被调用
     * 
     * @param commandPackage
     */
    public void beforePackageSend(CommandPackage commandPackage);

    /**
     * 在package被发送之后被调用
     * 
     * @param commandPackage
     */
    public void afterPackageSend(CommandPackage commandPackage);
}
