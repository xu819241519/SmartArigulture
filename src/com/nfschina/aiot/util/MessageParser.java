package com.nfschina.aiot.util;

import com.nfschina.aiot.entity.PushMsg;
import com.nfschina.aiot.entity.ThresholdMsg;

/**
 * 后台推送的报文解析类
 * @author wujian
 */
public class MessageParser {
	private PushMsg pushMsg;

	public PushMsg parser(String message){
		// 解析报文
		pushMsg = new PushMsg();
		// 实时数据
		pushMsg.setTemperatureNum(message.substring(6, 10));
		pushMsg.setCo2Num(message.substring(10, 14));
		pushMsg.setWaterNum(message.substring(14, 16));
		pushMsg.setLightNum(message.substring(16, 21));
		// 设备状态
		pushMsg.setPumpState(message.substring(21, 22));
		pushMsg.setRollingState(message.substring(22, 23));
		pushMsg.setCo2producterState(message.substring(23, 24));
		pushMsg.setHotwindState(message.substring(24, 25));
		pushMsg.setAirState(message.substring(25, 26));
		// 当前阀值
		pushMsg.setThreshold(new ThresholdMsg(message.substring(26, 30),
				message.substring(34, 38), message.substring(42, 44), 
				message.substring(46, 51), message.substring(30, 34), 
				message.substring(38, 42), message.substring(44, 46),
				message.substring(51, 56)));
		// 扩展
		pushMsg.setExtendPart(message.substring(56, 64));
		// 采集时间
		pushMsg.setCollectTime(message.substring(64, 78));
		// 报警标示
		pushMsg.setAirState(message.substring(78, 79));
		// 温室编号
		pushMsg.setGreenhouseId(message.substring(79, 83));
		return pushMsg;
	}
}
