/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.net.netty;

import java.util.concurrent.ConcurrentHashMap;

import org.footoo.common.exception.CoverRegisterException;
import org.footoo.common.exception.InvalidHandlerRegisterException;
import org.footoo.common.net.netty.handlers.DefaultCommandHandler;

/**
 * 命令句柄的处理表，命令处理表按照命令的code进行句柄查找
 * 如果没有找到就使用默认的处理句柄
 * 
 * @author fengjing.yfj
 * @version $Id: CommandHandlerTable.java, v 0.1 2014年2月15日 下午5:38:31 fengjing.yfj Exp $
 */
public abstract class CommandHandlerTable {
    /** 所有的处理句柄 */
    private static final ConcurrentHashMap<Integer, CommandHandler> handlers = new ConcurrentHashMap<Integer, CommandHandler>();
    /** 默认的处理句柄,这个必须不能为空 */
    private static CommandHandler                                   defaultHandler;

    static {
        //注册默认的句柄
        setDefaultHandler(new DefaultCommandHandler());
        try {
            //注册echo句柄
            Class.forName("org.footoo.common.net.netty.handlers.EchoCommandHandler");
        } catch (ClassNotFoundException e) {
            ;
        }
    }

    /**
     * 注册句柄
     * 
     * @param handler 句柄
     * @throws CoverRegisterException 
     * @throws InvalidHandlerRegisterException 
     */
    public static final void registerHandler(CommandHandler handler) throws CoverRegisterException,
                                                                    InvalidHandlerRegisterException {
        if (handler.getCode() == CommandHandler.CODE_ALL) {
            throw new InvalidHandlerRegisterException("无法注册不提供code的CODE_ALL处理句柄");
        }
        //进行注册
        registerHandler(handler.getCode(), handler);

    }

    /**
     * 注册句柄
     * 
     * @param code 可以处理的命令码
     * @param handler
     * @throws CoverRegisterException
     */
    public static final void registerHandler(int code, CommandHandler handler)
                                                                              throws CoverRegisterException {
        CommandHandler former = handlers.get(code);
        if (former != null && !former.equals(handler)) {
            throw new CoverRegisterException("新句柄[" + handler + "]会覆盖旧句柄[" + former + "]");
        }
        handlers.put(code, handler);
    }

    /**
     * 获取处理句柄
     * 
     * @param code
     * @return
     */
    public static final CommandHandler findHandler(int code) {
        CommandHandler handler = handlers.get(code);
        return handler == null ? defaultHandler : handler;
    }

    /**
     * 设置默认的句柄
     * 
     * @param defaultHandler
     */
    public static final void setDefaultHandler(CommandHandler defaultHandler) {
        CommandHandlerTable.defaultHandler = defaultHandler;
    }

    /**
     * 获取默认的句柄
     * 
     * @return
     */
    public static final CommandHandler getDefaultHandler() {
        return defaultHandler;
    }
}
