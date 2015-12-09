package com.nfschina.aiot.view;
import com.nfschina.aiot.activity.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
/**
 * 自定义进度圈对话框
 * @author wujian
 */
public class MyProgressDialog extends ProgressDialog {
	private Context context;
	private TextView progressdialog_tv;
	private String message;

	public MyProgressDialog(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = LayoutInflater.from(context).inflate(R.layout.myprogressdialog_view, null);
		progressdialog_tv = (TextView) view.findViewById(R.id.progressdialog_tv);
		progressdialog_tv.setText(message);
		setContentView(view);
	}
	
	@Override
	public void setMessage(CharSequence message) {
		super.setMessage(message);
		this.message = (String) message;
	}
	
	
}
