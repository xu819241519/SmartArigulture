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
	// 报警正文
	private String Content;
	// 报警时间
	private String Time;
	// 报警状态
	private int State;
	// 等级
	private String Level;
	// 二氧化碳超出值
	private int CO2;
	// 温度超出值
	private float Temperature;
	// 湿度超出值
	private float Humidity;
	// 光照超出值
	private int Illuminance;

	public AlarmEntity(int iD, String greenHouseID, float temperature, int co2, float humidity, int illuminance,
			String time, int state) {
		ID = iD;
		GreenHouseID = greenHouseID;
		Time = time;
		State = state;
		Temperature = temperature;
		Humidity = humidity;
		illuminance = Illuminance;
		CO2 = co2;
		Level = "";
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
		if (Temperature > 0) {
			result += "温度超上限" + Temperature + "度" + "\n";
		} else if (Temperature < 0) {
			result += "温度超下限" + (-Temperature) + "度" + "\n";
		}

		if (Humidity > 0) {
			result += "湿度超上限" + Humidity + "\n";
		} else if (Humidity < 0) {
			result += "湿度超下限" + (-Humidity) + "\n";
		}

		if (CO2 > 0) {
			result += "CO2超上限" + CO2 + "\n";
		} else if (CO2 < 0) {
			result += "C02超下限" + (-CO2) + "\n";
		}

		if (Illuminance > 0) {
			result += "光照超上限" + Illuminance + "\n";
		} else if (Illuminance < 0) {
			result += "光照超下限" + (-Illuminance) + "\n";
		}

		return result;
	}

	public String getTime() {
		return Time;
	}

	public void setTime(String time) {
		Time = time;
	}

	public String getState() {
		String result = "";
		switch (State) {
		case 0:
			result = "未处理";
			break;
		case 1:
			result = "已处理";
			break;
		case 2:
			result = "已忽略";
			break;
		default:
			break;
		}
		return result;
	}

	public void setState(int state) {
		State = state;
	}

	public String getLevel() {
		return "待定";
	}

	public void setLevel(String level) {
		Level = level;
	}

}
