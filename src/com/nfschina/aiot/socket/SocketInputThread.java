package com.nfschina.aiot.socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

import com.nfschina.aiot.activity.Home;

import android.content.Intent;
import android.util.Log;

/**
 * 客户端读消息线程
 * @author wujian
 */
public class SocketInputThread extends Thread {
	private boolean isStart = true;
	// private MessageListener messageListener;// 消息监听接口对象
	public SocketInputThread() {
	}
	/**
	 * 在类外部控制这个线程停止还是执行的方法
	 * @param isStart
	 */
	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}
	@Override
	public void run() {
		while (isStart) {
			// 判断手机网络情况，手机能联网，读socket数据
			if (NetManager.instance().isNetworkConnected()) {
				System.out.println("NetManager");
				//再判断当前socket是否保持连接
				if (!TCPClient.instance().isConnect()) {
					Log.e("socket","TCPClient connet server is fail read thread sleep second"
									+ Const.SOCKET_SLEEP_SECOND);
					try {
						sleep(Const.SOCKET_SLEEP_SECOND * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				readSocket();
				// 如果连接服务器失败,服务器连接失败，sleep固定的时间，能联网，就不要sleep
				Log.e("socket", "TCPClient.instance().isConnect() "
						+ TCPClient.instance().isConnect());
			}
		}
	}
	/**
	 * 读取服务器发送的消息的方法
	 */
	public void readSocket() {
		Selector selector = TCPClient.instance().getSelector();
		System.out.println("readSocket");
		if (selector == null) {
			System.out.println("selector.select() = 0");
			return;
		}
		try {
			// 如果没有数据过来，一直阻塞
			while (selector.select() > 0 ) {
				System.out.println("selector.select() > 0");
				for (SelectionKey sk : selector.selectedKeys()) {
					// 如果该SelectionKey对应的Channel中有可读的数据
					if (sk.isReadable()) {
						// 使用NIO读取Channel中的数据
						SocketChannel sc = (SocketChannel) sk.channel();
						ByteBuffer buffer = ByteBuffer.allocate(1024);
						try {
							sc.read(buffer);
						} catch (IOException e) {
							e.printStackTrace();
							// continue;
						}
						buffer.flip();
						String receivedString = "";
						// 打印收到的数据
						try {
							receivedString = Charset.forName("UTF-8")
									.newDecoder().decode(buffer).toString();
							//CLog.e(tag, receivedString);
							System.out.println("接受报文个数"+receivedString.length()+"");
							Intent i = new Intent("BC");
							i.putExtra("response", receivedString);
							// 发广播
							Home.context.sendBroadcast(i);
						} catch (CharacterCodingException e) {
							e.printStackTrace();
						}
						//为下一次接收数据做准备
						buffer.clear();
						buffer = null;
						try {
							// 为下次读取作准备
							sk.interestOps(SelectionKey.OP_READ);
							// 删除正在处理的SelectionKey
							selector.selectedKeys().remove(sk);
						} catch (CancelledKeyException e) {
							e.printStackTrace();
						}
					}
				}
			}
//			 selector.close();
//			 TCPClient.instance().repareRead();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ClosedSelectorException e2) {
		}
	}
}
