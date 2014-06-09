/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.timer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 定时器
 * 
 * @author jeff
 * @version $Id: Timer.java, v 0.1 2014年5月6日 下午10:34:13 jeff Exp $
 */
public class Timer {
    /** 线程池 */
    private ExecutorService  executor = Executors.newCachedThreadPool();
    /** 定时事件 */
    private List<Event>      events   = new LinkedList<>();
    /** 事件的保护锁 */
    private Object           lock     = new Object();
    /** 是否活跃 */
    private volatile boolean alive    = true;

    public Timer() {
        //启动
        new Thread(new Clock()).start();
    }

    /**
     * 添加定时事件
     * 
     * @param eventMark 定时事件的标志，用以唯一区分一个事件
     * @param time 剩余时间
     * @param callback 超时的回调函数
     */
    public void addEvent(Object eventMark, int time, TimerEvent callback) {
        //计时没有意义
        if (callback == null) {
            return;
        }
        Event event = new Event(eventMark, time, callback);
        synchronized (lock) {
            events.add(event);
        }
    }

    /**
     * 取消定时事件,取消掉第一个符合的事件
     * 
     * @param eventMark
     */
    public void cancelEvent(Object eventMark) {
        synchronized (lock) {
            Iterator<Event> iterator = events.iterator();
            while (iterator.hasNext()) {
                Event event = iterator.next();
                if (eventMark.equals(event.eventMark)) {
                    iterator.remove();
                    break;
                }
            }
        }
    }

    /**
     * 停止计时器
     */
    public void stop() {
        alive = false;
        executor.shutdown();
    }

    /**
     * 滴答线程
     * 
     * @author jeff
     * @version $Id: Timer.java, v 0.1 2014年5月6日 下午11:13:04 jeff Exp $
     */
    private class Clock implements Runnable {

        @Override
        public void run() {
            while (alive) {
                synchronized (lock) {
                    Iterator<Event> iterator = events.iterator();
                    while (iterator.hasNext()) {
                        Event event = iterator.next();
                        if (event.reduceAndCheckTimeout(1)) {
                            iterator.remove();
                            //调用超时函数
                            executor.execute(event.getCallback());
                        }
                    }
                }
                //休眠1秒
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    ;
                }
            }
        }
    }

    /**
     * 将超时事件的回调函数包装成线程池线程
     * 
     * @author jeff
     * @version $Id: Timer.java, v 0.1 2014年5月6日 下午11:21:18 jeff Exp $
     */
    private class Worker implements Runnable {
        /** 超时的回调函数 */
        private TimerEvent callback;

        public Worker(TimerEvent callback) {
            this.callback = callback;
        }

        @Override
        public void run() {
            callback.timeout();
        }

    }

    /**
     * 每一个定时事件
     * 
     * @author jeff
     * @version $Id: Timer.java, v 0.1 2014年5月6日 下午10:49:26 jeff Exp $
     */
    @SuppressWarnings("unused")
    private class Event {
        /** 定时事件的标志,用于唯一区分一个事件,如果为NULL，则这个事件永远没有办法取消 */
        private Object eventMark;
        /** 剩余的超时时间（单位：秒）*/
        private int    timeLeft;
        /** 超时的回调函数 */
        private Worker callback;

        /**
         * @param eventMark
         * @param timeLeft
         * @param callback
         */
        public Event(Object eventMark, int timeLeft, TimerEvent callback) {

            this.eventMark = eventMark;
            this.timeLeft = timeLeft;
            this.callback = new Worker(callback);
        }

        /**
         * 减少n秒剩余时间，并且判断是否超时
         * 
         * @param n
         * @return
         */
        public boolean reduceAndCheckTimeout(int n) {
            return (timeLeft -= n) <= 0;
        }

        /**
         * Getter method for property <tt>eventMark</tt>.
         * 
         * @return property value of eventMark
         */
        public final Object getEventMark() {
            return eventMark;
        }

        /**
         * Setter method for property <tt>eventMark</tt>.
         * 
         * @param eventMark value to be assigned to property eventMark
         */
        public final void setEventMark(Object eventMark) {
            this.eventMark = eventMark;
        }

        /**
         * Getter method for property <tt>timeLeft</tt>.
         * 
         * @return property value of timeLeft
         */
        public final int getTimeLeft() {
            return timeLeft;
        }

        /**
         * Setter method for property <tt>timeLeft</tt>.
         * 
         * @param timeLeft value to be assigned to property timeLeft
         */
        public final void setTimeLeft(int timeLeft) {
            this.timeLeft = timeLeft;
        }

        /**
         * Getter method for property <tt>callback</tt>.
         * 
         * @return property value of callback
         */
        public final Worker getCallback() {
            return callback;
        }

        /**
         * Setter method for property <tt>callback</tt>.
         * 
         * @param callback value to be assigned to property callback
         */
        public final void setCallback(Worker callback) {
            this.callback = callback;
        }

    }
}
