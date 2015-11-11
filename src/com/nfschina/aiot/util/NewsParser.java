package com.nfschina.aiot.util;

import org.jsoup.nodes.Document;

public interface NewsParser {
	
	/**
	 * 获取某一页的url
	 * @param page
	 * @return
	 */
	public String getURL(int page);
	
	/**
	 * 获取页数
	 * 
	 * @param document
	 *            文档
	 * @return 页数
	 */
	public int getPageCount(Document document);

}
