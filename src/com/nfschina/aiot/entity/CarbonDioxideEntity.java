package com.nfschina.aiot.entity;

import com.nfschina.aiot.constant.Constant;

/**
 * ������̼ʵ��
 * 
 * @author xu
 *
 */

public class CarbonDioxideEntity extends EnvironmentParameterEntity {

	// ������̼ʵ�������
	private int mData;
	private String mTime;

	public CarbonDioxideEntity(int mData, String time) {
		this.mData = mData;
		mTime = time;
		setKind(Constant.CARBONDIOXIDE);
	}

	public int getData() {
		return mData;
	}

	public String getTime() {
		return mTime;
	}
}
