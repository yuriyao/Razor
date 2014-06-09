/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.exception;

/**
 * 网络异常
 * 
 * @author fengjing.yfj
 * @version $Id: NetException.java, v 0.1 2014年2月12日 下午7:11:30 fengjing.yfj Exp $
 */
public class NetException extends DistributeCommonException {

    /** 序列号 */
    private static final long serialVersionUID = -8401603972557391547L;

    public NetException() {
        super();
    }

    public NetException(Throwable throwable, String info) {
        super(throwable, info);
    }

    public NetException(Exception e) {
        super(e);
    }

    public NetException(String info) {
        super(info);
    }

}
