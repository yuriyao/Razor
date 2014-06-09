/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.net;

import org.footoo.common.protocol.CommandPackage;

/**
 * 远程请求调用结果返回时的回调函数
 * 需要处理成功和异常两种情况
 * 
 * @author fengjing.yfj
 * @version $Id: CommandCallback.java, v 0.1 2014年2月12日 下午7:15:43 fengjing.yfj Exp $
 */
public interface CommandInvokedCallback {
    /**
     * 接受到响应报文时候进行回调
     * 
     * @param responsePackage 响应的报文
     */
    public void responseReceived(CommandPackage responsePackage);

    /**
     * 发生异常
     * 
     * @param e 发生的异常
     */
    public void exceptionOccur(Exception e);
}
