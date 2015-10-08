package com.nfschina.aiot.fragment;

import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nfschina.aiot.R;
import com.nfschina.aiot.adapter.AlarmAdapter;
import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.constant.ConstantFun;
import com.nfschina.aiot.db.AccessDataBase;
import com.nfschina.aiot.entity.AlarmEntity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class AlarmHistory extends Fragment {

	// the UI controls
	private PullToRefreshListView mPullRefreshListView;
	private ListView mListView;
	private View mView;

	// the current page
	private int mPage = 0;
	// the count of one page
	private int mSize = 15;

	// the adapter of the alarmhistory
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
	 * Initialize the UI controls
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
		//mPullRefreshListView.setRefreshing(true);
	}

	/**
	 * get the data from internet
	 * 
	 * @author xu
	 *
	 */
	private class GetDataTask extends AsyncTask<Boolean, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Boolean... params) {
			Boolean result = false;
			// pull down
			if (params[0]) {
				mPage = 0;
				mAlarmAdapter.clearData();
				mAlarmAdapter.addData(AccessDataBase.getAlarmHistoryData(mPage, mSize));
				result = true;
			}
			// pull up
			else {
				List<AlarmEntity> list = AccessDataBase.getAlarmHistoryData(mPage + 1, mSize);
				if (list != null && list.size() != 0) {
					mPage++;
					mAlarmAdapter.addData(list);
					result = true;
				} else if (list != null) {
					mPullRefreshListView
							.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.PULL_FROM_START);
				} else {
					Toast.makeText(getActivity(), Constant.UNDEF, Toast.LENGTH_SHORT).show();
				}

			}
			return result;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if(result)
				mAlarmAdapter.notifyDataSetChanged();
			mPullRefreshListView.onRefreshComplete();
			
		}
	}
}
