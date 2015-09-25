package com.nfschina.aiot.fragment;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nfschina.aiot.R;
import com.nfschina.aiot.adapter.InstructionsAdapter;
import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.constant.ConstantFun;

import android.content.res.Resources.Theme;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ListView;
import android.widget.Toast;

public class InstructionsHistory extends Fragment {

	// the UI controls
	private PullToRefreshListView mPullRefreshListView;
	private ListView mListView;
	private View mView;

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

		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				new GetDataTask().execute();
			}
		});

		mPullRefreshListView.setOnPullEventListener(ConstantFun.getSoundListener(getActivity()));

		mPullRefreshListView.setOnLastItemVisibleListener(ConstantFun.getLastItemVisibleListener(getActivity()));

		mListView = mPullRefreshListView.getRefreshableView();
		mListView.setAdapter(new InstructionsAdapter());
	}

	/**
	 * get the data from internet
	 * 
	 * @author xu
	 *
	 */
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
			}
			return Constant.TestListItem;
		}

		@Override
		protected void onPostExecute(String[] result) {

			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}
}
