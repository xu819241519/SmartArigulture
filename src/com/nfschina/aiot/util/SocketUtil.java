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
	
		//������SocketChannel��Selector����
		private Selector selector=null;
		//���崦�����ͽ�����ַ���
		private Charset charset=Charset.forName("UTF-8");
		//�ͻ���SocketChannel
		private SocketChannel sc=null;
		
		
		public void init() throws IOException{
			selector=Selector.open();
			InetSocketAddress isa=new InetSocketAddress(serverIP,port);
			//����open��̬�����������ӵ�ָ��������SocketChannel
			sc=SocketChannel.open(isa);
			//���ø�sc�Է�������ʽ����
			sc.configureBlocking(false);
			//��Socketchannel����ע�ᵽָ��Selector
			sc.register(selector, SelectionKey.OP_WRITE);
			//������ȡ�����������ݵ��߳�
			new ClientThread().start();
			//��������������
			sc.write(charset.encode(data_send));
			sc.register(selector, SelectionKey.OP_READ);
		}
		//�����ȡ���������ݵ��߳�
		private class ClientThread extends Thread{
			public void run(){
				try{
					while(selector.select()>0){
						//����ÿ���п���IO����Channel��Ӧ��SelectionKey
						for(SelectionKey sk:selector.selectedKeys()){
							//ɾ�����ڴ����SelectionKey
							selector.selectedKeys().remove(sk);
							//�����SelectionKey��Ӧ��Channel���пɶ�������
							if(sk.isReadable()){
								//ʹ��NIO��ȡchannel�е�����
								SocketChannel sc=(SocketChannel) sk.channel();
								ByteBuffer buff=ByteBuffer.allocate(1024);
								String content="";
								while(sc.read(buff)>0){
									sc.read(buff);
									buff.flip();
									content+=charset.decode(buff);
								}
								//��ӡ�����ȡ������
								System.out.println("������Ϣ"+content);
								//Ϊ��һ�ζ�ȡ��׼��
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
