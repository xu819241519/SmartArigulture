package com.nfschina.aiot.entity;

import com.nfschina.aiot.constant.Constant;

/**
 * �¶�ʵ��
 * 
 * @author xu
 *
 */

public class TemperatureEntity extends EnvironmentParameterEntity {

	// �¶�ʵ�������
	private float mData;
	private String mTime;

	public float getData() {
		return mData;
	}

	public String getTime() {
		return mTime;
	}

	public TemperatureEntity(float mData, String time) {
		super();
		this.mData = mData;
		this.mTime = time;
		setKind(Constant.TEMPERATURE);
	}

}
