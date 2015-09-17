package com.nfschina.aiot.activity;

import java.sql.SQLClientInfoException;
import java.sql.SQLData;
import java.util.Set;

import com.nfschina.aiot.R;
import com.nfschina.aiot.R.id;
import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.db.SharePerencesHelper;

import android.R.bool;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity implements OnClickListener {

	// 用户名输入的EditText
	private EditText mUserNameEditText;
	// 密码输入的EditText
	private EditText mPasswordEditText;
	// 记住密码CheckBox
	private CheckBox mRemberCheckBox;
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
		checkAutoLogin();
		setListener();

	}

	/**
	 * 初始化UI
	 */
	public void initUIControls() {
		mUserNameEditText = (EditText) findViewById(R.id.edit_username);
		mPasswordEditText = (EditText) findViewById(R.id.edit_password);
		mRemberCheckBox = (CheckBox) findViewById(R.id.cb_rember_pswd);
		mAutoLoginCheckBox = (CheckBox) findViewById(R.id.cb_auto_login);
		mLoginButton = (Button) findViewById(R.id.bt_login);
		mRegisterButton = (Button) findViewById(R.id.bt_register);

		initEditViews();
	}

	/**
	 * 初始化UI相关变量
	 */
	public void initEditViews() {
		mRemember = SharePerencesHelper.getBoolean(this,Constant.IS_REMEMBER_PWD, false);
		mAutoLogin = SharePerencesHelper.getBoolean(this,Constant.IS_AUTO_LOGIN, false);
		if (mRemember) {
			mUserName = SharePerencesHelper.getString(this,Constant.USER_NAME, null);
			mPassword = SharePerencesHelper.getString(this,Constant.PWD, null);
			if (mPassword != null && mUserName != null) {
				mUserNameEditText.setText(mUserName);
				mPasswordEditText.setText(mPassword);
			} else {
				SharePerencesHelper.putBoolean(this,Constant.IS_REMEMBER_PWD, false);
				SharePerencesHelper.putBoolean(this,Constant.IS_AUTO_LOGIN, false);
				SharePerencesHelper.putString(this,Constant.USER_NAME, null);
				SharePerencesHelper.putString(this,Constant.PWD, null);
				mRemember = false;
				mAutoLogin = false;
			}
		}
	}

	/**
	 * 检查自动登录
	 */
	public void checkAutoLogin() {
		if (mAutoLogin) {

		}
	}

	/**
	 * 设置监听器
	 */
	public void setListener() {
		mLoginButton.setOnClickListener(this);
		mRegisterButton.setOnClickListener(this);
		mRemberCheckBox.setOnClickListener(this);
		mAutoLoginCheckBox.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.bt_login:
			Toast.makeText(this, "R.id.bt_login", 3000).show();
			break;
		case R.id.bt_register:
			Toast.makeText(this, "R.id.bt_register", 3000).show();
			break;
		case R.id.cb_auto_login:
			Toast.makeText(this, "R.id.cb_auto_login", 3000).show();
			break;
		case R.id.cb_rember_pswd:
			Toast.makeText(this, "R.id.cb_rember_pswd", 3000).show();
			break;
		default:
			Toast.makeText(this, "null", 3000).show();
			break;
		}
	}
}
