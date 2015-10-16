package com.nfschina.aiot.adapter;

import java.util.ArrayList;
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

/**
 * 指令记录适配器
 * @author xu
 *
 */

public class InstructionsAdapter extends BaseAdapter {

	//指令记录的list
	private List<InstructionEntity> mList;
	
	
	public InstructionsAdapter() {
		super();
		mList = new ArrayList<InstructionEntity>();
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
			convertView = LinearLayout.inflate(parent.getContext(), R.layout.instructions_history_item, null);

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
		
		if (position % 2 == 0) {
			convertView.setBackgroundColor(parent.getResources().getColor(R.color.table_back_1));
		} else {
			convertView.setBackgroundColor(parent.getResources().getColor(R.color.table_back_2));
		}

		return convertView;
	}

	public class Holder {
		//指令ID
		private TextView mID;
		//指令发送时间
		private TextView mSendTime;
		//指令执行时间
		private TextView mPerformTime;
		//发出用户
		private TextView mUser;
		//温室ID
		private TextView mGreenHouse;
		//指令内容
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
	 * 添加指令记录数据
	 * 
	 * @param alarmEntities 指令记录实体list
	 * @return 成功添加返回true，否则false
	 */
	public boolean addData(List<InstructionEntity> alarmEntities) {
		if (mList != null) {
			return mList.addAll(alarmEntities);
		} else
			return false;
	}

	/**
	 * 清空list
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
