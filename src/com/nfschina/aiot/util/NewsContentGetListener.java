package com.nfschina.aiot.util;

import com.nfschina.aiot.entity.NewsContentEntity;


public interface NewsContentGetListener {
	/**
	 * ��ʼ��ȡ����
	 */
	public void startGetNewsContent();

	/**
	 * ��ȡ�������
	 * 
	 * @param newsContentEntity
	 *            ��ȡ���������ĵ�ʵ��
	 */
	public void updateNewsContent(NewsContentEntity newsContentEntity);
}
