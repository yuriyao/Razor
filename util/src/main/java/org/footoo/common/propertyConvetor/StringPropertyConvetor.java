/**
 * IBMTC HIT 
 * Copyright (c) 2014-2014 All Rights Reserved.
 */
package org.footoo.common.propertyConvetor;

/**
 * string转换为String
 * 
 * @author jeff
 * @version $Id: StringPropertyConvetor.java, v 0.1 2014年3月24日 下午7:00:07 jeff Exp $
 */
public class StringPropertyConvetor implements PropertyConvetor {

    /** 
     * @see org.footoo.common.propertyConvetor.PropertyConvetor#convetor(java.lang.String)
     */
    @Override
    public Object convetor(String str) throws PropertyConvetorException {
        return str;
    }

}
