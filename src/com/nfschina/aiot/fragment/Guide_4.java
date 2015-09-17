package com.nfschina.aiot.fragment;

import com.nfschina.aiot.R;
import com.nfschina.aiot.adapter.FragmentAdapter;

import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class Guide_4 extends Fragment {

	private ImageView imageStart;
	
	private FragmentAdapter fAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.what_new_four, null);
		imageStart = (ImageView) view.findViewById(R.id.start);
		imageStart.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(fAdapter !=null){
					fAdapter.setGuided();
					fAdapter.goLogin();				
				}
			}
		});
		return view;
	}
	
	/**
	 * 设置适配器
	 * @param fAdapter 适配器的对象
	 */
	public void SetAdapter(FragmentAdapter fAdapter){
		this.fAdapter = fAdapter;
	}

}
