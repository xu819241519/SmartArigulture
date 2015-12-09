package com.nfschina.aiot.adapter;

import java.util.ArrayList;
import java.util.List;

import com.nfschina.aiot.activity.History;
import com.nfschina.aiot.activity.R;
import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.entity.GreenHouseEntity;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * À˘”–¥Û≈Ô  ≈‰∆˜
 * 
 * @author xu
 *
 */

public class AllGreenAdapter extends BaseAdapter {

	private List<GreenHouseEntity> mEntities;

	private Activity mActivity;

	public AllGreenAdapter(Activity activity) {
		mActivity = activity;
		mEntities = new ArrayList<GreenHouseEntity>();
	}

	@Override
	public int getCount() {
		return mEntities.size();
	}

	@Override
	public Object getItem(int position) {
		return mEntities.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.greenhouse_history_list_item, null);
			holder.setmTextView((TextView) convertView.findViewById(R.id.greenhouse_item_name));
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.getmTextView().setText(mEntities.get(position).getName());
		LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.greenhouse_item);
		final int pos = position;
		layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mActivity, History.class);
				intent.putExtra(Constant.INTENT_EXTRA_HISTORY_HOUSE_ID, mEntities.get(pos).getID());
				mActivity.startActivity(intent);
			}
		});
		return convertView;
	}

	public class Holder {
		private TextView mTextView;

		public TextView getmTextView() {
			return mTextView;
		}

		public void setmTextView(TextView mTextView) {
			this.mTextView = mTextView;
		}

	}

	public void clearOldData() {
		if (mEntities != null && mEntities.size() != 0)
			mEntities.clear();
	}

	public void addGreenHouse(GreenHouseEntity entity) {
		mEntities.add(entity);
	}

}
