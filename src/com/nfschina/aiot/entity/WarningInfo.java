package com.nfschina.aiot.entity;

public class WarningInfo {
	
	private int id;
	private String greenHouseid;//针对哪个温室进行报警
	private float temperature;
	private int co2;
	private float humidity;
	private int illuminance;
	private String warningtime;
	private int warningstate;//报警信息状态
	
	public WarningInfo(){}
	public WarningInfo(String greenHouseid) {
		this.greenHouseid = greenHouseid;
	}
	public WarningInfo(int id, String greenHouseid, float temperature, int co2,
			float humidity, int illuminance, String warningtime,
			int warningstate) {
		this.id = id;
		this.greenHouseid = greenHouseid;
		this.temperature = temperature;
		this.co2 = co2;
		this.humidity = humidity;
		this.illuminance = illuminance;
		this.warningtime = warningtime;
		this.warningstate = warningstate;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGreenHouseid() {
		return greenHouseid;
	}

	public void setGreenHouseid(String greenHouseid) {
		this.greenHouseid = greenHouseid;
	}

	public float getTemperature() {
		return temperature;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	public int getCo2() {
		return co2;
	}

	public void setCo2(int co2) {
		this.co2 = co2;
	}

	public float getHumidity() {
		return humidity;
	}

	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}

	public int getIlluminance() {
		return illuminance;
	}

	public void setIlluminance(int illuminance) {
		this.illuminance = illuminance;
	}

	public String getWarningtime() {
		return warningtime;
	}

	public void setWarningtime(String warningtime) {
		this.warningtime = warningtime;
	}

	public int getWarningstate() {
		return warningstate;
	}

	public void setWarningstate(int warningstate) {
		this.warningstate = warningstate;
	}
	
	
	
}
