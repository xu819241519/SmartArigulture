package com.nfschina.aiot.fragment;

import com.nfschina.aiot.R;
import com.nfschina.aiot.entity.NewsContentEntity;
import com.nfschina.aiot.entity.NewsListEntity;
import com.nfschina.aiot.util.NewsContentGetUtil;

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
 * @author xu
 *
 */

public class NewsContent extends Fragment {

	
	
	// 标题
	private TextView mTitle;
	private TextView mContent;
	private View mView;
	
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
		
		NewsContentGetUtil newsContentGetUtil = new NewsContentGetUtil(this, mNewsListEntity.getURL());
		newsContentGetUtil.getNewsContent();
	}

	/**
	 * 初始化UI控件
	 */
	private void initUIControls() {
		mTitle = (TextView) mView.findViewById(R.id.news_content_title);
		mContent = (TextView) mView.findViewById(R.id.news_content_content);
		
	}
	
	public void displayContent(NewsContentEntity newsContentEntity){
		mTitle.setText(newsContentEntity.getTitle());
		mContent.setText(newsContentEntity.getContext());
		mContent.setMovementMethod(ScrollingMovementMethod.getInstance());
	}
}
