package com.nfschina.aiot.util;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;

import com.nfschina.aiot.listener.NewsGetListener;

public class NewsGetUtil {
	// ����ҳ��
	public int PAGE_COUNT = -1;
	// jsoup��document�����html�����ĵ�
	private List<Document> Documents = null;

	// ��ǰ״̬
	private int mCurrentState = STATE_GET_DATA;
	// ��ȡҳ��
	private static int STATE_GET_PAGE_COUNT = 1;
	// ��ȡ�б�
	private static int STATE_GET_DATA = 2;
	// ��ȡ���
	private static int STATE_GET_DATA_COMPLETE = 3;

	public static int NEWS_LIST = 0;

	public static int NEWS_CONTENT = 1;

	private int mType;

	// Ҫ��ȡ�ڼ�ҳ
	private int mRequirePage;

	// ������
	private NewsGetListener mListener;

	// ������
	private NewsParser mParser;

	/**
	 * ��ȡ����ʵ��
	 * 
	 * @param page
	 *            �ڼ�ҳ
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
	 * ���캯��
	 * 
	 * @param newsList
	 *            ����ʹ�ô����NewsList
	 */
	public NewsGetUtil(NewsGetListener listener, NewsParser parser, int type) {
		mListener = listener;
		mParser = parser;
		mType = type;
	}

	/**
	 * �����õ�document���ݣ����ǻ�ȡҳ��������һ���ͻ�ȡ�����ҳ�����ǻ�ȡ�������ҳ����һ���͸�����ʾ
	 */
	public void DealDocumentData(Document document) {
		if (mCurrentState == STATE_GET_PAGE_COUNT) {
			PAGE_COUNT = mParser.getPageCount(document);

			// updateNews(mRequirePage);
		}
		// ��������������б�
		if (mType == NewsGetUtil.NEWS_LIST) {
			mCurrentState = STATE_GET_DATA_COMPLETE;
			List<Document> docs = new ArrayList<Document>();
			docs.add(document);
			mListener.updateNews(mParser.getNewsEntities(docs));
		}
		// �����������������
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
