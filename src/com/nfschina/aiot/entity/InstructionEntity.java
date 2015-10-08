package com.nfschina.aiot.entity;

import java.sql.Timestamp;

public class InstructionEntity {

	//the Alarm table attributes
	private int ID;
	private String Content;
	private Timestamp SendTime;
	private Timestamp RunTime;
	private int GreenHouseID;
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
