package com.nfschina.aiot.util;

public interface ParserFactory {

	/**
	 * 获得新闻正文解析器
	 * 
	 * @return 新闻正文解析器
	 */
	public NewsParser getNewsContentParser(String url);

	/**
	 * 获得新闻列表解析器
	 * 
	 * @return 新闻列表解析器
	 */
	public NewsParser getNewsListParser();
}
