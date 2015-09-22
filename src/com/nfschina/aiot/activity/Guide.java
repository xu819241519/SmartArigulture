package com.nfschina.aiot.activity;

import java.util.ArrayList;
import java.util.List;
import com.nfschina.aiot.R;
import com.nfschina.aiot.adapter.FragmentAdapter;
import com.nfschina.aiot.fragment.Guide_1;
import com.nfschina.aiot.fragment.Guide_2;
import com.nfschina.aiot.fragment.Guide_3;
import com.nfschina.aiot.fragment.Guide_4;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 
 * @author xu
 *
 */
public class Guide extends FragmentActivity implements OnPageChangeListener {

	//ViewPager控件
	private ViewPager mpager;
	//fragment适配器
	private FragmentPagerAdapter mAdapter;
	//存放fragment的list
	private List<Fragment> mFragments;
	//引导页面的小圆点
	private ImageView[] mdots;
	//引导页面的当前页面索引
	private int mcurrentIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide);

		initFragments();

		initDots();
	}

	/**
	 * 初始化fragments
	 */
	private void initFragments() {
		mFragments = new ArrayList<Fragment>();
		mFragments.add(new Guide_1());
		mFragments.add(new Guide_2());
		mFragments.add(new Guide_3());
		mFragments.add(new Guide_4());
		
		mAdapter = new FragmentAdapter(getSupportFragmentManager(),mFragments, this);

		mpager = (ViewPager) findViewById(R.id.guide_viewpager);
		mpager.setAdapter(mAdapter);
		mpager.setOnPageChangeListener(Guide.this);

	}

	/*
	 * 初始化小圆点
	 */
	private void initDots() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

		mdots = new ImageView[mFragments.size()];

		// 循环取得小点图片
		for (int i = 0; i < mFragments.size(); i++) {
			mdots[i] = (ImageView) ll.getChildAt(i);
			mdots[i].setEnabled(true);// 都设为灰色
		}

		mcurrentIndex = 0;
		mdots[mcurrentIndex].setEnabled(false);// 设置为白色，即选中状态
	}

	/**
	 * 设置小圆点的当前页面
	 * @param position 当前页面的索引值
	 */
	private void setCurrentDot(int position) {
		if (position < 0 || position > mFragments.size() - 1 || mcurrentIndex == position) {
			return;
		}

		mdots[position].setEnabled(false);
		mdots[mcurrentIndex].setEnabled(true);

		mcurrentIndex = position;
	}

	// 当滑动状态改变时调用
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	// 当当前页面被滑动时调用
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	// 当新的页面被选中时调用
	@Override
	public void onPageSelected(int arg0) {
		// 设置底部小点选中状态
		setCurrentDot(arg0);
	}

}
