/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.tools;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 长整数序列生成器，可以生成全局唯一的长整数
 * 
 * @author fengjing.yfj
 * @version $Id: LongSequenceGenerator.java, v 0.1 2014年2月16日 下午3:46:04 fengjing.yfj Exp $
 */
public abstract class LongSequenceGenerator {
    /** 序列号 */
    private static AtomicLong sequence = new AtomicLong();

    /**
     * 生成一个长整数
     * 
     * @return
     */
    public static long generate() {
        return sequence.incrementAndGet();
    }
}
