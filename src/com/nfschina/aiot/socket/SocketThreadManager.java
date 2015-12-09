package com.nfschina.aiot.socket;

import android.os.Handler;

/**
 * Socket线程管理类
 * @author wujian
 */
public class SocketThreadManager {
	private static SocketThreadManager mSocketManager = null;
	// 读取从服务端信息的功能线程
	private static SocketInputThread mInputThread = null;
	// 往服务器端发送数据的功能线程
	private static SocketOutputThread mOutThread = null;
	// 心跳包线程，主要功能是，负责客户端与服务端保持一直连接
	private static SocketHeartThread mHeartThread = null;
	// 获取单例
	public static SocketThreadManager sharedInstance() {
		if (mSocketManager == null) {
			mSocketManager = new SocketThreadManager();
			mSocketManager.startThreads();
		}
		return mSocketManager;
	}
	// 单例，不允许在外部构建对象
	private SocketThreadManager() {
		mHeartThread = SocketHeartThread.instance();
		mInputThread = new SocketInputThread();
		mOutThread = new SocketOutputThread();
	}
	/**
	 * 启动线程
	 */
	private void startThreads() {
		
		// 在这一步连接服务器
		if (!mHeartThread.isAlive()) {
			mHeartThread.start();
		}
		if (!mInputThread.isAlive()) {
			mInputThread.start();
		}
		if (!mOutThread.isAlive()) {
			mOutThread.start();
		}
		mInputThread.setStart(true);
		mInputThread.setStart(true);
		// mDnsthread.start();
	}
	/**
	 * stop线程
	 */
	public void stopThreads() {
		mHeartThread.stopThread();
		mInputThread.setStart(false);
		mOutThread.setStart(false);
		mInputThread = null;
		mOutThread = null;
		mHeartThread = null;
	}
	/**
	 * 消除对象
	 */
	public void releaseInstance() {
		if (mSocketManager != null) {
			mSocketManager.stopThreads();
			mSocketManager = null;
		}
	}
	/**
	 * 发送信息
	 * @param buffer
	 * @param handler
	 */
	public void sendMsg(byte[] buffer, Handler handler) {
		MsgEntity entity = new MsgEntity(buffer, handler);
		mOutThread.addMsgToSendList(entity);
	}

}
