package com.nfschina.aiot.adapter;

import com.nfschina.aiot.R;
import com.nfschina.aiot.constant.Constant;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NewsAdapter extends BaseAdapter {

	public NewsAdapter() {

	}

	@Override
	public int getCount() {
		return Constant.TestListItem.length;
	}

	@Override
	public Object getItem(int position) {
		return Constant.TestListItem[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			convertView = LinearLayout.inflate(parent.getContext(), R.layout.news_list_item, null);
			holder = new Holder();
			holder.setTime((TextView) convertView.findViewById(R.id.news_item_time));
			holder.setTitle((TextView) convertView.findViewById(R.id.news_item_title));
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		if (holder != null) {

			holder.getTime().setText(Constant.TestListItem[position]);
			holder.getTitle().setText(Constant.TestListItem[position]);
		}
		return convertView;
	}

	public class Holder {
		private TextView mTitle;
		private TextView mTime;

		public TextView getTitle() {
			return mTitle;
		}

		public void setTitle(TextView mTitle) {
			this.mTitle = mTitle;
		}

		public TextView getTime() {
			return mTime;
		}

		public void setTime(TextView mTime) {
			this.mTime = mTime;
		}
	}
}
