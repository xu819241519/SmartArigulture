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

	// ������
	private NewsContentParser mParser;

	public NewsContentGetUtil(NewsContent newsContent, NewsContentParser parser) {
		mContent = newsContent;
		mDocuments = new ArrayList<Document>();
		mParser = parser;
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
	 * �����õ�document���ݣ����ǻ�ȡҳ��������һ���ͻ�ȡ�����ҳ�����ǻ�ȡ�������ҳ����һ���͸�����ʾ
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
