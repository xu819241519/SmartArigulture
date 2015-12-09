/**
 * shareperferences��������
 */
package com.nfschina.aiot.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * shareperferences���
 * 
 * @author xu
 *
 */

public class SharePerencesHelper {

	// shareperference�ļ�������
	private static String DB_NAME = "CacheData";

	/**
	 * ����һ��SharePerencesDBHelper
	 * 
	 * @param activity
	 */
	public SharePerencesHelper() {
	}

	/**
	 * ��shareperferences��ȡһ��������ֵ
	 * 
	 * @param context
	 * @param key
	 *            �ؼ���
	 * @param defValue
	 *            shareperferences������ʱ���ص�Ĭ��ֵ
	 * @return :���ز����ͽ��ֵ�����shareperferences�����ڣ�����defValue
	 */
	public static boolean getBoolean(Context context, String key, boolean defValue) {
		SharedPreferences sPreferences = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);

		return sPreferences.getBoolean(key, defValue);
	}

	/**
	 * ����shareperferences��һ������ֵ
	 * 
	 * @param context
	 * @param key
	 *            �ؼ���
	 * @param value
	 *            ���õĲ���ֵ
	 * @return ������óɹ�������true�����򷵻�false
	 */
	public static boolean putBoolean(Context context, String key, boolean value) {
		Editor editor = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE).edit();
		editor.putBoolean(key, value);
		editor.commit();
		return true;
	}

	/**
	 * ��shareperferences��ȡһ��string��ֵ
	 * 
	 * @param context
	 * @param key
	 *            �ؼ���
	 * @param defValue
	 *            ��������ڣ�����defValue
	 * @return ����ؼ��ִ��ڣ����ؽ�������򷵻�defValue
	 */
	public static String getString(Context context, String key, String defValue) {
		SharedPreferences sPreferences = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);
		return sPreferences.getString(key, defValue);
	}

	/**
	 * ����һ��shareperferences��ֵ
	 * 
	 * @param context
	 * @param key
	 *            �ؼ���
	 * @param value
	 *            �����µ�ֵ
	 * @return ������óɹ�������true�����򷵻�false
	 */
	public static boolean putString(Context context, String key, String value) {
		Editor editor = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE).edit();
		editor.putString(key, value);
		editor.commit();
		return true;
	}

	/**
	 * ��shareperferences��ȡһ��intֵ
	 * 
	 * @param context
	 * @param key
	 *            �ؼ���
	 * @param defValue
	 *            ��������ڣ��򷵻�defValue
	 * @return ������óɹ�������ֵ
	 */
	public static int getInt(Context context, String key, int defValue) {
		SharedPreferences sPreferences = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);
		return sPreferences.getInt(key, defValue);
	}

	/**
	 * ����һ��shareperferences��ֵ
	 * 
	 * @param context
	 * @param key
	 *            �ؼ���
	 * @param value
	 *            Ҫ���õ�ֵ
	 * @return ������óɹ�������true
	 */
	public static boolean putInt(Context context, String key, int value) {
		Editor editor = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE).edit();
		editor.putInt(key, value);
		editor.commit();
		return true;
	}
}
