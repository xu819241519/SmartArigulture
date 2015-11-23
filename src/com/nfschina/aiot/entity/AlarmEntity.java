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
	// ��������
	private String Content;
	// ����ʱ��
	private String Time;
	// ����״̬
	private int State;
	// �ȼ�
	private String Level;
	// ������̼����ֵ
	private int CO2;
	// �¶ȳ���ֵ
	private float Temperature;
	// ʪ�ȳ���ֵ
	private float Humidity;
	// ���ճ���ֵ
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
			result += "�¶ȳ�����" + Temperature + "��" + "\n";
		} else if (Temperature < 0) {
			result += "�¶ȳ�����" + (-Temperature) + "��" + "\n";
		}

		if (Humidity > 0) {
			result += "ʪ�ȳ�����" + Humidity + "\n";
		} else if (Humidity < 0) {
			result += "ʪ�ȳ�����" + (-Humidity) + "\n";
		}

		if (CO2 > 0) {
			result += "CO2������" + CO2 + "\n";
		} else if (CO2 < 0) {
			result += "C02������" + (-CO2) + "\n";
		}

		if (Illuminance > 0) {
			result += "���ճ�����" + Illuminance + "\n";
		} else if (Illuminance < 0) {
			result += "���ճ�����" + (-Illuminance) + "\n";
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
			result = "δ����";
			break;
		case 1:
			result = "�Ѵ���";
			break;
		case 2:
			result = "�Ѻ���";
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
		return "����";
	}

	public void setLevel(String level) {
		Level = level;
	}

}
