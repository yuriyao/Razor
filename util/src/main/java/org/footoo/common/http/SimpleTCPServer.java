/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.footoo.common.log.Logger;
import org.footoo.common.log.LoggerFactory;

/**
 * 简单TCP服务端
 * 
 * @author jeff
 * @version $Id: HttpServer.java, v 0.1 2014年4月14日 上午9:53:26 jeff Exp $
 */
public abstract class SimpleTCPServer {
    /** 服务端 */
    private ServerSocket        server;
    /** 绑定的端口 */
    private int                 port   = 8080;
    /** 绑定的地址 */
    private String              host;
    /** 日志 */
    private final static Logger logger = LoggerFactory.getLogger(SimpleTCPServer.class);
    /** 是否活跃 */
    private boolean             alive  = true;
    /** 重试次数 */
    private int                 retry  = 0;

    public SimpleTCPServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public SimpleTCPServer(int port) {
        this.port = port;
    }

    /**
     * 启动服务器
     * @throws IOException 
     */
    public final void start() throws IOException {
        try {
            server = new ServerSocket(port, 40);
        } catch (IOException e) {
            logger.error(e, "无法启动服务端, [port=" + port + "]");
            throw e;
        }
        while (alive) {
            try {
                Socket client = server.accept();
                handle(client);
            } catch (IOException e) {
                //进行重试，允许一定的重试次数
                retry++;
                logger.warn(e, "接受连接发生异常，重试次数[" + retry + "]");
                if (retry >= 5) {
                    alive = false;
                    throw e;
                }
            } catch (Exception e) {//这个可能是handle抛出的异常，直接包裹
                logger.warn(e, "发现未知异常");
            }
            retry = 0;
        }

    }

    /**
     * 实际的处理句柄，由子类去实现
     * 
     * @param socket
     */
    protected abstract void handle(Socket socket);

    /**
     * Getter method for property <tt>port</tt>.
     * 
     * @return property value of port
     */
    public final int getPort() {
        return port;
    }

    /**
     * Setter method for property <tt>port</tt>.
     * 
     * @param port value to be assigned to property port
     */
    public final void setPort(int port) {
        this.port = port;
    }

    /**
     * Getter method for property <tt>host</tt>.
     * 
     * @return property value of host
     */
    public final String getHost() {
        return host;
    }

    /**
     * Setter method for property <tt>host</tt>.
     * 
     * @param host value to be assigned to property host
     */
    public final void setHost(String host) {
        this.host = host;
    }

}
