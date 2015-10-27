package com.nfschina.aiot.activity;

import com.nfschina.aiot.R;
import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.entity.NewsContentEntity;
import com.nfschina.aiot.entity.NewsListEntity;
import com.nfschina.aiot.fragment.NewsContent;
import com.nfschina.aiot.fragment.NewsList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * ����ҳ��
 * ��ʾ�����б���������顣��������ʹ��fragment��ʾ
 * @author xu
 *
 */

public class News extends FragmentActivity implements OnClickListener {

	// ���ذ�ť
	private ImageButton mBack;
	//��ҳ��ť
	private ImageButton mHome;
	
	private FragmentManager mFragmentManager;
	//�����б�
	private NewsList mNewsList;
	//��������
	private NewsContent mNewsContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news);
		
		InitUIControls();
		setListener();
	}

	/**
	 * ��ʼ��UI�ؼ�
	 */
	private void InitUIControls() {
		mBack = (ImageButton) findViewById(R.id.news_back);
		mHome = (ImageButton) findViewById(R.id.news_gohome);
		
		mFragmentManager = getSupportFragmentManager();
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		mNewsList = new NewsList();
		ft.add(R.id.news_fragment,mNewsList, Constant.NEWS_LIST);
		ft.commit();
	}

	/**
	 * ����UI�ؼ��ļ���
	 */
	private void setListener() {
		mBack.setOnClickListener(this);
		mHome.setOnClickListener(this);
	}

	/**
	 * ��ť����¼�
	 */
	@Override
	public void onClick(View v) {
		
		if (v.getId() == R.id.news_back){
			onBackPressed();
		}
			
		else if(v.getId() == R.id.news_gohome){
			Intent intent = new Intent(this,Home.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
	}
	
	/**
	 * ����������ϸҳ��
	 */
	public void goNewsContent(NewsListEntity newsListEntity){
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		ft.hide(mNewsList);
		mNewsContent = new NewsContent(newsListEntity);
		ft.add(R.id.news_fragment,mNewsContent,Constant.NEWS_CONTENT);
		ft.addToBackStack(null);
		//ft.replace(R.id.news_fragment, , Constant.NEWS_CONTENT);
		//ft.show(mNewsContent);
		ft.commit();
	}
	
	@Override
	public void onBackPressed() {
		if(mFragmentManager.getBackStackEntryCount() == 0)
			super.onBackPressed();
		else {
			FragmentTransaction ft = mFragmentManager.beginTransaction();
			mFragmentManager.popBackStack();
			ft.remove(mNewsContent);
			ft.show(mNewsList);
			ft.commit();
		}
		
	}
}
