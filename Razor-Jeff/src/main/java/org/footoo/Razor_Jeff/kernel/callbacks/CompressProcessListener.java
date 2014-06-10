/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.Razor_Jeff.kernel.callbacks;

/**
 * 处理进度监听器
 * 
 * @author jeff
 * @version $Id: CompressProcessListener.java, v 0.1 2014年6月10日 下午12:26:17 jeff Exp $
 */
public interface CompressProcessListener {
    /**
     * 压缩进度监听器
     * 
     * @param process 压缩进度[0-100]
     */
    public void processChanged(int process);
}
