package com.nfschina.aiot.util;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.nfschina.aiot.entity.NewsListEntity;

/**
 * 新闻列表提取规则类
 * @author xu
 * 通过jsoup，获取指定网站中的新闻列表
 */

public class NewsListParseUtil {

	/**
	 * 获取新闻列表的页数
	 * @param document 通过网络获得的指定新闻网站的document对象
	 * @return 新闻列表的页数
	 */
	public static int getPageCount(Document document) {
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
	 * 提取新闻列表，返回新闻列表的实体
	 * @param document 新闻列表所在网站的document对象
	 * @return 新闻列表的实体
	 */
	public static List<NewsListEntity> getNewsListEntity(Document document) {
		List<NewsListEntity> result = null;
		if(document != null){
			Elements elements = document.getElementsByClass("list-list-li");
			for(Element element : elements){
				Element titleElement = element.getElementsByClass("yui3-u").get(0);
				Element timeElement = element.getElementsByClass("yui3-u").get(1);
				String title = titleElement.text();
				String time = timeElement.text();
				String url = titleElement.getElementsByClass("vvqqq").get(0).absUrl("href");
				if(result == null){
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

}
