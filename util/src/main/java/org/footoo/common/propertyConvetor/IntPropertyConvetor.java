/**
 * IBMTC HIT 
 * Copyright (c) 2014-2014 All Rights Reserved.
 */
package org.footoo.common.propertyConvetor;

/**
 * 将字符串转换为对应Int
 * 
 * @author jeff
 * @version $Id: IntPropertyConvetor.java, v 0.1 2014年3月24日 下午6:52:08 jeff Exp $
 */
public class IntPropertyConvetor implements PropertyConvetor {

    /** 
     * @see org.footoo.common.propertyConvetor.PropertyConvetor#convetor(java.lang.String)
     */
    @Override
    public Object convetor(String str) throws PropertyConvetorException {
        try {
            return Integer.valueOf(str);
        } catch (Exception e) {
            throw new PropertyConvetorException("无法将" + str + "转换为整数");
        }
    }
}
