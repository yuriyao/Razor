/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.net;

import org.footoo.common.exception.NetException;
import org.footoo.common.exception.NetTimeoutException;
import org.footoo.common.protocol.CommandPackage;

/**
 * 实现remoteClient,使用netty的nio
 * 
 * @author fengjing.yfj
 * @version $Id: RemoteClientImpl.java, v 0.1 2014年2月13日 下午7:13:49 fengjing.yfj Exp $
 */
public class RemoteClientNettyImpl implements RemoteClient {

    /** 
     * @see org.footoo.common.net.RemoteClient#invokeCommandSync(org.footoo.common.protocol.CommandPackage, int)
     */
    @Override
    public CommandPackage invokeCommandSync(CommandPackage commandPackage, int timeoutms)
                                                                                         throws NetTimeoutException,
                                                                                         NetException {
        return null;
    }

    /** 
     * @see org.footoo.common.net.RemoteClient#invokeCommandAsync(org.footoo.common.protocol.CommandPackage, int, org.footoo.common.net.CommandInvokedCallback)
     */
    @Override
    public void invokeCommandAsync(CommandPackage commandPackage, int timeoutms,
                                   CommandInvokedCallback callback) {
    }

    /** 
     * @see org.footoo.common.net.RemoteClient#sendResponseSync(org.footoo.common.protocol.CommandPackage, int)
     */
    @Override
    public void sendResponseSync(CommandPackage responsePackage, int timeoutms)
                                                                               throws NetTimeoutException,
                                                                               NetException {
    }

    /** 
     * @see org.footoo.common.net.RemoteClient#sendResponseAsync(org.footoo.common.protocol.CommandPackage, int, org.footoo.common.net.SendedCallback)
     */
    @Override
    public void sendResponseAsync(CommandPackage responsePackage, int timeoutms,
                                  SendedCallback callback) {
    }

    /** 
     * @see org.footoo.common.net.RemoteClient#start()
     */
    @Override
    public void start() {
    }

    /** 
     * @see org.footoo.common.net.RemoteClient#shutdown()
     */
    @Override
    public void shutdown() {
    }

}
