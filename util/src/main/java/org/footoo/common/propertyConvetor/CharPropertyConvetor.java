/**
 * IBMTC HIT Copyright (c) 2014-2014 All Rights Reserved.
 */
package org.footoo.common.propertyConvetor;

/**
 * 将单一字符串转换为对应的char字符
 *
 * @author Toky
 * @version $Id: DoublePropertyConvetor.java, v 0.1 2014年3月25日
 */
public class CharPropertyConvetor implements PropertyConvetor {

    /**
     * @see
     * org.footoo.common.propertyConvetor.PropertyConvetor#convetor(java.lang.String)
     */
    @Override
    public Object convetor(String str) throws PropertyConvetorException {
        if (str.length() != 1) {
            throw new PropertyConvetorException("无法将" + str + "转化为Char字符");
        } else {
            return str.charAt(0);
        }
    }
}
