package com.nfschina.aiot.fragment;

import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nfschina.aiot.R;
import com.nfschina.aiot.activity.History;
import com.nfschina.aiot.adapter.InstructionsAdapter;
import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.constant.ConstantFun;
import com.nfschina.aiot.db.AccessDataBase;
import com.nfschina.aiot.entity.InstructionEntity;

import android.graphics.AvoidXfermode.Mode;
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
 * 指令记录 通过上拉控件实现展示数据
 * 
 * @author xu
 *
 */

public class InstructionsHistory extends Fragment {

	// 下拉刷新控件
	private PullToRefreshListView mPullRefreshListView;
	private ListView mListView;
	private View mView;

	// 温室ID
	private String GreenHouseID;

	// 当前页码
	private int mPage = 0;
	// 每页的数量
	private int mSize = 15;

	// 适配器
	private InstructionsAdapter mInstructionsAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.instruction_history, null);
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
		mPullRefreshListView = (PullToRefreshListView) mView.findViewById(R.id.instructions_list);
		GreenHouseID = ((History) getActivity()).getGreenHouseID();
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

		mListView = mPullRefreshListView.getRefreshableView();
		mInstructionsAdapter = new InstructionsAdapter();
		mListView.setAdapter(mInstructionsAdapter);

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
	 * 连接服务器获取指令数据
	 * 
	 * @author xu
	 *
	 */
	private class GetDataTask extends AsyncTask<Boolean, Void, Boolean> {

		private boolean hasPullUpData = true;

		@Override
		protected Boolean doInBackground(Boolean... params) {
			Boolean result = false;
			// pull down
			if (params[0]) {
				mPage = 0;
				mInstructionsAdapter.clearData();
				List<InstructionEntity> data = AccessDataBase.getInstructionHistoryData(mPage, mSize, GreenHouseID);
				if (data != null && data.size() > 0)
					mInstructionsAdapter.addData(data);
				result = true;
			} else {
				List<InstructionEntity> list = AccessDataBase.getInstructionHistoryData(mPage + 1, mSize, GreenHouseID);
				if (list != null && list.size() != 0) {
					mPage++;
					mInstructionsAdapter.addData(list);
					result = true;
					hasPullUpData = true;
				} else {
					hasPullUpData = false;
				}
			}
			return result;
		}

		@Override
		protected void onPostExecute(Boolean result) {

			if (result)
				mInstructionsAdapter.notifyDataSetChanged();
			else {
				// mPullRefreshListView.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.PULL_FROM_START);

				Toast.makeText(getActivity(), Constant.END_OF_LIST, Toast.LENGTH_SHORT).show();
			}
			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshListView.onRefreshComplete();

			if (!hasPullUpData) {
				mPullRefreshListView
						.setOnLastItemVisibleListener(ConstantFun.getLastItemVisibleListener(getActivity()));
			}

			super.onPostExecute(result);
		}
	}
}
