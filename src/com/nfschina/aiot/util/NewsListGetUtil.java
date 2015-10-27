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

	// ������վURL
	private String NEWS_URL = "http://www.farmer.com.cn/xwpd/jjsn/";
	// ����ҳ��
	public int PAGE_COUNT = -1;
	// jsoup��document�����html�����ĵ�
	private Document DOCUMENT = null;
	// ��ʾ�Ի���
	private ProgressDialog mDialog = null;

	// ��ǰ״̬
	private int mCurrentState = STATE_GET_LIST;
	// ��ȡҳ��
	private static int STATE_GET_PAGE_COUNT = 1;
	// ��ȡ�б�
	private static int STATE_GET_LIST = 2;
	// ��ȡ���
	private static int STATE_GET_LIST_COMPLETE = 3;

	// Ҫ��ȡ�ڼ�ҳ
	private int mRequirePage;

	// newslist �� fragment
	private NewsList mNewsList;

	/**
	 * ��ȡ����ʵ��
	 * 
	 * @param page
	 *            �ڼ�ҳ
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
	 * ���캯��
	 * 
	 * @param activity
	 *            ���ø����activity
	 * @param parseObject
	 *            ָ����Ҫ��ȡ�����б����������ģ�ΪPARSE_LIST��ʾҪ��ȡ�����б�ΪPARSE_CONTENT����Ҫ��ȡ����
	 */
	public NewsListGetUtil(NewsList newsList) {
		mNewsList = newsList;
	}

	/**
	 * �����õ�document���ݣ����ǻ�ȡҳ��������һ���ͻ�ȡ�����ҳ�����ǻ�ȡ�������ҳ����һ���͸�����ʾ
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
	 * ��ʾ�ȴ��Ի���
	 * 
	 * @param show
	 *            ������ʾ���ǹر�
	 */
	private void showDialog(boolean show) {
		if (show) {
			if (mDialog == null) {
				mDialog = new ProgressDialog(mNewsList.getActivity());
				mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				mDialog.setMessage("���ڻ�ȡ����...");
				mDialog.show();
			}
		} else if (mDialog != null) {
			mDialog.cancel();
			mDialog = null;
		}
	}

	/**
	 * ���������ȡhtml document
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
