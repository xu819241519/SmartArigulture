package com.nfschina.aiot.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class SocketUtil {
	
	private Socket socket ;
	private String serverIP = "10.50.200.120";
	private int port = 9666;
	BufferedReader br ;
	private String data_send = "CONNECT";
	
		//定义检测SocketChannel的Selector对象
		private Selector selector=null;
		//定义处理编码和解码的字符集
		private Charset charset=Charset.forName("UTF-8");
		//客户端SocketChannel
		private SocketChannel sc=null;
		
		
		public void init() throws IOException{
			selector=Selector.open();
			InetSocketAddress isa=new InetSocketAddress(serverIP,port);
			//调用open静态方法创建连接到指定主机的SocketChannel
			sc=SocketChannel.open(isa);
			//设置该sc以非阻塞方式工作
			sc.configureBlocking(false);
			//将Socketchannel对象注册到指定Selector
			sc.register(selector, SelectionKey.OP_WRITE);
			//启动读取服务器端数据的线程
			new ClientThread().start();
			//创建键盘输入流
			sc.write(charset.encode(data_send));
			sc.register(selector, SelectionKey.OP_READ);
		}
		//定义读取服务器数据的线程
		private class ClientThread extends Thread{
			public void run(){
				try{
					while(selector.select()>0){
						//遍历每个有可用IO操作Channel对应的SelectionKey
						for(SelectionKey sk:selector.selectedKeys()){
							//删除正在处理的SelectionKey
							selector.selectedKeys().remove(sk);
							//如果该SelectionKey对应的Channel中有可读的数据
							if(sk.isReadable()){
								//使用NIO读取channel中的数据
								SocketChannel sc=(SocketChannel) sk.channel();
								ByteBuffer buff=ByteBuffer.allocate(1024);
								String content="";
								while(sc.read(buff)>0){
									sc.read(buff);
									buff.flip();
									content+=charset.decode(buff);
								}
								//打印输出读取的内容
								System.out.println("聊天信息"+content);
								//为下一次读取做准备
								sk.interestOps(SelectionKey.OP_READ);
							}
						}
					}
				}catch(IOException ex){
					ex.printStackTrace();
				}
			}
		}
	public static void main(String[] args) {
		try {
			new SocketUtil().init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
