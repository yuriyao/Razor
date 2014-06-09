/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.net.netty;

/**
 * 连接状态监听器
 * 
 * @author fengjing.yfj
 * @version $Id: NettyConnectionStatusListener.java, v 0.1 2014年2月14日 上午11:22:10 fengjing.yfj Exp $
 */
public interface NettyConnectionStatusListener extends NettyListener {

    /**
     * 连接建立
     */
    public void connectionEstablished();

    /**
     * 连接发生异常
     * 
     * @param e
     */
    public void exceptionOccur(Throwable e);

    /**
     * 从异常中恢复
     */
    public void resume();

    /**
     * 连接关闭
     */
    public void connectionClosed();
}
