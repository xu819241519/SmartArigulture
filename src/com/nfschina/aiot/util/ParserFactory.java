package com.nfschina.aiot.util;

public interface ParserFactory {

	/**
	 * ����������Ľ�����
	 * 
	 * @return �������Ľ�����
	 */
	public NewsParser getNewsContentParser(String url);

	/**
	 * ��������б������
	 * 
	 * @return �����б������
	 */
	public NewsParser getNewsListParser();
}
