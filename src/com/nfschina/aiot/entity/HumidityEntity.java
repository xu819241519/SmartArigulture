package com.nfschina.aiot.entity;

import com.nfschina.aiot.constant.Constant;

/**
 * 湿度实体
 * 
 * @author xu
 *
 */

public class HumidityEntity extends EnvironmentParameterEntity {

	// 湿度实体的属性
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
