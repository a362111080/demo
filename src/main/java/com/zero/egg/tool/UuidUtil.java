package com.zero.egg.tool;

import java.util.Random;
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
	/**  
	 * Title: getRandom 
	 * Description:  获取随机数
	 * @param number 位数
	 * @return  
	 */  
	public static String getRandom(Integer number) {
		Integer password = new Random().nextInt(new Double(Math.pow(10, number)).intValue());
	    if (password < new Double(Math.pow(10, number-1)).intValue())
	    {
	    	password += new Double(Math.pow(10, number-1)).intValue();
	    }
	    return password.toString();
	}
}

