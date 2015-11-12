package com.nfschina.aiot.util;

/**
 * ũҵ��������������������ȡ�������Ľ������������б������
 * 
 * @author xu
 *
 */
public class FarmerComParserFactory implements ParserFactory {

	@Override
	public NewsContentParser getNewsContentParser(String url) {

		return new NewsContentParseFarmerCom(url);
	}

	@Override
	public NewsListParser getNewsListParser() {

		return new NewsListParseFarmerCom();
	}

}
