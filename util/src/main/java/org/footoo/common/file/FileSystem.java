/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.file;

/**
 * 文件系统
 * 
 * @author jeff
 * @version $Id: FileSystem.java, v 0.1 2014年3月5日 上午9:19:12 jeff Exp $
 */
public interface FileSystem {

    /** kB的字节数 */
    public static final long K = 1024L;

    /** M 的字节数 */
    public static final long M = 1024 * K;

    /** G的字节数 */
    public static final long G = 1024 * M;

    /**
     * 获取文件系统的魔数
     * 
     * @return
     */
    public long getMagicNumber();

    /**
     * 存储数据
     * 
     * @param object
     * @return
     */
    public Object put(Object obj);

    /**
     * 删除数据
     * 
     * @param obj
     * @return
     */
    public Object delete(Object obj);

    /**
     * 更新数据
     * 
     * @param newObj
     * @param oldObject
     * @return
     */
    public Object update(Object newObj, Object oldObject);

    /**
     * 查找数据
     * 
     * @param obj
     * @return
     */
    public Object find(Object obj);

    /**
     * 文件系统启动
     */
    public void start();

    /**
     * 文件系统关闭
     */
    public void stop();
}
