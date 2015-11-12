package com.nfschina.aiot.util;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import com.nfschina.aiot.fragment.NewsList;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class NewsListGetUtil {

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

	// ������
	private NewsListGetListener mListener;

	// ������
	private NewsListParser mParser;

	/**
	 * ��ȡ����ʵ��
	 * 
	 * @param page
	 *            �ڼ�ҳ
	 */
	public void updateNewsList(int page) {
		if (page < 0) {
			//showDialog(false);
			mListener.updateNewsList(null);
			return;
		}

		mRequirePage = page;
		if (PAGE_COUNT < 0) {
			mCurrentState = STATE_GET_PAGE_COUNT;
			//showDialog(true);
			mListener.startGetNewsList();
			new GetHtmlDocument().execute(mParser.getURL(0));
			return;
		} else {
			if (page >= PAGE_COUNT) {
				//showDialog(false);
				mListener.updateNewsList(null);
				return;
			} else {
				String url = mParser.getURL(page);
				mCurrentState = STATE_GET_LIST;
				//showDialog(true);
				mListener.startGetNewsList();
				new NewsListGetUtil.GetHtmlDocument().execute(url);

			}
		}

	}

	/**
	 * ���캯��
	 * 
	 * @param newsList
	 *            ����ʹ�ô����NewsList
	 */
	public NewsListGetUtil(NewsListGetListener listener, NewsListParser parser) {
		mListener = listener;
		mParser = parser;
	}

	/**
	 * �����õ�document���ݣ����ǻ�ȡҳ��������һ���ͻ�ȡ�����ҳ�����ǻ�ȡ�������ҳ����һ���͸�����ʾ
	 */
	private void DealDocumentData() {
		if (mCurrentState == STATE_GET_PAGE_COUNT) {
			PAGE_COUNT = mParser.getPageCount(DOCUMENT);
			updateNewsList(mRequirePage);
		} else if (mCurrentState == STATE_GET_LIST) {
			mCurrentState = STATE_GET_LIST_COMPLETE;
			mListener.updateNewsList(mParser.getNewsListEntities(DOCUMENT));
			//showDialog(false);
			
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
				// HttpGet httpGet = new HttpGet(params[0]);
				// httpGet.addHeader("User-Agent",
				// "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36
				// (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36");
				// httpGet.addHeader("Cookie",
				// "Hm_lvt_82cae308c81856487b36410ef6593067=1441763722,1441769414;
				// vid=C6C18D504D200001649C3CC07B506700;
				// __FTabceffgh=2015-10-15-9-35-18; __NRUabceffgh=1444872918942;
				// __RECabceffgh=1; __RTabceffgh=2015-10-16-10-6-16;
				// wafenterurl=/; wafverify=71f8ef0da3409a5c8c1dba10ae820dfe;
				// PFT_COOKIE_RF=-;
				// PFT_c7635a737118e31da04549e708b25757=C6D580C724000001E1C247C3C3501603;
				// RF_mapc7635a737118e31da04549e708b25757=0*-;
				// judge=C6D580C724000001E1C247C3C3501603; PFT_SJKD=10;
				// wafcookie=371fc03d095dc6042008918620942cee;
				// __utma=107656469.1801252722.1447124499.1447124499.1447124499.1;
				// __utmc=107656469;
				// __utmz=107656469.1447124499.1.1.utmcsr=baidu|utmccn=(organic)|utmcmd=organic");
				// HttpResponse response = new
				// DefaultHttpClient().execute(httpGet);
				// HttpEntity httpEntity = response.getEntity();
				// document =
				// Jsoup.parse(EntityUtils.toString(httpEntity,"gb2312"));
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
			DOCUMENT = result;
			DealDocumentData();
		}

	}
}
