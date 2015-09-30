package com.nfschina.aiot.activity;

import com.nfschina.aiot.R;
import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.db.AccessDataBase;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChangePassword extends Activity implements OnClickListener {

	// the UI controls
	private EditText mOldPassword;
	private EditText mNewPassword;
	private EditText mConfirmPassword;
	private Button mChangePassword;
	private TextView mBack;
	private AlertDialog mAlertDialog;

	// the string of the EditText
	private String mOldPasswordString;
	private String mNewPasswordString;
	private String mConfirmPasswordString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.changepassword);
		initUIControls();
		setListener();

		mOldPasswordString = mNewPasswordString = mConfirmPasswordString = null;
	}

	/**
	 * Initialize the UI controls
	 */
	private void initUIControls() {
		mOldPassword = (EditText) findViewById(R.id.other_old_pswd);
		mNewPassword = (EditText) findViewById(R.id.other_new_pswd);
		mConfirmPassword = (EditText) findViewById(R.id.other_confirm_pswd);
		mBack = (TextView) findViewById(R.id.change_password_back);
		mChangePassword = (Button) findViewById(R.id.bt_change_pswd);
	}

	/**
	 * set the listener of the UI controls
	 */
	private void setListener() {
		mChangePassword.setOnClickListener(this);
		mBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.change_password_back)
			this.finish();
		else if (v.getId() == R.id.bt_change_pswd) {
			if (GetChangePasswordData()) {
				PerformChangePassword();
			}
		}

	}

	/**
	 * get data of the password
	 * 
	 * @return return true if the data is corrent, or false
	 */
	private boolean GetChangePasswordData() {
		mOldPasswordString = mOldPassword.getText().toString().trim();
		mNewPasswordString = mNewPassword.getText().toString().trim();
		mConfirmPasswordString = mConfirmPassword.getText().toString().trim();
		if (mOldPasswordString == null || "".equals(mOldPasswordString) || mNewPasswordString == null
				|| "".equals(mNewPasswordString) || mConfirmPasswordString == null
				|| "".equals(mConfirmPasswordString)) {
			Toast.makeText(this, Constant.FILL_PASSWORD, Toast.LENGTH_SHORT).show();
			return false;
		} else if (mOldPasswordString.equals(mNewPasswordString)) {
			Toast.makeText(this, Constant.DIFF_PASSWORD, Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	/**
	 * perform changing the password
	 */
	private void PerformChangePassword() {
		mAlertDialog = new AlertDialog.Builder(this).create();
		mAlertDialog.setMessage("ÕýÔÚÐÞ¸ÄÃÜÂë...");
		mAlertDialog.show();
		new PerformLinkChangePassword(this).execute();
	}

	/**
	 * dismiss the alertdialog
	 */
	public void finishAlertDialog() {
		if (mAlertDialog != null) {
			mAlertDialog.dismiss();
		}
	}

	public class PerformLinkChangePassword extends AsyncTask<Void, Void, Integer> {

		private Activity mActivity;

		public PerformLinkChangePassword(Activity activity) {
			mActivity = activity;
		}

		@Override
		protected Integer doInBackground(Void... params) {
			int resultCode = Constant.SERVER_CONNECT_FAILED;
			try {
				resultCode = AccessDataBase.ConnectChangePassword(Constant.CURRENT_USER, mOldPasswordString, mNewPasswordString);
				return resultCode;
			} catch (Exception e) {
				e.printStackTrace();
			} 
			return resultCode;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			finishAlertDialog();
			if (result == Constant.SERVER_CONNECT_FAILED) {
				Toast.makeText(mActivity, Constant.CONNECT_FAILED_INFO, Toast.LENGTH_SHORT).show();
			} else if (result == Constant.SERVER_CHANGE_PASSWORD_FAILED) {
				Toast.makeText(mActivity, Constant.CHANGE_PASSWORD_FAILED, Toast.LENGTH_SHORT).show();
			} else if (result == Constant.SERVER_CHANGE_PASSWORD_SUCCESS) {
				Toast.makeText(mActivity, Constant.CHANGE_PASSWORD_SUCCESS, Toast.LENGTH_SHORT).show();
				finish();
			} else if (result == Constant.SERVER_SQL_FAILED) {
				Toast.makeText(mActivity, Constant.SQL_FAILED_INFO, Toast.LENGTH_SHORT).show();
			}
		}

	}

}
