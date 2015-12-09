package com.nfschina.aiot.entity;

import java.io.Serializable;

public class Threshold implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private float uptem,lowtem;//温度阀值
	private int upco2,lowco2;
	private float uphum,lowhum;//湿度阀值
	private int upillum,lowillum;//光照阀值
	private String userId;//创建人
	private String greenhouseId;//对应的温室
	private String updatetime;//更新时间
	private int thresholdFlag;//标志位
	
	public Threshold(){}
	public Threshold(float uptem, float lowtem, int upco2, int lowco2,
			float uphum, float lowhum, int upillum, int lowillum,String updatetime) {
		this.uptem = uptem;
		this.lowtem = lowtem;
		this.upco2 = upco2;
		this.lowco2 = lowco2;
		this.uphum = uphum;
		this.lowhum = lowhum;
		this.upillum = upillum;
		this.lowillum = lowillum;
		this.updatetime = updatetime;
	}
	public Threshold(int id, float uptem, float lowtem, int upco2, int lowco2,
			float uphum, float lowhum, int upillum, int lowillum,
			String userId, String greenhouseId, String updatetime,
			int thresholdFlag) {
		this.id = id;
		this.uptem = uptem;
		this.lowtem = lowtem;
		this.upco2 = upco2;
		this.lowco2 = lowco2;
		this.uphum = uphum;
		this.lowhum = lowhum;
		this.upillum = upillum;
		this.lowillum = lowillum;
		this.userId = userId;
		this.greenhouseId = greenhouseId;
		this.updatetime = updatetime;
		this.thresholdFlag = thresholdFlag;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public float getUptem() {
		return uptem;
	}
	public void setUptem(float uptem) {
		this.uptem = uptem;
	}
	public float getLowtem() {
		return lowtem;
	}
	public void setLowtem(float lowtem) {
		this.lowtem = lowtem;
	}
	public int getUpco2() {
		return upco2;
	}
	public void setUpco2(int upco2) {
		this.upco2 = upco2;
	}
	public int getLowco2() {
		return lowco2;
	}
	public void setLowco2(int lowco2) {
		this.lowco2 = lowco2;
	}
	public float getUphum() {
		return uphum;
	}
	public void setUphum(float uphum) {
		this.uphum = uphum;
	}
	public float getLowhum() {
		return lowhum;
	}
	public void setLowhum(float lowhum) {
		this.lowhum = lowhum;
	}
	public int getUpillum() {
		return upillum;
	}
	public void setUpillum(int upillum) {
		this.upillum = upillum;
	}
	public int getLowillum() {
		return lowillum;
	}
	public void setLowillum(int lowillum) {
		this.lowillum = lowillum;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getGreenhouseId() {
		return greenhouseId;
	}
	public void setGreenhouseId(String greenhouseId) {
		this.greenhouseId = greenhouseId;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public int getThresholdFlag() {
		return thresholdFlag;
	}
	public void setThresholdFlag(int thresholdFlag) {
		this.thresholdFlag = thresholdFlag;
	}
	
	
	
	

}
