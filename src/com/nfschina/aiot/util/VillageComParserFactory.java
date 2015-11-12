package com.nfschina.aiot.util;

/**
 * ũ��������������������������Ľ������������б������
 * 
 * @author xu
 *
 */
public class VillageComParserFactory implements ParserFactory {

	@Override
	public NewsContentParser getNewsContentParser(String url) {

		return new NewsContentParserVillageCom(url);
	}

	@Override
	public NewsListParser getNewsListParser() {
		return new NewsListParserVillageCom();
	}

}
