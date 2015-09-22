package com.nfschina.aiot.adapter;

import java.util.List;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * fragment的适配器
 * @author xu
 *
 */

public class FragmentPicAdapter extends FragmentPagerAdapter {

	 //fragment的列表
    private List<Fragment> mFragments;

    /**
     * 构造函数
     * @param fManager :fragmentmanager的对象
     * @param fragments :fragment的列表
     */
    public FragmentPicAdapter(android.support.v4.app.FragmentManager fManager,List<Fragment> fragments) {
        super(fManager);
    	this.mFragments = fragments;
    }
   
    // 获得当前界面数
    @Override
    public int getCount() {
        if (mFragments != null) {
            return mFragments.size();
        }
        return 0;
    }


	@Override
	public Fragment getItem(int arg0) {
		return mFragments.get(arg0);
	}

}
