/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 将javabean和json相互之间的转换
 * 
 * @author fengjing.yfj
 * @version $Id: JsonSerializable.java, v 0.1 2014年2月13日 上午11:34:54 fengjing.yfj Exp $
 */
public abstract class JsonSerializable {

    /**
     * 进行序列化
     * 
     * @param obj
     * @return
     */
    public static final String serialize(Object obj) {
        return JSON.toJSONString(obj);
    }

    /**
     * 反序列化
     * 
     * @param <T>
     * 
     * @param str
     * @return
     */
    public static final <T> T deserialize(String str, Class<T> clz) {
        return JSON.parseObject(str, clz);
    }

    /**
     * 可以序列化范型
     * 
     * @param obj
     * @return
     */
    public static final String serializeWithClass(Object obj) {
        return JSON.toJSONString(obj, SerializerFeature.WriteClassName);
    }
}
