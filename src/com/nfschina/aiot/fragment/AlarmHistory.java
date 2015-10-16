package com.nfschina.aiot.fragment;

import java.util.List;

import javax.security.auth.callback.Callback;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nfschina.aiot.R;
import com.nfschina.aiot.adapter.AlarmAdapter;
import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.constant.ConstantFun;
import com.nfschina.aiot.db.AccessDataBase;
import com.nfschina.aiot.entity.AlarmEntity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;


/**
 * 报警记录
 * 用上拉控件实现展示
 * @author xu
 *
 */

public class AlarmHistory extends Fragment {

	// 下拉刷新控件
	private PullToRefreshListView mPullRefreshListView;
	private ListView mListView;
	private View mView;

	// 当前页码
	private int mPage = 0;
	// 一页的数量
	private int mSize = 15;

	// 适配器
	private AlarmAdapter mAlarmAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.alarm_history, null);
		return mView;
	}

	@Override
	public void onStart() {
		super.onStart();
		initUIControls();
	}

	/**
	 * 初始化UI控件
	 */
	private void initUIControls() {
		mPullRefreshListView = (PullToRefreshListView) mView.findViewById(R.id.alarm_list);
		mPullRefreshListView.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.BOTH);
		mPullRefreshListView.setScrollingWhileRefreshingEnabled(true);
		mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				new GetDataTask().execute(true);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				new GetDataTask().execute(false);
			}

		});
		mPullRefreshListView.setOnPullEventListener(ConstantFun.getSoundListener(getActivity()));
		mPullRefreshListView.setOnLastItemVisibleListener(ConstantFun.getLastItemVisibleListener(getActivity()));
		mListView = mPullRefreshListView.getRefreshableView();
		mAlarmAdapter = new AlarmAdapter();
		mListView.setAdapter(mAlarmAdapter);

		new Handler(new Handler.Callback() {

			@Override
			public boolean handleMessage(Message msg) {
				mPullRefreshListView.setRefreshing();
				return true;
			}
		}).sendEmptyMessageDelayed(0, 300);
		// mPullRefreshListView.setRefreshing(true);
	}

	/**
	 * 从服务器获取报警记录
	 * 
	 * @author xu
	 *
	 */
	private class GetDataTask extends AsyncTask<Boolean, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Boolean... params) {
			Boolean result = false;
			// 下拉
			if (params[0]) {
				mPage = 0;
				mAlarmAdapter.clearData();
				mAlarmAdapter.addData(AccessDataBase.getAlarmHistoryData(mPage, mSize));
				result = true;
			}
			// 上拉
			else {
				List<AlarmEntity> list = AccessDataBase.getAlarmHistoryData(mPage + 1, mSize);
				if (list != null && list.size() != 0) {
					mPage++;
					mAlarmAdapter.addData(list);
					result = true;
				}
			}
			return result;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result)
				mAlarmAdapter.notifyDataSetChanged();
			else {
				Toast.makeText(getActivity(), Constant.END_OF_LIST, Toast.LENGTH_SHORT).show();
			}
			mPullRefreshListView.onRefreshComplete();

		}
	}
}
