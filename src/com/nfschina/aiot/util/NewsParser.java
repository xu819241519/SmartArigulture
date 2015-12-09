package com.nfschina.aiot.util;

import java.util.List;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;

import com.nfschina.aiot.entity.NewsEntity;

public abstract class NewsParser {

	public static int NO_PRE_COUNT = -1;

	/**
	 * ��ȡĳһҳ��url
	 * 
	 * @param page
	 * @return
	 */
	public abstract String getURL(int page);

	/**
	 * Ԥ�ȶ���ҳ�������ٶ������
	 * 
	 * @return ������Ԥ��֪��ҳ��������NO_PRE_COUNT�����򷵻�Ԥ�ȵ�ҳ��
	 */
	public int getPrePageCount() {
		return NO_PRE_COUNT;
	};

	/**
	 * ��ȡҳ��
	 * 
	 * @param document
	 *            �ĵ�
	 * @return ҳ��
	 */
	public abstract int getPageCount(Document document);

	/**
	 * ��ȡ����ʵ��
	 * 
	 * @param document
	 *            �ĵ�
	 * @return ����ʵ��list
	 */
	public abstract List<NewsEntity> getNewsEntities(List<Document> documents);

	/**
	 * ���http get ͷ��Ϣ������Ҫ���һЩͷ����Ϣʱ����д�ú���
	 * 
	 * @param connection
	 * @return
	 */
	public Connection addHeader(Connection connection) {
		return connection;
	}

}
