package com.nfschina.aiot.entity;

import com.nfschina.aiot.util.NewsTextFormat;

public class NewsContentEntity extends NewsEntity {

	// ���ű���
	private String Title;
	// ʱ��
	private String Time;
	// ��Դ
	private String Source;
	// ����
	private CharSequence Content;
	// ����ժҪ,����ժҪ�ĸ��ֲ���Ҫ�ֶ���ӣ������Զ����
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
