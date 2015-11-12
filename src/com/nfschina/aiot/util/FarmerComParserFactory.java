package com.nfschina.aiot.util;

/**
 * 农业新闻网解析器工厂，获取新闻正文解析器和新闻列表解析器
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
