/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.net.netty.handlers;

import org.footoo.common.exception.DistributeCommonException;
import org.footoo.common.protocol.CommandErrorCode;
import org.footoo.common.protocol.CommandPackage;

/**
 * 默认的处理句柄 
 * 
 * @author fengjing.yfj
 * @version $Id: DefaultCommandHandler.java, v 0.1 2014年2月15日 下午6:43:54 fengjing.yfj Exp $
 */
public class DefaultCommandHandler extends AbstractCommandHandler {

    /** 
     * @see org.footoo.common.net.netty.CommandHandler#handle(org.footoo.common.protocol.CommandPackage)
     */
    @Override
    public CommandPackage handle(CommandPackage commandPackage) throws DistributeCommonException {
        return new CommandPackage(CommandErrorCode.UNKNOWN_COMMAND, null,
            commandPackage.getOpaque(), null);
    }

    /** 
     * @see org.footoo.common.net.netty.CommandHandler#getCode()
     */
    @Override
    public int getCode() {
        return CODE_ALL;
    }

}
