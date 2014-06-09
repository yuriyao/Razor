/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.net.netty.handlers;

import org.footoo.common.exception.DistributeCommonException;
import org.footoo.common.net.netty.CommandHandlerTable;
import org.footoo.common.protocol.CommandCode;
import org.footoo.common.protocol.CommandErrorCode;
import org.footoo.common.protocol.CommandPackage;

/**
 * Echo的处理句柄
 * 
 * @author fengjing.yfj
 * @version $Id: EchoCommandHandler.java, v 0.1 2014年2月16日 下午2:02:45 fengjing.yfj Exp $
 */
public class EchoCommandHandler extends AbstractCommandHandler {

    /** 单例 */
    private static final EchoCommandHandler ECHO_COMMAND_HANDLER = new EchoCommandHandler();

    /** 
     * 使用单例
     */
    private EchoCommandHandler() {

    }

    static {
        //注册到命令表
        try {
            CommandHandlerTable.registerHandler(ECHO_COMMAND_HANDLER);
        } catch (Exception e) {
            ;
        }
    }

    /** 
     * @see org.footoo.common.net.netty.CommandHandler#handle(org.footoo.common.protocol.CommandPackage)
     */
    @Override
    public CommandPackage handle(CommandPackage commandPackage) throws DistributeCommonException {
        CommandPackage response = new CommandPackage(commandPackage);
        response.setErrorCode(CommandErrorCode.OK);

        return response;
    }

    /** 
     * @see org.footoo.common.net.netty.CommandHandler#getCode()
     */
    @Override
    public int getCode() {
        return CommandCode.ECHO.getCode();
    }

}
