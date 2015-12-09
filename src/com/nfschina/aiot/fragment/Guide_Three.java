package com.nfschina.aiot.fragment;

import com.nfschina.aiot.activity.R;
import com.nfschina.aiot.adapter.FragmentAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.internal.view.menu.MenuView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 引导页面3
 * 
 * @author xu
 *
 */

public class Guide_Three extends Fragment {

	private View mView;

	// 开始按钮
	private ImageView imageStart;

	// 适配器
	private FragmentAdapter fAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.what_new_three, null);

		imageStart = (ImageView) mView.findViewById(R.id.start);
		// 设置开始按钮的事件，点击开始进入主页面
		imageStart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (fAdapter != null) {
					fAdapter.setGuided();
					fAdapter.goLogin();
				}
			}
		});
		return mView;
	}

	/**
	 * 设置适配器
	 * 
	 * @param fAdapter
	 *            适配器的对象
	 */
	public void SetAdapter(FragmentAdapter fAdapter) {
		this.fAdapter = fAdapter;
	}

}
