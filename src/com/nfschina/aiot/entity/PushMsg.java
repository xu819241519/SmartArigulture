package com.nfschina.aiot.entity;
/**
 * 消息推送报文
 * @author wujian
 *
 */
public class PushMsg {

	private String headMark;
	private String type;
	private String fromMark;
	
	private String temperatureNum;
	private String co2Num;
	private String waterNum;
	private String lightNum;
	
	private String pumpState;//水泵
	private String rollingState;//卷帘机
	private String co2producterState;//co2产生器
	private String hotwindState;//热风机
	private String airState;//通风机
	
	private ThresholdMsg threshold;//阀值，最大最小值的集合
	
	private String extendPart;
	private String collectTime;
	private String whetherAlarm;//报警标示，1或者0
	private String whereAlarm;//报警标示位，表示那个地方报警
	private String greenhouseId;
	private String checkNum;
	/**
	 * 解析推送报文的内容，生成用来通知用户的报警信息
	 * @param pushMsg 推送信息
	 * @return 可识别的报警信息
	 */
	public PushContent getPushMsgContent(PushMsg pushMsg){
		PushContent content = new PushContent();
		content.setGreenHouseid(pushMsg.getGreenhouseId());
		content.setWarningtime(pushMsg.getCollectTime());
		//计算温度
			if (Float.parseFloat(pushMsg.getThreshold().getTemperatureMin()) > Float.parseFloat(pushMsg.getTemperatureNum())) {
				content.setTemperature("温度低于最小阀值"+(Float.parseFloat(pushMsg.getThreshold().getTemperatureMin()) - Float.parseFloat(pushMsg.getTemperatureNum()))+"℃;");
			}
			if (Float.parseFloat(pushMsg.getTemperatureNum()) > Float.parseFloat(pushMsg.getThreshold().getTemperatureMin())) {
				content.setTemperature("温度高于最大阀值"+(Float.parseFloat(pushMsg.getTemperatureNum()) - Float.parseFloat(pushMsg.getThreshold().getTemperatureMin()))+"℃;");
			}
		//计算co2
			if (Integer.parseInt(pushMsg.threshold.getCo2Min()) > Integer.parseInt(pushMsg.getCo2Num())) {
				content.setCo2("CO2量低于最小阀值"+(Integer.parseInt(pushMsg.threshold.getCo2Min()) - Integer.parseInt(pushMsg.getCo2Num()))+"ppm;");
			}
			if (Integer.parseInt(pushMsg.getCo2Num()) > Integer.parseInt(pushMsg.getThreshold().getCo2Max())) {
				content.setCo2("CO2量高于最大阀值"+(Integer.parseInt(pushMsg.getCo2Num()) - Integer.parseInt(pushMsg.getThreshold().getCo2Max()))+"ppm;");
			}
		//计算湿度
			if (Float.parseFloat(pushMsg.threshold.getWaterMin()) > Float.parseFloat(pushMsg.getWaterNum())) {
				content.setHumidity("湿度低于最小阀值"+(Float.parseFloat(pushMsg.threshold.getWaterMin()) - Float.parseFloat(pushMsg.getWaterNum()))+"%;");
			}
			if(Float.parseFloat(pushMsg.getWaterNum()) > Float.parseFloat(pushMsg.threshold.getWaterMax())){
				content.setHumidity("湿度高于最大阀值"+(Float.parseFloat(pushMsg.getWaterNum()) - Float.parseFloat(pushMsg.threshold.getWaterMax()))+"%;");
			}
		//计算光照
			if (Integer.parseInt(pushMsg.threshold.getLightMin()) > Integer.parseInt(pushMsg.getLightNum())) {
				content.setIlluminance("光照低于最小阀值"+(Integer.parseInt(pushMsg.threshold.getLightMin()) - Integer.parseInt(pushMsg.getLightNum()))+"xl;");
			}
			if (Integer.parseInt(pushMsg.getLightNum()) > Integer.parseInt(pushMsg.threshold.getLightMax())) {
				content.setIlluminance("光照高于最大阀值"+(Integer.parseInt(pushMsg.getLightNum()) - Integer.parseInt(pushMsg.threshold.getLightMax()))+"xl;");
			}
		return content;
	}
	
	public PushMsg(){}

	public PushMsg(String headMark, String type, String fromMark,
			String temperatureNum, String co2Num, String waterNum,
			String lightNum, String pumpState, String rollingState,
			String co2producterState, String hotwindState, String airState,
			ThresholdMsg threshold, String extendPart, String collectTime,
			String whetherAlarm, String whereAlarm, String greenhouseId,
			String checkNum) {
		this.headMark = headMark;
		this.type = type;
		this.fromMark = fromMark;
		this.temperatureNum = temperatureNum;
		this.co2Num = co2Num;
		this.waterNum = waterNum;
		this.lightNum = lightNum;
		this.pumpState = pumpState;
		this.rollingState = rollingState;
		this.co2producterState = co2producterState;
		this.hotwindState = hotwindState;
		this.airState = airState;
		this.threshold = threshold;
		this.extendPart = extendPart;
		this.collectTime = collectTime;
		this.whetherAlarm = whetherAlarm;
		this.whereAlarm = whereAlarm;
		this.greenhouseId = greenhouseId;
		this.checkNum = checkNum;
	}
	public PushMsg(String temperatureNum, String co2Num, String waterNum,
			String lightNum, String pumpState, String rollingState,
			String co2producterState, String hotwindState, String airState,
			ThresholdMsg threshold, String extendPart, String collectTime,
			String whetherAlarm, String whereAlarm, String greenhouseId
			) {
		this.temperatureNum = temperatureNum;
		this.co2Num = co2Num;
		this.waterNum = waterNum;
		this.lightNum = lightNum;
		this.pumpState = pumpState;
		this.rollingState = rollingState;
		this.co2producterState = co2producterState;
		this.hotwindState = hotwindState;
		this.airState = airState;
		this.threshold = threshold;
		this.extendPart = extendPart;
		this.collectTime = collectTime;
		this.whetherAlarm = whetherAlarm;
		this.whereAlarm = whereAlarm;
		this.greenhouseId = greenhouseId;
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

	public String getTemperatureNum() {
		return temperatureNum;
	}

	public void setTemperatureNum(String temperatureNum) {
		this.temperatureNum = temperatureNum;
	}

	public String getCo2Num() {
		return co2Num;
	}

	public void setCo2Num(String co2Num) {
		this.co2Num = co2Num;
	}

	public String getWaterNum() {
		return waterNum;
	}

	public void setWaterNum(String waterNum) {
		this.waterNum = waterNum;
	}

	public String getLightNum() {
		return lightNum;
	}

	public void setLightNum(String lightNum) {
		this.lightNum = lightNum;
	}

	public String getPumpState() {
		return pumpState;
	}

	public void setPumpState(String pumpState) {
		this.pumpState = pumpState;
	}

	public String getRollingState() {
		return rollingState;
	}

	public void setRollingState(String rollingState) {
		this.rollingState = rollingState;
	}

	public String getCo2producterState() {
		return co2producterState;
	}

	public void setCo2producterState(String co2producterState) {
		this.co2producterState = co2producterState;
	}

	public String getHotwindState() {
		return hotwindState;
	}

	public void setHotwindState(String hotwindState) {
		this.hotwindState = hotwindState;
	}

	public String getAirState() {
		return airState;
	}

	public void setAirState(String airState) {
		this.airState = airState;
	}

	public ThresholdMsg getThreshold() {
		return threshold;
	}

	public void setThreshold(ThresholdMsg threshold) {
		this.threshold = threshold;
	}

	public String getExtendPart() {
		return extendPart;
	}

	public void setExtendPart(String extendPart) {
		this.extendPart = extendPart;
	}

	public String getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(String collectTime) {
		this.collectTime = collectTime;
	}

	public String getWhetherAlarm() {
		return whetherAlarm;
	}

	public void setWhetherAlarm(String whetherAlarm) {
		this.whetherAlarm = whetherAlarm;
	}

	public String getWhereAlarm() {
		return whereAlarm;
	}

	public void setWhereAlarm(String whereAlarm) {
		this.whereAlarm = whereAlarm;
	}

	public String getGreenhouseId() {
		return greenhouseId;
	}

	public void setGreenhouseId(String greenhouseId) {
		this.greenhouseId = greenhouseId;
	}

	public String getCheckNum() {
		return checkNum;
	}

	public void setCheckNum(String checkNum) {
		this.checkNum = checkNum;
	}

	
}
