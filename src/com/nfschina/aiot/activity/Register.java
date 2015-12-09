package com.nfschina.aiot.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.db.AccessDataBase;

/**
 * ע��ҳ��
 * 
 * @author xu
 *
 */

public class Register extends Activity implements OnClickListener {

	// ע�ᰴť
	private Button mRegister;
	// ��¼���༭��
	private EditText mUserIDEditText;
	// ����༭��
	private EditText mPasswordEditText;
	// �û���
	private EditText mUserNameEditText;
	// ���ذ�ť
	private ImageButton mBack;

	// ��ʾ�Ի���
	private AlertDialog mAlertDialog;

	// ��¼��
	private String mUserID;
	// ����
	private String mPassword;
	// �û���
	private String mUserName;

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
		mUserIDEditText = (EditText) findViewById(R.id.register_userid);
		mBack = (ImageButton) findViewById(R.id.register_back);
		mUserNameEditText = (EditText) findViewById(R.id.register_username);
	}

	/**
	 * ����UI�ؼ��ļ���
	 */
	private void SetListener() {
		mRegister.setOnClickListener(this);
		mBack.setOnClickListener(this);
	}

	/**
	 * ��֤��д����Ϣ�Ƿ����Ҫ��
	 * 
	 * @return ������Ϸ���true������false
	 */
	private boolean GetRegisterData() {
		mUserID = mUserIDEditText.getText().toString();
		mPassword = mPasswordEditText.getText().toString();
		mUserName = mUserNameEditText.getText().toString();
		if ("".equals(mUserID) || "".equals(mPassword) || "".equals(mUserName) || mUserID == null || mPassword == null
				|| mUserName == null) {
			Toast.makeText(this, Constant.FILL_NAME_PASSWORD, Toast.LENGTH_SHORT).show();
			return false;
		} else if (hasSpecialChar(mUserID)) {
			Toast.makeText(this, "��¼�������벻�ܰ��������ַ���", Toast.LENGTH_SHORT).show();
			return false;
		} else if (hasSpecialChar(mPassword)) {
			Toast.makeText(this, "�����в��ܰ��������ַ���", Toast.LENGTH_SHORT).show();
			return false;
		} else if (hasSpecialChar(mUserName)) {
			Toast.makeText(this, "�û����в��ܰ��������ַ���", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	private boolean hasSpecialChar(String str) {
		boolean result = false;
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~��@#��%����&*��������+|{}������������������������]";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(str);
		if (matcher.find())
			return true;
		else
			return false;
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
		// ע��
		case R.id.register_reg:
			if (GetRegisterData())
				PerformRegister();
			break;
		// ����
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
	 * 
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
				resultCode = AccessDataBase.connectRegister(mUserID, mPassword, mUserName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return resultCode;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// ��ʾ��Ϣ
			finishAlertDialog();
			if (result == Constant.SERVER_CONNECT_FAILED) {
				Toast.makeText(mActivity, Constant.CONNECT_FAILED_INFO, Toast.LENGTH_SHORT).show();
			} else if (result == Constant.SERVER_REGISTER_FAILED) {
				Toast.makeText(mActivity, Constant.REGISTER_FAILED_INFO, Toast.LENGTH_SHORT).show();
			} else if (result == Constant.SERVER_REGISTER_SUCCESS) {
				Intent intent = new Intent();
				intent.putExtra(Constant.REG_RETURN, mUserID);
				setResult(Constant.REG_SUCCESS, intent);
				finish();
			} else if (result == Constant.SERVER_SQL_FAILED) {
				Toast.makeText(mActivity, Constant.SQL_FAILED_INFO, Toast.LENGTH_SHORT).show();
			}
		}
	}
}
