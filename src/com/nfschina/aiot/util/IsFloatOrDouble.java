package com.nfschina.aiot.util;

import java.util.regex.Pattern;

public class IsFloatOrDouble {
	
	/* 
	  * �ж��Ƿ�Ϊ������������double��float 
	  * @param str ������ַ��� 
	  * @return �Ǹ���������true,���򷵻�false 
	*/  
	  public static boolean isDouble(String str) {  
		  Pattern pattern = Pattern.compile("[\\d]+\\.[\\d]+");
		  return pattern.matcher(str).matches();
	  }


	public static void main(String[] args) {
		//System.out.println("float���ͣ�������"+IsFloatOrDouble.isDouble("1"));
		//float i = Float.parseFloat("123.3");//[String]��ת�����ַ���
		//System.out.println((int)i+"");
		System.out.println(Integer.parseInt("123")+"");
//		if (IsFloatOrDouble.isDouble("1.5")) {
//			int i = (int) Float.parseFloat("123.3");
//			System.out.println(i+"");
//		}
	}
}
