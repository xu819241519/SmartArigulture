package com.nfschina.aiot.util;

public class XinnongNewsFactory extends NewsFactory {

	@Override
	public NewsParser getNewsContentParser(String url) {

		return new NewsContentParserXinnong(url);
	}

	@Override
	public NewsParser getNewsListParser() {

		return new NewsListParserXinnong();
	}

	@Override
	public int getFactoryID() {
		return 2;
	}

}
