package com.nfschina.aiot.activity;

import com.nfschina.aiot.R;
import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.fragment.NewsContent;
import com.nfschina.aiot.fragment.NewsList;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class News extends FragmentActivity implements OnClickListener {

	// the UI controls
	private TextView mBack;
	
	//relate to fragment
	private FragmentManager mFragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news);
		InitUIControls();
		setListener();
	}

	/**
	 * Initialize the UI controls
	 */
	private void InitUIControls() {
		mBack = (TextView) findViewById(R.id.news_back);
		
		mFragmentManager = getSupportFragmentManager();
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		ft.add(R.id.news_fragment,new NewsList(), Constant.NEWS_LIST);
		ft.commit();
	}

	/**
	 * set the listener of the UI controls
	 */
	private void setListener() {
		mBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.news_back)
			finish();
	}
	
	/**
	 * go to the content page of the news
	 */
	public void goNewsContent(String id){
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		ft.replace(R.id.news_fragment, new NewsContent(id), Constant.NEWS_CONTENT);
		ft.addToBackStack(null);
		ft.commit();
	}
}
