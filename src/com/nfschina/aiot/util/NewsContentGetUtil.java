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

	// �������ĵ�fragment
	private NewsContent mContent;
	// ����ҳ��
	private int PAGE_COUNT = -1;
	// jsoup��document����Ż�ȡҳ����html�����ĵ�
	private Document mDocumentC;
	// jsoup��document�������������ҳ��html�����ĵ�
	private List<Document> mDocuments;
	// ��ʾ�Ի���
	private ProgressDialog mDialog = null;

	// ��ǰ״̬
	private int mCurrentState = STATE_GET_CONTENT;
	// ��ȡҳ��
	private static int STATE_GET_PAGE_COUNT = 1;
	// ��ȡ�б�
	private static int STATE_GET_CONTENT = 2;
	// ��ȡ���
	private static int STATE_GET_CONTENT_COMPLETE = 3;

	// Ҫ��ȡ�ڼ�ҳ
	private int mRequirePage;

	// ��ȡ�������ĵ���ַ
	private String URL;

	public NewsContentGetUtil(NewsContent newsContent, String url) {
		mContent = newsContent;
		URL = url;
		mDocuments = new ArrayList<Document>();
	}

	/**
	 * ��ȡ��������
	 */
	public void getNewsContent() {
		mRequirePage = 0;
		updateNewsContent(mRequirePage);
	}

	/**
	 * ��ȡÿһҳ����������document
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
	 * �����õ�document���ݣ����ǻ�ȡҳ��������һ���ͻ�ȡ�����ҳ�����ǻ�ȡ�������ҳ����һ���͸�����ʾ
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
	 * ��ʾ�ȴ��Ի���
	 * 
	 * @param show
	 *            ������ʾ���ǹر�
	 */
	private void showDialog(boolean show) {
		if (show) {
			if (mDialog == null) {
				mDialog = new ProgressDialog(mContent.getActivity());
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
			if (mCurrentState != STATE_GET_PAGE_COUNT)
				mDocuments.add(result);
			else 
				mDocumentC = result;
			DealDocumentData();
		}
	}

}
