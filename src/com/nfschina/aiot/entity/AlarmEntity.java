package com.nfschina.aiot.entity;

import java.sql.Timestamp;

public class AlarmEntity {

	//the Instructions table attributes
	private int ID;
	private int GreenHouseID;
	private String Content;
	private Timestamp Time;
	private String State;
	private String Level;
	
	public AlarmEntity(int iD, int greenHouseID, String content, Timestamp time, String state,
			String level) {
		super();
		ID = iD;
		GreenHouseID = greenHouseID;
		Content = content;
		Time = time;
		State = state;
		Level = level;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getGreenHouseID() {
		return GreenHouseID;
	}

	public void setGreenHouseID(int greenHouseID) {
		GreenHouseID = greenHouseID;
	}


	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public Timestamp getTime() {
		return Time;
	}

	public void setTime(Timestamp time) {
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
