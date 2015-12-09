package com.nfschina.aiot.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import com.nfschina.aiot.entity.NewsContentEntity;
import com.nfschina.aiot.entity.NewsEntity;
import com.nfschina.aiot.entity.TemperatureEntity;

import android.R.bool;

/**
 * 新闻正文分析器，提供新闻正文的页数，给出每一页对应的URL，通过解析html源码构造新闻正文实体并返回
 * 爬取中国农村网（http://www.nongcun5.com/news/）
 * 
 * @author xu
 *
 */
public class NewsContentParserVillageCom extends NewsParser {

	private String Url;

	public NewsContentParserVillageCom(String url) {
		Url = url;
	}

	@Override
	public String getURL(int page) {

		return Url;
	}

	@Override
	public int getPageCount(Document document) {
		return 1;
	}

	@Override
	public List<NewsEntity> getNewsEntities(List<Document> documents) {
		List<NewsEntity> newsContentEntities = new ArrayList<NewsEntity>();
		NewsContentEntity newsContentEntity = new NewsContentEntity();
		Document document = documents.get(0);
		if (document != null) {
			Elements elements = document.select("#title");
			if (elements != null) {
				newsContentEntity.setTitle(elements.text());

				// 匹配时间
				String info = document.select("div.info").text();
				Pattern pattern = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");
				Matcher matcher = pattern.matcher(info);
				if (matcher.find()) {
					newsContentEntity.setTime(matcher.group(0));
				}

				// 匹配来源
				int indexSource = info.indexOf("来源：") + 3;
				int indexEnd = info.indexOf("浏览次数", indexSource);
				if (indexEnd == -1 || indexSource == -1) {
					return null;
				}
				newsContentEntity.setSource(info.substring(indexSource, indexEnd));

				// 摘要
				Elements es = document.select("div.introduce");
				newsContentEntity.setSummary(es.get(0).text().substring(5));
				// content += NewsTextFormat.getContent(es.get(0).text());
				String content = "";
				es = document.select("#article");
				List<Node> nodes = es.get(0).childNodes();
				// needNewParagraph表示是否需要新的段落，0表示上一个段落结束，1表示段落还未结束。当为1并且遇到标签p时，表示在标签外的文字已经结束
				int needNewParagraph = 0;
				String tmpString = "";
				for (Node node : nodes) {
					if (node instanceof Element && ((Element) node).tagName().equals("p")) {
						if (needNewParagraph == 1) {
							content += NewsTextFormat.getContent(tmpString);

						}
						content += NewsTextFormat.getContent(node.toString());
						needNewParagraph = 0;
						tmpString = "";
					} else {
						if (node instanceof Element)
							tmpString += ((Element) node).text();
						else
							tmpString += node.toString();
						needNewParagraph = 1;
					}

				}

				if (!tmpString.equals("")) {
					content += NewsTextFormat.getContent(tmpString);
				}

				newsContentEntity.setContent(content);

				newsContentEntities.add(newsContentEntity);
			}

		}

		return newsContentEntities;
	}

	@Override
	public Connection addHeader(Connection connection) {
		return connection;
	}

	@Override
	public int getPrePageCount() {
		return 1;
	}
}
