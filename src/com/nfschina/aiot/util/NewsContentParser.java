package com.nfschina.aiot.util;

import java.util.List;

import org.jsoup.nodes.Document;

import com.nfschina.aiot.entity.NewsContentEntity;

public abstract class NewsContentParser implements NewsParser{

	/**
	 * 获取新闻内容实体
	 * @param document 文档
	 * @return 新闻内容实体
	 */
	public abstract NewsContentEntity getNewsContentEntity(List<Document> documents); 
	
}
