package com.nfschina.aiot.entity;

import java.sql.Timestamp;


/**
 * �¶�ʵ��
 * @author xu
 *
 */

public class TemperatureEntity {

	//�¶�ʵ�������
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
