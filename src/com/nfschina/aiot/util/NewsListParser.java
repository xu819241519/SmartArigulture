package com.nfschina.aiot.util;

import java.util.List;

import org.jsoup.nodes.Document;

import com.nfschina.aiot.entity.NewsListEntity;

public abstract class NewsListParser implements NewsParser {
	

	/**
	 * 获取新闻列表实体
	 * @param document 文档
	 * @return 新闻实体列表
	 */
	public abstract List<NewsListEntity> getNewsListEntities(Document document);

}
