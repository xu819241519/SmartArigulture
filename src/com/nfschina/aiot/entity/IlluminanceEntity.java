package com.nfschina.aiot.entity;

import com.nfschina.aiot.constant.Constant;

/**
 * ����ʵ��
 * 
 * @author xu
 *
 */

public class IlluminanceEntity extends EnvironmentParameterEntity {

	// ����ʵ�������
	private int mData;
	private String mTime;

	public int getData() {
		return mData;
	}

	public String getTime() {
		return mTime;
	}

	public IlluminanceEntity(int mData, String time) {
		super();
		this.mData = mData;
		this.mTime = time;
		setKind(Constant.ILLUMINANCE);
	}

}
