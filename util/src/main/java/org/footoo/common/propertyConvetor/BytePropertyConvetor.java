/**
 * IBMTC HIT Copyright (c) 2014-2014 All Rights Reserved.
 */
package org.footoo.common.propertyConvetor;

/**
 * 将字符串转换为对应Byte字节流
 *
 * @author Toky
 * @version $Id: DoublePropertyConvetor.java, v 0.1 2014年3月25日
 */
public class BytePropertyConvetor implements PropertyConvetor {

    /**
     * @see
     * org.footoo.common.propertyConvetor.PropertyConvetor#convetor(java.lang.String)
     */
    @Override
    public Object convetor(String str) throws PropertyConvetorException {
        try {
            return str.getBytes("UTF-8");
        } catch (Exception e) {
            throw new PropertyConvetorException("无法将" + str + "转换为Byte");
        }
    }
}
