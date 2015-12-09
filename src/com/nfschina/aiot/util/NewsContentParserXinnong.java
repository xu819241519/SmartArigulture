package com.nfschina.aiot.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.nfschina.aiot.entity.NewsContentEntity;
import com.nfschina.aiot.entity.NewsEntity;

public class NewsContentParserXinnong extends NewsParser {

	private String Url;

	public NewsContentParserXinnong(String url) {
		Url = url;
	}

	@Override
	public String getURL(int page) {
		if (page == 0) {
			return Url;
		} else {
			String result = Url.substring(0, Url.lastIndexOf('.'));
			result += "_" + (page + 1);
			result += Url.substring(Url.lastIndexOf('.'));
			return result;
		}
	}

	@Override
	public int getPageCount(Document document) {
		Element e = document.getElementsByClass("arcbreak").get(0);
		Elements elements = e.getElementsByTag("li");
		if (elements == null || elements.size() == 0) {
			return 1;
		} else {
			String info = elements.get(0).text();
			Pattern pattern = Pattern.compile("[0-9]+");
			Matcher matcher = pattern.matcher(info);
			if (matcher.find()) {
				info = matcher.group(0);
				if (info != null && !info.equals(""))
					return Integer.parseInt(info);
				else
					return 1;
			} else
				return 1;

		}
	}

	@Override
	public List<NewsEntity> getNewsEntities(List<Document> documents) {
		List<NewsEntity> result = null;
		if (documents != null && documents.size() > 0) {
			result = new ArrayList<NewsEntity>();
			NewsContentEntity newsContentEntity = new NewsContentEntity();
			Elements elements = documents.get(0).getElementsByClass("arctit");
			if (elements != null && elements.size() > 0) {
				Element element = elements.get(0);
				newsContentEntity.setSummary(element.getElementsByClass("arcdes").get(0).text().substring(3));
				newsContentEntity.setTitle(element.getElementsByTag("h1").get(0).text());
				String info = element.getElementsByClass("arcinfo").get(0).text();
				newsContentEntity.setSource(info.substring(info.indexOf("À´Ô´£º") + 3));
				Pattern pattern = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");
				Matcher matcher = pattern.matcher(info);
				if (matcher.find()) {
					newsContentEntity.setTime(matcher.group(0));
				}
			}
			String content = "";
			for (int i = 0; i < documents.size(); ++i) {
				Document document = documents.get(i);
				Element element = document.getElementById("article");
				Elements eles = element.children();
				for (Element e : eles) {
					content += NewsTextFormat.getContent(e.text());
				}
			}
			newsContentEntity.setContent(content);
			result.add(newsContentEntity);
			return result;
		}

		return null;
	}

}
