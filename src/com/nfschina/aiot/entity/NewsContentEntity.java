package com.nfschina.aiot.entity;

import com.nfschina.aiot.util.NewsTextFormat;

public class NewsContentEntity extends NewsEntity {

	// 新闻标题
	private String Title;
	// 时间
	private String Time;
	// 来源
	private String Source;
	// 正文
	private CharSequence Content;
	// 内容摘要,内容摘要四个字不需要手动添加，程序自动添加
	private String Summary;

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getTime() {
		return Time;
	}

	public void setTime(String time) {
		Time = time;
	}

	public String getSource() {
		return Source;
	}

	public void setSource(String source) {
		Source = source;
	}

	public CharSequence getContext() {
		return Content;
	}

	public void setContent(CharSequence content) {
		Content = content;
	}

	public CharSequence getSummary() {
		if (Summary != null && !Summary.equals("")) {

			return NewsTextFormat.getSummary(Summary);
		}
		return null;
	}

	public void setSummary(String summary) {
		Summary = summary;
	}

}
