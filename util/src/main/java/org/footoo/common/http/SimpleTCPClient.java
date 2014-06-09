/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.footoo.common.buffer.ByteBuffer;
import org.footoo.common.log.Logger;
import org.footoo.common.log.LoggerFactory;

/**
 * 简单TCP客户端
 * 
 * @author jeff
 * @version $Id: SimpleTCPClient.java, v 0.1 2014年4月14日 上午11:04:49 jeff Exp $
 */
public class SimpleTCPClient {
    /** 客户端 */
    private Socket              client;
    /** 目标主机 */
    private String              host;
    /** 目标端口 */
    private int                 port;
    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(SimpleTCPClient.class);

    public SimpleTCPClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * 进行连接
     * 
     * @throws UnknownHostException
     * @throws IOException
     */
    public void connect() throws UnknownHostException, IOException {
        client = new Socket();
        client.connect(new InetSocketAddress(host, port));
    }

    /**
     * 进行发送
     * 
     * @param buffer
     * @param begin
     * @param len
     * @throws IOException
     */
    public void send(byte[] buffer, int begin, int len) throws IOException {
        client.getOutputStream().write(buffer, begin, len);
    }

    /**
     * 进行发送
     * 
     * @param buffer
     * @throws IOException
     */
    public void send(byte[] buffer) throws IOException {
        client.getOutputStream().write(buffer);
    }

    /**
     * 接受数据
     * 
     * @param buffer
     * @param from
     * @param len
     * @return
     * @throws IOException
     */
    public int recv(byte[] buffer, int from, int len) throws IOException {
        //client.getInputStream().read(b)
        return client.getInputStream().read(buffer, from, len);
    }

    /**
     * 接受数据，并且只等待timeout毫秒
     * 
     * @param buffer
     * @param from
     * @param len
     * @param timeout
     * @return
     * @throws IOException 
     */
    public int recv(byte[] buffer, int from, int len, int timeout) throws IOException {
        //设置超时时间
        try {
            client.setSoTimeout(timeout);
        } catch (SocketException e) {
            logger.error(e, "设置超时时间发生异常");
        }
        int result = client.getInputStream().read(buffer, from, len);

        //取消超时设置
        try {
            client.setSoTimeout(0);
        } catch (SocketException e) {
            logger.error(e, "取消超时时间发生异常");
        }

        return result;
    }

    /**
     * 接受数据，知道对方关闭连接
     * 
     * @return
     * @throws IOException 
     */
    public byte[] recvUntilClose() throws IOException {
        //注意这是内部实现的变长的byte缓存
        ByteBuffer byteBuffer = new ByteBuffer();
        byte[] buffer = new byte[100];
        try {
            int len = 0;
            while ((len = client.getInputStream().read(buffer)) != -1) {
                byteBuffer.append(buffer, 0, len);
            }
        } catch (IOException e) {
            logger.error(e, "接受数据发生异常");
            throw e;
        }

        return byteBuffer.toByteArray();
    }

    /**
     * 接受数据，知道对方关闭连接
     * 
     * @param timeout
     * @return
     * @throws IOException
     */
    public byte[] recvUntilClose(int timeout) throws IOException {
        //设置超时时间
        try {
            client.setSoTimeout(timeout);
        } catch (SocketException e) {
            logger.error(e, "设置超时时间发生异常");
        }
        byte[] result = recvUntilClose();
        //取消超时设置
        try {
            client.setSoTimeout(0);
        } catch (SocketException e) {
            logger.error(e, "取消超时时间发生异常");
        }

        return result;
    }

    /**
     * Getter method for property <tt>client</tt>.
     * 
     * @return property value of client
     */
    public final Socket getClient() {
        return client;
    }

    /**
     * Setter method for property <tt>client</tt>.
     * 
     * @param client value to be assigned to property client
     */
    public final void setClient(Socket client) {
        this.client = client;
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

}
