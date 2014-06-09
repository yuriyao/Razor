/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.http;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;

import org.footoo.common.buffer.ByteBuffer;

/**
 * 
 * @author jeff
 * @version $Id: SimpleHttpClient.java, v 0.1 2014年4月14日 下午4:56:34 jeff Exp $
 */
public class SimpleHttpClient extends SimpleTCPClient {

    public SimpleHttpClient(String host, int port) {
        super(host, port);
    }

    /**
     * 访问网页
     * 
     * @param method
     * @param headers
     * @param path
     * @param context
     * @return
     * @throws UnknownHostException
     * @throws IOException
     */
    public byte[] visit(HttpMethodEnum method, Map<String, String> headers, String path,
                        byte[] context) throws UnknownHostException, IOException {
        ByteBuffer buffer = new ByteBuffer();
        //构造第一行请求头
        buffer.append(method.getName() + " " + path + " HTTP/1.0\r\n");

        //添加头
        if (headers != null) {
            for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
                buffer.append(headerEntry.getKey() + ": " + headerEntry.getValue() + "\r\n");
            }
        }
        //添加长度
        if (context != null && context.length != 0) {
            buffer.append("Content-Length: " + context.length + "\r\n");
        }
        //http头结尾
        buffer.append("\r\n");
        //
        if (context != null && context.length != 0) {
            buffer.append(context);
        }
        //进行连接
        this.connect();
        //发送数据
        this.send(buffer.toByteArray());

        System.out.println(new String(buffer.toByteArray()));
        //接受数据
        return this.recvUntilClose();
    }

    /**
     * 访问网页,超时时常timeout毫秒
     * 
     * @param method
     * @param headers
     * @param path
     * @param context
     * @param timeout
     * @return
     * @throws UnknownHostException
     * @throws IOException
     */
    public byte[] visit(HttpMethodEnum method, Map<String, String> headers, String path,
                        byte[] context, int timeout) throws UnknownHostException, IOException {
        ByteBuffer buffer = new ByteBuffer();
        //构造第一行请求头
        buffer.append(method.getName() + " " + path + " HTTP/1.0\r\n");

        //添加头
        if (headers != null) {
            for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
                buffer.append(headerEntry.getKey() + ": " + headerEntry.getValue() + "\r\n");
            }
        }
        //添加长度
        if (context != null && context.length != 0) {
            buffer.append("Content-Length: " + context.length + "\r\n");
        }
        //http头结尾
        buffer.append("\r\n");
        //
        if (context != null && context.length != 0) {
            buffer.append(context);
        }
        //进行连接
        this.connect();
        //发送数据
        this.send(buffer.toByteArray());

        System.out.println(new String(buffer.toByteArray()));
        //接受数据
        return this.recvUntilClose(timeout);
    }
}
