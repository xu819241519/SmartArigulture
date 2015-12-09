package com.nfschina.aiot.entity;

/**
 * 指令实体
 * 
 * @author xu
 *
 */
public class InstructionEntity {

	// 指令ID
	private int ID;
	// 指令正文
	private String Content;
	// 指令发送时间
	private String SendTime;
	// 指令执行时间
	private String RunTime;
	// 温室ID
	private String GreenHouseID;
	// 发送指令用户
	private String UserID;
	// 运行周期
	private String RunPeriod;

	public InstructionEntity(int iD, String content, String sendTime, String runTime, String greenHouseID,
			String userID, String runperiod) {
		super();
		ID = iD;
		Content = content;
		SendTime = sendTime;
		RunTime = runTime;
		GreenHouseID = greenHouseID;
		UserID = userID;
		RunPeriod = runperiod;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getContent() {

		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getSendTime() {
		return SendTime;
	}

	public void setSendTime(String sendTime) {
		SendTime = sendTime;
	}

	public String getRunTime() {
		return RunTime;
	}

	public void setRunTime(String runTime) {
		RunTime = runTime;
	}

	public String getGreenHouseID() {
		return GreenHouseID;
	}

	public void setGreenHouseID(String greenHouseID) {
		GreenHouseID = greenHouseID;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getRunPeriod() {
		return RunPeriod;
	}

	public void setRunPeriod(String runPeriod) {
		RunPeriod = runPeriod;
	}

}
