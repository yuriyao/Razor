/**
 * IBMTC HIT Copyright (c) 2014-2014 All Rights Reserved.
 */
package org.footoo.common.propertyConvetor;

import java.util.ArrayList;
import java.util.List;

/**
 * 将字符串转换为对应List
 * 
 * @author Toky
 * @version $Id: DoublePropertyConvetor.java, v 0.1 2014年3月25日
 */
public class ListPropertyConvetor implements PropertyConvetor {

	/**
	 * @see org.footoo.common.propertyConvetor.PropertyConvetor#convetor(java.lang.String)
	 *      String 形如"[a,b,c,d,]"的字符串，以","作为划分，其他必须加"/"作为转义符号
	 *      首尾字符必须为"["和"]"，否则抛出异常
	 */
	@Override
	public Object convetor(String str) throws PropertyConvetorException {
		List<String> ret = new ArrayList<>();
		StringBuilder sb = new StringBuilder(str);
		int indexStart;
		int indexStop = 1;
		if (sb.length() <= 2) {
			throw new PropertyConvetorException("无法将字符串" + str
					+ "转换为List,字符串长度至少为3");
		}
		if (sb.charAt(0) == '[' && sb.charAt(sb.length() - 1) == ']') {
			indexStart = indexStop;
			while (indexStop < sb.length() - 1) {
				switch (sb.charAt(indexStop)) {
				case '/':
					sb.replace(indexStop, indexStop + 2,
							sb.substring(indexStop + 1, indexStop + 2));
					indexStop++;
					break;
				case ',':
					String value = sb.substring(indexStart, indexStop);
					indexStop++;
					indexStart = indexStop;
					ret.add(value);
					break;
				default:
					indexStop++;
					break;
				}
			}
			String value = sb.substring(indexStart, indexStop);
			ret.add(value);
			return ret;
		}
		throw new PropertyConvetorException("无法将" + str
				+ "转换为List,字符串必须以中括号\"[]\"包含");
	}
}
