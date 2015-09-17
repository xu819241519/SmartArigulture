package com.nfschina.aiot.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import com.nfschina.aiot.R;
import com.nfschina.aiot.db.SharePerencesDBHelper;


public class Splash extends Activity {

	//是否是第一次进入
	private boolean mFirstIn = false;
	// 启动界面延迟显示的时间
	private static final int SPLASH_DISPLAY_TIME = 3000;
	//进入登录界面的标志
	private static final int GO_Login = 1000;
	//进入引导界面的标志
	private static final int GO_GUIDE = 1001;

	/*
	 * 第一次启动进入引导页（guide），否则直接进入登录页面
	 */
	private Handler mHander = new Handler() {

		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			//进入登录页面
			case GO_Login:
				goHome();
				break;
			//进入引导页面
			case GO_GUIDE:
				goGuide();
				break;
			}
			super.handleMessage(msg);

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		//初始化
		init();
	}

	/*
	 * 初始化启动，发送启动消息
	 */
	public void init() {
		
		SharePerencesDBHelper spDBHelper;
		spDBHelper = new SharePerencesDBHelper(this);
		mFirstIn = spDBHelper.getBoolean("isFirstIn", true);

		if (mFirstIn) {
			mHander.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DISPLAY_TIME);
		} else {
			mHander.sendEmptyMessageDelayed(GO_Login, SPLASH_DISPLAY_TIME);
		}
	}

	/*
	 * 进入登录页面
	 */
	private void goHome() {
		Intent intent = new Intent(Splash.this, Login.class);
		startActivity(intent);
		this.finish();
	}

	/*
	 * 进入引导页面
	 */
	private void goGuide() {
		Intent intent = new Intent(Splash.this, Guide.class);
		startActivity(intent);
		Splash.this.finish();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
