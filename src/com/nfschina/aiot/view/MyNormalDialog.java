package com.nfschina.aiot.view;

import java.util.ArrayList;
import java.util.List;

import com.nfschina.aiot.activity.R;
import com.nfschina.aiot.adapter.DialogListViewAdapter;
import com.nfschina.aiot.listener.MyDialogListener;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyNormalDialog extends Dialog implements  OnCheckedChangeListener{
	private Context context;
	private String title;
	private LinearLayout dialog_preview;
	private ListView dialog_listview;
	private RelativeLayout ad_item_rl;
	private Button canclebtn ;
	private Button surebtn ;
	private TextView titleTextview;
	private CheckBox ad_item_allchoose;
	private Boolean isAllChecked = true;//Ϊ���ж��Ƿ���ȫѡ��������ֹȫѡ��ʱ�򣬵�ѡ�Ļص�����ִ��
	private Boolean isAllUnChecked = true;
	private List<String> items=new ArrayList<String>();
	DialogListViewAdapter adapter;
	MyDialogListener dialogListener;
	

	public MyNormalDialog(Context context,String title,MyDialogListener dialogListener) {
		super(context, R.style.blend_theme_dialog);
		this.context=context;
		this.title=title;
		this.dialogListener = dialogListener;
	}
	@Override
	public void setTitle(CharSequence title) {
		this.title = (String) title;
		super.setTitle(title);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LayoutInflater inflater =LayoutInflater.from(context);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.alertdialog_view, null);

		titleTextview = (TextView) layout.findViewById(R.id.ad_title_tv);
		canclebtn = (Button) layout.findViewById(R.id.dialog_cancle_btn);
		surebtn = (Button) layout.findViewById(R.id.dialog_sure_btn);
		canclebtn.setText(context.getResources().getString(R.string.no));
		dialog_preview = (LinearLayout) layout.findViewById(R.id.blend_dialog_preview);
		dialog_listview = (ListView) layout.findViewById(R.id.blend_dialog_nextview);
		ad_item_rl = (RelativeLayout) layout.findViewById(R.id.ad_item_rl);
		ad_item_allchoose = (CheckBox) layout.findViewById(R.id.ad_item_allchoose);
		
		ad_item_allchoose.setOnCheckedChangeListener(this);
		this.setCanceledOnTouchOutside(true);
		//ȡ����ť
		canclebtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dismiss();
				dialogListener.onCancel();
			}
		});
		//ȷ����ť
		surebtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				dialogListener.onSure();
			}
		});
		
		this.setContentView(layout);
	}
	
	public void initListViewData(List<String> list){
		titleTextview.setText(title);
		items=list;
		dialog_preview.setVisibility(View.GONE);
		ad_item_rl.setVisibility(View.VISIBLE);
		dialog_listview.setVisibility(View.VISIBLE);
		ad_item_allchoose.setChecked(false);
		adapter = new DialogListViewAdapter(context, list,dialogListener);
		dialog_listview.setAdapter(adapter);
			
	}

	
	@Override
	public void dismiss() {
		super.dismiss();
		//��������Ϊ��ʵ��ÿ�δ򿪶Ի��򶼻������ת��
		dialog_preview.setVisibility(View.VISIBLE);
		dialog_listview.setVisibility(View.GONE);
		ad_item_rl.setVisibility(View.GONE);
	}
	//ȫѡ�Ĺ���ʵ��
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.ad_item_allchoose:
			if (isChecked) {
				
				adapter.checkAll(isAllChecked);
				dialogListener.onListItemAllChecked();
			}else {
				adapter.checkNone(isAllUnChecked);
				dialogListener.onListItemAllUnChecked();
			}
			break;
		}
				
	}


}
