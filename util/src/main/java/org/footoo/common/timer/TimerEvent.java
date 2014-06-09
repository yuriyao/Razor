/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.timer;

/**
 * 定时超时事件的回调接口
 * @author jeff
 * @version $Id: TimerEvent.java, v 0.1 2014年5月6日 下午10:40:22 jeff Exp $
 */
public interface TimerEvent {
    /**
     * 超时回调的函数，这个函数必须是线程安全的
     * 同时这个函数不要执行太长时间
     */
    public void timeout();
}
