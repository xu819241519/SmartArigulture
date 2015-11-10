package com.nfschina.aiot.activity;

import com.nfschina.aiot.R;
import com.nfschina.aiot.adapter.AllGreenAdapter;
import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.constant.ConstantFun;
import com.nfschina.aiot.db.AccessDataBase;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 显示所有有权限的大棚，选择大棚进入，查看历史记录
 * 所有大棚都从服务器获取，显示在GridView中
 * @author xu
 *
 */

public class AllGreenActivity extends Activity implements OnClickListener {

	//返回按钮
	private ImageButton mGoBack;
	//主页按钮
	private ImageButton mGoHome;
	//存放大棚的网格控件
	private GridView mGridView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.greenhouse_list);
		InitUIControls();
	}

	/**
	 * 初始化UI控件
	 */
	private void InitUIControls(){
		//绑定控件
		mGridView = (GridView)findViewById(R.id.greenhouse_list_gv);
		mGoBack = (ImageButton)findViewById(R.id.greenhouse_back);
		mGoHome = (ImageButton)findViewById(R.id.greenhouse_gohome);
		//设置返回按钮监听事件
		mGoBack.setOnClickListener(this);
		//设置主页按钮监听事件
		mGoHome.setOnClickListener(this);
		
		//执行查询
		new GetDataTask().execute();
	}
	
	
	/**
	 * 从服务器获取拥有权限的大棚
	 * @author xu
	 *
	 */
	public class GetDataTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			//获取大棚的名字
			Constant.GreenHouseName = AccessDataBase.getGreenHouseName();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			//设置适配器
			mGridView.setAdapter(new AllGreenAdapter(AllGreenActivity.this));
		}
	}


	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.greenhouse_back){
			finish();
		}else if(v.getId() == R.id.greenhouse_gohome){
			Intent intent = new Intent(this,Home.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
	}
}
