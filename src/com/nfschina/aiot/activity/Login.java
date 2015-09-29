package com.nfschina.aiot.activity;

import com.nfschina.aiot.R;
import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.db.SharePerencesHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity implements OnClickListener {

	// 用户名输入的EditText
	private EditText mUserNameEditText;
	// 密码输入的EditText
	private EditText mPasswordEditText;
	// 记住密码CheckBox
	private CheckBox mRememberCheckBox;
	// 自动登录CheckBox
	private CheckBox mAutoLoginCheckBox;
	// 登录按钮
	private Button mLoginButton;
	// 注册按钮
	private Button mRegisterButton;
	// 用户名
	private String mUserName;
	// 密码
	private String mPassword;
	// 是否记住密码
	private boolean mRemember;
	// 是否自动登录
	private boolean mAutoLogin;


	public Login() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		initUIControls();
		setListener();

	}

	/**
	 * initialize the UI controls
	 */
	public void initUIControls() {
		mUserNameEditText = (EditText) findViewById(R.id.edit_username);
		mPasswordEditText = (EditText) findViewById(R.id.edit_password);
		mRememberCheckBox = (CheckBox) findViewById(R.id.cb_rember_pswd);
		mAutoLoginCheckBox = (CheckBox) findViewById(R.id.cb_auto_login);
		mLoginButton = (Button) findViewById(R.id.bt_login);
		mRegisterButton = (Button) findViewById(R.id.bt_register);

		initEditViews();
	}

	/**
	 * initialize controls of the view
	 */
	public void initEditViews() {
		mRemember = SharePerencesHelper.getBoolean(this, Constant.IS_REMEMBER_PWD, false);
		mAutoLogin = SharePerencesHelper.getBoolean(this, Constant.IS_AUTO_LOGIN, false);
		if (mRemember) {
			mUserName = SharePerencesHelper.getString(this, Constant.USER_NAME, null);
			mPassword = SharePerencesHelper.getString(this, Constant.PWD, null);
			if (mPassword != null && mUserName != null) {
				mUserNameEditText.setText(mUserName);
				mPasswordEditText.setText(mPassword);
			} else {
				SharePerencesHelper.putBoolean(this, Constant.IS_REMEMBER_PWD, false);
				SharePerencesHelper.putBoolean(this, Constant.IS_AUTO_LOGIN, false);
				SharePerencesHelper.putString(this, Constant.USER_NAME, null);
				SharePerencesHelper.putString(this, Constant.PWD, null);
				mRemember = false;
				mAutoLogin = false;
			}
		}
	}

	/**
	 * set the listener of the controls
	 */
	public void setListener() {
		mLoginButton.setOnClickListener(this);
		mRegisterButton.setOnClickListener(this);
		mRememberCheckBox.setOnClickListener(this);
		mAutoLoginCheckBox.setOnClickListener(this);

	}

	/**
	 * get the data for login
	 * 
	 * @return return true if success,or false
	 */
	private boolean GetLoginData() {
		mUserName = mUserNameEditText.getText().toString();
		mPassword = mPasswordEditText.getText().toString();
		if ("".equals(mUserName.trim()) || mUserName.equals(null) || "".equals(mPassword.trim())
				|| mPassword.equals(null)) {
			Toast.makeText(this, Constant.FILL_NAME_PASSWORD, Toast.LENGTH_SHORT).show();
			return false;
		} else
			return true;
	}

	/**
	 * perform the login action
	 */
	public void PerformLogin() {

		if (mRemember) {
			SharePerencesHelper.putString(this, Constant.USER_NAME, mUserName);
			SharePerencesHelper.putString(this, Constant.PWD, mPassword);
		}
		if(mAutoLogin){
			SharePerencesHelper.putBoolean(this, Constant.IS_AUTO_LOGIN, true);
		}
		Intent intent = new Intent(this, Home.class);
		startActivity(intent);
		this.finish();
	}

	/**
	 * the clicking listener of the view
	 * 
	 * @param v
	 *            the view which to be listened
	 */
	@Override
	public void onClick(View v) {

		Intent intent = new Intent();
		switch (v.getId()) {

		case R.id.bt_login:
			if (GetLoginData()) {
				PerformLogin();
			}
			break;
		case R.id.bt_register:
			intent.setClass(Login.this, Register.class);
			startActivityForResult(intent, Constant.REG_CODE);
			break;
		case R.id.cb_auto_login:
			if (mAutoLoginCheckBox.isChecked() == true) {
				mRememberCheckBox.setChecked(true);
				mRemember = true;
				mAutoLogin = true;
			} else {
				mAutoLogin = false;
			}
			break;
		case R.id.cb_rember_pswd:
			if (mRememberCheckBox.isChecked() == true) {
				mRemember = true;
			} else {
				if (mAutoLoginCheckBox.isChecked() == true) {
					mAutoLogin = false;
					mAutoLoginCheckBox.setChecked(false);
				}
				mRemember = false;
			}
			break;
		default:
			Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Constant.REG_SUCCESS) {
			String username = data.getStringExtra("username");
			mUserNameEditText.setText(username);
		}
	}
	

}
