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
 * 历史记录页面 分为指令记录、报警记录和数据记录。三个记录用fragment和viewpager左右切换，或者用radiobutton点击切换。
 * 数据记录里包括温度、湿度、光照、二氧化碳浓度
 * 
 * @author xu
 *
 */

public class History extends FragmentActivity
		implements OnPageChangeListener, OnCheckedChangeListener, OnClickListener {

	// UI控件
	private ViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;
	private List<Fragment> mFragmentList;
	private RadioGroup mRadioGroup;
	private ImageButton mGoBack;
	private ImageButton mGoHome;

	// 表明进入的是哪个温室大棚
	private String GreenHouseID;

	// viewpager的当前页面索引
	private int mCurrentItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);

		InitUIControls();
		setListener();

	}

	/**
	 * 初始化UI控件
	 * 
	 * @author xu
	 */
	private void InitUIControls() {

		// 绑定控件
		mViewPager = (ViewPager) findViewById(R.id.history_viewpager);
		mRadioGroup = (RadioGroup) findViewById(R.id.history_group);
		mGoBack = (ImageButton) findViewById(R.id.history_back);
		mGoHome = (ImageButton) findViewById(R.id.history_gohome);

		// 设置温室大棚ID
		Intent intent = getIntent();
		GreenHouseID = intent.getStringExtra(Constant.INTENT_EXTRA_HISTORY_HOUSE_ID);

		// 初始化framelist页面
		mFragmentList = new ArrayList<Fragment>();
		mFragmentList.add(new InstructionsHistory());
		mFragmentList.add(new GreenHouseHistory());
		mFragmentList.add(new AlarmHistory());
		mAdapter = new GenelFragmentAdapter(getSupportFragmentManager(), mFragmentList);

		// 设置适配器
		mViewPager.setAdapter(mAdapter);
		mViewPager.setCurrentItem(1);
		mCurrentItem = 0;

	}

	/**
	 * 设置UI监听事件
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
	 * 监听viewpager选择的事件
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
	 * 获取温室ID
	 * 
	 * @return 返回当前温室ID
	 */
	public String getGreenHouseID() {
		return GreenHouseID;
	}

	/**
	 * 点击radiogroup切换fragment页面事件
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
