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
 * 向服务器发送时间请求报文的实现类
 * @author wujian
 */
public class TimeRequest {
	private  ISocketService binder;//绑定服务的中介
	private  MyConn conn;//与服务建立连接
	private BroadcastReceiver receiver;//广播接收者
	private Context mContext;
	private boolean isSendMsg = false;//表示当前是否发送了报文
	
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
	 * 生成时间请求报文的方法
	 * @return 报文
	 */
	public String getTimeRequestMsg(){
		message += CRC16.getCRC16(message.getBytes());
		return message;
	}
	/**
	 * 绑定服务，注册广播接收者，实现登陆之后同步服务端时间的操作
	 */
	public void bindServiceAndRegisterReceiver(){
		System.out.println("bindServiceAndRegisterReceiver");
		//绑定服务
		Intent intent = new Intent(mContext, SocketService.class);
		conn = new MyConn();
		mContext.bindService(intent, conn, 0);
		// 广播接收者，这里是专门接收阀值设置完成后，服务端反馈报文的。
		receiver = new BroadcastReceiver() {
			@SuppressWarnings("deprecation")
			@Override
			public void onReceive(Context context, Intent intent) {// 当接收到广播之后
				if (isSendMsg) {// 并且是当前fragment发送了报文，服务端反馈的报文
					isSendMsg = false;
					String value = intent.getStringExtra("response");
					// 判断是否是服务端对时请求的反馈报文//CRC验证码
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
		// 广播过滤器，表示将接收什么样的广播
		IntentFilter intentToReceiveFilter = new IntentFilter();
		intentToReceiveFilter.addAction(Const.BC);
		mContext.registerReceiver(receiver, intentToReceiveFilter);
	}
	/**
	 * 发送时间请求报文
	 */
	public void sendTimeRequestMsg(){
		getTimeRequestMsg();
		binder.sentMsg(message.getBytes(), handler);
		isSendMsg = true;
	}
	/**
	 * 服务连接事件
	 * @author wujian
	 */
	private class MyConn implements ServiceConnection{
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			//获得到了服务中介，可以调用服务中的方法了
			binder = (ISocketService) service;
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {
		}
	}
	/**
	 * 解绑服务，取消注册广播接收者
	 */
	public void unDoServiceAndReceiver(){
		if(receiver != null){
			mContext.unregisterReceiver(receiver);
		}
		mContext.unbindService(conn);
	}
//	/**
//	 * 需要异步发送shijianqingqiu
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
