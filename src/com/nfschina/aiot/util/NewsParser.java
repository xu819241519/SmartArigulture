package com.nfschina.aiot.util;

import org.jsoup.nodes.Document;

public interface NewsParser {
	
	/**
	 * ��ȡĳһҳ��url
	 * @param page
	 * @return
	 */
	public String getURL(int page);
	
	/**
	 * ��ȡҳ��
	 * 
	 * @param document
	 *            �ĵ�
	 * @return ҳ��
	 */
	public int getPageCount(Document document);

}
