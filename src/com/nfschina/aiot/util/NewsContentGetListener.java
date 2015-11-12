package com.nfschina.aiot.util;

import com.nfschina.aiot.entity.NewsContentEntity;


public interface NewsContentGetListener {
	/**
	 * 开始获取数据
	 */
	public void startGetNewsContent();

	/**
	 * 获取数据完成
	 * 
	 * @param newsContentEntity
	 *            获取到新闻正文的实体
	 */
	public void updateNewsContent(NewsContentEntity newsContentEntity);
}
