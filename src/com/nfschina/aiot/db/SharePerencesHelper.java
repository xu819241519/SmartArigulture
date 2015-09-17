/**
 * shareperferences辅助操作
 */
package com.nfschina.aiot.db;

import java.net.ContentHandler;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class SharePerencesHelper {

	// shareperference文件的名字
	private static String DB_NAME = "CacheData";

	/**
	 * 构造一个SharePerencesDBHelper
	 * 
	 * @param activity
	 */
	public SharePerencesHelper() {
	}

	/**
	 * 获取shareperferences的布尔型值
	 * 
	 * @param context
	 * @param key
	 *            :The name of the preference to retrieve
	 * @param defValue
	 *            :Value to return if this preference does not exist
	 * @return :Returns the preference value if it exists, or defValue. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a boolean.
	 */
	public static boolean getBoolean(Context context,String key, boolean defValue) {
		SharedPreferences sPreferences = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);
		
		return sPreferences.getBoolean(key, defValue);
	}

	/**
	 * Set a boolean value in the preferences 
	 * @param context
	 * @param key The name of the preference to modify
	 * @param value The name of the preference to modify
	 * @return if the setting succeeds,return true,otherwise return false
	 */
	public static boolean putBoolean(Context context,String key, boolean value) {		
		Editor editor = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE).edit();
		editor.putBoolean(key, value);
		editor.commit();
		return true;
	}
	
	/**
	 * Retrieve a String value from the preferences
	 * @param context
	 * @param key The name of the preference to retrieve
	 * @param defValue  Value to return if this preference does not exist
	 * @return Returns the preference value if it exists, or defValue. Throws ClassCastException if there is a preference with this name that is not a String.
	 */
	public static String getString(Context context,String key,String defValue){
		SharedPreferences sPreferences = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);
		return sPreferences.getString(key, defValue);
	}
	
	/**
	 * Set a String value in the preferences
	 * @param context
	 * @param key The name of the preference to modify
	 * @param value The new value for the preference. Supplying null as the value is equivalent to calling remove(String) with this key
	 * @return Returns true if the action succeeds,or false.
	 */
	public static boolean putString(Context context,String key,String value){
		Editor editor = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE).edit();
		editor.putString(key, value);
		editor.commit();
		return true;
	}
}
