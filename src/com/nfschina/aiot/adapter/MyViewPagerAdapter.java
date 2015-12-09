package com.nfschina.aiot.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
/**
 * 自定义的viewpager适配器，主要是适配fragment  list
 * @author wujian
 *
 */
public class MyViewPagerAdapter extends FragmentStatePagerAdapter {
	
	private List<Fragment> fragments = new ArrayList<Fragment>();

	public MyViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	public MyViewPagerAdapter(FragmentManager fragmentManager,
			List<Fragment> fragments) {
		super(fragmentManager);
		this.fragments = fragments;
	}
	/*
	 * 返回当前位置的数据
	 */
	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}
	/*
	 * 获取列表中数据个数
	 */
	@Override
	public int getCount() {
		return fragments.size();
	}

}
