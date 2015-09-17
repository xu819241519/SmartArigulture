package com.nfschina.aiot.activity;

import com.nfschina.aiot.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends Activity {

	// �û��������EditText
	private EditText mUserNameEditText;
	// ���������EditText
	private EditText mPasswordEditText;
	// ��ס����CheckBox
	private CheckBox mRemberCheckBox;
	// �Զ���¼CheckBox
	private CheckBox mAutoLoginCheckBox;
	// ��¼��ť
	private Button mLoginButton;
	// ע�ᰴť
	private Button mRegisterButton;
	// �û���
	private String mUserName;
	// ����
	private String mPassword;
	// �Ƿ��ס����
	private boolean mRemember;
	// �Ƿ��Զ���¼
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
	 * ��ʼ��UI
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
	 * ��ʼ��UI��ر���
	 */
	public void initValues() {
		
	}
}
