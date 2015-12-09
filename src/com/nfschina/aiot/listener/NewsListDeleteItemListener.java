package com.nfschina.aiot.listener;

import com.nfschina.aiot.entity.NewsListEntity;

/**
 * 删除没有内容的新闻列表项的接口
 * 
 * @author xu
 *
 */
public interface NewsListDeleteItemListener {

	/**
	 * 删除指定新闻列表项
	 * 
	 * @param newsListEntity
	 *            新闻列表项
	 */
	public void deleteNewsListItem(NewsListEntity newsListEntity);
}
