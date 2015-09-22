package com.nfschina.aiot.activity;

import com.nfschina.aiot.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nfschina.aiot.constant.*;

public class Register extends Activity implements OnClickListener {

	// the register button
	private Button mRegister;
	// the EditText of username
	private EditText mUserNameEditText;
	// the EditText of password
	private EditText mPasswordEditText;
	// the layout of back btn
	private LinearLayout mBackLinearLayout;
	
	// the string of username
	private String mUserName;
	// the string of password;
	private String mPassword;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		InitUIControls();
		SetListener();
	}

	/**
	 * initialize the UI controls
	 */
	private void InitUIControls() {
		mRegister = (Button) findViewById(R.id.register_reg);
		mPasswordEditText = (EditText) findViewById(R.id.register_pswd);
		mUserNameEditText = (EditText) findViewById(R.id.register_username);
		mBackLinearLayout = (LinearLayout)findViewById(R.id.register_back);
	}

	/**
	 * set the listener of the UI controls
	 */
	private void SetListener() {
		mRegister.setOnClickListener(this);
		mBackLinearLayout.setOnClickListener(this);
	}
	
	/**
	 * get the data of the register
	 * @return return true if success,or false
	 */
	private boolean GetRegisterData() {
		mUserName = mUserNameEditText.getText().toString();
		mPassword = mPasswordEditText.getText().toString();
		if ("".equals(mUserName) || "".equals(mPassword) || mUserName == null || mPassword == null) {
			Toast.makeText(this, Constant.FILL_NAME_PASSWORD, Toast.LENGTH_SHORT).show();
			return false;
		} else
			return true;
	}

	/**
	 * perform the register action
	 */
	private void PerformRegister() {
		Intent intent = new Intent();
		intent.putExtra("username", mUserName);
		setResult(Constant.REG_SUCCESS, intent);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_reg:
			if (GetRegisterData())
				PerformRegister();
			break;

		case R.id.register_back:
			finish();
			break;
		default:
			Toast.makeText(this, Constant.UNDEF, Toast.LENGTH_SHORT).show();
			break;
		}

	}
}
