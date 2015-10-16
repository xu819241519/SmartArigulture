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
 * 注册页面
 * @author xu
 *
 */

public class Register extends Activity implements OnClickListener {

	// 注册按钮
	private Button mRegister;
	// 用户名编辑框
	private EditText mUserNameEditText;
	// 密码编辑框
	private EditText mPasswordEditText;
	// 返回按钮
	private LinearLayout mBackLinearLayout;

	// 提示对话框
	private AlertDialog mAlertDialog;

	// 用户名
	private String mUserName;
	// 密码
	private String mPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		InitUIControls();
		SetListener();
	}

	/**
	 * 初始化UI控件
	 */
	private void InitUIControls() {
		mRegister = (Button) findViewById(R.id.register_reg);
		mPasswordEditText = (EditText) findViewById(R.id.register_pswd);
		mUserNameEditText = (EditText) findViewById(R.id.register_username);
		mBackLinearLayout = (LinearLayout) findViewById(R.id.register_back);
	}

	/**
	 * 设置UI控件的监听
	 */
	private void SetListener() {
		mRegister.setOnClickListener(this);
		mBackLinearLayout.setOnClickListener(this);
	}

	/**
	 * 验证填写的信息是否符合要求
	 * 
	 * @return 如果符合返回true，否则false
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
	 * 执行注册
	 */
	private void PerformRegister() {
		mAlertDialog = new AlertDialog.Builder(this).create();
		mAlertDialog.setMessage("正在注册...");
		mAlertDialog.show();
		new PerformLinkRegister(this).execute();

	}

	/**
	 * 关闭等待对话框
	 */
	public void finishAlertDialog() {
		if (mAlertDialog != null) {
			mAlertDialog.dismiss();
		}
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//注册
		case R.id.register_reg:
			if (GetRegisterData())
				PerformRegister();
			break;
		//返回
		case R.id.register_back:
			finish();
			break;
		default:
			Toast.makeText(this, Constant.UNDEF, Toast.LENGTH_SHORT).show();
			break;
		}

	}

	/**
	 * 连接数据库执行注册
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
			//提示消息
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
