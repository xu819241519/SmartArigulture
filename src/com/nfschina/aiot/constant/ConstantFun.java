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

public class ConstantFun {

	/**
	 * get the refresh sound listener
	 * 
	 * @param activity
	 *            the activity relate to the sound listener
	 * @return the soundlistener
	 */
	public static SoundPullEventListener<ListView> getSoundListener(Activity activity) {
		SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(activity);
		soundListener.addSoundEvent(State.PULL_TO_REFRESH, R.raw.pull_event);
		soundListener.addSoundEvent(State.RESET, R.raw.reset_sound);
		soundListener.addSoundEvent(State.REFRESHING, R.raw.refreshing_sound);
		return soundListener;
	}

	/**
	 * when the list reaches the end, tell the user by toast
	 * @param activity the parent of the list
	 * @return the listener
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
	 * get a encoded MD5 string of the given string
	 * @param string the string which to be encoded
	 * @return the encoded MD5 string
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
	 * get the hex of a byte array
	 * @param digest the byte array
	 * @return the hex of the byte array
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
