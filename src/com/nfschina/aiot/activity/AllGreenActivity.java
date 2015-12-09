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
 * ��ʾ������Ȩ�޵Ĵ��ѡ�������룬�鿴��ʷ��¼ ���д��ﶼ�ӷ�������ȡ����ʾ��GridView��
 * 
 * @author xu
 *
 */

public class AllGreenActivity extends Activity implements OnClickListener {

	// ���ذ�ť
	private ImageButton mGoBack;
	// ��ҳ��ť
	private ImageButton mGoHome;
	// ���ҳ��
	private int mCurrentPage = 0;
	// һҳ��ŵĸ���
	private int PAGE_COUNT = 15;
	// ������
	private AllGreenAdapter mAdapter;

	// ��Ŵ������������ؼ�
	private PullToRefreshGridView mPullToRefreshGridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.greenhouse_history_list);
		InitUIControls();
	}

	/**
	 * ��ʼ��UI�ؼ�
	 */
	private void InitUIControls() {
		// �󶨿ؼ�
		mPullToRefreshGridView = (PullToRefreshGridView) findViewById(R.id.greenhouse_list_gv);
		mGoBack = (ImageButton) findViewById(R.id.greenhouse_back);
		mGoHome = (ImageButton) findViewById(R.id.greenhouse_gohome);
		// ���÷��ذ�ť�����¼�
		mGoBack.setOnClickListener(this);
		// ������ҳ��ť�����¼�
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
				// ִ�����ݲ�ѯ

				//new GetDataTask().execute(mCurrentPage);

				return true;
			}
		}).sendEmptyMessageDelayed(0, 500);

	}

	/**
	 * �ӷ�������ȡӵ��Ȩ�޵Ĵ���
	 * 
	 * @author xu
	 *
	 */
	public class GetDataTask extends AsyncTask<Integer, Void, Void> {
		// ��ȡ������������
		private int DataCount = 0;

		@Override
		protected Void doInBackground(Integer... params) {
			DataCount = 0;
			// ��ȡ���������
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
				// ����������
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
		Toast.makeText(this, "û���κ����ҿɹ�������ϵ����Ա", Toast.LENGTH_SHORT).show();
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
