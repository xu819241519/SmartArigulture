package com.nfschina.aiot.activity;

import com.nfschina.aiot.R;
import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.entity.NewsContentEntity;
import com.nfschina.aiot.entity.NewsListEntity;
import com.nfschina.aiot.fragment.NewsContent;
import com.nfschina.aiot.fragment.NewsList;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * 新闻页面
 * 显示新闻列表和新闻详情。新闻详情使用fragment显示
 * @author xu
 *
 */

public class News extends FragmentActivity implements OnClickListener {

	// 返回按钮
	private ImageButton mBack;
	//主页按钮
	private ImageButton mHome;
	
	private FragmentManager mFragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news);
		
		InitUIControls();
		setListener();
	}

	/**
	 * 初始化UI控件
	 */
	private void InitUIControls() {
		mBack = (ImageButton) findViewById(R.id.news_back);
		mHome = (ImageButton) findViewById(R.id.news_gohome);
		
		mFragmentManager = getSupportFragmentManager();
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		ft.add(R.id.news_fragment,new NewsList(), Constant.NEWS_LIST);
		ft.commit();
	}

	/**
	 * 设置UI控件的监听
	 */
	private void setListener() {
		mBack.setOnClickListener(this);
		mHome.setOnClickListener(this);
	}

	/**
	 * 按钮点击事件
	 */
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.news_back)
			finish();
		else if(v.getId() == R.id.news_gohome){
			finish();
		}
	}
	
	/**
	 * 进入新闻详细页面
	 */
	public void goNewsContent(NewsListEntity newsListEntity){
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		ft.replace(R.id.news_fragment, new NewsContent(newsListEntity), Constant.NEWS_CONTENT);
		ft.addToBackStack(null);
		ft.commit();
	}
}
