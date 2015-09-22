package com.nfschina.aiot.adapter;

import java.util.List;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * fragment��������
 * @author xu
 *
 */

public class FragmentPicAdapter extends FragmentPagerAdapter {

	 //fragment���б�
    private List<Fragment> mFragments;

    /**
     * ���캯��
     * @param fManager :fragmentmanager�Ķ���
     * @param fragments :fragment���б�
     */
    public FragmentPicAdapter(android.support.v4.app.FragmentManager fManager,List<Fragment> fragments) {
        super(fManager);
    	this.mFragments = fragments;
    }
   
    // ��õ�ǰ������
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
