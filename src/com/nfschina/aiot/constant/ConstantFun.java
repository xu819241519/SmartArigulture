package com.nfschina.aiot.constant;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;
import com.nfschina.aiot.R;

import android.app.Activity;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 常量函数
 * @author xu
 *
 */

public class ConstantFun {

	/**
	 * 获取SoundPullEventListener
	 * 
	 * @param activity 当前activity
	 * @return the SoundPullEventListener
	 */
	public static SoundPullEventListener<ListView> getSoundListener(Activity activity) {
		SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(activity);
		soundListener.addSoundEvent(State.PULL_TO_REFRESH, R.raw.pull_event);
		soundListener.addSoundEvent(State.RESET, R.raw.reset_sound);
		soundListener.addSoundEvent(State.REFRESHING, R.raw.refreshing_sound);
		return soundListener;
	}

	/**
	 * 当列表到达结尾，显示提示
	 * @param activity 当前activity
	 * @return OnLastItemVisibleListener
	 */
	public static OnLastItemVisibleListener getLastItemVisibleListener(final Activity activity) {
		return new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				Toast.makeText(activity, Constant.END_OF_LIST, Toast.LENGTH_SHORT).show();
			}
		};
	}

	/**
	 * 对给定字符串进行MD5加密
	 * @param string 给定的字符串
	 * @return 加密后的字符串
	 */
	public static String getMD5String(String string) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
			md5.update(string.getBytes());
			byte[] digest = md5.digest();
			return toHex(digest);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return string;

	}

	
	/**
	 * 转换成16进制的字符串
	 * @param digest 待转换的byte数组
	 * @return 转换后的16进制的字符串
	 */
	private static String toHex(byte[] digest) {
		StringBuilder sb = new StringBuilder();
		int len = digest.length;

		String out = null;
		for (int i = 0; i < len; i++) {
			out = Integer.toHexString(0xFF & digest[i]);// 原始方法
			if (out.length() == 1) {
				sb.append("0");// 如果为1位 前面补个0
			}
			sb.append(out);
		}
		return sb.toString();
	}

}
