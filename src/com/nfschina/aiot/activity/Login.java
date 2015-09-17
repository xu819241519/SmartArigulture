package com.nfschina.aiot.activity;

import com.nfschina.aiot.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends Activity {

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
		initValues();
	}

	/**
	 * 初始化UI相关变量
	 */
	public void initValues() {
		
	}
}
