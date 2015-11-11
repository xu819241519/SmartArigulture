package com.nfschina.aiot.entity;

public class NewsListEntity extends NewsEntity {

	// 新闻标题
	private String Title;
	// 时间
	private String Time;
	// 链接
	private String URL;

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

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

}
