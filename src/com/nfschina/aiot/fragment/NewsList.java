package com.nfschina.aiot.fragment;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nfschina.aiot.R;
import com.nfschina.aiot.activity.News;
import com.nfschina.aiot.adapter.NewsAdapter;
import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.constant.ConstantFun;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.text.format.DateUtils;

public class NewsList extends Fragment {

	
	// the UI controls
	private PullToRefreshListView mPullRefleshListView;
	private ListView mListView;
	private View mView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.news_list, null);
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
		mPullRefleshListView = (PullToRefreshListView) mView.findViewById(R.id.news_list);

		mPullRefleshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				new GetDataTask().execute();
			}
		});


		mPullRefleshListView.setOnPullEventListener(ConstantFun.getSoundListener(getActivity()));

		mPullRefleshListView.setOnLastItemVisibleListener(ConstantFun.getLastItemVisibleListener(getActivity()));
				

		mListView = mPullRefleshListView.getRefreshableView();
		mListView.setAdapter(new NewsAdapter());
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				News parents = (News) getActivity();
				parents.goNewsContent(parent.getAdapter().getItem(position).toString());
			}
		});
		
		
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
			mPullRefleshListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}
}
