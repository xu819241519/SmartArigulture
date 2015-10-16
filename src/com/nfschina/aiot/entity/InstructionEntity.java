package com.nfschina.aiot.entity;

import java.sql.Timestamp;

/**
 * 指令实体
 * @author xu
 *
 */
public class InstructionEntity {

	//指令ID
	private int ID;
	//指令正文
	private String Content;
	//指令发送时间
	private Timestamp SendTime;
	//指令执行时间
	private Timestamp RunTime;
	//温室ID
	private int GreenHouseID;
	//发送指令用户
	private int UserID;
	


	public InstructionEntity(int iD, String content, Timestamp sendTime, Timestamp runTime, int greenHouseID,
			int userID) {
		super();
		ID = iD;
		Content = content;
		SendTime = sendTime;
		RunTime = runTime;
		GreenHouseID = greenHouseID;
		UserID = userID;
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

	public Timestamp getSendTime() {
		return SendTime;
	}

	public void setSendTime(Timestamp sendTime) {
		SendTime = sendTime;
	}

	public Timestamp getRunTime() {
		return RunTime;
	}

	public void setRunTime(Timestamp runTime) {
		RunTime = runTime;
	}

	public int getGreenHouseID() {
		return GreenHouseID;
	}

	public void setGreenHouseID(int greenHouseID) {
		GreenHouseID = greenHouseID;
	}

	public int getUserID() {
		return UserID;
	}

	public void setUserID(int userID) {
		UserID = userID;
	}
	

}
