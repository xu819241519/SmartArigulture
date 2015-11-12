package com.nfschina.aiot.util;

import java.util.List;

import com.nfschina.aiot.entity.NewsListEntity;

public interface NewsListGetListener {
	/**
	 * 开始获取新闻列表
	 */
	public void startGetNewsList();

	/**
	 * 更新新闻列表
	 * 
	 * @param newsListEnities
	 *            新闻列表实体list
	 */
	public void updateNewsList(List<NewsListEntity> newsListEntities);
}
