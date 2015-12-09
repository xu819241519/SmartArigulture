package com.nfschina.aiot.util;

import java.util.regex.Pattern;

public class IsFloatOrDouble {
	
	/* 
	  * 判断是否为浮点数，包括double和float 
	  * @param str 传入的字符串 
	  * @return 是浮点数返回true,否则返回false 
	*/  
	  public static boolean isDouble(String str) {  
		  Pattern pattern = Pattern.compile("[\\d]+\\.[\\d]+");
		  return pattern.matcher(str).matches();
	  }


	public static void main(String[] args) {
		//System.out.println("float类型？？？？"+IsFloatOrDouble.isDouble("1"));
		//float i = Float.parseFloat("123.3");//[String]待转换的字符串
		//System.out.println((int)i+"");
		System.out.println(Integer.parseInt("123")+"");
//		if (IsFloatOrDouble.isDouble("1.5")) {
//			int i = (int) Float.parseFloat("123.3");
//			System.out.println(i+"");
//		}
	}
}
