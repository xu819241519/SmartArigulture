package com.nfschina.aiot.adapter;

import java.util.List;

import com.nfschina.aiot.R;
import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.entity.AlarmEntity;
import com.nfschina.aiot.entity.InstructionEntity;

import android.R.raw;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InstructionsAdapter extends BaseAdapter {

	//the list of the instruction history
	private List<InstructionEntity> mList;
	
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
			convertView = LinearLayout.inflate(parent.getContext(), R.layout.instructions_history_item, null);
			if (position % 2 == 0) {
				convertView.setBackgroundColor(parent.getResources().getColor(R.color.table_back_1));
			} else {
				convertView.setBackgroundColor(parent.getResources().getColor(R.color.table_back_2));
			}

			holder.setID((TextView) convertView.findViewById(R.id.instructions_history_id));
			holder.setSendTime((TextView) convertView.findViewById(R.id.instructions_history_sendtime));
			holder.setPerformTime((TextView) convertView.findViewById(R.id.instructions_history_performtime));
			holder.setUser((TextView) convertView.findViewById(R.id.instructions_history_user));
			holder.setGreenHouse((TextView) convertView.findViewById(R.id.instructions_history_greenhouse));
			holder.setContent((TextView) convertView.findViewById(R.id.instructions_history_content));
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		if (holder != null) {
			holder.getID().setText(Integer.toString(mList.get(position).getID()));
			holder.getGreenHouse().setText(Integer.toString(mList.get(position).getGreenHouseID()));
			holder.getContent().setText(mList.get(position).getContent());
			holder.getPerformTime().setText(mList.get(position).getRunTime().toString());
			holder.getSendTime().setText(mList.get(position).getSendTime().toString());
			holder.getUser().setText(Integer.toString(mList.get(position).getUserID()));
		}

		return convertView;
	}

	public class Holder {
		private TextView mID;
		private TextView mSendTime;
		private TextView mPerformTime;
		private TextView mUser;
		private TextView mGreenHouse;
		private TextView mContent;

		public TextView getID() {
			return mID;
		}

		public void setID(TextView mID) {
			this.mID = mID;
		}

		public TextView getSendTime() {
			return mSendTime;
		}

		public void setSendTime(TextView mSendTime) {
			this.mSendTime = mSendTime;
		}

		public TextView getPerformTime() {
			return mPerformTime;
		}

		public void setPerformTime(TextView mPerformTime) {
			this.mPerformTime = mPerformTime;
		}

		public TextView getUser() {
			return mUser;
		}

		public void setUser(TextView mUser) {
			this.mUser = mUser;
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
	}
	
	
	/**
	 * add data to the list
	 * 
	 * @param alarmEntities
	 *            the list of the InstructionEntity
	 * @return true if added,or false
	 */
	public boolean addData(List<InstructionEntity> alarmEntities) {
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
