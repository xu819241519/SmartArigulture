/**
 * 
 */
package com.nfschina.aiot.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import com.nfschina.aiot.R;
import com.nfschina.aiot.adapter.GenelFragmentAdapter;
import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.fragment.Pic_Advert1;
import com.nfschina.aiot.fragment.Pic_Advert2;
import com.nfschina.aiot.fragment.Pic_Advert3;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * @author xu
 *
 */
public class Home extends FragmentActivity implements OnClickListener, OnPageChangeListener {

	// the button to the subitem
	private LinearLayout[] mBtnItem;

	// the time of click back button
	private long mExitTime;
	// the viewpager filled with pics
	private ViewPager mViewPager;
	// the view filled with pics
	private List<Fragment> mFragmentsList;
	// the adapter of pics
	private GenelFragmentAdapter mPicAdapter;
	// the imageview of dots
	//private ImageView[] mDots;
	// the current page of the imageview
	private int mCurrentPage;

	// the time
	private Timer mTimer = new Timer();
	// teh timetask
	private TimerTask mTimeTask = new TimerTask() {

		@Override
		public void run() {
			Message msg = new Message();
			msg.what = 1;
			mHandler.sendMessage(msg);
		}
	};

	// the handler
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			mCurrentPage += 1;
			if (mCurrentPage >= Constant.getHomePicCount())
				mCurrentPage = 0;
			 mViewPager.setCurrentItem(mCurrentPage);
			 //setCurrentDot(mCurrentPage);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		InitUIControls();
		setListener();

		mExitTime = 0;
	}

	/**
	 * Initialize the UI controls
	 */
	private void InitUIControls() {
		mBtnItem = new LinearLayout[Constant.HOME_BTN_COUNT];
		mBtnItem[0] = (LinearLayout) findViewById(R.id.btn_history);
		mBtnItem[1] = (LinearLayout) findViewById(R.id.btn_monitor_center);
		mBtnItem[2] = (LinearLayout) findViewById(R.id.btn_news);
		mBtnItem[3] = (LinearLayout) findViewById(R.id.btn_other);

		mViewPager = (ViewPager) findViewById(R.id.home_viewpager);
		mFragmentsList = new ArrayList<Fragment>();
		mFragmentsList.add(new Pic_Advert1());
		mFragmentsList.add(new Pic_Advert2());
		mFragmentsList.add(new Pic_Advert3());
		mPicAdapter = new GenelFragmentAdapter(getSupportFragmentManager(), mFragmentsList);
		mViewPager.setAdapter(mPicAdapter);
		mViewPager.setOnPageChangeListener(this);

		//initDots();
		mTimer.schedule(mTimeTask, 5000, 5000);
	}

	/**
	 * set the listener of the UI controls
	 */
	private void setListener() {
		for (int i = 0; i < Constant.HOME_BTN_COUNT; ++i)
			mBtnItem[i].setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_history:
			Intent intent = new Intent(Home.this,AllGreenActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_monitor_center:
			break;
		case R.id.btn_news:
			break;
		case R.id.btn_other:
			break;
		default:
			Toast.makeText(this, Constant.UNDEF, Toast.LENGTH_SHORT).show();
			;
			break;
		}
	}

	@Override
	public void onBackPressed() {
		if (mExitTime == 0 || (mExitTime != 0 && System.currentTimeMillis() - mExitTime > 2000)) {
			Toast.makeText(this, Constant.CONFIRM_EXIT, Toast.LENGTH_SHORT).show();
			mExitTime = System.currentTimeMillis();
		} else
			finish();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		//setCurrentDot(arg0);
	}

	/*
	 * 初始化小圆点
	 */
//	private void initDots() {
//		LinearLayout ll = (LinearLayout) findViewById(R.id.home_ll);
//
//		mDots = new ImageView[mFragmentsList.size()];
//
//		// 循环取得小点图片
//		for (int i = 0; i < mFragmentsList.size(); i++) {
//			mDots[i] = (ImageView) ll.getChildAt(i);
//			mDots[i].setEnabled(false);// 都设为灰色
//		}
//
//		mCurrentPage = 0;
//		mDots[mCurrentPage].setEnabled(true);// 设置为白色，即选中状态
//		mDots[mCurrentPage].setVisibility(0);
//	}

	/**
	 * 设置小圆点的当前页面
	 * 
	 * @param position
	 *            当前页面的索引值
	 */
//	private void setCurrentDot(int position) {
//		if (position < 0 || position > mFragmentsList.size() - 1 || mCurrentPage == position) {
//			return;
//		}
//
//		for (int i = 0; i < Constant.HOME_PICS; ++i)
//			mDots[i].setEnabled(false);
//		mDots[mCurrentPage].setEnabled(true);
//
//		mCurrentPage = position;
//	}

}
