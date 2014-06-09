/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.http;

import java.net.Socket;

/**
 * http服务器
 * 
 * @author jeff
 * @version $Id: HttpServer.java, v 0.1 2014年4月14日 上午10:45:41 jeff Exp $
 */
public class HttpServer extends MultiThreadSimpleTCPServer {

    public HttpServer(int port) {
        super(port);
    }

    /** 
     * @see org.footoo.common.http.MultiThreadSimpleTCPServer#multiThreadHandle(java.net.Socket)
     */
    @Override
    protected void multiThreadHandle(Socket socket) {
        /*ByteBuffer buffer = new ByteBuffer();
        byte[] bytes = new byte[100];
        int len = 0;
        try {
            while((len = socket.getInputStream().read(bytes)) != -1) {
                buffer.append(bytes, 0, len);
                String string = new String(buffer.toByteArray());
                if(string.contains("\r\n\r\n")) {
                    string.indexOf("\r\n\r\n");
                }
            }
        } catch (IOException e) {
            logger.error("", e);
        }*/
    }

}
