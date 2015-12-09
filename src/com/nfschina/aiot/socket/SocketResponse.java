package com.nfschina.aiot.socket;

public abstract class SocketResponse<T> {
	/**
	 * 返回数据请求的结果
	 * @param response
	 */
	  public abstract void onResponse(T response);
}
