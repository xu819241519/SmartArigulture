package com.nfschina.aiot.entity;

import java.sql.Timestamp;

/**
 * ������¼ʵ��
 * @author xu
 *
 */

public class AlarmEntity {

	//����ID
	private int ID;
	//����ID
	private int GreenHouseID;
	//��������
	private String Content;
	//����ʱ��
	private String Time;
	//����״̬
	private String State;
	//�ȼ�
	private String Level;
	
	public AlarmEntity(int iD, int greenHouseID, String content, String time, String state,
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
