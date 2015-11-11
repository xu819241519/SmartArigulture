package com.nfschina.aiot.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.nfschina.aiot.fragment.NewsContent;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class NewsContentGetUtil {

	// 新闻正文的fragment
	private NewsContent mContent;
	// 新闻页数
	private int PAGE_COUNT = -1;
	// jsoup的document，存放获取页数的html解析文档
	private Document mDocumentC;
	// jsoup的document，存放所有新闻页的html解析文档
	private List<Document> mDocuments;
	// 提示对话框
	private ProgressDialog mDialog = null;

	// 当前状态
	private int mCurrentState = STATE_GET_CONTENT;
	// 获取页数
	private static int STATE_GET_PAGE_COUNT = 1;
	// 获取列表
	private static int STATE_GET_CONTENT = 2;
	// 获取完成
	private static int STATE_GET_CONTENT_COMPLETE = 3;

	// 要获取第几页
	private int mRequirePage;

	// 解析器
	private NewsContentParser mParser;

	public NewsContentGetUtil(NewsContent newsContent, NewsContentParser parser) {
		mContent = newsContent;
		mDocuments = new ArrayList<Document>();
		mParser = parser;
	}

	/**
	 * 获取新闻正文
	 */
	public void getNewsContent() {
		mRequirePage = 0;
		updateNewsContent(mRequirePage);
	}

	/**
	 * 获取每一页的新闻正文document
	 * 
	 * @param page
	 */
	private void updateNewsContent(int page) {
		if (page < 0)
			return;
		mRequirePage = page;
		if (PAGE_COUNT < 0 && mCurrentState != STATE_GET_CONTENT_COMPLETE) {
			mCurrentState = STATE_GET_PAGE_COUNT;
			showDialog(true);
			new GetHtmlDocument().execute(mParser.getURL(0));
		} else if (PAGE_COUNT >= 0 && mCurrentState == STATE_GET_CONTENT) {
			if (page >= PAGE_COUNT) {
				showDialog(false);
				return;
			} else {
				String url = mParser.getURL(mRequirePage);

				mCurrentState = STATE_GET_CONTENT;
				showDialog(true);
				new GetHtmlDocument().execute(url);
			}

		}
	}

	/**
	 * 处理获得的document数据，若是获取页数，则下一步就获取请求的页；若是获取的请求的页，下一步就更新显示
	 */
	private void DealDocumentData() {
		if (mCurrentState == STATE_GET_PAGE_COUNT) {
			PAGE_COUNT = mParser.getPageCount(mDocumentC);
			mCurrentState = STATE_GET_CONTENT;
			updateNewsContent(mRequirePage);
		} else if (mCurrentState == STATE_GET_CONTENT) {
			mRequirePage++;
			if (mRequirePage >= PAGE_COUNT) {
				mCurrentState = STATE_GET_CONTENT_COMPLETE;
				mContent.displayContent(mParser.getNewsContentEntity(mDocuments));
				showDialog(false);
			} else {
				updateNewsContent(mRequirePage);
			}

		}
	}

	/**
	 * 显示等待对话框
	 * 
	 * @param show
	 *            表明显示还是关闭
	 */
	private void showDialog(boolean show) {
		if (show) {
			if (mDialog == null) {
				mDialog = new ProgressDialog(mContent.getActivity());
				mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				mDialog.setMessage("正在获取数据...");
				mDialog.show();
			}
		} else if (mDialog != null) {
			mDialog.cancel();
			mDialog = null;
		}
	}

	/**
	 * 连接网络获取html document
	 * 
	 * @author xu
	 *
	 */
	private class GetHtmlDocument extends AsyncTask<String, Void, Document> {

		@Override
		protected Document doInBackground(String... params) {
			Document document = null;
			try {
				Connection connection = Jsoup.connect(params[0]);
				connection.header("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36");
				connection.header("Cookie",
						"Hm_lvt_82cae308c81856487b36410ef6593067=1441763722,1441769414; vid=C6C18D504D200001649C3CC07B506700; __FTabceffgh=2015-10-15-9-35-18; __NRUabceffgh=1444872918942; __RECabceffgh=1; __RTabceffgh=2015-10-16-10-6-16; wafenterurl=/; wafverify=71f8ef0da3409a5c8c1dba10ae820dfe; PFT_COOKIE_RF=-; PFT_c7635a737118e31da04549e708b25757=C6D580C724000001E1C247C3C3501603; RF_mapc7635a737118e31da04549e708b25757=0*-; judge=C6D580C724000001E1C247C3C3501603; PFT_SJKD=10; wafcookie=371fc03d095dc6042008918620942cee; __utma=107656469.1801252722.1447124499.1447124499.1447124499.1; __utmc=107656469; __utmz=107656469.1447124499.1.1.utmcsr=baidu|utmccn=(organic)|utmcmd=organic");
				document = connection.get();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return document;
		}

		@Override
		protected void onPostExecute(Document result) {
			super.onPostExecute(result);
			if (mCurrentState != STATE_GET_PAGE_COUNT)
				mDocuments.add(result);
			else
				mDocumentC = result;
			DealDocumentData();
		}
	}

}
