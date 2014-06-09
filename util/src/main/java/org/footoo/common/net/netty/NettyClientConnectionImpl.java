/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.net.netty;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.footoo.common.exception.DistributeCommonException;
import org.footoo.common.exception.NetException;
import org.footoo.common.exception.NetTimeoutException;
import org.footoo.common.log.Logger;
import org.footoo.common.log.LoggerFactory;
import org.footoo.common.net.CommandInvokedCallback;
import org.footoo.common.net.SendedCallback;
import org.footoo.common.protocol.CommandPackage;
import org.footoo.common.tools.JsonSerializable;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

/**
 * netty 的connection的实现，对于同一个（地址，端口）复用一个连接，不管是多少个线程
 * 
 * @author fengjing.yfj
 * @version $Id: NettyConnectionImpl.java, v 0.1 2014年2月14日 下午2:40:01 fengjing.yfj Exp $
 */
public class NettyClientConnectionImpl extends AbstractNettyConnectionImpl implements
                                                                          NettyClientConnection {

    /** 正在发送的报文的缓冲区 */
    private ConcurrentHashMap<ChannelFuture, SendingPackageInfo> sendingPackages  = new ConcurrentHashMap<ChannelFuture, SendingPackageInfo>();

    /** 用来等待响应的报文 */
    private ConcurrentHashMap<Long, SendingPackageInfo>          responsePackages = new ConcurrentHashMap<Long, SendingPackageInfo>();

    /** 目标的地址 */
    private String                                               addr             = "127.0.0.1";

    /** 连接的端口 */
    private int                                                  port             = 1234;

    /** 对应的实际的channel */
    private Channel                                              channel;

    /** channel Factory */
    private ChannelFactory                                       channelFactory;

    /** 状态 */
    private Status                                               status           = Status.INIT;

    /** 日志 */
    private static final Logger                                  logger           = LoggerFactory
                                                                                      .getLogger(NettyClientConnectionImpl.class);

    /**
     * 构造器
     * 
     * @param addr
     */
    public NettyClientConnectionImpl(String addr) {
        this.addr = addr;
    }

    /**
     * 构造器
     * 
     * @param port
     */
    public NettyClientConnectionImpl(int port) {
        this.port = port;
    }

    public NettyClientConnectionImpl(String addr, int port) {
        this.addr = addr;
        this.port = port;
    }

    public NettyClientConnectionImpl(Channel channel) {
        this.channel = channel;
        InetSocketAddress socketAddress = (InetSocketAddress) channel.getRemoteAddress();
        //已经设置了地址
        if (socketAddress != null) {
            this.addr = socketAddress.getAddress().getHostAddress();
            this.port = socketAddress.getPort();
        }
    }

    public NettyClientConnectionImpl(Channel channel, String addr, int port) {
        this.channel = channel;
        this.port = port;
        this.addr = addr;
    }

    /**
     * 同步的方式启动
     * 
     * @throws DistributeCommonException 
     */
    public void startSync() throws DistributeCommonException {
        status = Status.STARTING;
        if (channel == null) {
            if (channelFactory == null) {
                Executor executor = Executors.newFixedThreadPool(6);
                channelFactory = new NioClientSocketChannelFactory(executor, executor);
            }
            ClientBootstrap clientBootstrap = new ClientBootstrap(channelFactory);
            //设置处理链
            clientBootstrap.setPipelineFactory(new NettyClientChannelPipleFactory(this));
            //进行连接
            ChannelFuture future = clientBootstrap.connect(new InetSocketAddress(addr, port));
            //同步工具
            final CountDownLatch countDownLatch = new CountDownLatch(1);

            future.addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture f) throws Exception {
                    if (!f.isSuccess()) {
                        status = Status.STOPPED;

                        throw new NetException(f.getCause(), "连接[" + addr + ":" + port + "]失败");
                    } else {
                        //设置channel
                        channel = f.getChannel();
                        System.out.println("连接成功");
                    }
                    countDownLatch.countDown();
                }
            });

            //等待连接完成
            try {
                countDownLatch.await(10 * 1000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                status = Status.STOPPED;
                logger.warn(e);
            }
            //已经停止了，干
            if (status == Status.STOPPED) {
                throw new DistributeCommonException();
            }

        } else if (!channel.isConnected()) {
            //重新连接一下
            //channel.connect(new InetSocketAddress(addr, port));
            status = Status.STOPPED;
            throw new DistributeCommonException("无法启动关闭的连接");
        }

        status = Status.RUNNING;
    }

    /**
     * 关闭
     */
    public void shutDown() {
        if (isRunning()) {
            channel.close();
            status = Status.STOPPED;
        }
    }

    /**
     * 获取状态
     * 
     * @return
     */
    public Status getStatus() {
        return status;
    }

    /**
     * 是否正在运行
     * 
     * @return
     */
    public boolean isRunning() {
        return status == Status.RUNNING;
    }

    /**
     * 保存响应报文
     * 
     * @param commandPackage
     */
    public void saveResponsePackage(CommandPackage commandPackage) {
        SendingPackageInfo sendingPackageInfo = responsePackages.get(commandPackage.getOpaque());
        //接受到的数据
        //logger.info("接受到响应报文" + JsonSerializable.serialize(commandPackage));
        //可能已经发生超时，用于保存响应信息的槽已经被删除
        if (sendingPackageInfo == null) {
            logger.warn("没有找到报文[" + JsonSerializable.serialize(commandPackage) + "]的控制信息");
            return;
        }
        //异步的方式，不需要实际保存数据
        if (sendingPackageInfo.isAsync()) {
            //logger.info("异步方式处理报文" + JsonSerializable.serialize(commandPackage));
            handleAsyncResponse(commandPackage);
            return;
        }
        //logger.info("同步处理报文" + JsonSerializable.serialize(commandPackage));
        //开始保存数据
        sendingPackageInfo.setResponsePackage(commandPackage);
        //唤醒等待数据响应的线程
        sendingPackageInfo.getCountDownLatch().countDown();
    }

    /** 
     * @see org.footoo.common.net.netty.NettyConnection#getDestAddr()
     */
    @Override
    public String getDestAddr() {
        return addr;
    }

    @Override
    public int getPort() {
        return port;
    }

    /**
     * 处理异步报文的到达
     * 
     * @param responsePackage
     */
    public void handleAsyncResponse(CommandPackage responsePackage) {
        SendingPackageInfo sendingPackageInfo = responsePackages
            .remove(responsePackage.getOpaque());
        if (sendingPackageInfo == null || sendingPackageInfo.getCommandInvokedCallback() == null) {
            return;
        }

        sendingPackageInfo.getCommandInvokedCallback().responseReceived(responsePackage);

    }

    /** 
     * @throws DistributeCommonException 
     * @throws  
     * @see org.footoo.common.net.netty.NettyConnection#invokeCommandSync(org.footoo.common.protocol.CommandPackage, int)
     */
    @Override
    public CommandPackage invokeCommandSync(CommandPackage commandPackage, int timeoutms)
                                                                                         throws DistributeCommonException {
        //没有在运行，不能发送
        if (!isRunning()) {
            throw new NetException("还没有启动连接,或者连接还没有完成");
        }
        /////必须先设置发送数据信息，否则当发送接受数据速度过快，下面的还没有设置好
        ////导致接受到报文后找不到报文控制信息
        //保存发送的数据的信息
        SendingPackageInfo sendingPackageInfo = new SendingPackageInfo();
        //初始化保存的发送数据信息
        sendingPackageInfo.setAsync(false);
        sendingPackageInfo.setCommandPackage(commandPackage);
        sendingPackageInfo.setCountDownLatch(new CountDownLatch(1));
        //保存到请求结果处
        responsePackages.put(commandPackage.getOpaque(), sendingPackageInfo);
        //发送数据
        ///放到了设置报文信息的后面
        ChannelFuture future = channel.write(commandPackage);
        sendingPackageInfo.setChannelFuture(future);

        //等待超时
        try {
            sendingPackageInfo.getCountDownLatch().await(timeoutms, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            //throw new DistributeCommonException(e);
            e.printStackTrace();
            ;
        }
        //获取报文信息
        sendingPackageInfo = responsePackages.remove(commandPackage.getOpaque());
        //不可能出现的情况
        if (sendingPackageInfo == null) {
            throw new DistributeCommonException("无法获取报文[" + commandPackage + "]的报文信息");
        }
        //无法获取响应的报文
        if (sendingPackageInfo.getResponsePackage() == null) {
            Throwable cause = sendingPackageInfo.getCause();
            //出现异常
            if (cause != null) {
                if (cause instanceof DistributeCommonException) {
                    throw (DistributeCommonException) cause;
                } else {
                    throw new DistributeCommonException(cause);
                }
            } else {
                //超时
                throw new NetTimeoutException("invokeCommandSync发生超时");
            }
        }
        //成功获取到响应报文

        return sendingPackageInfo.getResponsePackage();
    }

    @Override
    public void invokeCommandAsync(CommandPackage commandPackage, CommandInvokedCallback callback)
                                                                                                  throws NetException {
        //没有在运行，不能发送
        if (!isRunning()) {
            throw new NetException("还没有启动连接,或者连接还没有完成");
        }

        //保存发送的数据的信息
        SendingPackageInfo sendingPackageInfo = new SendingPackageInfo();
        //初始化保存的发送数据信息

        sendingPackageInfo.setAsync(true);
        sendingPackageInfo.setCommandPackage(commandPackage);
        sendingPackageInfo.setCountDownLatch(null);
        sendingPackageInfo.setCommandInvokedCallback(callback);

        //保存到请求结果处
        responsePackages.put(commandPackage.getOpaque(), sendingPackageInfo);

        //发送数据
        ChannelFuture future = channel.write(commandPackage);
        sendingPackageInfo.setChannelFuture(future);
    }

    /** 
     * @throws DistributeCommonException 
     * @see org.footoo.common.net.netty.NettyConnection#sendResponseSync(org.footoo.common.protocol.CommandPackage, int)
     */
    @Override
    public void sendResponseSync(CommandPackage commandPackage, int timeoutms)
                                                                              throws DistributeCommonException {
        //没有在运行，不能发送
        if (!isRunning()) {
            throw new NetException("还没有启动连接,或者连接还没有完成");
        }
        //发送数据
        ChannelFuture future = channel.write(commandPackage);
        //保存发送的数据的信息
        SendingPackageInfo sendingPackageInfo = new SendingPackageInfo();
        //初始化保存的发送数据信息
        sendingPackageInfo.setChannelFuture(future);
        sendingPackageInfo.setAsync(false);
        sendingPackageInfo.setCommandPackage(commandPackage);
        sendingPackageInfo.setCountDownLatch(new CountDownLatch(1));
        //保存到请求结果处
        sendingPackages.put(future, sendingPackageInfo);
        //
        future.addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                SendingPackageInfo packageInfo = sendingPackages.get(future);
                if (packageInfo == null) {
                    return;
                }
                if (future.isSuccess()) {
                    packageInfo.setSuccess(true);
                } else {
                    packageInfo.setSuccess(false);
                    packageInfo.setCause(future.getCause());
                }
            }
        });

        //等待超时
        try {
            sendingPackageInfo.getCountDownLatch().await(timeoutms, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            //throw new DistributeCommonException(e);
            ;
        }
        //查看结果
        sendingPackageInfo = sendingPackages.get(future);
        if (sendingPackageInfo == null) {
            throw new DistributeCommonException("无法获取package信息");
        }
        //
        if (!sendingPackageInfo.isSuccess()) {
            //超时
            if (sendingPackageInfo.getCause() == null) {
                throw new NetTimeoutException("发送报文[" + commandPackage + "]超时");
            }
            //本系统异常
            else if (sendingPackageInfo.getCause() instanceof DistributeCommonException) {
                throw (DistributeCommonException) sendingPackageInfo.getCause();
            }
            //其他异常，包裹一下
            else {
                throw new DistributeCommonException(sendingPackageInfo.getCause());
            }
        }
    }

    @Override
    public void sendResponseAsync(final CommandPackage commandPackage, final SendedCallback callback)
                                                                                                     throws NetException {
        //没有在运行，不能发送
        if (!isRunning()) {
            throw new NetException("还没有启动连接,或者连接还没有完成");
        }
        //发送数据
        ChannelFuture future = channel.write(commandPackage);

        //
        if (callback != null) {
            future.addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        callback.sendOK(commandPackage);
                    } else {
                        callback.exceptionOccur(commandPackage, future.getCause());
                    }
                }
            });
        }
    }

}
