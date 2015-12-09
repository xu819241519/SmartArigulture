package com.nfschina.aiot.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.nfschina.aiot.socket.Const;
import com.nfschina.aiot.socket.ISocketService;
import com.nfschina.aiot.socket.SocketService;

/**
 * �����������ʱ�������ĵ�ʵ����
 * @author wujian
 */
public class TimeRequest {
	private  ISocketService binder;//�󶨷�����н�
	private  MyConn conn;//�����������
	private BroadcastReceiver receiver;//�㲥������
	private Context mContext;
	private boolean isSendMsg = false;//��ʾ��ǰ�Ƿ����˱���
	
	private int year,month,day,hour,minute;

	String message = "TTLKQAT000000000000000000000";
	SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				System.out.println("send success");
				break;
			case 0:
				System.out.println("send fail");
				break;
			}
		};
	};
	
	public TimeRequest(Context context){
		mContext = context;
		bindServiceAndRegisterReceiver();
	}
	/**
	 * ����ʱ�������ĵķ���
	 * @return ����
	 */
	public String getTimeRequestMsg(){
		message += CRC16.getCRC16(message.getBytes());
		return message;
	}
	/**
	 * �󶨷���ע��㲥�����ߣ�ʵ�ֵ�½֮��ͬ�������ʱ��Ĳ���
	 */
	public void bindServiceAndRegisterReceiver(){
		System.out.println("bindServiceAndRegisterReceiver");
		//�󶨷���
		Intent intent = new Intent(mContext, SocketService.class);
		conn = new MyConn();
		mContext.bindService(intent, conn, 0);
		// �㲥�����ߣ�������ר�Ž��շ�ֵ������ɺ󣬷���˷������ĵġ�
		receiver = new BroadcastReceiver() {
			@SuppressWarnings("deprecation")
			@Override
			public void onReceive(Context context, Intent intent) {// �����յ��㲥֮��
				if (isSendMsg) {// �����ǵ�ǰfragment�����˱��ģ�����˷����ı���
					isSendMsg = false;
					String value = intent.getStringExtra("response");
					// �ж��Ƿ��Ƿ���˶�ʱ����ķ�������//CRC��֤��
					if (value.substring(0, 6).equals("TTLKRZV")
							&& value.length() == 50
							&& CRC16.getCRC16(value.substring(0, 46).getBytes())
									.equals(value.substring(46))) {
						String dateString = value.substring(7, 21);
						System.out.println(dateString);
						try {
							Calendar calendar = Calendar.getInstance();  
							calendar.setTime(dateformat.parse(dateString));
							year = calendar.get(Calendar.YEAR);      
					        month = calendar.get(Calendar.MONTH)+1;      
					        day = calendar.get(Calendar.DATE);      
					        minute = calendar.get(Calendar.MINUTE);      
					        hour = calendar.get(Calendar.HOUR)+8;  
					        System.out.println(year+month+day+hour+minute);
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				}
			}
		};
		// �㲥����������ʾ������ʲô���Ĺ㲥
		IntentFilter intentToReceiveFilter = new IntentFilter();
		intentToReceiveFilter.addAction(Const.BC);
		mContext.registerReceiver(receiver, intentToReceiveFilter);
	}
	/**
	 * ����ʱ��������
	 */
	public void sendTimeRequestMsg(){
		getTimeRequestMsg();
		binder.sentMsg(message.getBytes(), handler);
		isSendMsg = true;
	}
	/**
	 * ���������¼�
	 * @author wujian
	 */
	private class MyConn implements ServiceConnection{
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			//��õ��˷����н飬���Ե��÷����еķ�����
			binder = (ISocketService) service;
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {
		}
	}
	/**
	 * ������ȡ��ע��㲥������
	 */
	public void unDoServiceAndReceiver(){
		if(receiver != null){
			mContext.unregisterReceiver(receiver);
		}
		mContext.unbindService(conn);
	}
//	/**
//	 * ��Ҫ�첽����shijianqingqiu
//	 * @author My
//	 *
//	 */
//	private class TimeRequestTask extends AsyncTask<Void, Void, Void>{
//
//		@Override
//		protected Void doInBackground(Void... params) {
//			
//			return null;
//		}
//		
//	}
}
