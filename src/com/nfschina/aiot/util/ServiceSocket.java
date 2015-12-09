package com.nfschina.aiot.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServiceSocket {
	
	 public static void main(String[] args) {
         ServerSocket serverSocket = null;
         Socket socket = null;
         OutputStream os = null;
         InputStream is = null;
         //监听端口号
         int port = 10000;
         try {
                  //建立连接
                  serverSocket = new ServerSocket(port);
                  //获得连接
                  socket = serverSocket.accept();
                  //接收客户端发送内容
                  is = socket.getInputStream();
                  byte[] b = new byte[1024];
                  int n = is.read(b);
                  //输出
                  System.out.println("客户端发送内容为：" + new String(b,0,n));
                  //向客户端发送反馈内容
                  Thread.sleep(2000);
                  os = socket.getOutputStream();
                  os.write(b, 0, n);
         } catch (Exception e) {
                  e.printStackTrace();
         }finally{
                  try{
                	  //Thread.sleep(3000);
                           //关闭流和连接
                           os.close();
                           is.close();
                           socket.close();
                           serverSocket.close();
                  }catch(Exception e){}
         }
}

}
