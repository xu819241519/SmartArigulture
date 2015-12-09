package com.nfschina.aiot.broadcastreceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.nfschina.aiot.activity.R;
import com.nfschina.aiot.entity.PushContent;
import com.nfschina.aiot.entity.PushMsg;
import com.nfschina.aiot.util.CRC16;
import com.nfschina.aiot.util.MessageParser;
/**
 * ���պ�̨���͵�ʵʱ����(������������)�Ĺ㲥������
 * @author wujian
 *
 */
public class MyReceiver extends BroadcastReceiver {
//	private MessageParser parser = new MessageParser();
//	private PushMsg pushMsg;
//	private PushContent content;
	public static int count;//������Ϣ����

	@Override
	public void onReceive(Context context, Intent intent) {
			// �����յ��㲥֮��
			String message = intent.getStringExtra("response");
			if (message.length() > 0) {
				if (message.substring(0, 6).equals("TTLKNZ") && message.substring(78, 79).equals("1") 
						&& (CRC16.getCRC16(message.substring(0, 83).getBytes())).equals(message.substring(83,87))) {
					//�ж��Ƿ������ͱ��ģ����ң��Ǳ�����Ϣ�����ͱ���
					//pushMsg = parser.parser(message);
					//content = pushMsg.getPushMsgContent(pushMsg);
					//System.out.println(content.toString());
					count++;
					//��������ϵͳ��Ϣ
					NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
					NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
					Intent clickintent = new Intent(context, NotificationClickReceiver.class);
					PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, clickintent, 1);
					builder.setContentTitle("������Ϣ")
					.setContentText("��ǰ��"+count+"��������Ϣ����ע��鿴��") 
					.setContentIntent(pendingIntent) 
					.setTicker("���ұ�����Ϣ")
					.setWhen(System.currentTimeMillis())
					.setPriority(Notification.PRIORITY_DEFAULT) 
					.setOngoing(false)
					.setDefaults(Notification.DEFAULT_VIBRATE)
					.setSmallIcon(R.drawable.home)
					.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_launcher));
					Notification notification = builder.build();
					notification.flags = Notification.FLAG_AUTO_CANCEL;
					manager.notify(0, notification);
					
				}
			}
	}

}
