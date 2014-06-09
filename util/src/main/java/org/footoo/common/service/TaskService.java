/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.service;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 定时服务
 * 
 * @author jeff
 * @version $Id: TaskService.java, v 0.1 2014年3月1日 下午7:20:56 jeff Exp $
 */
public abstract class TaskService extends TimerTask {
    /** 定时工具 */
    private Timer timer  = new Timer();

    /** 延时的时间(ms) */
    private long  delay  = 0;

    /** 执行频率(ms) */
    private long  period = 10000;

    /**
     * Getter method for property <tt>delay</tt>.
     * 
     * @return property value of delay
     */
    public long getDelay() {
        return delay;
    }

    /**
     * Setter method for property <tt>delay</tt>.
     * 
     * @param delay value to be assigned to property delay
     */
    public void setDelay(long delay) {
        this.delay = delay;
    }

    /**
     * Getter method for property <tt>period</tt>.
     * 
     * @return property value of period
     */
    public long getPeriod() {
        return period;
    }

    /**
     * Setter method for property <tt>period</tt>.
     * 
     * @param period value to be assigned to property period
     */
    public void setPeriod(long period) {
        this.period = period;
    }

    public void start() {
        timer.schedule(this, delay, period);
    }

    public void stop() {
        timer.cancel();
    }

}
