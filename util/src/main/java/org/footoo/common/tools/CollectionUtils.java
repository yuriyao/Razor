/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.tools;

import java.util.Collection;

/**
 * 集合工具集
 * 
 * @author jeff
 * @version $Id: CollectionUtils.java, v 0.1 2014年4月14日 上午10:04:06 jeff Exp $
 */
public abstract class CollectionUtils {
    /**
     * 集合是否为空
     * 
     * @param collection
     * @return 当集合为null或者为empty返回true
     */
    public static boolean isEmpty(Collection<?> collection) {
        if (collection == null || collection.isEmpty()) {
            return true;
        }
        return false;
    }
}
