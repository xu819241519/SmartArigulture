/**
 * shareperferences辅助操作
 */
package com.nfschina.aiot.db;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class SharePerencesDBHelper {

	// shareperference文件的名字
	private static String DB_NAME = "CacheData";
	// 相关的Activity
	private Activity mActivity;
	// shareperferences的对象
	private SharedPreferences mSharePreferences;

	/**
	 * 构造一个SharePerencesDBHelper
	 * 
	 * @param activity
	 */
	public SharePerencesDBHelper(Activity activity) {
		mActivity = activity;
		mSharePreferences = mActivity.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);
	}

	/**
	 * 获取shareperferences的布尔型值
	 * 
	 * @param key
	 *            :The name of the preference to retrieve
	 * @param defValue
	 *            :Value to return if this preference does not exist
	 * @return :Returns the preference value if it exists, or defValue. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a boolean.
	 */
	public boolean getBoolean(String key, boolean defValue) {
		return mSharePreferences.getBoolean(key, defValue);
	}

	/**
	 * Set a boolean value in the preferences 
	 * @param key The name of the preference to modify
	 * @param value The name of the preference to modify
	 * @return if the setting succeeds,return true,otherwise return false
	 */
	public boolean putBoolean(String key, boolean value) {
		Editor editor = mSharePreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
		return true;
	}
}
