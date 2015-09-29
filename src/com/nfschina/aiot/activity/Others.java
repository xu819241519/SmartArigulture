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

public class Others extends Activity implements OnClickListener{

	// the UI controls
	private LinearLayout mAbout;
	private LinearLayout mChangePswd;
	private TextView mBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.others);
		initUIControls();
		setListener();
	}

	/**
	 * Initialize the UI controls
	 */
	private void initUIControls() {
		mAbout = (LinearLayout) findViewById(R.id.others_about);
		mChangePswd = (LinearLayout) findViewById(R.id.others_change_pswd);
		mBack = (TextView)findViewById(R.id.others_back);
	}
	
	/**
	 * set the listener of the UI controls
	 */
	private void setListener(){
		mAbout.setOnClickListener(this);
		mChangePswd.setOnClickListener(this);
		mBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.others_about:
			intent = new Intent(this,About.class);
			break;
		case R.id.others_change_pswd:
			intent = new Intent(this,ChangePassword.class);
			break;
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
