package com.nfschina.aiot.activity;

import com.nfschina.aiot.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nfschina.aiot.constant.*;
import com.nfschina.aiot.db.AccessDataBase;


/**
 * ע��ҳ��
 * @author xu
 *
 */

public class Register extends Activity implements OnClickListener {

	// ע�ᰴť
	private Button mRegister;
	// �û����༭��
	private EditText mUserNameEditText;
	// ����༭��
	private EditText mPasswordEditText;
	// ���ذ�ť
	private LinearLayout mBackLinearLayout;

	// ��ʾ�Ի���
	private AlertDialog mAlertDialog;

	// �û���
	private String mUserName;
	// ����
	private String mPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		InitUIControls();
		SetListener();
	}

	/**
	 * ��ʼ��UI�ؼ�
	 */
	private void InitUIControls() {
		mRegister = (Button) findViewById(R.id.register_reg);
		mPasswordEditText = (EditText) findViewById(R.id.register_pswd);
		mUserNameEditText = (EditText) findViewById(R.id.register_username);
		mBackLinearLayout = (LinearLayout) findViewById(R.id.register_back);
	}

	/**
	 * ����UI�ؼ��ļ���
	 */
	private void SetListener() {
		mRegister.setOnClickListener(this);
		mBackLinearLayout.setOnClickListener(this);
	}

	/**
	 * ��֤��д����Ϣ�Ƿ����Ҫ��
	 * 
	 * @return ������Ϸ���true������false
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
	 * ִ��ע��
	 */
	private void PerformRegister() {
		mAlertDialog = new AlertDialog.Builder(this).create();
		mAlertDialog.setMessage("����ע��...");
		mAlertDialog.show();
		new PerformLinkRegister(this).execute();

	}

	/**
	 * �رյȴ��Ի���
	 */
	public void finishAlertDialog() {
		if (mAlertDialog != null) {
			mAlertDialog.dismiss();
		}
	}

	/**
	 * ����¼�
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//ע��
		case R.id.register_reg:
			if (GetRegisterData())
				PerformRegister();
			break;
		//����
		case R.id.register_back:
			finish();
			break;
		default:
			Toast.makeText(this, Constant.UNDEF, Toast.LENGTH_SHORT).show();
			break;
		}

	}

	/**
	 * �������ݿ�ִ��ע��
	 * @author xu
	 *
	 */
	public class PerformLinkRegister extends AsyncTask<Void, Void, Integer> {
		private Activity mActivity;

		public PerformLinkRegister(Activity activity) {
			mActivity = activity;
		}

		@Override
		protected Integer doInBackground(Void... params) {
			int resultCode = Constant.SERVER_CONNECT_FAILED;
			try {
				resultCode = AccessDataBase.connectRegister(mUserName, mPassword);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return resultCode;
		}

		@Override
		protected void onPostExecute(Integer result) {
			//��ʾ��Ϣ
			finishAlertDialog();
			if (result == Constant.SERVER_CONNECT_FAILED) {
				Toast.makeText(mActivity, Constant.CONNECT_FAILED_INFO, Toast.LENGTH_SHORT).show();
			} else if (result == Constant.SERVER_REGISTER_FAILED) {
				Toast.makeText(mActivity, Constant.LOGIN_FAILED_INFO, Toast.LENGTH_SHORT).show();
			} else if (result == Constant.SERVER_REGISTER_SUCCESS) {
				Intent intent = new Intent();
				intent.putExtra("username", mUserName);
				setResult(Constant.REG_SUCCESS, intent);
				finish();
			} else if (result == Constant.SERVER_SQL_FAILED) {
				Toast.makeText(mActivity, Constant.SQL_FAILED_INFO, Toast.LENGTH_SHORT).show();
			}
		}
	}
}
