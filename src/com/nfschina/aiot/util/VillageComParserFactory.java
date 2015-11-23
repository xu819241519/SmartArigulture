package com.nfschina.aiot.util;

/**
 * 农村网解析器工厂，获得新闻正文解析器和新闻列表解析器
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
