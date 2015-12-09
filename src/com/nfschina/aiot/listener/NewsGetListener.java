package com.nfschina.aiot.listener;

import java.util.List;

import com.nfschina.aiot.entity.NewsEntity;

public interface NewsGetListener {
	/**
	 * 开始获取新闻
	 */
	public void startGetNews();

	/**
	 * 更新新闻
	 * 
	 * @param newsEnities
	 *            新闻实体list
	 */
	public void updateNews(List<NewsEntity> newsEntities);
}
