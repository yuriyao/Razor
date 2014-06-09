/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.net.netty;

import org.footoo.common.exception.DistributeCommonException;

/**
 * 命令的处理句柄，这个类实现最好是无状态和线程安全的
 * 
 * @author fengjing.yfj
 * @version $Id: CommandHandler.java, v 0.1 2014年2月15日 下午5:33:10 fengjing.yfj
 *          Exp $
 */
public interface CommandHandler {

    /** 可以处理任何命令，注册的时候必须手动指定一个code */
    public static final int CODE_ALL = -1;

    /**
     * 处理请求命令
     * 
     * @param commandPackage
     * @return 响应报文，如果为空，表示不进行报文的响应
     * @throws DistributeCommonException
     */
    public void handle(CommandPackageInfo commandPackage);

    /**
     * 句柄处理的命令
     * 
     * @return 返回CODE_ALL 表示需要手动指定code；否则注册的时候抛出异常
     */
    public int getCode();
}
