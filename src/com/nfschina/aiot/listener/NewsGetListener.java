package com.nfschina.aiot.listener;

import java.util.List;

import com.nfschina.aiot.entity.NewsEntity;

public interface NewsGetListener {
	/**
	 * ��ʼ��ȡ����
	 */
	public void startGetNews();

	/**
	 * ��������
	 * 
	 * @param newsEnities
	 *            ����ʵ��list
	 */
	public void updateNews(List<NewsEntity> newsEntities);
}
