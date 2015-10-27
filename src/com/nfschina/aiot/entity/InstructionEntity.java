package com.nfschina.aiot.entity;

import java.sql.Timestamp;

/**
 * ָ��ʵ��
 * @author xu
 *
 */
public class InstructionEntity {

	//ָ��ID
	private int ID;
	//ָ������
	private String Content;
	//ָ���ʱ��
	private String SendTime;
	//ָ��ִ��ʱ��
	private String RunTime;
	//����ID
	private String GreenHouseID;
	//����ָ���û�
	private String UserID;
	


	public InstructionEntity(int iD, String content, String sendTime, String runTime, String greenHouseID,
			String userID) {
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
	

}
