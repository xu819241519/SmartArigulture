package com.nfschina.aiot.socket;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 客户端写消息线程
 * @author wujian
 */
public class SocketOutputThread extends Thread {
	private boolean isStart = true;
	private static String tag = "socketOutputThread";
	private List<MsgEntity> sendMsgList;//发送消息队列

	public SocketOutputThread() {
		sendMsgList = new CopyOnWriteArrayList<MsgEntity>();
	}
	/**
	 * 也是在外部控制线程的开闭
	 * @param isStart
	 */
	public void setStart(boolean isStart) {
		this.isStart = isStart;
		synchronized (this) {
			notify();
		}
	}
	/**
	 * 使用socket发送消息
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public boolean sendMsg(byte[] msg) throws Exception {
		if (msg == null) {
			Log.e(tag, "sendMsg is null");
			return false;
		}
		try {
			TCPClient.instance().sendMsg(msg);
		} catch (Exception e) {
			throw (e);
		}
		return true;
	}
	/**
	 * 把当前的消息先放到发送队列中
	 * @param msg
	 */
	public void addMsgToSendList(MsgEntity msg) {
		synchronized (this) {
			this.sendMsgList.add(msg);
			// 唤醒发送进程
			notify();
		}
	}
	@Override
	public void run() {
		while (isStart) {
			// 锁发送list
			synchronized (sendMsgList) {
				for (MsgEntity msg : sendMsgList) {
					Handler handler = msg.getHandler();
					try {
						// 发送信息
						sendMsg(msg.getBytes());
						sendMsgList.remove(msg);
						// 成功消息，通过hander回传
						if (handler != null) {
							Message message = new Message();
							message.obj = msg.getBytes();
							message.what = 1;
							handler.sendMessage(message);
							// handler.sendEmptyMessage(1);
						}
					} catch (Exception e) {
						e.printStackTrace();
						Log.e(tag, e.toString());
						// 错误消息，通过hander回传
						if (handler != null) {
							Message message = new Message();
							message.obj = msg.getBytes();
							message.what = 0;
							handler.sendMessage(message);
						}
					}
				}
			}
			synchronized (this) {
				try {// 发送消息队列中没有消息了，线程进入等待状态
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
