package com.nfschina.aiot.adapter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.nfschina.aiot.R;
import com.nfschina.aiot.entity.AlarmEntity;

import android.R.integer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AlarmAdapter extends BaseAdapter {

	// the list of the alarm history
	private List<AlarmEntity> mList;

	public AlarmAdapter() {
		super();
		mList = new ArrayList<AlarmEntity>();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
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
		
		holder.getContent().setText(mList.get(position).getContent());
		holder.getGreenHouse().setText(Integer.toString(mList.get(position).getGreenHouseID()));
		holder.getID().setText(Integer.toString(mList.get(position).getID()));
		holder.getLevel().setText(mList.get(position).getLevel());
		holder.getStatus().setText(mList.get(position).getState());
		holder.getTime().setText(((Timestamp) mList.get(position).getTime()).toString());

		if (position % 2 == 0) {
			convertView.setBackgroundColor(parent.getResources().getColor(R.color.table_back_1));
		} else {
			convertView.setBackgroundColor(parent.getResources().getColor(R.color.table_back_2));
		}
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

	/**
	 * add data to the list
	 * 
	 * @param alarmEntities
	 *            the list of the AlarmEntity
	 * @return true if added,or false
	 */
	public boolean addData(List<AlarmEntity> alarmEntities) {
		if (mList != null) {
			return mList.addAll(alarmEntities);
		} else
			return false;
	}

	/**
	 * clear the list
	 * 
	 * @return true if the list is clear,or false
	 */
	public boolean clearData() {
		if (mList != null && mList.isEmpty() == false) {
			mList.clear();
			return true;
		}
		return false;
	}
}
