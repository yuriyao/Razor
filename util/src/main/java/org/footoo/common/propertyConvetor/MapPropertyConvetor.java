/**
 * IBMTC HIT Copyright (c) 2014-2014 All Rights Reserved.
 */
package org.footoo.common.propertyConvetor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 将字符串转换为对应的Map
 * 
 * @author Toky
 * @version $Id: DoublePropertyConvetor.java, v 0.1 2014年3月25日
 */
public class MapPropertyConvetor implements PropertyConvetor {

	/**
	 * @see org.footoo.common.propertyConvetor.PropertyConvetor#convetor(java.lang.String)
	 */
	@Override
	public Object convetor(String str) throws PropertyConvetorException {
		Map<String, String> ret = new LinkedHashMap<String, String>();// LikedHashMap可以保持顺序
		// Map<String, String> ret = new TreeMap<String, String>();//TreeMap自然顺序
		StringBuilder sb = new StringBuilder(str);
		int indexStart;
		int indexStop = 1;
		int status = 0;
		String mapKey = null, mapValue;
		if (sb.length() < 5) {
			throw new PropertyConvetorException("无法将字符串" + str
					+ "转换为Map,字符串长度至少为5");
		}
		if (sb.charAt(0) == '{' && sb.charAt(sb.length() - 1) == '}') {
			indexStart = indexStop;
			while (indexStop < sb.length() - 1) {
				switch (sb.charAt(indexStop)) {
				// 转义符号
				case '/':
					sb.replace(indexStop, indexStop + 2,
							sb.substring(indexStop + 1, indexStop + 2));
					indexStop++;
					break;
				// 截取Key的值
				case ':':
					if (status != 0) {
						throw new PropertyConvetorException("无法将" + str
								+ "转换为Map");
					}
					mapKey = sb.substring(indexStart, indexStop);
					indexStop++;
					indexStart = indexStop;
					status = 1;
					break;
				// 截取Value的值
				case ',':
					if (status != 1) {
						throw new PropertyConvetorException("无法将" + str
								+ "转换为Map");
					}
					mapValue = sb.substring(indexStart, indexStop);
					ret.put(mapKey, mapValue);
					indexStop++;
					indexStart = indexStop;
					status = 0;
					break;
				default:
					indexStop++;
					break;
				}
			}
			if (status != 1) {
				throw new PropertyConvetorException("无法将" + str + "转换为Map");
			}
			mapValue = sb.substring(indexStart, indexStop);
			ret.put(mapKey, mapValue);
			return ret;
		}
		throw new PropertyConvetorException("无法将" + str
				+ "转换为Map,字符串必须以大括号\"{}\"包含");
	}
}
