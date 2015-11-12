package com.nfschina.aiot.util;

public interface ParserFactory {

	/**
	 * ����������Ľ�����
	 * 
	 * @return �������Ľ�����
	 */
	public NewsContentParser getNewsContentParser(String url);

	/**
	 * ��������б������
	 * 
	 * @return �����б������
	 */
	public NewsListParser getNewsListParser();
}
