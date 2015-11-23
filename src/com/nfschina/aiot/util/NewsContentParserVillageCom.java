package com.nfschina.aiot.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.nfschina.aiot.entity.NewsContentEntity;
import com.nfschina.aiot.entity.NewsEntity;
import com.nfschina.aiot.fragment.NewsContent;

import android.text.Html;

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
		newsContentEntity.setTitle(document.select("#title").text());

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
		newsContentEntity.setSource(info.substring(indexSource, indexEnd));

		String content = "";
		// ժҪ
		Elements elements = document.select("div.introduce");
		content += "<p style='padding-bottom: 0px; text-transform: none; text-indent: 0px; margin: 15px 0px; padding-left: 0px; padding-right: 0px; font: 16px/2em 'Microsoft YaHei', u5FAEu8F6Fu96C5u9ED1, Arial, SimSun, u5B8Bu4F53; white-space: normal; letter-spacing: normal; color: rgb(0,0,0); word-spacing: 0px; padding-top: 0px; -webkit-text-stroke-width: 0px'>"
				+ elements.get(0).text() + "</p>";

		elements = document.select("#article");
		for (Element e : elements.get(0).children()) {
			content += "<p style='padding-bottom: 0px; text-transform: none; text-indent: 0px; margin: 15px 0px; padding-left: 0px; padding-right: 0px; font: 16px/2em 'Microsoft YaHei', u5FAEu8F6Fu96C5u9ED1, Arial, SimSun, u5B8Bu4F53; white-space: normal; letter-spacing: normal; color: rgb(0,0,0); word-spacing: 0px; padding-top: 0px; -webkit-text-stroke-width: 0px'>"
					+ e.text() + "</p>";
		}

		newsContentEntity.setContent(Html.fromHtml(content));

		newsContentEntities.add(newsContentEntity);

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
