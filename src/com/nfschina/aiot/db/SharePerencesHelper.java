/**
 * shareperferences辅助操作
 */
package com.nfschina.aiot.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * shareperferences相关
 * 
 * @author xu
 *
 */

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
	 * 从shareperferences获取一个布尔型值
	 * 
	 * @param context
	 * @param key
	 *            关键字
	 * @param defValue
	 *            shareperferences不存在时返回的默认值
	 * @return :返回布尔型结果值，如果shareperferences不存在，返回defValue
	 */
	public static boolean getBoolean(Context context, String key, boolean defValue) {
		SharedPreferences sPreferences = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);

		return sPreferences.getBoolean(key, defValue);
	}

	/**
	 * 设置shareperferences的一个布尔值
	 * 
	 * @param context
	 * @param key
	 *            关键字
	 * @param value
	 *            设置的布尔值
	 * @return 如果设置成功，返回true，否则返回false
	 */
	public static boolean putBoolean(Context context, String key, boolean value) {
		Editor editor = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE).edit();
		editor.putBoolean(key, value);
		editor.commit();
		return true;
	}

	/**
	 * 从shareperferences获取一个string的值
	 * 
	 * @param context
	 * @param key
	 *            关键字
	 * @param defValue
	 *            如果不存在，返回defValue
	 * @return 如果关键字存在，返回结果，否则返回defValue
	 */
	public static String getString(Context context, String key, String defValue) {
		SharedPreferences sPreferences = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);
		return sPreferences.getString(key, defValue);
	}

	/**
	 * 设置一个shareperferences的值
	 * 
	 * @param context
	 * @param key
	 *            关键字
	 * @param value
	 *            设置新的值
	 * @return 如果设置成功，返回true，否则返回false
	 */
	public static boolean putString(Context context, String key, String value) {
		Editor editor = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE).edit();
		editor.putString(key, value);
		editor.commit();
		return true;
	}

	/**
	 * 从shareperferences获取一个int值
	 * 
	 * @param context
	 * @param key
	 *            关键字
	 * @param defValue
	 *            如果不存在，则返回defValue
	 * @return 如果设置成功，返回值
	 */
	public static int getInt(Context context, String key, int defValue) {
		SharedPreferences sPreferences = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);
		return sPreferences.getInt(key, defValue);
	}

	/**
	 * 设置一个shareperferences的值
	 * 
	 * @param context
	 * @param key
	 *            关键字
	 * @param value
	 *            要设置的值
	 * @return 如果设置成功，返回true
	 */
	public static boolean putInt(Context context, String key, int value) {
		Editor editor = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE).edit();
		editor.putInt(key, value);
		editor.commit();
		return true;
	}
}
