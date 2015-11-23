package com.nfschina.aiot.fragment;

import java.util.List;

import com.nfschina.aiot.R;
import com.nfschina.aiot.activity.News;
import com.nfschina.aiot.entity.NewsContentEntity;
import com.nfschina.aiot.entity.NewsEntity;
import com.nfschina.aiot.entity.NewsListEntity;
import com.nfschina.aiot.util.NewsContentParserVillageCom;
import com.nfschina.aiot.util.NewsGetListener;
import com.nfschina.aiot.util.NewsGetUtil;

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

public class NewsContent extends Fragment implements NewsGetListener {

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

		NewsGetUtil newsGetUtil = new NewsGetUtil(this,
				((News) getActivity()).getParserFactory().getNewsContentParser(mNewsListEntity.getURL()),NewsGetUtil.NEWS_CONTENT);
		newsGetUtil.updateNews(0);
	}

	/**
	 * 初始化UI控件
	 */
	private void initUIControls() {
		mTitle = (TextView) mView.findViewById(R.id.news_content_title);
		mContent = (TextView) mView.findViewById(R.id.news_content_content);

	}

	@Override
	public void startGetNews() {

		if (mDialog == null) {
			mDialog = new ProgressDialog(getActivity());
			mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mDialog.setMessage("正在获取数据...");
			mDialog.show();
		}

	}

	@Override
	public void updateNews(List<NewsEntity> newsEntities) {
		if (mDialog != null) {
			mDialog.cancel();
			mDialog = null;
		}
		if (newsEntities != null && newsEntities.size() > 0) {
			NewsContentEntity newsContentEntity = (NewsContentEntity) newsEntities.get(0);
			if (newsContentEntity != null) {
				mTitle.setText(newsContentEntity.getTitle());
				mContent.setText(newsContentEntity.getContext());
				mContent.setMovementMethod(ScrollingMovementMethod.getInstance());
			}
		}
	}
}
