package com.nfschina.aiot.util;

import java.util.List;

import org.jsoup.nodes.Document;

import com.nfschina.aiot.entity.NewsListEntity;

public abstract class NewsListParser implements NewsParser {
	

	/**
	 * ��ȡ�����б�ʵ��
	 * @param document �ĵ�
	 * @return ����ʵ���б�
	 */
	public abstract List<NewsListEntity> getNewsListEntities(Document document);

}
