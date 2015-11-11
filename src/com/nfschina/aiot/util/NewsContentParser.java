package com.nfschina.aiot.util;

import java.util.List;

import org.jsoup.nodes.Document;

import com.nfschina.aiot.entity.NewsContentEntity;

public abstract class NewsContentParser implements NewsParser{

	/**
	 * ��ȡ��������ʵ��
	 * @param document �ĵ�
	 * @return ��������ʵ��
	 */
	public abstract NewsContentEntity getNewsContentEntity(List<Document> documents); 
	
}
