package com.nfschina.aiot.entity;

import java.sql.Timestamp;

/**
 * ����ʵ��
 * @author xu
 *
 */

public class IlluminanceEntity {

	//����ʵ�������
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
