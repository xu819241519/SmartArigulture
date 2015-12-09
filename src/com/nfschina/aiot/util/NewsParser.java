package com.nfschina.aiot.util;

import java.util.List;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;

import com.nfschina.aiot.entity.NewsEntity;

public abstract class NewsParser {

	public static int NO_PRE_COUNT = -1;

	/**
	 * 获取某一页的url
	 * 
	 * @param page
	 * @return
	 */
	public abstract String getURL(int page);

	/**
	 * 预先定义页数，减少多次请求。
	 * 
	 * @return 当不能预先知道页数，返回NO_PRE_COUNT，否则返回预先的页数
	 */
	public int getPrePageCount() {
		return NO_PRE_COUNT;
	};

	/**
	 * 获取页数
	 * 
	 * @param document
	 *            文档
	 * @return 页数
	 */
	public abstract int getPageCount(Document document);

	/**
	 * 获取新闻实体
	 * 
	 * @param document
	 *            文档
	 * @return 新闻实体list
	 */
	public abstract List<NewsEntity> getNewsEntities(List<Document> documents);

	/**
	 * 添加http get 头信息，当需要添加一些头部信息时，重写该函数
	 * 
	 * @param connection
	 * @return
	 */
	public Connection addHeader(Connection connection) {
		return connection;
	}

}
