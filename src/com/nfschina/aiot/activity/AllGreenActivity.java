package com.nfschina.aiot.activity;

import com.nfschina.aiot.R;
import com.nfschina.aiot.adapter.AllGreenAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

public class AllGreenActivity extends Activity {

	
	private TextView mGoBack;
	private GridView mGridView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.greenhouse_list);
		InitUIControls();
	}

	private void InitUIControls(){
		mGridView = (GridView)findViewById(R.id.greenhouse_list_gv);
		mGridView.setAdapter(new AllGreenAdapter(this));
		mGoBack = (TextView)findViewById(R.id.greenhouse_list_back);
		mGoBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
