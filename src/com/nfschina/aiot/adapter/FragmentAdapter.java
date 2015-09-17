package com.nfschina.aiot.adapter;

import java.util.List;

import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Context;
import android.content.Intent;

import com.nfschina.aiot.activity.Login;
import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.db.SharePerencesHelper;
import com.nfschina.aiot.fragment.Guide_4;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * fragment��������
 * @author xu
 *
 */

public class FragmentAdapter extends FragmentPagerAdapter {

	 //fragment���б�
    private List<Fragment> mFragments;
    //����fragmentactivity
    private FragmentActivity mActivity;

    /**
     * ���캯��
     * @param fManager :fragmentmanager�Ķ���
     * @param fragments :fragment���б�
     * @param activity :activity�Ķ���
     */
    public FragmentAdapter(android.support.v4.app.FragmentManager fManager,List<Fragment> fragments, FragmentActivity activity) {
        super(fManager);
    	this.mFragments = fragments;
        this.mActivity = activity;
    }
   
    // ��õ�ǰ������
    @Override
    public int getCount() {
        if (mFragments != null) {
            return mFragments.size();
        }
        return 0;
    }

    /**
     * �����¼ҳ��
     */
    public void goLogin() {
        // ��ת
        Intent intent = new Intent(mActivity, Login.class);
        mActivity.startActivity(intent);
        mActivity.finish();
    }

    /**
     * 
     * �����Ѿ��������ˣ��´����������ٴ�����
     */
    public void setGuided() {
    	SharePerencesHelper.putBoolean(mActivity,Constant.IS_FIRST_IN, false);
    }

	@Override
	public Fragment getItem(int arg0) {
		if (arg0 == mFragments.size()-1) {
			Guide_4 fg = (Guide_4) mFragments.get(arg0);
			fg.SetAdapter(this);
          return fg;
		}
		return mFragments.get(arg0);
	}

}
