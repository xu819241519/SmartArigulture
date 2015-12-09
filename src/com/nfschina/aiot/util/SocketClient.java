package com.nfschina.aiot.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketClient {
	private String serverIp = "10.50.200.120";
	private int port = 9666;
	public String receiveMsg;//将要发送的报文
	private SocketChannel channel ;
	
	public SocketClient (String message){
		try {
			channel = SocketChannel.open();
			SocketAddress address = new InetSocketAddress(serverIp, port);
			channel.connect(address);
			sendMessage(channel, message);
			this.receiveMsg = receiveData(channel);//从服务端接受的数据
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 向服务器发送报文
	 * @throws Exception 
	 */
	public void sendMessage(SocketChannel channel, String message) throws Exception{
		byte[] bytes = message.getBytes();
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		channel.write(buffer);
		channel.socket().shutdownOutput();
	}
	/**
	 * 接受服务器返回的数据
	 * @param channel
	 * @return
	 * @throws Exception
	 */
	public String receiveData(SocketChannel channel) throws Exception {
		String receiveMsg = "";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte[] bytes;
		int count = 0;
		while ((count = channel.read(buffer)) >= 0) {
			buffer.flip();
			bytes = new byte[count];
			buffer.get(bytes);
			baos.write(bytes);
			buffer.clear();
		}
		bytes = baos.toByteArray();
		receiveMsg = new String(bytes);
		baos.close();
		buffer.clear();
		System.out.println(receiveMsg);
		return receiveMsg;
	}
//	public static void main(String[] args) {
//		new SocketClient("CONNECT");
//	}

}
