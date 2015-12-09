package com.nfschina.aiot.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nfschina.aiot.fragment.RealTimeTaskFragment;
import com.nfschina.aiot.fragment.TimedTaskFragment;
/**
 * ������������
 * @author wujian
 */
public class CreateTaskActivity extends FragmentActivity implements OnCheckedChangeListener, OnClickListener {
	@ViewInject(R.id.createtask_rg)
	private RadioGroup createtask_rg;
	@ViewInject(R.id.task_realtime_rb)
	private RadioButton task_realtime_rb;
	@ViewInject(R.id.task_timed_rb)
	private RadioButton task_timed_rb;
	@ViewInject(R.id.createtask_content_fl)
	private FrameLayout createtask_content_fl;
	@ViewInject(R.id.createtask_back_iv)
	private ImageView createtask_back_iv;
	private FragmentManager fragmentManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createtasks);
		ViewUtils.inject(this);
		
		fragmentManager = getSupportFragmentManager();
		task_realtime_rb.setChecked(true);
		createtask_rg.setOnCheckedChangeListener(this);
		createtask_back_iv.setOnClickListener(this);
		changeFragment(new RealTimeTaskFragment() , false);
	}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.task_realtime_rb://����ʵʱ����
			changeFragment(new RealTimeTaskFragment() , true);
			break;
		case R.id.task_timed_rb://������ʱ����
			changeFragment(new TimedTaskFragment(), true);
			break;
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.createtask_back_iv:
			finish();
			break;
		default:
			break;
		}
	}
	/**
	 * �л�fragment
	 * @param fragment ��Ҫ�����fragment
	 * @param isInit �Ƿ��ǵ�һ�ν���
	 */
	public void changeFragment(Fragment fragment,boolean isInit){
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.createtask_content_fl, fragment);
		if (!isInit) {
			transaction.addToBackStack(null);
		}
		transaction.commit();
	}
	//������ʱ��������ǰ��activity
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
