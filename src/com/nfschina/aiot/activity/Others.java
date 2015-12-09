package com.nfschina.aiot.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.db.SharePerencesHelper;


/**
 * ��������
 * ������������͹������
 * @author xu
 *
 */

public class Others extends Activity implements OnClickListener{

	// �������
	private LinearLayout mAbout;
	// ��������
	private LinearLayout mChangePswd;
	// ע��
	private LinearLayout mLogout;
	// ���ذ�ť
	private ImageButton mBack;
	// ��ҳ��ť
	private ImageButton mGoHome;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.others);
		
		initUIControls();
		setListener();
	}

	/**
	 * ��ʼ��UI�ؼ�
	 */
	private void initUIControls() {
		mAbout = (LinearLayout) findViewById(R.id.others_about);
		mChangePswd = (LinearLayout) findViewById(R.id.others_change_pswd);
		mLogout = (LinearLayout) findViewById(R.id.others_logout);
		mBack = (ImageButton)findViewById(R.id.others_back);
		mGoHome = (ImageButton)findViewById(R.id.others_gohome);
	}
	
	/**
	 * ����UI������
	 */
	private void setListener(){
		mAbout.setOnClickListener(this);
		mChangePswd.setOnClickListener(this);
		mLogout.setOnClickListener(this);
		mBack.setOnClickListener(this);
		mGoHome.setOnClickListener(this);
	}

	/**
	 *	�����ť���¼�	
	 */
	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		//����
		case R.id.others_about:
			intent = new Intent(this,About.class);
			break;
		//��������
		case R.id.others_change_pswd:
			intent = new Intent(this,ChangePassword.class);
			break;
		//ע��
		case R.id.others_logout:
			Constant.CURRENT_USER = null;
			Constant.CURRENT_PASSWORD = null;
			SharePerencesHelper.putBoolean(this, Constant.IS_AUTO_LOGIN, false);
			intent = new Intent(this,Login.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
			stopService(Home.intent);
			break;
		//����
		case R.id.others_back:
			this.finish();
			break;
		case R.id.others_gohome:
			intent = new Intent(this,Home.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
			break;
		default:
			Toast.makeText(this, Constant.UNDEF, Toast.LENGTH_SHORT).show();
			break;
		}
		if(intent != null){
			if(v.getId() != R.id.others_change_pswd)
				startActivity(intent);
			else{
				startActivityForResult(intent, Constant.CHANGE_PSWD_CODE);
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Constant.CHANGE_PSWD_SUCCESS_CODE){
			SharePerencesHelper.putBoolean(this, Constant.IS_REMEMBER_PWD, false);
			SharePerencesHelper.putBoolean(this, Constant.IS_AUTO_LOGIN, false);
			Constant.CURRENT_PASSWORD =  null;
			Intent intent = new Intent(this,Login.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
	}

}
