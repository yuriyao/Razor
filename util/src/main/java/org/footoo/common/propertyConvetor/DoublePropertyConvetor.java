/**
 * IBMTC HIT 
 * Copyright (c) 2014-2014 All Rights Reserved.
 */
package org.footoo.common.propertyConvetor;

/**
 * 将字符串转换为对应Double
 * 
 * @author Toky
 * @version $Id: DoublePropertyConvetor.java, v 0.1 2014年3月25日 
 */
public class DoublePropertyConvetor implements PropertyConvetor {

    /** 
     * @see org.footoo.common.propertyConvetor.PropertyConvetor#convetor(java.lang.String)
     */
    @Override
    public Object convetor(String str) throws PropertyConvetorException {
        try {
            return Double.valueOf(str);
        } catch (Exception e) {
            throw new PropertyConvetorException("无法将" + str + "转换为浮点数");
        }
    }
}
