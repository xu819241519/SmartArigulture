package com.nfschina.aiot.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.nfschina.aiot.entity.NewsContentEntity;
import com.nfschina.aiot.entity.NewsListEntity;
import com.nfschina.aiot.fragment.NewsList;

import android.R.anim;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.pdf.PdfDocument.Page;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.widget.Toast;

public class NewsListGetUtil {

	// 新闻网站URL
	private String NEWS_URL = "http://www.farmer.com.cn/xwpd/jjsn/";
	// 新闻页数
	public int PAGE_COUNT = -1;
	// jsoup的document，存放html解析文档
	private Document DOCUMENT = null;
	// 提示对话框
	private ProgressDialog mDialog = null;

	// 当前状态
	private int mCurrentState = STATE_GET_LIST;
	// 获取页数
	private static int STATE_GET_PAGE_COUNT = 1;
	// 获取列表
	private static int STATE_GET_LIST = 2;
	// 获取完成
	private static int STATE_GET_LIST_COMPLETE = 3;

	// 要获取第几页
	private int mRequirePage;

	// newslist 的 fragment
	private NewsList mNewsList;

	/**
	 * 获取新闻实体
	 * 
	 * @param page
	 *            第几页
	 */
	public void updateNewsList(int page) {
		if (page < 0) {
			showDialog(false);
			return;
		}

		mRequirePage = page;
		if (PAGE_COUNT < 0) {
			mCurrentState = STATE_GET_PAGE_COUNT;
			showDialog(true);
			new GetHtmlDocument().execute(NEWS_URL + "index.htm");
			return;
		} else {
			if (page >= PAGE_COUNT) {
				showDialog(false);
				return;
			} else {
				String url = NEWS_URL;
				if (page == 1)
					url += "index.htm";
				else if(page != 0){
					url += "index_" + mRequirePage + ".htm";
				}
				mCurrentState = STATE_GET_LIST;
				showDialog(true);
				new NewsListGetUtil.GetHtmlDocument().execute(url);

			}
		}

	}

	/**
	 * 构造函数
	 * 
	 * @param activity
	 *            调用该类的activity
	 * @param parseObject
	 *            指明需要获取新闻列表还是新闻正文，为PARSE_LIST表示要获取新闻列表，为PARSE_CONTENT表明要获取正文
	 */
	public NewsListGetUtil(NewsList newsList) {
		mNewsList = newsList;
	}

	/**
	 * 处理获得的document数据，若是获取页数，则下一步就获取请求的页；若是获取的请求的页，下一步就更新显示
	 */
	private void DealDocumentData() {
		if (mCurrentState == STATE_GET_PAGE_COUNT) {
			PAGE_COUNT = NewsListParseUtil.getPageCount(DOCUMENT);
			updateNewsList(mRequirePage);
		} else if (mCurrentState == STATE_GET_LIST) {
			mCurrentState = STATE_GET_LIST_COMPLETE;
			mNewsList.updateAdapter(NewsListParseUtil.getNewsListEntity(DOCUMENT));
			showDialog(false);
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
				mDialog = new ProgressDialog(mNewsList.getActivity());
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
			DOCUMENT = result;
			DealDocumentData();
		}

	}
}
