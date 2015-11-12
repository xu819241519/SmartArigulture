package com.nfschina.aiot.fragment;

import com.nfschina.aiot.R;
import com.nfschina.aiot.activity.News;
import com.nfschina.aiot.entity.NewsContentEntity;
import com.nfschina.aiot.entity.NewsListEntity;
import com.nfschina.aiot.util.NewsContentGetListener;
import com.nfschina.aiot.util.NewsContentGetUtil;
import com.nfschina.aiot.util.NewsContentParseFarmerCom;
import com.nfschina.aiot.util.NewsContentParserVillageCom;

import android.app.ProgressDialog;
import android.drm.ProcessedData;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 新闻正文页面
 * 
 * @author xu
 *
 */

public class NewsContent extends Fragment implements NewsContentGetListener {

	// 标题
	private TextView mTitle;
	private TextView mContent;
	private View mView;
	private ProgressDialog mDialog;

	private NewsListEntity mNewsListEntity;

	public NewsContent(NewsListEntity newsListEntity) {
		mNewsListEntity = newsListEntity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.news_content, null);
		return mView;
	}

	@Override
	public void onStart() {
		super.onStart();
		initUIControls();

		NewsContentGetUtil newsContentGetUtil = new NewsContentGetUtil(this,
				((News)getActivity()).getParserFactory().getNewsContentParser(mNewsListEntity.getURL()));
		newsContentGetUtil.startGetNewsContent();
	}

	/**
	 * 初始化UI控件
	 */
	private void initUIControls() {
		mTitle = (TextView) mView.findViewById(R.id.news_content_title);
		mContent = (TextView) mView.findViewById(R.id.news_content_content);

	}

	@Override
	public void startGetNewsContent() {

		if (mDialog == null) {
			mDialog = new ProgressDialog(getActivity());
			mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mDialog.setMessage("正在获取数据...");
			mDialog.show();
		}

	}

	@Override
	public void updateNewsContent(NewsContentEntity newsContentEntity) {
		if (mDialog != null) {
			mDialog.cancel();
			mDialog = null;
		}
		if (newsContentEntity != null) {
			mTitle.setText(newsContentEntity.getTitle());
			mContent.setText(newsContentEntity.getContext());
			mContent.setMovementMethod(ScrollingMovementMethod.getInstance());
		}
	}
}
