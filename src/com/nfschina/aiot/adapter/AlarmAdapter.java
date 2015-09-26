package com.nfschina.aiot.adapter;

import com.nfschina.aiot.R;
import com.nfschina.aiot.constant.Constant;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AlarmAdapter extends BaseAdapter {

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
			holder = new Holder();
			convertView = LinearLayout.inflate(parent.getContext(), R.layout.alarm_history_item, null);
			holder.setContent((TextView) convertView.findViewById(R.id.alarm_history_content));
			holder.setGreenHouse((TextView) convertView.findViewById(R.id.alarm_history_greenhouse));
			holder.setID((TextView) convertView.findViewById(R.id.alarm_history_id));
			holder.setLevel((TextView) convertView.findViewById(R.id.alarm_history_level));
			holder.setStatus((TextView) convertView.findViewById(R.id.alarm_history_status));
			holder.setTime((TextView) convertView.findViewById(R.id.alarm_history_time));
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		
		holder.getContent().setText(Constant.TestListItem[position]);
		holder.getGreenHouse().setText(Constant.TestListItem[position]);
		holder.getID().setText(Constant.TestListItem[position]);
		holder.getLevel().setText(Constant.TestListItem[position]);
		holder.getStatus().setText(Constant.TestListItem[position]);
		holder.getTime().setText(Constant.TestListItem[position]);
		return convertView;
	}

	public class Holder {
		private TextView mID;
		private TextView mTime;
		private TextView mGreenHouse;
		private TextView mContent;
		private TextView mLevel;
		private TextView mStatus;

		public TextView getID() {
			return mID;
		}

		public void setID(TextView mID) {
			this.mID = mID;
		}

		public TextView getTime() {
			return mTime;
		}

		public void setTime(TextView mTime) {
			this.mTime = mTime;
		}

		public TextView getGreenHouse() {
			return mGreenHouse;
		}

		public void setGreenHouse(TextView mGreenHouse) {
			this.mGreenHouse = mGreenHouse;
		}

		public TextView getContent() {
			return mContent;
		}

		public void setContent(TextView mContent) {
			this.mContent = mContent;
		}

		public TextView getLevel() {
			return mLevel;
		}

		public void setLevel(TextView mLevel) {
			this.mLevel = mLevel;
		}

		public TextView getStatus() {
			return mStatus;
		}

		public void setStatus(TextView mStatus) {
			this.mStatus = mStatus;
		}
	}
}
