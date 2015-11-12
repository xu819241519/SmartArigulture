package com.nfschina.aiot.util;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.nfschina.aiot.entity.NewsListEntity;

/**
 * 新闻列表分析器 ，提供新闻列表的页数，获取每一页对应的网址（URL），通过解析html源码构造新闻列表实体并返回
 * 爬取中国农村网（http://www.nongcun5.com/news/）
 * @author xu
 *
 */
public class NewsListParserVillageCom extends NewsListParser {

	private String BaseURL = "http://www.nongcun5.com/news/4/";
	
	@Override
	public String getURL(int page) {
		if(page <= 0)
			return BaseURL;
		else if(page > 0){
			return BaseURL.substring(0, BaseURL.length()-1) + "-" + page + "/";
		}
		return null;
	}

	@Override
	public int getPageCount(Document document) {
		Elements elements = document.getElementsByTag("cite");
		Element e = elements.get(0);
		String info = e.text();
		int result = Integer.parseInt(info.substring(info.indexOf('/') + 1, info.length() - 1));
		return result;
	}

	@Override
	public List<NewsListEntity> getNewsListEntities(Document document) {
		List<NewsListEntity> result = null;
		Elements elements = document.getElementsByClass("catlist_li");
		for(Element e : elements){
			NewsListEntity newsListEntity = new NewsListEntity();
			newsListEntity.setTitle(e.select("a[href]").text());
			newsListEntity.setTime(e.select("span").text());
			newsListEntity.setURL(e.select("a[href]").attr("href"));
			if(result == null){
				result = new ArrayList<NewsListEntity>();
			}
			result.add(newsListEntity);
		}
		return result;
	}

}
