package com.nfschina.aiot.entity;

import java.sql.Timestamp;

/**
 * 二氧化碳实体
 * @author xu
 *
 */

public class CarbonDioxideEntity {

	//二氧化碳实体的属性
	private int mData;
	private Timestamp mTimestamp;
	public CarbonDioxideEntity(int mData, Timestamp mTimestamp) {
		this.mData = mData;
		this.mTimestamp = mTimestamp;
	}
	public int getmData() {
		return mData;
	}
	public Timestamp getmTimestamp() {
		return mTimestamp;
	}
}
