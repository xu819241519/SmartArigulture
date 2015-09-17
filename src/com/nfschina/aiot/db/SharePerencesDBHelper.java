/**
 * shareperferences��������
 */
package com.nfschina.aiot.db;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class SharePerencesDBHelper {

	// shareperference�ļ�������
	private static String DB_NAME = "CacheData";
	// ��ص�Activity
	private Activity mActivity;
	// shareperferences�Ķ���
	private SharedPreferences mSharePreferences;

	/**
	 * ����һ��SharePerencesDBHelper
	 * 
	 * @param activity
	 */
	public SharePerencesDBHelper(Activity activity) {
		mActivity = activity;
		mSharePreferences = mActivity.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);
	}

	/**
	 * ��ȡshareperferences�Ĳ�����ֵ
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
	
	/**
	 * Retrieve a String value from the preferences
	 * @param key The name of the preference to retrieve
	 * @param defValue  Value to return if this preference does not exist
	 * @return Returns the preference value if it exists, or defValue. Throws ClassCastException if there is a preference with this name that is not a String.
	 */
	public String getString(String key,String defValue){
		return mSharePreferences.getString(key, defValue);
	}
	
	/**
	 * Set a String value in the preferences
	 * @param key The name of the preference to modify
	 * @param value The new value for the preference. Supplying null as the value is equivalent to calling remove(String) with this key
	 * @return Returns true if the action succeeds,or false.
	 */
	public boolean putString(String key,String value){
		Editor editor = mSharePreferences.edit();
		editor.putString(key, value);
		editor.commit();
		return true;
	}
}
