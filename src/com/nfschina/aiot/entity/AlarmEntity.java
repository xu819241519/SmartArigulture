package com.nfschina.aiot.entity;

/**
 * 报警记录实体
 * 
 * @author xu
 *
 */

public class AlarmEntity {

	// 报警ID
	private int ID;
	// 温室ID
	private String GreenHouseID;
	// 报警时间
	private String Time;
	// 报警状态
	private String State;
	// 等级
	private String Level;
	// 二氧化碳超出值
	private String CO2;
	// 温度超出值
	private String Temperature;
	// 湿度超出值
	private String Humidity;
	// 光照超出值
	private String Illuminance;

	public AlarmEntity(int iD, String greenHouseID, String temperature, String co2, String humidity, String illuminance,
			String time, String state, String level) {
		ID = iD;
		GreenHouseID = greenHouseID;
		Time = time;
		State = state;
		Temperature = temperature;
		Humidity = humidity;
		illuminance = Illuminance;
		CO2 = co2;
		Level = level;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getGreenHouseID() {
		return GreenHouseID;
	}

	public void setGreenHouseID(String greenHouseID) {
		GreenHouseID = greenHouseID;
	}

	public String getContent() {
		String result = "";
		if (CO2 != null && !CO2.equals(""))
			result += CO2 + "\n";
		if (Illuminance != null && !Illuminance.equals(""))
			result += Illuminance + "\n";
		if (Temperature != null && !Temperature.equals(""))
			result += Temperature + "\n";
		if (Humidity != null && !Humidity.equals(""))
			result += Humidity + "\n";
		if (result.indexOf("\n") != -1)
			result = result.substring(0, result.lastIndexOf("\n"));
		return result;
	}

	public String getTime() {
		return Time;
	}

	public void setTime(String time) {
		Time = time;
	}

	public String getState() {
		return State;
	}

	public void setState(String state) {
		State = state;
	}

	public String getLevel() {
		return Level;
	}

	public void setLevel(String level) {
		Level = level;
	}

}
