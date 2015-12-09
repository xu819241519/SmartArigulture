package com.nfschina.aiot.entity;
/**
 * 实时请求阀值的报文结构
 * @author wujian
 *
 */
public class RTthresholdMsg {
	
	private byte[] headMark;
	private byte[] type;
	private byte[] fromMark;
	private byte[] requestContent;
	private byte[] extendPart;
	private byte[] checkNum;
	
	public RTthresholdMsg(){}
	public RTthresholdMsg(byte[] headMark, byte[] type, byte[] fromMark,
			byte[] requestContent, byte[] extendPart, byte[] checkNum) {
		this.headMark = headMark;
		this.type = type;
		this.fromMark = fromMark;
		this.requestContent = requestContent;
		this.extendPart = extendPart;
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
	public byte[] getRequestContent() {
		return requestContent;
	}
	public void setRequestContent(byte[] requestContent) {
		this.requestContent = requestContent;
	}
	public byte[] getExtendPart() {
		return extendPart;
	}
	public void setExtendPart(byte[] extendPart) {
		this.extendPart = extendPart;
	}
	public byte[] getCheckNum() {
		return checkNum;
	}
	public void setCheckNum(byte[] checkNum) {
		this.checkNum = checkNum;
	}
	
	
	
	

}
