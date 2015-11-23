package com.nfschina.aiot.util;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.os.AsyncTask;

public class PerformGetData extends AsyncTask<String, Void, Document> {

	private NewsGetUtil mGetUtil;

	private NewsParser mParser;

	public PerformGetData(NewsGetUtil util, NewsParser parser) {
		mGetUtil = util;
		mParser = parser;
	}

	@Override
	protected Document doInBackground(String... params) {
		Document document = null;
		Connection con = Jsoup.connect(params[0]);
		con = mParser.addHeader(con);
		try {
			document = con.get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return document;
	}

	@Override
	protected void onPostExecute(Document result) {
		mGetUtil.DealDocumentData(result);
	}

}
