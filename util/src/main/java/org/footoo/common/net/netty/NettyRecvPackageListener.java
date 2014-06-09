/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.net.netty;

import org.footoo.common.protocol.CommandPackage;

/**
 * 接受到package时的监听器
 * 
 * @author fengjing.yfj
 * @version $Id: NettyRecvPackageListener.java, v 0.1 2014年2月14日 上午11:16:35 fengjing.yfj Exp $
 */
public interface NettyRecvPackageListener extends NettyListener {

    /**
     * 一个新的包到达
     * 
     * @param packageLength 新报的长度
     */
    public void newPackageComing(int packageLength);

    /**
     * 获取到新的报文片段
     * 
     * @param msg 报文片段的存放数组
     * @param begin 新报文在msg的起始
     * @param end 新报文在msg的结束
     */
    public void recvingPackage(byte[] msg, int begin, int end);

    /**
     * 一个新的报文接收到
     * 
     * @param commandPackage
     */
    public void fullPackageReceived(CommandPackage commandPackage);
}
