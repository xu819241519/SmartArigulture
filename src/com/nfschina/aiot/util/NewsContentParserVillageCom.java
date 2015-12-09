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
 * �������ķ��������ṩ�������ĵ�ҳ��������ÿһҳ��Ӧ��URL��ͨ������htmlԴ�빹����������ʵ�岢����
 * ��ȡ�й�ũ������http://www.nongcun5.com/news/��
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

				// ƥ��ʱ��
				String info = document.select("div.info").text();
				Pattern pattern = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");
				Matcher matcher = pattern.matcher(info);
				if (matcher.find()) {
					newsContentEntity.setTime(matcher.group(0));
				}

				// ƥ����Դ
				int indexSource = info.indexOf("��Դ��") + 3;
				int indexEnd = info.indexOf("�������", indexSource);
				if (indexEnd == -1 || indexSource == -1) {
					return null;
				}
				newsContentEntity.setSource(info.substring(indexSource, indexEnd));

				// ժҪ
				Elements es = document.select("div.introduce");
				newsContentEntity.setSummary(es.get(0).text().substring(5));
				// content += NewsTextFormat.getContent(es.get(0).text());
				String content = "";
				es = document.select("#article");
				List<Node> nodes = es.get(0).childNodes();
				// needNewParagraph��ʾ�Ƿ���Ҫ�µĶ��䣬0��ʾ��һ�����������1��ʾ���仹δ��������Ϊ1����������ǩpʱ����ʾ�ڱ�ǩ��������Ѿ�����
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
