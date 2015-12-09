package com.nfschina.aiot.util;

public abstract class NewsFactory {

	/**
	 * 获得新闻正文解析器
	 * 
	 * @return 新闻正文解析器
	 */
	public abstract NewsParser getNewsContentParser(String url);

	/**
	 * 获得新闻列表解析器
	 * 
	 * @return 新闻列表解析器
	 */
	public abstract NewsParser getNewsListParser();

	/**
	 * 获取工厂的ID
	 * 
	 * @return
	 */
	public abstract int getFactoryID();
}
