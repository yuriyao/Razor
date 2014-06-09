/**
 * IBMTC HIT 
 * Copyright (c) 2014-2014 All Rights Reserved.
 */
package org.footoo.common.propertyConvetor;

/**
 * 将字符串转换为对应的类型
 * 
 * @author jeff
 * @version $Id: PropertyConvetor.java, v 0.1 2014年3月24日 下午6:49:08 jeff Exp $
 */
public interface PropertyConvetor {

    /**
     * 将str转换为对应的Object
     * 
     * @param str
     * @return
     * @throws PropertyConvetorException
     */
    public Object convetor(String str) throws PropertyConvetorException;
}
