package com.nfschina.aiot.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.nfschina.aiot.activity.R;
import com.nfschina.aiot.listener.MyTimePickerListener;
import com.nfschina.aiot.view.PickerView.onSelectListener;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyDateTimePickDialog extends Dialog  {
	private Context context;
	private String title;
	private MyTimePickerListener timePickerListener;
	private LinearLayout dialog_preview;
	private Button canclebtn;
	private Button surebtn;
	private TextView titleTextview;
	private LinearLayout date_time_ll;
	//Ϊ�˻�ȡ��ǰ��ʱ��
	int year ;      
    int month ;      
    int day ;      
    int minute ;      
    int hour ;      
    int sec ; 
    //Ϊ������ѡ���ʱ��
    String year_selected ;      
    String month_selected ;      
    String day_selected ;      
    String minute_selected ;      
    String hour_selected ;      
    String sec_selected ;  
	
	private PickerView minute_pv;
	private PickerView second_pv;
	private PickerView hour_pv;
	private PickerView year_pv;
	private PickerView month_pv;
	private PickerView day_pv;
	private String initDateTime;//����ҳ���ʱ��
	
	private String time_selected;

	public MyDateTimePickDialog(Context context,MyTimePickerListener timePickerListener) {
		super(context, R.style.blend_theme_dialog);//ע����仰
		this.context = context;
		this.timePickerListener = timePickerListener;
	}
	@Override
	public void setTitle(CharSequence title) {
		this.title = (String) title;
		super.setTitle(title);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		View view = LayoutInflater.from(context).inflate(R.layout.timedialog_view, null);
		titleTextview = (TextView) view.findViewById(R.id.ad_title_tv);
		dialog_preview = (LinearLayout) view.findViewById(R.id.dialog_preview);
		canclebtn = (Button) view.findViewById(R.id.dialog_cancle_btn);
		canclebtn.setText(context.getResources().getString(R.string.no));
		surebtn = (Button) view.findViewById(R.id.dialog_sure_btn);
		date_time_ll = (LinearLayout) view.findViewById(R.id.date_time_layout);
		
		setCanceledOnTouchOutside(true);//ʹ�����Ļ�����ط����رնԻ���
		this.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				dismiss();
				timePickerListener.onCancel();
				
			}
		});
		canclebtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
				timePickerListener.onCancel();
				time_selected = "";
			}
		});
		surebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
				time_selected = year_selected+month_selected+day_selected+hour_selected+minute_selected+sec_selected;
				timePickerListener.onSure(time_selected);
			}
		});
		
		this.setContentView(view);
	}
	/**
	 * ��ȡ��ǰ��ʱ��
	 */
	public void getNowTime(){
		/*Time time = new Time("GMT+8");       
        time.setToNow();  
        year = time.year;      
        month = time.month;      
        day = time.monthDay;      
        minute = time.minute;      
        hour = time.hour;      
        sec = time.second; */
		Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);      
        month = calendar.get(Calendar.MONTH)+1;      
        day = calendar.get(Calendar.DATE);      
        minute = calendar.get(Calendar.MINUTE);      
        hour = calendar.get(Calendar.HOUR)+8;      
        sec = calendar.get(Calendar.SECOND); 
        System.out.println(year + "/" + month + "/" + day + " " +hour + ":" +minute + ":" + sec); 
        //���ȸ��赱ǰ��ʱ��
        year_selected = year+"" ;      
        month_selected = month+"" ;      
        day_selected = day+"" ;      
        minute_selected = minute+"";      
        hour_selected = hour+"";      
        sec_selected = sec+"" ; 
	}
	/**
	 * ����view��ʾ�ĳ�ʼ������
	 */
	public void initListViewData(){
		getNowTime();
		titleTextview.setText(title);
		dialog_preview.setVisibility(View.GONE);
		date_time_ll.setVisibility(View.VISIBLE);
		datatimePicker();
	}
	@Override
	public void dismiss() {
		super.dismiss();
		//��������Ϊ��ʵ��ÿ�δ򿪶Ի��򶼻������ת��
		dialog_preview.setVisibility(View.VISIBLE);
		date_time_ll.setVisibility(View.GONE);
	}
	/**
	 * ʱ������ѡ��������Ҫ����ʵ��
	 */
	public void datatimePicker(){
		minute_pv = (PickerView) findViewById(R.id.minute_pv);
		second_pv = (PickerView) findViewById(R.id.second_pv);
		hour_pv = (PickerView) findViewById(R.id.hour_pv);
		year_pv = (PickerView) findViewById(R.id.year_pv);
		month_pv = (PickerView) findViewById(R.id.month_pv);
		day_pv = (PickerView) findViewById(R.id.day_pv);
		List<String> year = new ArrayList<String>();
		List<String> month = new ArrayList<String>();
		List<String> day = new ArrayList<String>();

		List<String> hour = new ArrayList<String>();
		List<String> minute = new ArrayList<String>();
		List<String> seconds = new ArrayList<String>();
		// ��
		for (int i = 2014; i <= 2020; i++) {
			year.add(i+"");
		}
		year_pv.setData(year);
		year_pv.setOnSelectListener(new onSelectListener() {
			@Override
			public void onSelect(String text) {
				Toast.makeText(context, "ѡ���� " + text + " ��",
						Toast.LENGTH_SHORT).show();
				year_selected = text;
			}
		});
		year_pv.setSelected(this.year+"");
		// ��
		for (int i = 1; i < 32; i++) {
			day.add(i < 10 ? "0" + i : "" + i);
		}
		day_pv.setData(day);
		day_pv.setOnSelectListener(new onSelectListener() {
			@Override
			public void onSelect(String text) {
				Toast.makeText(context, "ѡ���� " + text + " ��",
						Toast.LENGTH_SHORT).show();
				day_selected = text;
			}
		});
		day_pv.setSelected(this.day+"");
		// ��
		for (int i = 1; i < 13; i++) {
			month.add(i < 10 ? "0" + i : "" + i);
		}
		month_pv.setData(month);
		month_pv.setOnSelectListener(new onSelectListener() {
			@Override
			public void onSelect(String text) {
				Toast.makeText(context, "ѡ���� " + text + " ��",
						Toast.LENGTH_SHORT).show();
				month_selected = text;
			}
		});
		month_pv.setSelected(this.month+"");
		System.out.println("month"+this.month+"");
		// ����
		for (int i = 0; i < 60; i++) {
			minute.add(i < 10 ? "0" + i : "" + i);
		}
		minute_pv.setData(minute);
		minute_pv.setOnSelectListener(new onSelectListener() {
			@Override
			public void onSelect(String text) {
				Toast.makeText(context, "ѡ���� " + text + " ��",
						Toast.LENGTH_SHORT).show();
				minute_selected = text;
			}
		});
		minute_pv.setSelected(this.minute+"");
		// Сʱ
		for (int i = 0; i < 24; i++) {
			hour.add(i < 10 ? "0" + i : "" + i);
		}
		hour_pv.setData(hour);
		hour_pv.setOnSelectListener(new onSelectListener() {
			@Override
			public void onSelect(String text) {
				Toast.makeText(context, "ѡ���� " + text + " ʱ",
						Toast.LENGTH_SHORT).show();
				hour_selected = text;
			}
		});
		hour_pv.setSelected((this.hour+8)+"");
		// ��
		for (int i = 0; i < 60; i++) {
			seconds.add(i < 10 ? "0" + i : "" + i);
		}
		second_pv.setData(seconds);
		second_pv.setOnSelectListener(new onSelectListener() {
			@Override
			public void onSelect(String text) {
				Toast.makeText(context, "ѡ���� " + text + " ��",
						Toast.LENGTH_SHORT).show();
				sec_selected = text;
			}
		});
		// ���ӵ�ǰѡ��00
		second_pv.setSelected(this.sec+"");
	}
}
