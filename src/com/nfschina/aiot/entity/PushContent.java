package com.nfschina.aiot.entity;
/**
 * �����ͱ������ս��������ʵ����
 * @author wujian
 */
public class PushContent {

	private String greenHouseid;//����ĸ����ҽ��б���
	private String temperature;
	private String co2;
	private String humidity;
	private String illuminance;
	private String warningtime;
	
	@Override
	public String toString() {
		return temperature+co2+humidity+illuminance+warningtime;
	}
	public String getGreenHouseid() {
		return greenHouseid;
	}
	public void setGreenHouseid(String greenHouseid) {
		this.greenHouseid = greenHouseid;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getCo2() {
		return co2;
	}
	public void setCo2(String co2) {
		this.co2 = co2;
	}
	public String getHumidity() {
		return humidity;
	}
	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}
	public String getIlluminance() {
		return illuminance;
	}
	public void setIlluminance(String illuminance) {
		this.illuminance = illuminance;
	}
	public String getWarningtime() {
		return warningtime;
	}
	public void setWarningtime(String warningtime) {
		this.warningtime = warningtime;
	}
}
