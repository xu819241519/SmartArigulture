package com.nfschina.aiot.entity;
/**
 * Ö¸Áî±¨ÎÄ
 * @author wujian
 *
 */
public class InstructionMsg {
	
	private String headMark;
	private String type;
	private String fromMark;
	private String content;
	private String greenhouseId;
	private String userId;
	private String sendTime;
	private String executeTime;
	private String exeperiod;
	private String checkNum;
	
	@Override
	public String toString() {
		
		return this.headMark+this.type+this.fromMark+this.content+this.greenhouseId
				+this.userId+this.sendTime+this.executeTime+this.exeperiod+this.checkNum;
	}
	public String getMessageEntity(){
		
		return this.headMark+this.type+this.fromMark+this.content+this.greenhouseId
				+this.userId+this.sendTime+this.executeTime+this.exeperiod;
		
	}
	
	public InstructionMsg(){}
	
	public InstructionMsg(String headMark, String type, String fromMark,
			String content, String greenhouseId, String userId,
			String sendTime, String executeTime, String exeperiod,
			String checkNum) {
		this.headMark = headMark;
		this.type = type;
		this.fromMark = fromMark;
		this.content = content;
		this.greenhouseId = greenhouseId;
		this.userId = userId;
		this.sendTime = sendTime;
		this.executeTime = executeTime;
		this.exeperiod = exeperiod;
		this.checkNum = checkNum;
	}

	public String getHeadMark() {
		return headMark;
	}

	public void setHeadMark(String headMark) {
		this.headMark = headMark;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFromMark() {
		return fromMark;
	}

	public void setFromMark(String fromMark) {
		this.fromMark = fromMark;
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

	public String getCheckNum() {
		return checkNum;
	}

	public void setCheckNum(String checkNum) {
		this.checkNum = checkNum;
	}
	
	

}
