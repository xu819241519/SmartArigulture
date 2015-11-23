package com.nfschina.aiot.util;

/**
 * ũ��������������������������Ľ������������б������
 * 
 * @author xu
 *
 */
public class VillageComParserFactory implements ParserFactory {

	@Override
	public NewsParser getNewsContentParser(String url) {

		return new NewsContentParserVillageCom(url);
	}

	@Override
	public NewsParser getNewsListParser() {
		return new NewsListParserVillageCom();
	}

}
