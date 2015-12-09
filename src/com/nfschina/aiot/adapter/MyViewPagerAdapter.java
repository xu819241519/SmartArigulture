package com.nfschina.aiot.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
/**
 * �Զ����viewpager����������Ҫ������fragment  list
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
	 * ���ص�ǰλ�õ�����
	 */
	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}
	/*
	 * ��ȡ�б������ݸ���
	 */
	@Override
	public int getCount() {
		return fragments.size();
	}

}
