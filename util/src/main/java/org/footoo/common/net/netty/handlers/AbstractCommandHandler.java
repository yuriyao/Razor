/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.net.netty.handlers;

import org.footoo.common.exception.DistributeCommonException;
import org.footoo.common.log.Logger;
import org.footoo.common.log.LoggerFactory;
import org.footoo.common.net.netty.CommandHandler;
import org.footoo.common.net.netty.CommandPackageInfo;
import org.footoo.common.protocol.CommandErrorCode;
import org.footoo.common.protocol.CommandPackage;

/**
 * 
 * @author jeff
 * @version $Id: AbstractCommandHandler.java, v 0.1 2014年3月16日 下午5:43:32 jeff Exp $
 */
public abstract class AbstractCommandHandler implements CommandHandler {
    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(AbstractCommandHandler.class);

    @Override
    public void handle(CommandPackageInfo commandPackageInfo) {
        // TODO Auto-generated method stub
        CommandPackage reponse = null;
        try {
            reponse = handle(commandPackageInfo.getCommandPackage());

        } catch (DistributeCommonException e) {
            reponse = new CommandPackage(CommandErrorCode.SERVER_INNER_ERROR, null, 0, null);
        } catch (Exception e) {
            reponse = new CommandPackage(CommandErrorCode.SERVER_INNER_ERROR, null, 0, null);
        }
        //
        logger.info("response " + reponse);
        reponse.setOpaque(commandPackageInfo.getCommandPackage().getOpaque());
        commandPackageInfo.getChannel().write(reponse);
    }

    protected abstract CommandPackage handle(CommandPackage commandPackage)
                                                                           throws DistributeCommonException;

}
