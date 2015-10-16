package com.nfschina.aiot.entity;

import java.sql.Timestamp;

/**
 * 湿度实体
 * @author xu
 *
 */

public class HumidityEntity {

	//湿度实体的属性
	private float mData;
	private Timestamp mTimestamp;
	public HumidityEntity(float mData, Timestamp mTimestamp) {
		super();
		this.mData = mData;
		this.mTimestamp = mTimestamp;
	}
	public float getmData() {
		return mData;
	}
	public Timestamp getmTimestamp() {
		return mTimestamp;
	}
}
