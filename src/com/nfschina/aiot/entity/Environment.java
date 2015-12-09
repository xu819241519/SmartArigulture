package com.nfschina.aiot.entity;

public class Environment {
	private int id;
	private float temperature;
	private int co2;
	private float humidity;
	private int illuminance;
	private String recordtime;
	
	public Environment(){}
	
	public Environment(int id, float temperature, int co2, float humidity,
			int illuminance, String recordtime) {
		this.id = id;
		this.temperature = temperature;
		this.co2 = co2;
		this.humidity = humidity;
		this.illuminance = illuminance;
		this.recordtime = recordtime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getRecordtime() {
		return recordtime;
	}

	public void setRecordtime(String recordtime) {
		this.recordtime = recordtime;
	}
	
	
	

}
