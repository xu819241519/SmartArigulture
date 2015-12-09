package com.nfschina.aiot.broadcastreceiver;

import com.nfschina.aiot.activity.AllGreenHouseActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * 当用户点击系统消息的时候，会执行这个
 * @author wujian
 *
 */
public class NotificationClickReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		MyReceiver.count = 0;
		//开启AllGreenHouseActivity时，如果没有就创建一个，如果已经存在，就从栈中取出，放在栈顶
		Intent newIntent = new Intent(context, AllGreenHouseActivity.class)
		.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
	    context.startActivity(newIntent);
	}

}
