package com.nfschina.aiot.entity;

import com.nfschina.aiot.constant.Constant;

/**
 * ʪ��ʵ��
 * 
 * @author xu
 *
 */

public class HumidityEntity extends EnvironmentParameterEntity {

	// ʪ��ʵ�������
	private float mData;
	private String mTime;

	public HumidityEntity(float mData, String time) {
		this.mData = mData;
		mTime = time;
		setKind(Constant.HUMIDITY);
	}

	public float getData() {
		return mData;
	}

	public String getTime() {
		return mTime;
	}
}
