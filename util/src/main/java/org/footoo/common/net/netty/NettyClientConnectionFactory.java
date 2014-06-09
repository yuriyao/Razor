/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.net.netty;

import java.util.concurrent.ConcurrentHashMap;

import org.footoo.common.log.Logger;
import org.footoo.common.log.LoggerFactory;

/**
 * 客户端的连接工厂
 * 
 * @author fengjing.yfj
 * @version $Id: NettyConnectionFactory.java, v 0.1 2014年2月14日 下午2:40:17 fengjing.yfj Exp $
 */
public abstract class NettyClientConnectionFactory {

    /** 连接池 */
    private static final ConcurrentHashMap<String, NettyClientConnectionImpl> connections = new ConcurrentHashMap<String, NettyClientConnectionImpl>();
    /** 日志 */
    private static final Logger                                               logger      = LoggerFactory
                                                                                              .getLogger(NettyClientConnectionFactory.class);

    /**
     * 获取连接
     * 
     * @param ip
     * @param port
     * @return
     */
    public static final NettyClientConnection getConnection(String ip, int port) {
        String key = ip + ":" + port;

        NettyClientConnectionImpl connection = connections.get(key);
        //连接可能已经关闭
        if (connection != null && !connection.isRunning()) {
            connections.remove(key, connection);
            connection = null;
        }

        //还没有建立连接
        if (connection == null) {
            connection = new NettyClientConnectionImpl(ip, port);
            try {
                connection.startSync();
            } catch (Exception e) {
                logger.error(e, "无法连接[" + ip + ":" + port + "]");
                return null;
            }
            connections.putIfAbsent(key, connection);
        }

        return connections.get(key);
    }
}
