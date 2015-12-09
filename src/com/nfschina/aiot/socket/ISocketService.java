package com.nfschina.aiot.socket;

import android.os.Handler;
/**
 * 访问服务中的方法的接口
 * @author wujian
 */
public interface ISocketService {
	
	public void sentMsg(byte[] buffer, Handler handler);

}
