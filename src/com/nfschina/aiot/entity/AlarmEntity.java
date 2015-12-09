package com.nfschina.aiot.entity;

/**
 * ������¼ʵ��
 * 
 * @author xu
 *
 */

public class AlarmEntity {

	// ����ID
	private int ID;
	// ����ID
	private String GreenHouseID;
	// ����ʱ��
	private String Time;
	// ����״̬
	private String State;
	// �ȼ�
	private String Level;
	// ������̼����ֵ
	private String CO2;
	// �¶ȳ���ֵ
	private String Temperature;
	// ʪ�ȳ���ֵ
	private String Humidity;
	// ���ճ���ֵ
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
