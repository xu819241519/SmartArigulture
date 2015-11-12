package com.nfschina.aiot.util;

import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.nfschina.aiot.entity.NewsContentEntity;

import android.text.Html;

/**
 * 新闻正文分析器，提供新闻正文的页数，给出每一页对应的URL，通过解析html源码构造新闻正文实体并返回
 * 爬取中国农业新闻网站（http://www.farmer.com.cn/）
 * 
 * @author xu 通过jsoup，提取指定新闻网站中的新闻正文
 */

public class NewsContentParseFarmerCom extends NewsContentParser{
	
	// 新闻网址
	private String URL;
	
	public NewsContentParseFarmerCom(String url) {
		URL = url;
	}

	/**
	 * 获取新闻一共有多少页
	 * 
	 * @param document
	 *            通过网络获得指定网址的document对象
	 * @return 新闻的页数
	 */
	@Override
	public int getPageCount(Document document) {
		int result = 1;
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
	 * 提取新闻正文，返回一个新闻正文实体
	 * 
	 * @param documents
	 *            新闻所有页面的document
	 * @return 新闻正文实体
	 */
	@Override
	public NewsContentEntity getNewsContentEntity(List<Document> documents) {
		NewsContentEntity result = null;
		if (documents != null && documents.size() != 0) {
			// 获取新闻标题
			String title = documents.get(0).getElementsByClass("wtitle").get(0).text();
			String other = documents.get(0).getElementsByClass("wlaiyuan").get(0).text();
			String[] others = other.split("\\|");
			String time = null, source = null;
			if (others.length == 3) {
				time = others[0];
				source = others[2];
				source = source.substring(3);
			}
			String content = new String();
			for (Document document : documents) {
				Element element = document.getElementsByClass("content").get(0);
				Elements elements = element.getElementsByTag("p");
				for (Element e : elements) {
					content += "<p style='padding-bottom: 0px; text-transform: none; text-indent: 0px; margin: 15px 0px; padding-left: 0px; padding-right: 0px; font: 16px/2em 'Microsoft YaHei', u5FAEu8F6Fu96C5u9ED1, Arial, SimSun, u5B8Bu4F53; white-space: normal; letter-spacing: normal; color: rgb(0,0,0); word-spacing: 0px; padding-top: 0px; -webkit-text-stroke-width: 0px'>"
							+ e.text() + "</p>";
				}
			}
			if (result == null) {
				result = new NewsContentEntity();
			}
			result.setTitle(title);
			result.setTime(time);
			result.setContent(Html.fromHtml(content));
			result.setSource(source);
		}
		return result;
	}

	@Override
	public String getURL(int page) {
		String result = null;
		if (page == 0)
			result = URL;
		else {
			result = URL.substring(0, URL.lastIndexOf(".htm")) + "_" + page + ".htm";
		}
		return result;
	}
}
