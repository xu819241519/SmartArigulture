package com.nfschina.aiot.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import com.nfschina.aiot.R;
import com.nfschina.aiot.db.SharePerencesDBHelper;


public class Splash extends Activity {

	//�Ƿ��ǵ�һ�ν���
	private boolean mFirstIn = false;
	// ���������ӳ���ʾ��ʱ��
	private static final int SPLASH_DISPLAY_TIME = 3000;
	//�����¼����ı�־
	private static final int GO_Login = 1000;
	//������������ı�־
	private static final int GO_GUIDE = 1001;

	/*
	 * ��һ��������������ҳ��guide��������ֱ�ӽ����¼ҳ��
	 */
	private Handler mHander = new Handler() {

		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			//�����¼ҳ��
			case GO_Login:
				goHome();
				break;
			//��������ҳ��
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
		//��ʼ��
		init();
	}

	/*
	 * ��ʼ������������������Ϣ
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
	 * �����¼ҳ��
	 */
	private void goHome() {
		Intent intent = new Intent(Splash.this, Login.class);
		startActivity(intent);
		this.finish();
	}

	/*
	 * ��������ҳ��
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
