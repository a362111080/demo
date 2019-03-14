package com.zero.egg.tool;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class StringTool {
	
	/**  
	 * Title: splitToList 
	 * Description:  将字符串转成list
	 * @param s 字符串
	 * @param regex 分隔符
	 * @return  
	 */  
	public static List<String> splitToList(String s,String regex) {
		if (StringUtils.isNotBlank(s)) {
			String [] strArray =s.split(regex);
			List<String> list = Arrays.asList(strArray);
			return list;
		}else {
			return null;
		}
		
	}

}
