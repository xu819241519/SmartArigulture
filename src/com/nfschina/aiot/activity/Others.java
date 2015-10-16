package com.nfschina.aiot.activity;

import com.nfschina.aiot.R;
import com.nfschina.aiot.constant.Constant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 其他设置
 * 包括更改密码和关于软件
 * @author xu
 *
 */

public class Others extends Activity implements OnClickListener{

	// 关于软件
	private LinearLayout mAbout;
	// 更改密码
	private LinearLayout mChangePswd;
	// 返回按钮
	private TextView mBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.others);
		
		initUIControls();
		setListener();
	}

	/**
	 * 初始化UI控件
	 */
	private void initUIControls() {
		mAbout = (LinearLayout) findViewById(R.id.others_about);
		mChangePswd = (LinearLayout) findViewById(R.id.others_change_pswd);
		mBack = (TextView)findViewById(R.id.others_back);
	}
	
	/**
	 * 设置UI监听器
	 */
	private void setListener(){
		mAbout.setOnClickListener(this);
		mChangePswd.setOnClickListener(this);
		mBack.setOnClickListener(this);
	}

	/**
	 *	点击按钮的事件	
	 */
	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		//关于
		case R.id.others_about:
			intent = new Intent(this,About.class);
			break;
		//更改密码
		case R.id.others_change_pswd:
			intent = new Intent(this,ChangePassword.class);
			break;
		//返回
		case R.id.others_back:
			this.finish();
			break;
		default:
			Toast.makeText(this, Constant.UNDEF, Toast.LENGTH_SHORT).show();
			break;
		}
		if(intent != null){
			startActivity(intent);
		}
	}

}
