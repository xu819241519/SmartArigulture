package com.nfschina.aiot.fragment;

import com.lidroid.xutils.ViewUtils;
import com.nfschina.aiot.activity.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HistoryThresholdFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.threshold_history, null);
		ViewUtils.inject(this,view);
		return view;
	}
}
