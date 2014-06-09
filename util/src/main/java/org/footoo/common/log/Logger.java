/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.log;

/**
 * 
 * @author jeff
 * @version $Id: Logger.java, v 0.1 2014年2月28日 下午7:47:08 jeff Exp $
 */
public interface Logger {
    /** debug级别 */
    public static final int DEBUG = 0;
    /** info级别 */
    public static final int INFO  = 1;
    /** warn级别 */
    public static final int WARN  = 2;
    /** error级别 */
    public static final int ERROR = 3;
    /** Fatal */
    public static final int FATAL = 4;

    /**
     * 是否启动了debug level 
     * 
     * @return
     */
    public boolean debugEnabled();

    /**
     * 是否启动了info level
     * 
     * @return
     */
    public boolean infoEnabled();

    /**
     * 是否启动了warn level
     * 
     * @return
     */
    public boolean warnEnabled();

    /**
     * 是否启动了error level
     * 
     * @return
     */
    public boolean errorEnabled();

    /**
     * 打印info信息，只有info级别被启动才会真正打印
     * 
     * @param objects
     */
    public void info(Object... objects);

    /**
     * 打印debug信息，只有debug级别被启动才会真正打印
     * 
     * @param objects
     */
    public void debug(Object... objects);

    /**
     * 打印warn信息，只有warn级别被启动才会真正打印
     * 
     * @param objects
     */
    public void warn(Object... objects);

    /**
     * 打印warn信息，只有warn级别被启动才会真正打印
     * 
     * @param e
     * @param objects
     */
    public void warn(Throwable e, Object... objects);

    /**
     * 打印error信息
     * 
     * @param objects
     */
    public void error(Object... objects);

    /**
     * 打印错误信息
     * 
     * @param e 异常
     * @param objects
     */
    public void error(Throwable e, Object... objects);

}
