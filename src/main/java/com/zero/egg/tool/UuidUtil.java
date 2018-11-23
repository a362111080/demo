package com.zero.egg.tool;

import java.util.UUID;

public class UuidUtil {

	public static String get32UUID() {
		String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
		return uuid;
	}
	public static void main(String[] args) {
		System.out.println(get32UUID());
	}
	
	/**
	 * 获取随机10位数
	 * @return
	 */
	public static String getRandomNum() {
		long randomNum = System.currentTimeMillis();  
		String rdn = String.valueOf(randomNum);
		rdn = rdn.substring(rdn.length()-6, rdn.length());
		int a = (int)(1+Math.random()*(1000-1+1));
		rdn = rdn + String.valueOf(a);
		return rdn;
	}
}

