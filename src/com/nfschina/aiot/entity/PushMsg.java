package com.nfschina.aiot.entity;
/**
 * ��Ϣ���ͱ���
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
	
	private String pumpState;//ˮ��
	private String rollingState;//������
	private String co2producterState;//co2������
	private String hotwindState;//�ȷ��
	private String airState;//ͨ���
	
	private ThresholdMsg threshold;//��ֵ�������Сֵ�ļ���
	
	private String extendPart;
	private String collectTime;
	private String whetherAlarm;//������ʾ��1����0
	private String whereAlarm;//������ʾλ����ʾ�Ǹ��ط�����
	private String greenhouseId;
	private String checkNum;
	/**
	 * �������ͱ��ĵ����ݣ���������֪ͨ�û��ı�����Ϣ
	 * @param pushMsg ������Ϣ
	 * @return ��ʶ��ı�����Ϣ
	 */
	public PushContent getPushMsgContent(PushMsg pushMsg){
		PushContent content = new PushContent();
		content.setGreenHouseid(pushMsg.getGreenhouseId());
		content.setWarningtime(pushMsg.getCollectTime());
		//�����¶�
			if (Float.parseFloat(pushMsg.getThreshold().getTemperatureMin()) > Float.parseFloat(pushMsg.getTemperatureNum())) {
				content.setTemperature("�¶ȵ�����С��ֵ"+(Float.parseFloat(pushMsg.getThreshold().getTemperatureMin()) - Float.parseFloat(pushMsg.getTemperatureNum()))+"��;");
			}
			if (Float.parseFloat(pushMsg.getTemperatureNum()) > Float.parseFloat(pushMsg.getThreshold().getTemperatureMin())) {
				content.setTemperature("�¶ȸ������ֵ"+(Float.parseFloat(pushMsg.getTemperatureNum()) - Float.parseFloat(pushMsg.getThreshold().getTemperatureMin()))+"��;");
			}
		//����co2
			if (Integer.parseInt(pushMsg.threshold.getCo2Min()) > Integer.parseInt(pushMsg.getCo2Num())) {
				content.setCo2("CO2��������С��ֵ"+(Integer.parseInt(pushMsg.threshold.getCo2Min()) - Integer.parseInt(pushMsg.getCo2Num()))+"ppm;");
			}
			if (Integer.parseInt(pushMsg.getCo2Num()) > Integer.parseInt(pushMsg.getThreshold().getCo2Max())) {
				content.setCo2("CO2���������ֵ"+(Integer.parseInt(pushMsg.getCo2Num()) - Integer.parseInt(pushMsg.getThreshold().getCo2Max()))+"ppm;");
			}
		//����ʪ��
			if (Float.parseFloat(pushMsg.threshold.getWaterMin()) > Float.parseFloat(pushMsg.getWaterNum())) {
				content.setHumidity("ʪ�ȵ�����С��ֵ"+(Float.parseFloat(pushMsg.threshold.getWaterMin()) - Float.parseFloat(pushMsg.getWaterNum()))+"%;");
			}
			if(Float.parseFloat(pushMsg.getWaterNum()) > Float.parseFloat(pushMsg.threshold.getWaterMax())){
				content.setHumidity("ʪ�ȸ������ֵ"+(Float.parseFloat(pushMsg.getWaterNum()) - Float.parseFloat(pushMsg.threshold.getWaterMax()))+"%;");
			}
		//�������
			if (Integer.parseInt(pushMsg.threshold.getLightMin()) > Integer.parseInt(pushMsg.getLightNum())) {
				content.setIlluminance("���յ�����С��ֵ"+(Integer.parseInt(pushMsg.threshold.getLightMin()) - Integer.parseInt(pushMsg.getLightNum()))+"xl;");
			}
			if (Integer.parseInt(pushMsg.getLightNum()) > Integer.parseInt(pushMsg.threshold.getLightMax())) {
				content.setIlluminance("���ո������ֵ"+(Integer.parseInt(pushMsg.getLightNum()) - Integer.parseInt(pushMsg.threshold.getLightMax()))+"xl;");
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
