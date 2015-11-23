package com.nfschina.aiot.fragment;

import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nfschina.aiot.R;
import com.nfschina.aiot.activity.News;
import com.nfschina.aiot.adapter.NewsAdapter;
import com.nfschina.aiot.constant.ConstantFun;
import com.nfschina.aiot.entity.NewsEntity;
import com.nfschina.aiot.entity.NewsListEntity;
import com.nfschina.aiot.util.NewsGetListener;
import com.nfschina.aiot.util.NewsGetUtil;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.text.format.DateUtils;

/**
 * 新闻列表
 * 
 * @author xu
 *
 */

public class NewsList extends Fragment implements NewsGetListener {

	// 下拉刷新控件
	private PullToRefreshListView mPullRefleshListView;
	private ListView mListView;
	private View mView;

	// 第几页
	private int mPage;

	// 联网获取新闻的工具类
	private NewsGetUtil mNewsGetUtil;

	// 适配器
	private NewsAdapter mNewsAdapter;
	// 等待对话框
	private ProgressDialog mDialog;
	

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
	 * 初始化UI控件
	 */
	private void initUIControls() {

		mPage = 0;
		mPullRefleshListView = (PullToRefreshListView) mView.findViewById(R.id.news_list);

		mPullRefleshListView.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.BOTH);
		mPullRefleshListView.setScrollingWhileRefreshingEnabled(true);
		mPullRefleshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				mPage = 0;
				mNewsGetUtil.updateNews(mPage);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				mPage++;
				mNewsGetUtil.updateNews(mPage);
			}

		});

		mPullRefleshListView.setOnPullEventListener(ConstantFun.getSoundListener(getActivity()));
		
		mListView = mPullRefleshListView.getRefreshableView();
		mNewsAdapter = new NewsAdapter();
		mNewsGetUtil = new NewsGetUtil(this, ((News)getActivity()).getParserFactory().getNewsListParser(),NewsGetUtil.NEWS_LIST);
		mNewsGetUtil.updateNews(mPage);
		mListView.setAdapter(mNewsAdapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				News parents = (News) getActivity();
				((News) parents).goNewsContent((NewsListEntity) parent.getAdapter().getItem(position));
			}
		});

	}

	@Override
	public void startGetNews() {
		if (mDialog == null) {
			mDialog = new ProgressDialog(getActivity());
			mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mDialog.setMessage("正在获取数据...");
			mDialog.show();
		}
	}

	@Override
	public void updateNews(List<NewsEntity> newsEntities) {

		if (mDialog != null) {
			mDialog.cancel();
			mDialog = null;
		}
		if (newsEntities != null && newsEntities.size() > 0) {
			List<NewsListEntity> newsListEntities = new ArrayList<NewsListEntity>();
			for(int i = 0; i < newsEntities.size(); ++i)
				newsListEntities.add((NewsListEntity) newsEntities.get(i));
			mNewsAdapter.addData(newsListEntities);
			mPullRefleshListView.onRefreshComplete();
			if (newsEntities == null || newsEntities.size() == 0) {
				mPullRefleshListView
						.setOnLastItemVisibleListener(ConstantFun.getLastItemVisibleListener(getActivity()));
			}
		}
	}
}
