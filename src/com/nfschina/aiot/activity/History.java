package com.nfschina.aiot.activity;

import java.util.ArrayList;
import java.util.List;

import com.nfschina.aiot.R;
import com.nfschina.aiot.adapter.GenelFragmentAdapter;
import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.fragment.AlarmHistory;
import com.nfschina.aiot.fragment.GreenHouseHistory;
import com.nfschina.aiot.fragment.InstructionsHistory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

/**
 * ��ʷ��¼ҳ�� ��Ϊָ���¼��������¼�����ݼ�¼��������¼��fragment��viewpager�����л���������radiobutton����л���
 * ���ݼ�¼������¶ȡ�ʪ�ȡ����ա�������̼Ũ��
 * 
 * @author xu
 *
 */

public class History extends FragmentActivity
		implements OnPageChangeListener, OnCheckedChangeListener, OnClickListener {

	// UI�ؼ�
	private ViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;
	private List<Fragment> mFragmentList;
	private RadioGroup mRadioGroup;
	private ImageButton mGoBack;
	private ImageButton mGoHome;

	// ������������ĸ����Ҵ���
	private String GreenHouseID;

	// viewpager�ĵ�ǰҳ������
	private int mCurrentItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);

		InitUIControls();
		setListener();

	}

	/**
	 * ��ʼ��UI�ؼ�
	 * 
	 * @author xu
	 */
	private void InitUIControls() {

		// �󶨿ؼ�
		mViewPager = (ViewPager) findViewById(R.id.history_viewpager);
		mRadioGroup = (RadioGroup) findViewById(R.id.history_group);
		mGoBack = (ImageButton) findViewById(R.id.history_back);
		mGoHome = (ImageButton) findViewById(R.id.history_gohome);

		// �������Ҵ���ID
		Intent intent = getIntent();
		GreenHouseID = intent.getStringExtra(Constant.INTENT_EXTRA_HISTORY_HOUSE_ID);

		// ��ʼ��framelistҳ��
		mFragmentList = new ArrayList<Fragment>();
		mFragmentList.add(new InstructionsHistory());
		mFragmentList.add(new GreenHouseHistory());
		mFragmentList.add(new AlarmHistory());
		mAdapter = new GenelFragmentAdapter(getSupportFragmentManager(), mFragmentList);

		// ����������
		mViewPager.setAdapter(mAdapter);
		mViewPager.setCurrentItem(1);
		mCurrentItem = 0;

	}

	/**
	 * ����UI�����¼�
	 */
	private void setListener() {
		mViewPager.setOnPageChangeListener(this);
		mRadioGroup.setOnCheckedChangeListener(this);
		mGoHome.setOnClickListener(this);
		mGoBack.setOnClickListener(this);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	/**
	 * ����viewpagerѡ����¼�
	 */
	@Override
	public void onPageSelected(int arg0) {
		mCurrentItem = 1;
		switch (arg0) {
		case 0:
			mCurrentItem = 0;
			mRadioGroup.check(R.id.radio_history_instructions);
			break;
		case 1:
			mCurrentItem = 1;
			mRadioGroup.check(R.id.radio_history_greenhouse);
			break;
		case 2:
			mCurrentItem = 2;
			mRadioGroup.check(R.id.radio_history_alarm);
			break;
		default:
			Toast.makeText(this, Constant.UNDEF, Toast.LENGTH_SHORT).show();
			break;
		}

	}

	/**
	 * ��ȡ����ID
	 * 
	 * @return ���ص�ǰ����ID
	 */
	public String getGreenHouseID() {
		return GreenHouseID;
	}

	/**
	 * ���radiogroup�л�fragmentҳ���¼�
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		mCurrentItem = 1;
		switch (checkedId) {
		case R.id.radio_history_greenhouse:
			mCurrentItem = 1;
			break;
		case R.id.radio_history_instructions:
			mCurrentItem = 0;
			break;
		case R.id.radio_history_alarm:
			mCurrentItem = 2;
			break;
		default:
			Toast.makeText(this, Constant.UNDEF, Toast.LENGTH_SHORT).show();
			break;
		}
		mViewPager.setCurrentItem(mCurrentItem);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.history_back) {
			finish();
		} else if (v.getId() == R.id.history_gohome) {
			Intent intent = new Intent(this, Home.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
	}
}
