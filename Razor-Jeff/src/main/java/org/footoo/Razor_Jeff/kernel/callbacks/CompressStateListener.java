/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.Razor_Jeff.kernel.callbacks;

/**
 * 压缩状态监听器
 * 
 * @author jeff
 * @version $Id: CompressState.java, v 0.1 2014年6月10日 下午12:18:59 jeff Exp $
 */
public interface CompressStateListener {
    /**
     * 开始压缩处理
     */
    public static final int COMPRESS_START  = 0;
    /**
     * 正在搜索所有的文件
     */
    public static final int SEARCHING_FILES = 1;
    /**
     * 正在压缩
     */
    public static final int COMPRESSING     = 2;
    /**
     * 压缩完成
     */
    public static final int COMPRESS_OK     = 3;
    /**
     * 压缩失败
     */
    public static final int COMPRESS_FAIL   = 4;

    /**
     * 状态改变的回调函数
     * 
     * @param state
     */
    public void stateChanged(int state);

}
