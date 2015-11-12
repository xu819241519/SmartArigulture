package com.nfschina.aiot.util;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.nfschina.aiot.entity.NewsListEntity;

/**
 * �����б������ ���ṩ�����б��ҳ������ȡÿһҳ��Ӧ����ַ��URL����ͨ������htmlԴ�빹�������б�ʵ�岢����
 * ��ȡ�й�ũҵ������վ��http://www.farmer.com.cn/��
 * 
 * @author xu ͨ��jsoup����ȡָ����վ�е������б�
 */

public class NewsListParseFarmerCom extends NewsListParser {

	// ����url
	private String BaseURL = "http://www.farmer.com.cn/xwpd/jjsn/";

	/**
	 * ��ȡ�����б��ҳ��
	 * 
	 * @param document
	 *            ͨ�������õ�ָ��������վ��document����
	 * @return �����б��ҳ��
	 */
	@Override
	public int getPageCount(Document document) {
		int result = 0;
		if (document != null) {
			Elements elements = document.getElementsByClass("pages");
			if (elements != null && elements.size() != 0) {
				Element element = elements.get(0);
				String text = element.data();
				text = text.substring(text.lastIndexOf("createPageHTML"));
				text = text.substring("createPageHTML(".length(), text.indexOf(","));
				result = Integer.parseInt(text);
			}
		}
		return result;
	}

	/**
	 * ��ȡ�����б����������б��ʵ��
	 * 
	 * @param document
	 *            �����б�������վ��document����
	 * @return �����б��ʵ��
	 */
	@Override
	public List<NewsListEntity> getNewsListEntities(Document document) {
		List<NewsListEntity> result = null;
		if (document != null) {
			Elements elements = document.getElementsByClass("list-list-li");
			for (Element element : elements) {
				Element titleElement = element.getElementsByClass("yui3-u").get(0);
				Element timeElement = element.getElementsByClass("yui3-u").get(1);
				String title = titleElement.text();
				String time = timeElement.text();
				String url = titleElement.getElementsByClass("vvqqq").get(0).absUrl("href");
				if (result == null) {
					result = new ArrayList<NewsListEntity>();
				}
				NewsListEntity newsListEntity = new NewsListEntity();
				newsListEntity.setTitle(title);
				newsListEntity.setTime(time);
				newsListEntity.setURL(url);
				result.add(newsListEntity);
			}

		}
		return result;
	}

	@Override
	public String getURL(int page) {
		if (page <= 0) {
			return BaseURL + "index.htm";
		} else if (page > 0) {
			return BaseURL + "index_" + page + ".htm";
		}
		return null;
	}

}
