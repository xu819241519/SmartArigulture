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
 * 注册页面
 * 
 * @author xu
 *
 */

public class Register extends Activity implements OnClickListener {

	// 注册按钮
	private Button mRegister;
	// 登录名编辑框
	private EditText mUserIDEditText;
	// 密码编辑框
	private EditText mPasswordEditText;
	// 用户名
	private EditText mUserNameEditText;
	// 返回按钮
	private ImageButton mBack;

	// 提示对话框
	private AlertDialog mAlertDialog;

	// 登录名
	private String mUserID;
	// 密码
	private String mPassword;
	// 用户名
	private String mUserName;

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
		mUserIDEditText = (EditText) findViewById(R.id.register_userid);
		mBack = (ImageButton) findViewById(R.id.register_back);
		mUserNameEditText = (EditText) findViewById(R.id.register_username);
	}

	/**
	 * 设置UI控件的监听
	 */
	private void SetListener() {
		mRegister.setOnClickListener(this);
		mBack.setOnClickListener(this);
	}

	/**
	 * 验证填写的信息是否符合要求
	 * 
	 * @return 如果符合返回true，否则false
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
			Toast.makeText(this, "登录名中输入不能包含特殊字符！", Toast.LENGTH_SHORT).show();
			return false;
		} else if (hasSpecialChar(mPassword)) {
			Toast.makeText(this, "密码中不能包含特殊字符！", Toast.LENGTH_SHORT).show();
			return false;
		} else if (hasSpecialChar(mUserName)) {
			Toast.makeText(this, "用户名中不能包含特殊字符！", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	private boolean hasSpecialChar(String str) {
		boolean result = false;
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(str);
		if (matcher.find())
			return true;
		else
			return false;
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
		// 注册
		case R.id.register_reg:
			if (GetRegisterData())
				PerformRegister();
			break;
		// 返回
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
			// 提示消息
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
