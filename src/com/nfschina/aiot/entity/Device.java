package com.nfschina.aiot.entity;

public class Device {
	private String id;
	private String deviceName;
	private int state;
	public Device(String id, String deviceName, int state) {
		this.id = id;
		this.deviceName = deviceName;
		this.state = state;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
	

}
