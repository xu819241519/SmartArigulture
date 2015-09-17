package com.nfschina.aiot.adapter;

import java.util.List;
import android.content.Context;
import android.content.Intent;

import com.nfschina.aiot.activity.Login;
import com.nfschina.aiot.db.SharePerencesDBHelper;
import com.nfschina.aiot.fragment.Guide_4;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * fragment的适配器
 * @author xu
 *
 */

public class FragmentAdapter extends FragmentPagerAdapter {

	 //fragment的列表
    private List<Fragment> mFragments;
    //保存fragmentactivity
    private FragmentActivity mActivity;

    /**
     * 构造函数
     * @param fManager :fragmentmanager的对象
     * @param fragments :fragment的列表
     * @param activity :activity的对象
     */
    public FragmentAdapter(android.support.v4.app.FragmentManager fManager,List<Fragment> fragments, FragmentActivity activity) {
        super(fManager);
    	this.mFragments = fragments;
        this.mActivity = activity;
    }
   
    // 获得当前界面数
    @Override
    public int getCount() {
        if (mFragments != null) {
            return mFragments.size();
        }
        return 0;
    }

    /**
     * 进入登录页面
     */
    public void goLogin() {
        // 跳转
        Intent intent = new Intent(mActivity, Login.class);
        mActivity.startActivity(intent);
        mActivity.finish();
    }

    /**
     * 
     * 设置已经引导过了，下次启动不用再次引导
     */
    public void setGuided() {
    	SharePerencesDBHelper spDbHelper = new SharePerencesDBHelper(mActivity);
    	spDbHelper.putBoolean("isFirstIn", false);
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
