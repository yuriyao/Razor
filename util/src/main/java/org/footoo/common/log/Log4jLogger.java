/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.log;

/**
 * log4j实现的日志
 * 
 * @author jeff
 * @version $Id: Log4jLogger.java, v 0.1 2014年2月28日 下午8:06:09 jeff Exp $
 */
public class Log4jLogger implements Logger {
    /** 实际的logger工具 */
    private org.apache.log4j.Logger logger;

    public Log4jLogger(Class<?> clz) {
        logger = org.apache.log4j.Logger.getLogger(clz);
    }

    /** 
     * @see org.footoo.common.log.Logger#debugEnabled()
     */
    @Override
    public boolean debugEnabled() {
        return logger.isDebugEnabled();
    }

    /** 
     * @see org.footoo.common.log.Logger#infoEnabled()
     */
    @Override
    public boolean infoEnabled() {
        return logger.isInfoEnabled();
    }

    /** 
     * @see org.footoo.common.log.Logger#warnEnabled()
     */
    @Override
    public boolean warnEnabled() {
        return true;
    }

    /** 
     * @see org.footoo.common.log.Logger#errorEnabled()
     */
    @Override
    public boolean errorEnabled() {
        return true;
    }

    /** 
     * @see org.footoo.common.log.Logger#info(java.lang.Object[])
     */
    @Override
    public void info(Object... objects) {
        //没有启动info级别的日志
        if (!infoEnabled()) {
            return;
        }
        String info = "";
        for (Object object : objects) {
            info += object + ", ";
        }
        logger.info(info);
    }

    /** 
     * @see org.footoo.common.log.Logger#debug(java.lang.Object[])
     */
    @Override
    public void debug(Object... objects) {
        if (!debugEnabled()) {
            return;
        }

        String info = "";
        for (Object object : objects) {
            info += object + ", ";
        }
        logger.debug(info);
    }

    /** 
     * @see org.footoo.common.log.Logger#warn(java.lang.Object[])
     */
    @Override
    public void warn(Object... objects) {
        String info = "";
        for (Object object : objects) {
            info += object + ", ";
        }
        logger.warn(info);
    }

    /** 
     * @see org.footoo.common.log.Logger#error(java.lang.Object[])
     */
    @Override
    public void error(Object... objects) {
        String info = "";
        for (Object object : objects) {
            info += object + ", ";
        }
        logger.error(info);
    }

    /** 
     * @see org.footoo.common.log.Logger#error(java.lang.Throwable, java.lang.Object[])
     */
    @Override
    public void error(Throwable e, Object... objects) {
        String info = "";
        for (Object object : objects) {
            info += object + ", ";
        }
        logger.error(info, e);
    }

    @Override
    public void warn(Throwable e, Object... objects) {
        String info = "";
        for (Object object : objects) {
            info += object + ", ";
        }
        logger.warn(info, e);
    }

}
