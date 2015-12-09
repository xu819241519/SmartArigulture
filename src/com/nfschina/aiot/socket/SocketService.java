package com.nfschina.aiot.socket;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
/**
 * 继承安卓组件Service实现的一个Socket服务，负责向服务端发送信息，以及实时接收服务端发送过来的数据
 * @author wujian
 */
public class SocketService extends Service {
	//线程管理对象
	private SocketThreadManager threadManager;

	@Override
	public IBinder onBind(Intent intent) {
		return new MyBinder();
	}
	/**
	 * 服务中介，通过它访问服务内部的方法，为了安全，使用private,通过接口进行访问
	 * @author wujian
	 */
	private class MyBinder extends Binder implements ISocketService{
		@Override
		public void sentMsg(byte[] buffer, Handler handler) {
			threadManager.sendMsg(buffer, handler);
		}
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//startService方法调用后，会执行这个方法，在这里启动三个线程
		threadManager = SocketThreadManager.sharedInstance();
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public void onDestroy() {
		System.out.println("service is stop");
		super.onDestroy();
		//想要使用stopService停止服务，需要在onDestory方法中停止线程
		//threadManager.releaseInstance();
		
	}
	
}
