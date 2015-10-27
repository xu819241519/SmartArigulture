package com.nfschina.aiot.activity;

import com.nfschina.aiot.R;
import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.constant.ConstantFun;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 修改密码页面
 * 需要输入旧密码和两遍新密码，当输入合法时连接服务器修改
 * @author xu
 *
 */

public class ChangePassword extends Activity implements OnClickListener {

	// 旧密码编辑框
	private EditText mOldPassword;
	// 新密码编辑框
	private EditText mNewPassword;
	// 确认新密码编辑框
	private EditText mConfirmPassword;
	// 更改密码按钮
	private Button mChangePassword;
	// 返回按钮
	private ImageButton mBack;
	// 主页按钮
	private ImageButton mGoHome;
	// 提示对话框
	private AlertDialog mAlertDialog;

	// 与UI控件相关的字符串变量
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
	 * 初始化UI控件
	 */
	private void initUIControls() {
		mOldPassword = (EditText) findViewById(R.id.other_old_pswd);
		mNewPassword = (EditText) findViewById(R.id.other_new_pswd);
		mConfirmPassword = (EditText) findViewById(R.id.other_confirm_pswd);
		mBack = (ImageButton) findViewById(R.id.change_password_back);
		mGoHome = (ImageButton) findViewById(R.id.change_password_gohome);
		mChangePassword = (Button) findViewById(R.id.bt_change_pswd);
	}

	/**
	 * 给UI控件设置监听
	 */
	private void setListener() {
		mChangePassword.setOnClickListener(this);
		mBack.setOnClickListener(this);
		mGoHome.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.change_password_back)
			this.finish();
		else if (v.getId() == R.id.bt_change_pswd) {
			if (GetChangePasswordData()) {
				PerformChangePassword();
			}
		}else if(v.getId() == R.id.change_password_gohome){
			Intent intent = new Intent(ChangePassword.this,Home.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}

	}

	/**
	 * 获取填写的信息
	 * 
	 * @return 如果填写的信息符合要求，返回true，否则false
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
		} else if (!mConfirmPasswordString.equals(mNewPasswordString)) {
			Toast.makeText(this, Constant.DIFF_PASSWORD, Toast.LENGTH_SHORT).show();
			return false;
		} else if(mConfirmPasswordString.equals(mOldPasswordString)){
			Toast.makeText(this, Constant.SAME_PASSWORD, Toast.LENGTH_SHORT).show();
			return false;
		}
//		mOldPasswordString = ConstantFun.getMD5String(mOldPasswordString);
//		mNewPasswordString = ConstantFun.getMD5String(mNewPasswordString);
		return true;
	}

	/**
	 * 执行更改密码
	 */
	private void PerformChangePassword() {
		mAlertDialog = new AlertDialog.Builder(this).create();
		mAlertDialog.setMessage("正在修改密码...");
		mAlertDialog.show();
		new PerformLinkChangePassword(this).execute();
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
	 * 向服务器发送修改密码的命令
	 * @author xu
	 * 根据返回的resultcode来判断是否修改成功
	 */
	public class PerformLinkChangePassword extends AsyncTask<Void, Void, Integer> {

		private Activity mActivity;

		public PerformLinkChangePassword(Activity activity) {
			mActivity = activity;
		}

		@Override
		protected Integer doInBackground(Void... params) {
			int resultCode = Constant.SERVER_CONNECT_FAILED;
			try {
				resultCode = AccessDataBase.connectChangePassword(Constant.CURRENT_USER, mOldPasswordString, mNewPasswordString);
				return resultCode;
			} catch (Exception e) {
				e.printStackTrace();
			} 
			return resultCode;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			//关闭等待对话框
			finishAlertDialog();
			
			//提示消息
			if (result == Constant.SERVER_CONNECT_FAILED) {
				Toast.makeText(mActivity, Constant.CONNECT_FAILED_INFO, Toast.LENGTH_SHORT).show();
			} else if (result == Constant.SERVER_CHANGE_PASSWORD_FAILED) {
				Toast.makeText(mActivity, Constant.CHANGE_PASSWORD_FAILED, Toast.LENGTH_SHORT).show();
			} else if (result == Constant.SERVER_CHANGE_PASSWORD_SUCCESS) {
				Toast.makeText(mActivity, Constant.CHANGE_PASSWORD_SUCCESS, Toast.LENGTH_SHORT).show();
				setResult(Constant.CHANGE_PSWD_SUCCESS_CODE);
				finish();
			} else if (result == Constant.SERVER_SQL_FAILED) {
				Toast.makeText(mActivity, Constant.SQL_FAILED_INFO, Toast.LENGTH_SHORT).show();
			}
		}

	}

}
