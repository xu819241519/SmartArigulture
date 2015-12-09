package com.nfschina.aiot.util;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;

import com.nfschina.aiot.listener.NewsGetListener;

public class NewsGetUtil {
	// 新闻页数
	public int PAGE_COUNT = -1;
	// jsoup的document，存放html解析文档
	private List<Document> Documents = null;

	// 当前状态
	private int mCurrentState = STATE_GET_DATA;
	// 获取页数
	private static int STATE_GET_PAGE_COUNT = 1;
	// 获取列表
	private static int STATE_GET_DATA = 2;
	// 获取完成
	private static int STATE_GET_DATA_COMPLETE = 3;

	public static int NEWS_LIST = 0;

	public static int NEWS_CONTENT = 1;

	private int mType;

	// 要获取第几页
	private int mRequirePage;

	// 监听器
	private NewsGetListener mListener;

	// 解析器
	private NewsParser mParser;

	/**
	 * 获取新闻实体
	 * 
	 * @param page
	 *            第几页
	 */
	public void updateNews(int page) {
		if (page < 0) {
			// showDialog(false);
			mListener.updateNews(null);
			return;
		}

		mRequirePage = page;

		if (PAGE_COUNT < 0 && mParser.getPrePageCount() == NewsParser.NO_PRE_COUNT) {
			mCurrentState = STATE_GET_PAGE_COUNT;
			// showDialog(true);
			mListener.startGetNews();
			new PerformGetData(this, mParser).execute(mParser.getURL(0));
			return;
		} else {
			if (PAGE_COUNT < 0 && mParser.getPrePageCount() != NewsParser.NO_PRE_COUNT) {
				PAGE_COUNT = mParser.getPrePageCount();
			}
			if (page >= PAGE_COUNT) {
				// showDialog(false);
				mListener.updateNews(null);
				return;
			} else {
				String url = mParser.getURL(page);
				mCurrentState = STATE_GET_DATA;
				// showDialog(true);
				mListener.startGetNews();
				new PerformGetData(this, mParser).execute(url);

			}
		}

	}

	/**
	 * 构造函数
	 * 
	 * @param newsList
	 *            表明使用此类的NewsList
	 */
	public NewsGetUtil(NewsGetListener listener, NewsParser parser, int type) {
		mListener = listener;
		mParser = parser;
		mType = type;
	}

	/**
	 * 处理获得的document数据，若是获取页数，则下一步就获取请求的页；若是获取的请求的页，下一步就更新显示
	 */
	public void DealDocumentData(Document document) {
		if (mCurrentState == STATE_GET_PAGE_COUNT) {
			PAGE_COUNT = mParser.getPageCount(document);

			// updateNews(mRequirePage);
		}
		// 若请求的是新闻列表
		if (mType == NewsGetUtil.NEWS_LIST) {
			mCurrentState = STATE_GET_DATA_COMPLETE;
			List<Document> docs = new ArrayList<Document>();
			docs.add(document);
			mListener.updateNews(mParser.getNewsEntities(docs));
		}
		// 若请求的是新闻正文
		else if (mType == NewsGetUtil.NEWS_CONTENT) {
			if (Documents == null) {
				Documents = new ArrayList<Document>();
			}
			Documents.add(document);
			mRequirePage++;
			if (mRequirePage >= PAGE_COUNT) {
				mCurrentState = STATE_GET_DATA_COMPLETE;
				mListener.updateNews(mParser.getNewsEntities(Documents));
			} else {
				updateNews(mRequirePage);
			}
		}

	}
}
