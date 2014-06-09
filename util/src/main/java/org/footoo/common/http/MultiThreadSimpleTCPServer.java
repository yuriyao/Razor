/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.http;

import java.io.IOException;
import java.net.Socket;

/**
 * 多线程版本的简单的TCP服务器
 * 
 * @author jeff
 * @version $Id: MultiThreadSimpleTCPServer.java, v 0.1 2014年4月14日 上午10:28:15 jeff Exp $
 */
public abstract class MultiThreadSimpleTCPServer extends SimpleTCPServer {

    public MultiThreadSimpleTCPServer(int port) {
        super(port);
    }

    /** 
     * @see org.footoo.common.http.SimpleTCPServer#handle(java.net.Socket)
     */
    @Override
    protected void handle(Socket socket) {
        //启动处理线程
        new Thread(new Task(socket)).start();
    }

    /**
     * 多线程版本的处理函数
     * 
     * @param socket
     */
    protected abstract void multiThreadHandle(Socket socket);

    /**
     * 处理连接请求的线程任务
     * 
     * @author jeff
     * @version $Id: MultiThreadSimpleTCPServer.java, v 0.1 2014年4月14日 上午10:29:34 jeff Exp $
     */
    private class Task implements Runnable {
        private Socket socket;

        public Task(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            //进行实际处理
            multiThreadHandle(socket);
            //关闭套接字，释放资源
            try {
                socket.close();
            } catch (IOException e) {
                ;
            }
        }
    }

}
