package com.nfschina.aiot.adapter;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.nfschina.aiot.R;
import com.nfschina.aiot.constant.ConstantFun;
import com.nfschina.aiot.entity.AlarmEntity;

import android.R.integer;
import android.provider.ContactsContract.Contacts.Data;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 报警记录适配器
 * 
 * @author xu
 *
 */
public class AlarmAdapter extends BaseAdapter {

	// 报警记录实体的list
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
		holder.getTime().setText(ConstantFun.formatTimeString(mList.get(position).getTime(),true));

//		if (position % 2 == 0) {
//			convertView.setBackgroundColor(parent.getResources().getColor(R.color.table_back_1));
//		} else {
//			convertView.setBackgroundColor(parent.getResources().getColor(R.color.table_back_2));
//		}
		return convertView;
	}

	public class Holder {
		// 报警ID
		private TextView mID;
		// 报警时间
		private TextView mTime;
		// 温室ID
		private TextView mGreenHouse;
		// 报警内容
		private TextView mContent;
		// 报警等级
		private TextView mLevel;
		// 状态
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
	 * 添加报警记录实体
	 * 
	 * @param alarmEntities
	 *            报警记录的list
	 * @return 成功添加返回true，否则false
	 */
	public boolean addData(List<AlarmEntity> alarmEntities) {
		if (mList != null) {
			return mList.addAll(alarmEntities);
		} else
			return false;
	}

	/**
	 * 清空报警记录list
	 * 
	 * @return 清空成功返回true，否则返回false
	 */
	public boolean clearData() {
		if (mList != null && mList.isEmpty() == false) {
			mList.clear();
			return true;
		}
		return false;
	}
}
