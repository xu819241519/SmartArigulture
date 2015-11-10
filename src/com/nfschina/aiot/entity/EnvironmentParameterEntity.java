package com.nfschina.aiot.entity;

public abstract class EnvironmentParameterEntity {

	// 表明哪种环境参数
	protected int mKind;

	public int getKind() {
		return mKind;
	}

	public void setKind(int Kind) {
		this.mKind = Kind;
	}

}
