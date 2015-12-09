package com.nfschina.aiot.util;

/**
 * ũ��������������������������Ľ������������б������
 * 
 * @author xu
 *
 */
public class VillageComNewsFactory extends NewsFactory {

	@Override
	public NewsParser getNewsContentParser(String url) {

		return new NewsContentParserVillageCom(url);
	}

	@Override
	public NewsParser getNewsListParser() {
		return new NewsListParserVillageCom();
	}

	@Override
	public int getFactoryID() {
		return 1;
	}

}
