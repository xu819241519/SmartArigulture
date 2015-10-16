package com.nfschina.aiot.adapter;

import com.nfschina.aiot.R;
import com.nfschina.aiot.activity.History;
import com.nfschina.aiot.constant.Constant;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * À˘”–¥Û≈Ô  ≈‰∆˜
 * @author xu
 *
 */

public class AllGreenAdapter extends BaseAdapter {

	private Activity mActivity;
	public AllGreenAdapter(Activity activity) {
		mActivity = activity;
	}

	@Override
	public int getCount() {
		return Constant.GreenHouseName.size();
	}

	@Override
	public Object getItem(int position) {
		return Constant.GreenHouseName.get(position);
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
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.greenhouse_list_item, null);
			holder.setmTextView((TextView) convertView.findViewById(R.id.greenhouse_item_name));
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.getmTextView().setText(Integer.toString(Constant.GreenHouseName.get(position)));
		holder.getmTextView().setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mActivity,History.class);
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

}
