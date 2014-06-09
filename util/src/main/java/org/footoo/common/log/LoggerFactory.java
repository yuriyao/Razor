/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.log;

/**
 * 日志的factory
 * 
 * @author jeff
 * @version $Id: LoggerFactory.java, v 0.1 2014年2月28日 下午8:07:03 jeff Exp $
 */
public abstract class LoggerFactory {
    /**
     * 获得日志工具
     * 
     * @param clz
     * @return
     */
    public static Logger getLogger(Class<?> clz) {
        return new Log4jLogger(clz);
    }
}
