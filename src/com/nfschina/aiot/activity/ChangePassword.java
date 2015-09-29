package com.nfschina.aiot.activity;

import org.w3c.dom.Text;

import com.nfschina.aiot.R;
import com.nfschina.aiot.constant.Constant;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
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
		}
		return true;
	}

	/**
	 * perform changing the password
	 */
	private void PerformChangePassword() {

	}

}
