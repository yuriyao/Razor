/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 配置文件的属性
 * 
 * @author jeff
 * @version $Id: ConfigureAttr.java, v 0.1 2014年4月1日 下午5:34:46 jeff Exp $
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConfigureAttr {
    /**
     * 默认值
     * 
     * @return
     */
    public String defaultValue();

}
