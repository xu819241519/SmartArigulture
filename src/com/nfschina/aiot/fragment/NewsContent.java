package com.nfschina.aiot.fragment;

import com.nfschina.aiot.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NewsContent extends Fragment {

	
	
	// the UI controls
	private TextView mTitle;
	private TextView mContent;
	private View mView;
	
	//test id
	private String mID;

	
	public NewsContent(String id) {
		mID = id;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.news_content, null);
		return mView;
	}

	@Override
	public void onStart() {
		super.onStart();
		initUIControls();
	}

	/**
	 * Initialize the UI controls
	 */
	private void initUIControls() {
		mTitle = (TextView) mView.findViewById(R.id.news_content_title);
		mContent = (TextView) mView.findViewById(R.id.news_content_content);
		
		mTitle.setText(mID);
	}
}
