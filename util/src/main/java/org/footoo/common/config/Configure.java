/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.config;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.footoo.common.log.Logger;
import org.footoo.common.log.LoggerFactory;
import org.footoo.common.propertyConvetor.IntPropertyConvetor;
import org.footoo.common.propertyConvetor.ListPropertyConvetor;
import org.footoo.common.propertyConvetor.PropertyConvetor;
import org.footoo.common.propertyConvetor.StringPropertyConvetor;

/**
 * 
 * 
 * @author jeff
 * @version $Id: Configure.java, v 0.1 2014年4月1日 下午5:34:29 jeff Exp $
 */
public class Configure {
    /** 用于将字符串转换为对应的数据类型 */
    private static final Map<Class<?>, PropertyConvetor> conventors = new HashMap<Class<?>, PropertyConvetor>();

    /** 日志 */
    private static final Logger                          logger     = LoggerFactory
                                                                        .getLogger(Configure.class);

    static {
        //设置默认的属性编辑器
        //Int
        conventors.put(Integer.class, new IntPropertyConvetor());
        conventors.put(Integer.TYPE, new IntPropertyConvetor());
        //Char

        //String
        conventors.put(String.class, new StringPropertyConvetor());
        //List
        conventors.put(List.class, new ListPropertyConvetor());
    }

    /**
     * 赋予属性值
     * 
     * @param properties
     */
    public void assignAttrs(Properties properties) {
        Field[] fields = this.getClass().getDeclaredFields();

        for (Field field : fields) {
            try {
                Method setMethod = this.getClass().getMethod(createSetMethodName(field.getName()),
                    field.getType());
                setMethod.setAccessible(true);
                //获取属性注解
                ConfigureAttr attr = field.getAnnotation(ConfigureAttr.class);
                //获取配置的属性值
                String value = properties.getProperty(field.getName(), attr.defaultValue());
                //获取类型转换器
                PropertyConvetor convetor = conventors.get(field.getType());
                if (convetor == null) {
                    logger.warn("没有配置[" + field.getType() + "]的属性转换器");
                    continue;
                }
                //转换为对应的类型,并调用set方法
                setMethod.invoke(this, convetor.convetor(value));
            } catch (Exception e) {
                ;
            }
        }

    }

    /**
     * 赋属性
     * 
     * @param inputStream
     */
    public void assignAttrs(InputStream inputStream) {
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
            assignAttrs(properties);
        } catch (IOException e) {
            logger.error(e, "无法加载属性");
        }

    }

    /**
     * 获取set方法
     * 
     * @param field
     * @return
     */
    private String createSetMethodName(String field) {
        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("set");
        //首字母大写
        char c = field.charAt(0);
        if (c >= 'a' && c <= 'z') {
            c += 'A' - 'a';
        }
        stringBuffer.append(c);

        stringBuffer.append(field.substring(1));

        return stringBuffer.toString();
    }
}
