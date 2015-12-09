package com.nfschina.aiot.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.Html;

public class NewsTextFormat {

	/**
	 * 获取带格式的正文字符串
	 * 
	 * @param content
	 * @return
	 */
	public static CharSequence getContent(String content) {
		String result = "";
		Pattern pattern = Pattern.compile("([\u4E00-\u9FA5]+|[0-9]+|[a-zA-Z]+)");
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			result += "<p style='padding-bottom: 0px; text-transform: none; text-indent: 0px; margin: 15px 0px; padding-left: 0px; padding-right: 0px; font: 16px/2em 'Microsoft YaHei', u5FAEu8F6Fu96C5u9ED1, Arial, SimSun, u5B8Bu4F53; white-space: normal; letter-spacing: normal; color: rgb(102,102,102); word-spacing: 0px; padding-top: 0px; -webkit-text-stroke-width: 0px'>";
			result += content;
			result += "</p>";
			return Html.fromHtml(result);
		}
		return result;
	}

	public static CharSequence getSummary(String summary) {
		String result = "<strong>内容摘要：</strong>";
		result += summary;

		return Html.fromHtml(result);
	}

}
