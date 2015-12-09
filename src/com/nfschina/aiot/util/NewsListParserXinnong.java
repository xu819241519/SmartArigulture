package com.nfschina.aiot.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.nfschina.aiot.entity.NewsEntity;
import com.nfschina.aiot.entity.NewsListEntity;

public class NewsListParserXinnong extends NewsParser {

	private String Url = "http://www.xinnong.net/news/hangye/list_14_";

	private String UrlTail = ".html";

	@Override
	public String getURL(int page) {
		return Url + (page + 1) + UrlTail;
	}

	@Override
	public int getPageCount(Document document) {
		Element element = document.getElementsByClass("pageinfo").get(0);
		String info = element.text();
		Pattern pattern = Pattern.compile("[0-9]*ҳ");
		Matcher matcher = pattern.matcher(info);
		if (matcher.find()) {
			String count = matcher.group(0);
			count = count.substring(0, count.length() - 1);
			return Integer.parseInt(count);
		}
		return 1;
	}

	@Override
	public List<NewsEntity> getNewsEntities(List<Document> documents) {
		if (documents != null && documents.size() != 0) {
			List<NewsEntity> result = new ArrayList<NewsEntity>();

			Document document = documents.get(0);
			Element element = document.getElementsByClass("newslist").get(0);
			Elements elements = element.getElementsByTag("li");
			for (Element e : elements) {
				NewsListEntity newsListEntity = new NewsListEntity();
				Element ee = e.getElementsByTag("a").get(0);
				newsListEntity.setTitle(ee.text());
				newsListEntity.setURL(ee.attr("abs:href"));
				ee = e.getElementsByTag("span").get(0);
				newsListEntity.setTime(ee.text());
				result.add(newsListEntity);
			}
			return result;
		}
		return null;
	}

}
