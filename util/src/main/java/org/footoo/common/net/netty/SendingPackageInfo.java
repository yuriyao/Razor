/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.net.netty;

import java.util.concurrent.CountDownLatch;

import org.footoo.common.net.CommandInvokedCallback;
import org.footoo.common.net.SendedCallback;
import org.footoo.common.protocol.CommandPackage;
import org.jboss.netty.channel.ChannelFuture;

/**
 * 待发送的package的信息
 * 
 * @author fengjing.yfj
 * @version $Id: SendingPackageInfo.java, v 0.1 2014年2月14日 下午2:28:17 fengjing.yfj Exp $
 */
public class SendingPackageInfo {
    /** 发送的报文 */
    private CommandPackage         commandPackage;
    /** 发送的期待结果 */
    private ChannelFuture          channelFuture;
    /** 发送完成的回调函数 */
    private SendedCallback         sendedCallback;
    /** 接受到响应的回调函数 */
    private CommandInvokedCallback commandInvokedCallback;
    /** 是否采用的异步的方式 */
    private boolean                async;
    /** 同步工具 */
    private CountDownLatch         countDownLatch;
    /** 响应的报文 */
    private CommandPackage         responsePackage;
    /** 处理时发生的异常 */
    private Throwable              cause;
    /** 操作是否成功 */
    private boolean                success;

    /**
     * Getter method for property <tt>commandPackage</tt>.
     * 
     * @return property value of commandPackage
     */
    public CommandPackage getCommandPackage() {
        return commandPackage;
    }

    /**
     * Setter method for property <tt>commandPackage</tt>.
     * 
     * @param commandPackage value to be assigned to property commandPackage
     */
    public void setCommandPackage(CommandPackage commandPackage) {
        this.commandPackage = commandPackage;
    }

    /**
     * Getter method for property <tt>channelFuture</tt>.
     * 
     * @return property value of channelFuture
     */
    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }

    /**
     * Setter method for property <tt>channelFuture</tt>.
     * 
     * @param channelFuture value to be assigned to property channelFuture
     */
    public void setChannelFuture(ChannelFuture channelFuture) {
        this.channelFuture = channelFuture;
    }

    /**
     * Getter method for property <tt>sendedCallback</tt>.
     * 
     * @return property value of sendedCallback
     */
    public SendedCallback getSendedCallback() {
        return sendedCallback;
    }

    /**
     * Setter method for property <tt>sendedCallback</tt>.
     * 
     * @param sendedCallback value to be assigned to property sendedCallback
     */
    public void setSendedCallback(SendedCallback sendedCallback) {
        this.sendedCallback = sendedCallback;
    }

    /**
     * Getter method for property <tt>commandInvokedCallback</tt>.
     * 
     * @return property value of commandInvokedCallback
     */
    public CommandInvokedCallback getCommandInvokedCallback() {
        return commandInvokedCallback;
    }

    /**
     * Setter method for property <tt>commandInvokedCallback</tt>.
     * 
     * @param commandInvokedCallback value to be assigned to property commandInvokedCallback
     */
    public void setCommandInvokedCallback(CommandInvokedCallback commandInvokedCallback) {
        this.commandInvokedCallback = commandInvokedCallback;
    }

    /**
     * Getter method for property <tt>oneWay</tt>.
     * 
     * @return property value of oneWay
     */
    public boolean isAsync() {
        return async;
    }

    /**
     * Setter method for property <tt>oneWay</tt>.
     * 
     * @param oneWay value to be assigned to property oneWay
     */
    public void setAsync(boolean async) {
        this.async = async;
    }

    /**
     * Getter method for property <tt>countDownLatch</tt>.
     * 
     * @return property value of countDownLatch
     */
    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    /**
     * Setter method for property <tt>countDownLatch</tt>.
     * 
     * @param countDownLatch value to be assigned to property countDownLatch
     */
    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    /**
     * Getter method for property <tt>responsePackage</tt>.
     * 
     * @return property value of responsePackage
     */
    public CommandPackage getResponsePackage() {
        return responsePackage;
    }

    /**
     * Setter method for property <tt>responsePackage</tt>.
     * 
     * @param responsePackage value to be assigned to property responsePackage
     */
    public void setResponsePackage(CommandPackage responsePackage) {
        this.responsePackage = responsePackage;
    }

    /**
     * Getter method for property <tt>cause</tt>.
     * 
     * @return property value of cause
     */
    public Throwable getCause() {
        return cause;
    }

    /**
     * Setter method for property <tt>cause</tt>.
     * 
     * @param cause value to be assigned to property cause
     */
    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    /**
     * Getter method for property <tt>success</tt>.
     * 
     * @return property value of success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Setter method for property <tt>success</tt>.
     * 
     * @param success value to be assigned to property success
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

}
