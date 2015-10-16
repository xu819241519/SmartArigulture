package com.nfschina.aiot.entity;

import java.sql.Timestamp;


/**
 * 温度实体
 * @author xu
 *
 */

public class TemperatureEntity {

	//温度实体的属性
	private float mData;
	private Timestamp mTimestamp;
	public float getmData() {
		return mData;
	}
	public Timestamp getmTimestamp() {
		return mTimestamp;
	}
	public TemperatureEntity(float mData, Timestamp mTimestamp) {
		super();
		this.mData = mData;
		this.mTimestamp = mTimestamp;
	}
	
	
}
