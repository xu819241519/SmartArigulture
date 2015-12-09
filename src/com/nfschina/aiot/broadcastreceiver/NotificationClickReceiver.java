package com.nfschina.aiot.broadcastreceiver;

import com.nfschina.aiot.activity.AllGreenHouseActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * ���û����ϵͳ��Ϣ��ʱ�򣬻�ִ�����
 * @author wujian
 *
 */
public class NotificationClickReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		MyReceiver.count = 0;
		//����AllGreenHouseActivityʱ�����û�оʹ���һ��������Ѿ����ڣ��ʹ�ջ��ȡ��������ջ��
		Intent newIntent = new Intent(context, AllGreenHouseActivity.class)
		.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
	    context.startActivity(newIntent);
	}

}
