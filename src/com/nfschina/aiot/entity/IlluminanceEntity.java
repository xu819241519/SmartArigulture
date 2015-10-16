package com.nfschina.aiot.entity;

import java.sql.Timestamp;

/**
 * 光照实体
 * @author xu
 *
 */

public class IlluminanceEntity {

	//光照实体的属性
	private int mData;
	private Timestamp mTimestamp;
	public int getmData() {
		return mData;
	}
	public Timestamp getmTimestamp() {
		return mTimestamp;
	}
	public IlluminanceEntity(int mData, Timestamp mTimestamp) {
		super();
		this.mData = mData;
		this.mTimestamp = mTimestamp;
	}
	
	

}
