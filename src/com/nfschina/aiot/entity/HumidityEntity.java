package com.nfschina.aiot.entity;

import java.sql.Timestamp;

/**
 * ʪ��ʵ��
 * @author xu
 *
 */

public class HumidityEntity {

	//ʪ��ʵ�������
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
