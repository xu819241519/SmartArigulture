package com.nfschina.aiot.socket;
/**
 * 心跳包线程，保持客户端与服务端一直保持连接
 * @author wujian
 */
public class SocketHeartThread extends Thread {
	boolean isStop = false;//是否停止
	boolean mIsConnectSocketSuccess = false;
	static SocketHeartThread heartThread;
	private TCPClient mTcpClient = null;
	static final String tag = "SocketHeartThread";
	/**
	 * 单例模式初始化
	 * @return
	 */
	public static synchronized SocketHeartThread instance() {
		if (heartThread == null) {
			heartThread = new SocketHeartThread();
		}
		return heartThread;
	}
	/**
	 * 进行TCP连接
	 */
	public SocketHeartThread() {
		TCPClient.instance();
		// 连接服务器
		// mIsConnectSocketSuccess = connect();
	}
	/**
	 * 停止线程,可以让这个线程返回，停止
	 */
	public void stopThread() {
		isStop = true;
	}
	/**
	 * 连接socket到服务器, 并发送初始化的Socket信息
	 * @return
	 */
	private boolean reConnect() {
		return TCPClient.instance().reConnect();
	}
	@Override
	public void run() {
		isStop = false;
		while (!isStop) {
			// 发送一个心跳包看服务器是否正常
			boolean canConnectToServer = TCPClient.instance()
					.canConnectToServer();
			if (canConnectToServer == false) {
				//如果已经断开连接了，重新连接
				reConnect();
			}
			try {
				//每隔一段时间，检测一次
				Thread.sleep(Const.SOCKET_HEART_SECOND * 10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
