package com.nfschina.aiot.activity;


import com.nfschina.aiot.R;
import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.db.AccessDataBase;
import com.nfschina.aiot.db.SharePerencesHelper;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * ��¼ҳ�� �����û�����������е�¼
 * 
 * @author xu
 *
 */

public class Login extends Activity implements OnClickListener {

	// ��¼���༭��
	private EditText mUserIDEditText;
	// ����༭��
	private EditText mPasswordEditText;
	// ��ס�����ѡ��
	private CheckBox mRememberCheckBox;
	// �Զ���¼��ѡ��
	private CheckBox mAutoLoginCheckBox;
	// ��¼��ť
	private Button mLoginButton;
	// ע�ᰴť
	private Button mRegisterButton;
	// ��¼��
	private String mUserID;
	// ����
	private String mPassword;
	// �Ƿ��ס����
	private boolean mRemember;
	// �Ƿ��Զ���¼
	private boolean mAutoLogin;
	// ��ʾ�Ի���
	private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		initUIControls();
		setListener();

		// Toast.makeText(this, Integer.toString(newsGetUtil.getPageCount("f")),
		// Toast.LENGTH_SHORT).show();

	}

	/**
	 * ��ʼ��UI�ؼ�
	 */
	public void initUIControls() {
		mUserIDEditText = (EditText) findViewById(R.id.edit_username);
		mPasswordEditText = (EditText) findViewById(R.id.edit_password);
		mRememberCheckBox = (CheckBox) findViewById(R.id.cb_rember_pswd);
		mAutoLoginCheckBox = (CheckBox) findViewById(R.id.cb_auto_login);
		mLoginButton = (Button) findViewById(R.id.bt_login);
		mRegisterButton = (Button) findViewById(R.id.bt_register);

		initEditViews();
	}

	/**
	 * ��ʼ���༭��
	 */
	public void initEditViews() {
		if(Constant.CURRENT_USER != null){
			mUserIDEditText.setText(Constant.CURRENT_USER);
		}
		mRemember = SharePerencesHelper.getBoolean(this, Constant.IS_REMEMBER_PWD, false);
		mAutoLogin = SharePerencesHelper.getBoolean(this, Constant.IS_AUTO_LOGIN, false);
		if (mRemember) {
			mUserID = SharePerencesHelper.getString(this, Constant.USER_ID, null);
			mPassword = SharePerencesHelper.getString(this, Constant.PWD, null);
			if (mPassword != null && mUserID != null) {
				mUserIDEditText.setText(mUserID);
				mPasswordEditText.setText(mPassword);
				mRememberCheckBox.setChecked(true);
				if (mAutoLogin) {
					showAlertDialog();
					mAutoLoginCheckBox.setChecked(true);
					onClick(mLoginButton);
				}
			} else {
				SharePerencesHelper.putBoolean(this, Constant.IS_REMEMBER_PWD, false);
				SharePerencesHelper.putBoolean(this, Constant.IS_AUTO_LOGIN, false);
				SharePerencesHelper.putString(this, Constant.USER_ID, null);
				SharePerencesHelper.putString(this, Constant.PWD, null);
				mRemember = false;
				mAutoLogin = false;
			}
		}
	}

	/**
	 * ����UI�ļ���
	 */
	public void setListener() {
		mLoginButton.setOnClickListener(this);
		mRegisterButton.setOnClickListener(this);
		mRememberCheckBox.setOnClickListener(this);
		mAutoLoginCheckBox.setOnClickListener(this);

	}

	/**
	 * ��д����Ϣ�Ƿ����Ҫ��
	 * 
	 * @return ������ϣ�����true������false
	 */
	private boolean GetLoginData() {
		mUserID = mUserIDEditText.getText().toString().trim();
		mPassword = mPasswordEditText.getText().toString().trim();
		if ("".equals(mUserID.trim()) || mUserID.equals(null) || "".equals(mPassword.trim())
				|| mPassword.equals(null)) {
			Toast.makeText(this, Constant.FILL_NAME_PASSWORD, Toast.LENGTH_SHORT).show();
			return false;
		} else {
			// mPassword = ConstantFun.getMD5String(mPassword);
			return true;
		}

	}

	/**
	 * ִ�е�¼
	 */
	public void PerformLogin() {
		showAlertDialog();
		new PerformLinkLogin(this).execute();
	}

	/**
	 * ����¼�����
	 */
	@Override
	public void onClick(View v) {

		Intent intent = new Intent();
		switch (v.getId()) {
		// �����¼��ť
		case R.id.bt_login:
			if (GetLoginData()) {
				PerformLogin();
			}
			break;
		// ���ע�ᰴť
		case R.id.bt_register:
			intent.setClass(Login.this, Register.class);
			startActivityForResult(intent, Constant.REG_CODE);
			break;
		// ����Զ���¼
		case R.id.cb_auto_login:
			if (mAutoLoginCheckBox.isChecked() == true) {
				mRememberCheckBox.setChecked(true);
				mRemember = true;
				mAutoLogin = true;
			} else {
				mAutoLogin = false;
			}
			break;
		// �����ס����
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

	/**
	 * ��ʾ��ʾ�Ի���
	 */
	public void showAlertDialog(){
		if(mProgressDialog == null){
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setMessage("���ڵ�¼...");
			mProgressDialog.show();
		}
	}
	
	/**
	 * �ر���ʾ�Ի���
	 */
	public void finishAlertDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.cancel();
			mProgressDialog = null;
		}
	}

	// ע��ҳ�淵��
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Constant.REG_SUCCESS) {
			String username = data.getStringExtra(Constant.REG_RETURN);
			mUserIDEditText.setText(username);
			SharePerencesHelper.putBoolean(this, Constant.IS_REMEMBER_PWD, false);
			SharePerencesHelper.putBoolean(this, Constant.IS_AUTO_LOGIN, false);
			mPasswordEditText.setText("");
			mRememberCheckBox.setChecked(false);
			mAutoLoginCheckBox.setChecked(false);
		}
	}

	/**
	 * �����¼��Ϣ
	 */
	private void saveInfo() {
		if (mRemember) {
			SharePerencesHelper.putString(this, Constant.USER_ID, mUserID);
			SharePerencesHelper.putString(this, Constant.PWD, mPassword);
			SharePerencesHelper.putBoolean(this, Constant.IS_REMEMBER_PWD, true);
		}
		if (mAutoLogin) {
			SharePerencesHelper.putBoolean(this, Constant.IS_AUTO_LOGIN, true);
		}
	}

	/**
	 * �������ݿ�ִ�е�¼
	 * 
	 * @author xu
	 *
	 */
	public class PerformLinkLogin extends AsyncTask<Void, Void, Integer> {

		private Activity mActivity;

		public PerformLinkLogin(Activity activity) {
			mActivity = activity;
		}

		@Override
		protected Integer doInBackground(Void... params) {
			int result = Constant.SERVER_CONNECT_FAILED;
			try {
				result = AccessDataBase.connectLogin(mUserID, mPassword);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {

			// ��ʾ��Ϣ
			if (result == Constant.SERVER_CONNECT_FAILED) {
				Toast.makeText(mActivity, Constant.CONNECT_FAILED_INFO, Toast.LENGTH_SHORT).show();
			} else if (result == Constant.SERVER_LOGIN_FAILED) {
				Toast.makeText(mActivity, Constant.LOGIN_FAILED_INFO, Toast.LENGTH_SHORT).show();
			} else if (result == Constant.SERVER_LOGIN_SUCCESS) {
				saveInfo();
				Intent intent = new Intent(Login.this, Home.class);
				startActivity(intent);
				Constant.CURRENT_USER = mUserID;
				Constant.CURRENT_PASSWORD = mPassword;
				//finishAlertDialog();
				mActivity.finish();
			} else if (result == Constant.SERVER_SQL_FAILED) {
				Toast.makeText(mActivity, Constant.SQL_FAILED_INFO, Toast.LENGTH_SHORT).show();
			}

			// �ر���ʾ�Ի���
			finishAlertDialog();
		}

	}

}
