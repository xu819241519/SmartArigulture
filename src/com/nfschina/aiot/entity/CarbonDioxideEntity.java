package com.nfschina.aiot.entity;

import java.sql.Timestamp;

/**
 * ������̼ʵ��
 * @author xu
 *
 */

public class CarbonDioxideEntity {

	//������̼ʵ�������
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
