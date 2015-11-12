package com.nfschina.aiot.util;

import java.util.List;

import com.nfschina.aiot.entity.NewsListEntity;

public interface NewsListGetListener {
	/**
	 * ��ʼ��ȡ�����б�
	 */
	public void startGetNewsList();

	/**
	 * ���������б�
	 * 
	 * @param newsListEnities
	 *            �����б�ʵ��list
	 */
	public void updateNewsList(List<NewsListEntity> newsListEntities);
}
