package com.nfschina.aiot.fragment;

import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nfschina.aiot.R;
import com.nfschina.aiot.adapter.InstructionsAdapter;
import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.constant.ConstantFun;
import com.nfschina.aiot.db.AccessDataBase;
import com.nfschina.aiot.entity.AlarmEntity;
import com.nfschina.aiot.entity.InstructionEntity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class InstructionsHistory extends Fragment {

	// the UI controls
	private PullToRefreshListView mPullRefreshListView;
	private ListView mListView;
	private View mView;

	// the current page
	private int mPage;
	// the count of one page
	private int mSize;

	// the adapter
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
	 * Initialize the UI controls
	 */
	private void initUIControls() {
		mPullRefreshListView = (PullToRefreshListView) mView.findViewById(R.id.instructions_list);

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
		mInstructionsAdapter = new InstructionsAdapter();
		mListView.setAdapter(mInstructionsAdapter);

		// mPullRefreshListView.setRefreshing(true);
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
			//pull down
			if (params[0]) {
				mPage = 0;
				mInstructionsAdapter.clearData();
				mInstructionsAdapter.addData(AccessDataBase.getInstructionHistoryData(mPage, mSize));
				result = true;
			} else {
				List<InstructionEntity> list = AccessDataBase.getInstructionHistoryData(mPage + 1, mSize);
				if (list != null && list.size() != 0) {
					mPage++;
					mInstructionsAdapter.addData(list);
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
				mInstructionsAdapter.notifyDataSetChanged();
			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}
}
