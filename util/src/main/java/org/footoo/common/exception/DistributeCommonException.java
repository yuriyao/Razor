/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.exception;

/**
 * 分布式数据结构系统的基本异常
 * 
 * @author fengjing.yfj
 * @version $Id: DistributeCommonException.java, v 0.1 2014年2月12日 下午7:06:06 fengjing.yfj Exp $
 */
public class DistributeCommonException extends Exception {

    /** 序列号  */
    private static final long serialVersionUID = -3000494848662588504L;

    public DistributeCommonException() {
        super();
    }

    public DistributeCommonException(Throwable cause) {
        super(cause);
    }

    public DistributeCommonException(String info) {
        super(info);
    }

    public DistributeCommonException(Throwable throwable, String info) {
        super(info, throwable);
    }

}
