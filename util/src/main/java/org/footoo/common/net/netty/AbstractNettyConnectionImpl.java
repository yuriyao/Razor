/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.net.netty;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import org.footoo.common.protocol.CommandPackage;

/**
 * 
 * @author fengjing.yfj
 * @version $Id: AbstractNettyConnectionImpl.java, v 0.1 2014年2月15日 下午7:23:47 fengjing.yfj Exp $
 */
public abstract class AbstractNettyConnectionImpl implements NettyConnection {

    /** 监听器列表 */
    protected ConcurrentHashMap<NettyListener, NettyListener> listeners = new ConcurrentHashMap<NettyListener, NettyListener>();

    /** 
     * @see org.footoo.common.net.netty.NettyConnection#registerNettyListener(org.footoo.common.net.netty.NettyListener)
     */
    @Override
    public void registerNettyListener(NettyListener listener) {
        //添加新的监听器
        listeners.put(listener, listener);
    }

    /** 
     * @see org.footoo.common.net.netty.NettyConnection#unregisterNettyListener(org.footoo.common.net.netty.NettyListener)
     */
    @Override
    public void unregisterNettyListener(NettyListener listener) {
        listeners.remove(listener);
    }

    /** 防止多次调用 */
    private boolean invokedRecvNewPackage = false;

    /**
     * 调用接受到新的包时的回调函数
     * 注意，只要一有包含长度的报文到达，就会调用这个函数
     * 所以这个函数必须使用invokedRecvNewPackage
     * 
     * @param len
     */
    @Override
    public void invokeRecvNewPackageSync(int len) {
        //还是在接受同一个包
        if (invokedRecvNewPackage) {
            return;
        }
        //调用同步函数
        Enumeration<NettyListener> listenerList = listeners.keys();
        while (listenerList.hasMoreElements()) {
            NettyListener listener = listenerList.nextElement();
            if (listener instanceof NettyRecvPackageListener) {
                ((NettyRecvPackageListener) listener).newPackageComing(len);
            }
        }
    }

    /**
     * 接收到一个完整的包，调用的回调函数
     * 
     * @param commandPackage
     */
    @Override
    public void invokeRecvFullPackageSync(CommandPackage commandPackage) {
        //可以继续调用接受到新package的同步函数
        invokedRecvNewPackage = false;
        //调用同步函数
        Enumeration<NettyListener> listenerList = listeners.keys();
        while (listenerList.hasMoreElements()) {
            NettyListener listener = listenerList.nextElement();
            if (listener instanceof NettyRecvPackageListener) {
                ((NettyRecvPackageListener) listener).fullPackageReceived(commandPackage);
            }
        }
    }

    /**
     * 发送一个报文前调用的同步函数
     * 
     * @param commandPackage
     */
    @Override
    public void invokeBeforePackageSendRecv(CommandPackage commandPackage) {
        //调用同步函数
        Enumeration<NettyListener> listenerList = listeners.keys();
        while (listenerList.hasMoreElements()) {
            NettyListener listener = listenerList.nextElement();
            if (listener instanceof NettySendPackageListener) {
                ((NettySendPackageListener) listener).beforePackageSend(commandPackage);
            }
        }
    }

    /**
     * 发送一个报文后调用的同步函数
     * 
     * @param commandPackage
     */
    @Override
    public void invokeAfterPackageSendRecv(CommandPackage commandPackage) {
        //调用同步函数
        Enumeration<NettyListener> listenerList = listeners.keys();
        while (listenerList.hasMoreElements()) {
            NettyListener listener = listenerList.nextElement();
            if (listener instanceof NettySendPackageListener) {
                ((NettySendPackageListener) listener).afterPackageSend(commandPackage);
            }
        }
    }
}
