package com.nfschina.aiot.adapter;

import java.util.ArrayList;
import java.util.List;

import com.nfschina.aiot.activity.R;
import com.nfschina.aiot.entity.NewsListEntity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 新闻适配器
 * 
 * @author xu
 *
 */
public class NewsAdapter extends BaseAdapter {

	List<NewsListEntity> mNewsListEntity;

	public NewsAdapter() {
		mNewsListEntity = new ArrayList<NewsListEntity>();
	}

	@Override
	public int getCount() {
		return mNewsListEntity.size();
	}

	@Override
	public Object getItem(int position) {
		return mNewsListEntity.get(position);
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

			holder.getTime().setText(mNewsListEntity.get(position).getTime());
			holder.getTitle().setText(mNewsListEntity.get(position).getTitle());
			//System.out.println(mNewsListEntity.get(position).getTitle() + "-------->" + mNewsListEntity.get(position).getTitle().length() );
			if(mNewsListEntity.get(position).getTitle().length() > 30){
				holder.getTitle().setText(mNewsListEntity.get(position).getTitle().substring(0,30) + "......");
			}
		}

		// if (position % 2 == 0) {
		// convertView.setBackgroundColor(parent.getResources().getColor(R.color.table_back_1));
		// } else {
		// convertView.setBackgroundColor(parent.getResources().getColor(R.color.table_back_2));
		// }
		return convertView;
	}

	public class Holder {
		// 新闻标题
		private TextView mTitle;
		// 新闻时间
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

	/**
	 * 添加新闻列表项
	 * 
	 * @param newsListEntities
	 *            新闻列表项
	 */
	public void addData(List<NewsListEntity> newsListEntities) {
		mNewsListEntity.addAll(newsListEntities);
		notifyDataSetChanged();
	}

	/**
	 * 删除新闻列表项
	 * 
	 */
	public void deleteItem(NewsListEntity newsListEntity) {
		for (int i = 0; i < mNewsListEntity.size(); ++i) {
			NewsListEntity entity = mNewsListEntity.get(i);
			if (entity.getTitle().equals(newsListEntity.getTitle())) {
				mNewsListEntity.remove(i);
				notifyDataSetChanged();
				break;
			}
		}
	}
}
