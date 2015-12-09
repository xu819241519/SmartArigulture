package com.nfschina.aiot.entity;


public class Task {
	private int id;
	private String content;
	private String greenhouseId;
	private String userId;
	private String sendTime;
	private String executeTime;
	private String exeperiod;
	
	public Task(){}
	public Task(int id,String content, String greenhouseId, String userId,
			String sendTime, String executeTime, String exeperiod) {
		this.id = id;
		this.content = content;
		this.greenhouseId = greenhouseId;
		this.userId = userId;
		this.sendTime = sendTime;
		this.executeTime = executeTime;
		this.exeperiod = exeperiod;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public String getExecuteTime() {
		return executeTime;
	}
	public void setExecuteTime(String executeTime) {
		this.executeTime = executeTime;
	}
	public String getExeperiod() {
		return exeperiod;
	}
	public void setExeperiod(String exeperiod) {
		this.exeperiod = exeperiod;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
}
