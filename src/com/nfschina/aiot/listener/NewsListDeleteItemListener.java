package com.nfschina.aiot.listener;

import com.nfschina.aiot.entity.NewsListEntity;

/**
 * ɾ��û�����ݵ������б���Ľӿ�
 * 
 * @author xu
 *
 */
public interface NewsListDeleteItemListener {

	/**
	 * ɾ��ָ�������б���
	 * 
	 * @param newsListEntity
	 *            �����б���
	 */
	public void deleteNewsListItem(NewsListEntity newsListEntity);
}
