package com.nfschina.aiot.entity;
/**
 * ·´À¡±¨ÎÄ 
 * @author wujian
 *
 */
public class FeedbackMsg {
	
	private byte[] headMark;
	private byte[] type;
	private byte[] fromMark;
	private byte[] content;
	private byte[] checkNum;
	
	
	public FeedbackMsg(){}
	public FeedbackMsg(byte[] headMark, byte[] type, byte[] fromMark,
			byte[] content, byte[] checkNum) {
		this.headMark = headMark;
		this.type = type;
		this.fromMark = fromMark;
		this.content = content;
		this.checkNum = checkNum;
	}
	public byte[] getHeadMark() {
		return headMark;
	}
	public void setHeadMark(byte[] headMark) {
		this.headMark = headMark;
	}
	public byte[] getType() {
		return type;
	}
	public void setType(byte[] type) {
		this.type = type;
	}
	public byte[] getFromMark() {
		return fromMark;
	}
	public void setFromMark(byte[] fromMark) {
		this.fromMark = fromMark;
	}
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
	public byte[] getCheckNum() {
		return checkNum;
	}
	public void setCheckNum(byte[] checkNum) {
		this.checkNum = checkNum;
	}
	
	

}
