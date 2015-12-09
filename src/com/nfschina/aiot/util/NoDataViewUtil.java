package com.nfschina.aiot.util;

import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nfschina.aiot.activity.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class NoDataViewUtil {

	public static void setNoDataView(Activity activity, PullToRefreshListView listView) {
		// 当没有数据时显示的界面
		View emptyView = LayoutInflater.from(activity).inflate(R.layout.nodata_view, null);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		((ViewGroup) listView.getParent()).addView(emptyView);
		listView.setEmptyView(emptyView);
	}

	public static void setNoDataView(Activity activity, PullToRefreshGridView gridView) {
		// 当没有数据时显示的界面
		View emptyView = LayoutInflater.from(activity).inflate(R.layout.nodata_view, null);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		((ViewGroup) gridView.getParent()).addView(emptyView);
		gridView.setEmptyView(emptyView);
	}
}
