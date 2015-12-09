package com.nfschina.aiot.entity;
/**
 * 阀值设置报文结构
 * @author wujian
 *
 */
public class ThresholdMsg {
	
	private String headMark;
	private String type;
	private String fromMark;
	private String temperatureMax;
	private String co2Max;
	private String waterMax;//湿度
	private String lightMax;//光照
	private String temperatureMin;
	private String co2Min;
	private String waterMin;//湿度
	private String lightMin;//光照
	private String extendPart;//扩展备用
	private String greenhouseId;
	private String userId;
	private String updateTime;
	private String checkNum;
	
	@Override
	public String toString() {
		return headMark+type+fromMark+temperatureMax+co2Max+waterMax+lightMax
				+temperatureMin+co2Min+waterMin+lightMin+extendPart
				+greenhouseId+userId+updateTime+checkNum;
	}
	public String getMsgEntity(){
		return headMark+type+fromMark+temperatureMax+co2Max+waterMax+lightMax
				+temperatureMin+co2Min+waterMin+lightMin+extendPart
				+greenhouseId+userId+updateTime;
	}
	
	public ThresholdMsg(){}
	public ThresholdMsg(String headMark, String type, String fromMark,
			String temperatureMax, String co2Max, String waterMax,
			String lightMax, String temperatureMin, String co2Min,
			String waterMin, String lightMin, String extendPart,
			 String greenhouseId, String userId,
			String updateTime) {
		this.headMark = headMark;
		this.type = type;
		this.fromMark = fromMark;
		this.temperatureMax = temperatureMax;
		this.co2Max = co2Max;
		this.waterMax = waterMax;
		this.lightMax = lightMax;
		this.temperatureMin = temperatureMin;
		this.co2Min = co2Min;
		this.waterMin = waterMin;
		this.lightMin = lightMin;
		this.extendPart = extendPart;
		this.greenhouseId = greenhouseId;
		this.userId = userId;
		this.updateTime = updateTime;
	}
	public ThresholdMsg(String headMark, String type, String fromMark,
			String temperatureMax, String co2Max, String waterMax,
			String lightMax, String temperatureMin, String co2Min,
			String waterMin, String lightMin, String extendPart,
			String greenhouseId, String userId,
			String updateTime, String checkNum) {
		this.headMark = headMark;
		this.type = type;
		this.fromMark = fromMark;
		this.temperatureMax = temperatureMax;
		this.co2Max = co2Max;
		this.waterMax = waterMax;
		this.lightMax = lightMax;
		this.temperatureMin = temperatureMin;
		this.co2Min = co2Min;
		this.waterMin = waterMin;
		this.lightMin = lightMin;
		this.extendPart = extendPart;
		this.greenhouseId = greenhouseId;
		this.userId = userId;
		this.updateTime = updateTime;
		this.checkNum = checkNum;
	}
	public ThresholdMsg(
			String temperatureMax, String co2Max, String waterMax,
			String lightMax, String temperatureMin, String co2Min,
			String waterMin, String lightMin) {
		this.temperatureMax = temperatureMax;
		this.co2Max = co2Max;
		this.waterMax = waterMax;
		this.lightMax = lightMax;
		this.temperatureMin = temperatureMin;
		this.co2Min = co2Min;
		this.waterMin = waterMin;
		this.lightMin = lightMin;
	}


	public String getHeadMark() {
		return headMark;
	}

	public void setHeadMark(String headMark) {
		this.headMark = headMark;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFromMark() {
		return fromMark;
	}

	public void setFromMark(String fromMark) {
		this.fromMark = fromMark;
	}

	public String getTemperatureMax() {
		return temperatureMax;
	}

	public void setTemperatureMax(String temperatureMax) {
		this.temperatureMax = temperatureMax;
	}

	public String getCo2Max() {
		return co2Max;
	}

	public void setCo2Max(String co2Max) {
		this.co2Max = co2Max;
	}

	public String getWaterMax() {
		return waterMax;
	}

	public void setWaterMax(String waterMax) {
		this.waterMax = waterMax;
	}

	public String getLightMax() {
		return lightMax;
	}

	public void setLightMax(String lightMax) {
		this.lightMax = lightMax;
	}

	public String getTemperatureMin() {
		return temperatureMin;
	}

	public void setTemperatureMin(String temperatureMin) {
		this.temperatureMin = temperatureMin;
	}

	public String getCo2Min() {
		return co2Min;
	}

	public void setCo2Min(String co2Min) {
		this.co2Min = co2Min;
	}

	public String getWaterMin() {
		return waterMin;
	}

	public void setWaterMin(String waterMin) {
		this.waterMin = waterMin;
	}

	public String getLightMin() {
		return lightMin;
	}

	public void setLightMin(String lightMin) {
		this.lightMin = lightMin;
	}

	public String getExtendPart() {
		return extendPart;
	}

	public void setExtendPart(String extendPart) {
		this.extendPart = extendPart;
	}

	public String getGreenhouseId() {
		return greenhouseId;
	}

	public void setGreenhouseId(String greenhouseId) {
		this.greenhouseId = greenhouseId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getCheckNum() {
		return checkNum;
	}

	public void setCheckNum(String checkNum) {
		this.checkNum = checkNum;
	}
	

}
