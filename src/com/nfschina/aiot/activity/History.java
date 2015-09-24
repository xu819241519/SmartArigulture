package com.nfschina.aiot.activity;

import java.util.ArrayList;
import java.util.List;

import com.nfschina.aiot.R;
import com.nfschina.aiot.adapter.GenelFragmentAdapter;
import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.fragment.AlarmHistory;
import com.nfschina.aiot.fragment.GreenHouseHistory;
import com.nfschina.aiot.fragment.InstructionsHistory;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class History extends FragmentActivity implements OnPageChangeListener, OnCheckedChangeListener {

	// the UI controls
	private ViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;
	private List<Fragment> mFragmentList;
	private RadioGroup mRadioGroup;
	private TextView mGoBack;

	// current selected items
	private int mCurrentItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);
		InitUIControls();
		setListener();

	}

	/**
	 * Initialize the UI controls
	 * 
	 * @author xu
	 */
	private void InitUIControls() {

		mViewPager = (ViewPager) findViewById(R.id.history_viewpager);
		mRadioGroup = (RadioGroup) findViewById(R.id.history_group);
		mGoBack = (TextView) findViewById(R.id.history_back);

		mFragmentList = new ArrayList<Fragment>();
		mFragmentList.add(new InstructionsHistory());
		mFragmentList.add(new GreenHouseHistory());
		mFragmentList.add(new AlarmHistory());
		mAdapter = new GenelFragmentAdapter(getSupportFragmentManager(), mFragmentList);
		

		mViewPager.setCurrentItem(1);
		mViewPager.setAdapter(mAdapter);
		mCurrentItem = 0;

	}

	private void setListener() {
		mViewPager.setOnPageChangeListener(this);
		mRadioGroup.setOnCheckedChangeListener(this);
		mGoBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	/**
	 * listen to the viewpager's change
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
}
