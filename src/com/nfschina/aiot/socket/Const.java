package com.nfschina.aiot.socket;
/**
 * 常量类
 * @author wujian
 */
public class Const {

	public final static String SOCKET_SERVER = "10.50.200.120";
	//public final static String SOCKET_SERVER = "222.173.73.19";
	public final static int SOCKET_PORT = 9666;

	// 默认timeout 时间 60s
	public final static int SOCKET_TIMOUT = 60 * 1000;

	public final static int SOCKET_READ_TIMOUT = 15 * 1000;

	// 如果没有连接无服务器。读线程的sleep时间
	public final static int SOCKET_SLEEP_SECOND = 3;

	// 心跳包发送间隔时隔
	public final static int SOCKET_HEART_SECOND = 3;

	public final static String BC = "BC";

}
