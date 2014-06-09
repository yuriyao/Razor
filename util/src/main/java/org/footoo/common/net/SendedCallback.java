/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.net;

import org.footoo.common.protocol.CommandPackage;

/**
 * 报文发送结束后的回调函数
 * 
 * @author fengjing.yfj
 * @version $Id: SendedCallback.java, v 0.1 2014年2月12日 下午7:24:46 fengjing.yfj Exp $
 */
public interface SendedCallback {

    /**
     * 报文发送成功
     * 
     * @param commandPackage 想要发送的报文
     */
    public void sendOK(CommandPackage commandPackage);

    /**
     * 异常发生
     * 
     * @param commandPackage 想要发送的报文
     * @param throwable 发生的异常信息
     */
    public void exceptionOccur(CommandPackage commandPackage, Throwable throwable);
}
