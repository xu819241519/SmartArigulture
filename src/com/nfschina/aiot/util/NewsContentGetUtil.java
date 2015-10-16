package com.nfschina.aiot.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

	// 获取新闻正文的网址
	private String URL;

	public NewsContentGetUtil(NewsContent newsContent, String url) {
		mContent = newsContent;
		URL = url;
		mDocuments = new ArrayList<Document>();
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
			new GetHtmlDocument().execute(URL);
		} else if (PAGE_COUNT >= 0 && mCurrentState == STATE_GET_CONTENT) {
			if (page >= PAGE_COUNT) {
				showDialog(false);
				return;
			} else {
				String url = null;
				if (page == 0)
					url = URL;
				else {
					url = URL.substring(0, URL.lastIndexOf(".htm")) + "_" + mRequirePage + ".htm";
				}
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
			PAGE_COUNT = NewsContentParseUtil.getPageCount(mDocumentC);
			mCurrentState = STATE_GET_CONTENT;
			updateNewsContent(mRequirePage);
		} else if (mCurrentState == STATE_GET_CONTENT) {
			mRequirePage++;
			if (mRequirePage >= PAGE_COUNT) {
				mCurrentState = STATE_GET_CONTENT_COMPLETE;
				mContent.displayContent(NewsContentParseUtil.getNewsContentEntity(mDocuments));
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
				document = Jsoup.connect(params[0]).get();
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
