package com.nfschina.aiot.util;

import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.nfschina.aiot.entity.NewsContentEntity;

import android.text.Html;

/**
 * �������ķ��������ṩ�������ĵ�ҳ��������ÿһҳ��Ӧ��URL��ͨ������htmlԴ�빹����������ʵ�岢����
 * ��ȡ�й�ũҵ������վ��http://www.farmer.com.cn/��
 * 
 * @author xu ͨ��jsoup����ȡָ��������վ�е���������
 */

public class NewsContentParseFarmerCom extends NewsContentParser{
	
	// ������ַ
	private String URL;
	
	public NewsContentParseFarmerCom(String url) {
		URL = url;
	}

	/**
	 * ��ȡ����һ���ж���ҳ
	 * 
	 * @param document
	 *            ͨ��������ָ����ַ��document����
	 * @return ���ŵ�ҳ��
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
	 * ��ȡ�������ģ�����һ����������ʵ��
	 * 
	 * @param documents
	 *            ��������ҳ���document
	 * @return ��������ʵ��
	 */
	@Override
	public NewsContentEntity getNewsContentEntity(List<Document> documents) {
		NewsContentEntity result = null;
		if (documents != null && documents.size() != 0) {
			// ��ȡ���ű���
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
