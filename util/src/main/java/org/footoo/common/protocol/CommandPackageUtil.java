/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.protocol;

import org.footoo.common.tools.JsonSerializable;
import org.footoo.common.tools.LongSequenceGenerator;

/**
 * 命令报文工具
 * 
 * @author jeff
 * @version $Id: CommandPackageUtil.java, v 0.1 2014年4月10日 下午3:10:16 jeff Exp $
 */
public class CommandPackageUtil {
    /**
     * 创建简单的报文
     * 
     * @param code
     * @return
     */
    public static final CommandPackage buildSimplePackage(CommandCode code) {
        return new CommandPackage(code, null, LongSequenceGenerator.generate(), null);
    }

    /**
     * 创建简单的响应报文
     * 
     * @param errorCode
     * @return
     */
    public static final CommandPackage buildSimplePackage(CommandErrorCode errorCode) {
        return new CommandPackage(errorCode, null, LongSequenceGenerator.generate(), null);
    }

    /**
     * 创建响应报文
     * 
     * @param errorCode
     * @param obj
     * @return
     */
    public static final CommandPackage buildPackage(CommandErrorCode errorCode, Object obj) {
        return new CommandPackage(errorCode, null, LongSequenceGenerator.generate(),
            JsonSerializable.serialize(obj).getBytes());
    }

    /**
     * 创建命令报文
     * 
     * @param code
     * @param obj
     * @return
     */
    public static final CommandPackage buildPackage(CommandCode code, Object obj) {
        return new CommandPackage(code, null, LongSequenceGenerator.generate(), JsonSerializable
            .serialize(obj).getBytes());
    }

    /**
     * 解析body
     * 
     * @param pkg
     * @param clz
     * @return
     */
    public static final <T> T parseBody(CommandPackage pkg, Class<T> clz) {
        return JsonSerializable.deserialize(new String(pkg.getBody()), clz);
    }
}
