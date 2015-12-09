package com.nfschina.aiot.activity;

import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.nfschina.aiot.adapter.AllGreenAdapter;
import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.db.AccessDataBase;
import com.nfschina.aiot.entity.GreenHouseEntity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.Contacts.Data;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * 显示所有有权限的大棚，选择大棚进入，查看历史记录 所有大棚都从服务器获取，显示在GridView中
 * 
 * @author xu
 *
 */

public class AllGreenActivity extends Activity implements OnClickListener {

	// 返回按钮
	private ImageButton mGoBack;
	// 主页按钮
	private ImageButton mGoHome;
	// 存放页数
	private int mCurrentPage = 0;
	// 一页存放的个数
	private int PAGE_COUNT = 15;
	// 适配器
	private AllGreenAdapter mAdapter;

	// 存放大棚的下拉网格控件
	private PullToRefreshGridView mPullToRefreshGridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.greenhouse_history_list);
		InitUIControls();
	}

	/**
	 * 初始化UI控件
	 */
	private void InitUIControls() {
		// 绑定控件
		mPullToRefreshGridView = (PullToRefreshGridView) findViewById(R.id.greenhouse_list_gv);
		mGoBack = (ImageButton) findViewById(R.id.greenhouse_back);
		mGoHome = (ImageButton) findViewById(R.id.greenhouse_gohome);
		// 设置返回按钮监听事件
		mGoBack.setOnClickListener(this);
		// 设置主页按钮监听事件
		mGoHome.setOnClickListener(this);

		mPullToRefreshGridView.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.BOTH);

		mPullToRefreshGridView.setScrollingWhileRefreshingEnabled(false);

		mPullToRefreshGridView.setOnRefreshListener(new OnRefreshListener2<GridView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
				mPullToRefreshGridView.setRefreshing();
				mCurrentPage = 0;
				new GetDataTask().execute(mCurrentPage);

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
				mPullToRefreshGridView.setRefreshing();
				mCurrentPage++;
				new GetDataTask().execute(mCurrentPage);

			}
		});

		new Handler(new Handler.Callback() {

			@Override
			public boolean handleMessage(Message msg) {
				mPullToRefreshGridView.setRefreshing();
				// 执行数据查询

				//new GetDataTask().execute(mCurrentPage);

				return true;
			}
		}).sendEmptyMessageDelayed(0, 500);

	}

	/**
	 * 从服务器获取拥有权限的大棚
	 * 
	 * @author xu
	 *
	 */
	public class GetDataTask extends AsyncTask<Integer, Void, Void> {
		// 获取到的数据条数
		private int DataCount = 0;

		@Override
		protected Void doInBackground(Integer... params) {
			DataCount = 0;
			// 获取大棚的名字
			List<GreenHouseEntity> entities = AccessDataBase.getGreenHouseName(PAGE_COUNT * params[0], PAGE_COUNT);
			if (entities != null && entities.size() != 0) {
				if (mAdapter == null) {
					mAdapter = new AllGreenAdapter(AllGreenActivity.this);
				}
				if (params[0] == 0)
					mAdapter.clearOldData();
				for (int i = 0; i < entities.size(); ++i) {
					mAdapter.addGreenHouse(entities.get(i));

				}
				DataCount = entities.size();

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			mPullToRefreshGridView.onRefreshComplete();

			if (DataCount != 0) {
				// 设置适配器
				mPullToRefreshGridView.setAdapter(mAdapter);
				if (DataCount < PAGE_COUNT)
					Toast.makeText(AllGreenActivity.this, Constant.END_OF_LIST, Toast.LENGTH_SHORT).show();
			} else {
				if (mAdapter == null || mAdapter.getCount() == 0) {
					closeSelf();
				} else
					Toast.makeText(AllGreenActivity.this, Constant.END_OF_LIST, Toast.LENGTH_SHORT).show();
			}

		}
	}

	public void closeSelf() {
		Toast.makeText(this, "没有任何温室可管理，请联系管理员", Toast.LENGTH_SHORT).show();
		finish();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.greenhouse_back) {
			finish();
		} else if (v.getId() == R.id.greenhouse_gohome) {
			Intent intent = new Intent(this, Home.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
	}
}
