/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.net.netty;

import java.net.InetSocketAddress;
import java.util.Enumeration;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * netty的服务端
 * 
 * @author fengjing.yfj
 * @version $Id: NettyServerImpl.java, v 0.1 2014年2月15日 下午5:22:26 fengjing.yfj Exp $
 */
public class NettyServerConnectionImpl extends AbstractNettyConnectionImpl implements
                                                                          NettyServerConnection {
    /** 绑定的地址 */
    private String   addr;

    /** 绑定的端口 */
    private int      port;

    /** 线程池大小 */
    private int      threadPoolSize = 8;

    /** 线程池 */
    private Executor executor;

    /**
     * 构造函数
     * 
     * @param port
     */
    public NettyServerConnectionImpl(int port) {
        this(null, port);
    }

    /**
     * 构造器
     * 
     * @param addr
     * @param port
     */
    public NettyServerConnectionImpl(String addr, int port) {
        this.addr = addr;
        this.port = port;
    }

    /**
     * 启动
     */
    public void start() {
        //线程池
        if (executor == null) {
            executor = Executors.newFixedThreadPool(threadPoolSize);
        }
        ChannelFactory channelFactory = new NioServerSocketChannelFactory(executor, executor);
        //ChannelFactory channelFactory = new NioServerSocketChannelFactory(
        //Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
        ServerBootstrap serverBootstrap = new ServerBootstrap(channelFactory);
        serverBootstrap.setPipelineFactory(new NettyServerChannelPipleFactory(this));

        //启动
        InetSocketAddress localAddress;
        //设置邦定地址
        if (addr != null) {
            localAddress = new InetSocketAddress(addr, port);
        } else {
            localAddress = new InetSocketAddress(port);
        }
        //绑定地址
        serverBootstrap.bind(localAddress);
    }

    @Override
    public void invokeConnectionComingSync(String clientAddr, int clientPort, Channel channel) {
        //调用同步函数
        Enumeration<NettyListener> listenerList = listeners.keys();
        while (listenerList.hasMoreElements()) {
            NettyListener listener = listenerList.nextElement();
            if (listener instanceof NettyConnectionComingListener) {
                ((NettyConnectionComingListener) listener).connecting(clientAddr, clientPort,
                    channel);
            }
        }
    }

    /**
     * Getter method for property <tt>addr</tt>.
     * 
     * @return property value of addr
     */
    public String getAddr() {
        return addr;
    }

    /**
     * Setter method for property <tt>addr</tt>.
     * 
     * @param addr value to be assigned to property addr
     */
    public void setAddr(String addr) {
        this.addr = addr;
    }

    /**
     * Getter method for property <tt>port</tt>.
     * 
     * @return property value of port
     */
    public int getPort() {
        return port;
    }

    /**
     * Setter method for property <tt>port</tt>.
     * 
     * @param port value to be assigned to property port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Getter method for property <tt>threadPoolSize</tt>.
     * 
     * @return property value of threadPoolSize
     */
    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    /**
     * Setter method for property <tt>threadPoolSize</tt>.
     * 
     * @param threadPoolSize value to be assigned to property threadPoolSize
     */
    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    /**
     * Getter method for property <tt>executor</tt>.
     * 
     * @return property value of executor
     */
    public Executor getExecutor() {
        return executor;
    }

    /**
     * Setter method for property <tt>executor</tt>.
     * 
     * @param executor value to be assigned to property executor
     */
    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

}
