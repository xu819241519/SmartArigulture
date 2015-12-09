package com.nfschina.aiot.constant;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Html;
import android.widget.ListView;
import android.widget.Toast;

/**
 * ��������
 * 
 * @author xu
 *
 */

public class ConstantFun {

	/**
	 * ��ȡSoundPullEventListener
	 * 
	 * @param activity
	 *            ��ǰactivity
	 * @return the SoundPullEventListener
	 */
	public static SoundPullEventListener<ListView> getSoundListener(Activity activity) {
		SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(activity);
//		soundListener.addSoundEvent(State.PULL_TO_REFRESH, R.raw.pull_event);
//		soundListener.addSoundEvent(State.RESET, R.raw.reset_sound);
//		soundListener.addSoundEvent(State.REFRESHING, R.raw.refreshing_sound);
		return soundListener;
	}

	/**
	 * ���б����β����ʾ��ʾ
	 * 
	 * @param activity
	 *            ��ǰactivity
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
	 * �Ը����ַ�������MD5����
	 * 
	 * @param string
	 *            �������ַ���
	 * @return ���ܺ���ַ���
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
	 * ת����16���Ƶ��ַ���
	 * 
	 * @param digest
	 *            ��ת����byte����
	 * @return ת�����16���Ƶ��ַ���
	 */
	private static String toHex(byte[] digest) {
		StringBuilder sb = new StringBuilder();
		int len = digest.length;

		String out = null;
		for (int i = 0; i < len; i++) {
			out = Integer.toHexString(0xFF & digest[i]);// ԭʼ����
			if (out.length() == 1) {
				sb.append("0");// ���Ϊ1λ ǰ�油��0
			}
			sb.append(out);
		}
		return sb.toString();
	}

	/**
	 * ��ʽ��ʱ���ַ���
	 * 
	 * @param time
	 *            ����ʽ�����ַ���
	 * @param timeBig
	 *            ʱ�����Ƿ��ô�����
	 * @return ��ʽ������ַ���
	 */
	public static CharSequence formatTimeString(String time, boolean timeBig) {
		String result = time;
		if (time != null && time.length() == 14) {
			result = "<font>";
			result = time.substring(0, 4);
			result += "/";
			result += time.substring(4, 6);
			result += "/";
			result += time.substring(6, 8);
			result += "</font><br><font>";
			if (timeBig)
				result += "<big>";
			result += time.substring(8, 10);
			result += ":";
			result += time.substring(10, 12);
			result += ":";
			result += time.substring(12, 14);
			if (timeBig)
				result += "</big>";
			result += "</font>";
		}
		return Html.fromHtml(result);
	}

	public static boolean netStateAvailable(Activity activity) {

		boolean result = false;

		Context context = activity.getApplicationContext();
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
		for (int i = 0; i < networkInfos.length; ++i) {
			if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
				result = true;
				break;
			}
		}

		if (!result) {
			Toast.makeText(context, Constant.NET_STATE_DISABLE, Toast.LENGTH_SHORT).show();
		}

		return result;
	}

}
