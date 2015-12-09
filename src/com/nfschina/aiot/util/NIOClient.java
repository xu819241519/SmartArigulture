package com.nfschina.aiot.util;

import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOClient {

	/*��ʶ����*/
	private static int flag = 0;
	/*��������С*/
	private static int BLOCK = 4096;
	/*�������ݻ�����*/
	private static ByteBuffer sendbuffer = ByteBuffer.allocate(BLOCK);
	/*�������ݻ�����*/
	private static ByteBuffer receivebuffer = ByteBuffer.allocate(BLOCK);
	/*�������˵�ַ*/
	private final static InetSocketAddress SERVER_ADDRESS = new InetSocketAddress(
			"10.50.200.120", 9666);
	SocketChannel socketChannel ;

	public  NIOClient(){
		// ��ѡ����
		Selector selector;
		try {
			// ��socketͨ��
			socketChannel = SocketChannel.open();
			// ����Ϊ��������ʽ
			socketChannel.configureBlocking(false);
			selector = Selector.open();
			// ע�����ӷ����socket����
			socketChannel.register(selector, SelectionKey.OP_CONNECT);
			// ����
			socketChannel.connect(SERVER_ADDRESS);
		// ���仺������С�ڴ�
		Set<SelectionKey> selectionKeys;
		Iterator<SelectionKey> iterator;
		SelectionKey selectionKey;
		SocketChannel client;
		String receiveText;
		String sendText;
		int count=0;

		while (true) {
			//ѡ��һ���������Ӧ��ͨ����Ϊ I/O ����׼��������
			//�˷���ִ�д�������ģʽ��ѡ�������
			selector.select();
			//���ش�ѡ��������ѡ�������
			selectionKeys = selector.selectedKeys();
			//System.out.println(selectionKeys.size());
			iterator = selectionKeys.iterator();
			while (iterator.hasNext()) {
				selectionKey = iterator.next();
				if (selectionKey.isConnectable()) {
					System.out.println("client connect");
					client = (SocketChannel) selectionKey.channel();
					// �жϴ�ͨ�����Ƿ����ڽ������Ӳ�����
					// ����׽���ͨ�������ӹ��̡�
					if (client.isConnectionPending()) {
						client.finishConnect();
						System.out.println("�������!");
						sendbuffer.clear();
						sendbuffer.put("CONNECT".getBytes());
						sendbuffer.flip();
						client.write(sendbuffer);
					}
					client.register(selector, SelectionKey.OP_READ);
				} else if (selectionKey.isReadable()) {
					client = (SocketChannel) selectionKey.channel();
					//������������Ա��´ζ�ȡ
					receivebuffer.clear();
					//��ȡ�����������������ݵ���������
					count=client.read(receivebuffer);
					if(count>0){
						receiveText = new String( receivebuffer.array(),0,count);
						System.out.println("�ͻ��˽��ܷ�����������--:"+receiveText);
						client.register(selector, SelectionKey.OP_WRITE);
					}

				} else if (selectionKey.isWritable()) {
					sendbuffer.clear();
					client = (SocketChannel) selectionKey.channel();
					sendText = "message from client--" + (flag++);
					sendbuffer.put(sendText.getBytes());
					 //������������־��λ,��Ϊ������put�����ݱ�־���ı�Ҫ����ж�ȡ���ݷ��������,��Ҫ��λ
					sendbuffer.flip();
					client.write(sendbuffer);
					System.out.println("�ͻ�����������˷�������--��"+sendText);
					client.register(selector, SelectionKey.OP_READ);
				}
			}
			selectionKeys.clear();
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * ����������ͱ���
	 * @throws Exception 
	 */
	public void sendMessage(String message) throws Exception{
		byte[] bytes = message.getBytes();
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		socketChannel.write(buffer);
		socketChannel.socket().shutdownOutput();
	}
	/**
	 * ���ܷ��������ص�����
	 * @param channel
	 * @return
	 * @throws Exception
	 */
	public String receiveData() throws Exception {
		String receiveMsg = "";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte[] bytes;
		int count = 0;
		while ((count = socketChannel.read(buffer)) >= 0) {
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
}
