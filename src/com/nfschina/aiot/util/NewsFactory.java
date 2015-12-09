package com.nfschina.aiot.util;

public abstract class NewsFactory {

	/**
	 * ����������Ľ�����
	 * 
	 * @return �������Ľ�����
	 */
	public abstract NewsParser getNewsContentParser(String url);

	/**
	 * ��������б������
	 * 
	 * @return �����б������
	 */
	public abstract NewsParser getNewsListParser();

	/**
	 * ��ȡ������ID
	 * 
	 * @return
	 */
	public abstract int getFactoryID();
}
