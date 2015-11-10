package com.nfschina.aiot.entity;

import com.nfschina.aiot.constant.Constant;

/**
 * 二氧化碳实体
 * 
 * @author xu
 *
 */

public class CarbonDioxideEntity extends EnvironmentParameterEntity {

	// 二氧化碳实体的属性
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
