package com.nfschina.aiot.entity;

import com.nfschina.aiot.constant.Constant;

/**
 * 温度实体
 * 
 * @author xu
 *
 */

public class TemperatureEntity extends EnvironmentParameterEntity {

	// 温度实体的属性
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
