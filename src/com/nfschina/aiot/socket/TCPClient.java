package com.nfschina.aiot.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * Socket TCP连接的客户端
 * @author wujian
 */
public class TCPClient {
	// 信道选择器
	private Selector selector;
	// 与服务器通信的信道
	SocketChannel socketChannel;
	// 要连接的服务器Ip地址
	private String hostIp;
	// 要连接的远程服务器在监听的端口
	private int hostListenningPort;
	private static TCPClient client = null;
	//是否进行初始化了
	public boolean isInitialized = false;
	/**
	 * 单例模式，保持只有一个客户端被创建
	 * @return
	 */
	public static synchronized TCPClient instance() {
		if (client == null) {
			client = new TCPClient(Const.SOCKET_SERVER, Const.SOCKET_PORT);
		}
		return client;
	}
	/**
	 * 构造函数，连接服务器
	 * @param HostIp 服务器ip
	 * @param HostListenningPort  服务器端口
	 * @throws IOException
	 */
	public TCPClient(String HostIp, int HostListenningPort) {
		this.hostIp = HostIp;
		this.hostListenningPort = HostListenningPort;
		try {
			// 这是一个联网操作，需要放在子线程里面
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						initialize();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
			this.isInitialized = true;
		} catch (Exception e) {
			this.isInitialized = false;
			e.printStackTrace();
		}
	}
	/**
	 * 初始化，连接服务器的操作
	 * @throws IOException
	 */
	public void initialize() throws IOException {
		boolean done = false;
		try {
			// 打开监听信道并设置为非阻塞模式
			socketChannel = SocketChannel.open();
			SocketAddress address = new InetSocketAddress(hostIp,
					hostListenningPort);
			socketChannel.connect(address);
			if (socketChannel != null) {
				socketChannel.socket().setTcpNoDelay(false);
				socketChannel.socket().setKeepAlive(true);
				// 设置 读socket的timeout时间
				socketChannel.socket().setSoTimeout(Const.SOCKET_READ_TIMOUT);
				socketChannel.configureBlocking(false);
				// 打开并注册选择器到信道
				selector = Selector.open();
				if (selector != null) {
					//想选择器注册一个读事件
					socketChannel.register(selector, SelectionKey.OP_READ);
					done = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//如果没有完成初始化
			if (!done && selector != null) {
				selector.close();
			}
			if (!done) {
				socketChannel.close();
			}
		}
	}

	static void blockUntil(SelectionKey key, long timeout) throws IOException {

		int nkeys = 0;
		if (timeout > 0) {
			nkeys = key.selector().select(timeout);

		} else if (timeout == 0) {
			nkeys = key.selector().selectNow();
		}

		if (nkeys == 0) {
			throw new SocketTimeoutException();
		}
	}

	/**
	 * 发送字符串到服务器
	 * 
	 * @param message
	 * @throws IOException
	 */
	public void sendMsg(String message) throws IOException {
		ByteBuffer writeBuffer = ByteBuffer.wrap(message.getBytes("utf-8"));

		if (socketChannel == null) {
			throw new IOException();
		}
		socketChannel.write(writeBuffer);
	}

	/**
	 * 发送数据
	 * 
	 * @param bytes
	 * @throws IOException
	 */
	public void sendMsg(byte[] bytes) throws IOException {
		ByteBuffer writeBuffer = ByteBuffer.wrap(bytes);

		if (socketChannel == null) {
			throw new IOException();
		}
		socketChannel.write(writeBuffer);
	}

	/**
	 * 
	 * @return
	 */
	public synchronized Selector getSelector() {
		return this.selector;
	}

	/**
	 * Socket连接是否是正常的
	 * 
	 * @return
	 */
	public boolean isConnect() {
		boolean isConnect = false;
		if (this.isInitialized) {
			isConnect = this.socketChannel.isConnected();
		}
		return isConnect;
	}

	/**
	 * 关闭socket 重新连接
	 * 
	 * @return
	 */
	public boolean reConnect() {
		closeTCPSocket();

		try {
			initialize();
			isInitialized = true;
		} catch (IOException e) {
			isInitialized = false;
			e.printStackTrace();
		} catch (Exception e) {
			isInitialized = false;
			e.printStackTrace();
		}
		return isInitialized;
	}

	/**
	 * 服务器是否关闭，通过发送一个socket信息
	 * 
	 * @return
	 */
	public boolean canConnectToServer() {
		try {
			if (socketChannel != null) {
				socketChannel.socket().sendUrgentData(0xff);
				System.out.println("已经发送了心跳包");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 关闭socket
	 */
	public void closeTCPSocket() {
		try {
			if (socketChannel != null) {
				socketChannel.close();
			}

		} catch (IOException e) {

		}
		try {
			if (selector != null) {
				selector.close();
			}
		} catch (IOException e) {
		}
	}

	/**
	 * 每次读完数据后，需要重新注册selector，读取数据
	 */
	public synchronized void repareRead() {
		if (socketChannel != null) {
			try {
				selector = Selector.open();
				socketChannel.register(selector, SelectionKey.OP_READ);
			} catch (ClosedChannelException e) {
				e.printStackTrace();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
