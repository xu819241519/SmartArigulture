package com.nfschina.aiot.activity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.entity.GreenHouse;
import com.nfschina.aiot.util.DBPro;

/**
 * 显示所有的温室名
 * @author wujian
 */
public class AllGreenHouseActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.greenhouse_list_back)
	private TextView greenhouse_list_back;
	@ViewInject(R.id.greenhouse_list_home)
	private TextView greenhouse_list_home;
	@ViewInject(R.id.greenhouse_list_gv)
	private PullToRefreshGridView greenhouse_list_gv;
	//存放温室
	private List<GreenHouse> greenHouses = new ArrayList<GreenHouse>();
	private int page,size = 15;
	private DBPro dbPro ;//数据库存储过程
	private MyGreenHouseAdapter greenHouseAdapter;
	MyHolder holder = null;
	
	//private Intent intent ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.greenhouse_list);
		ViewUtils.inject(this);
		greenHouseAdapter = new MyGreenHouseAdapter();
		greenhouse_list_back.setOnClickListener(this);
		greenhouse_list_home.setOnClickListener(this);
		//设置表格的模式：下拉刷新，上拉加载更多
		greenhouse_list_gv.setMode(Mode.BOTH);
		greenhouse_list_gv.setScrollingWhileRefreshingEnabled(false);
		greenhouse_list_gv.setOnRefreshListener(new OnRefreshListener<GridView>() {
			@Override
			public void onRefresh(PullToRefreshBase<GridView> refreshView) {
				//实现下拉刷新，上拉加载更多的方法
				loadDatas(greenhouse_list_gv.getScrollY()<0);
			}
		});
		// 首次来到页面是需要自动加载数据
		new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				greenhouse_list_gv.setRefreshing();// 刷新加载数据
				return true;
			}
		}).sendEmptyMessageDelayed(0, 1000);// 1000毫秒后主动加载数据,这个地方最好设置时间稍长一点，不然还没取到数据
	}
	/**
	 * 实现下拉刷新、下拉加载更多的方法
	 * @param isRefresh
	 */
	protected void loadDatas(boolean isRefresh) {
		if (isRefresh) {
			page = 0;//下拉刷新，如果是刷新，就把页面设为第一个
			greenHouses.clear();
			new GetGreenHouseInfoTask(page,size).execute();
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					greenhouse_list_gv.setMode(Mode.BOTH);
				}
			});
		}else {
			page++;//上拉加载更多，加载下一个页面
			new GetGreenHouseInfoTask(page,size).execute();
			greenHouseAdapter.notifyDataSetChanged();
		}
	}
	//处理点击事件
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.greenhouse_list_back:
			finish();
			break;
		case R.id.greenhouse_list_home:
			finish();
			break;
		}
	}
	/**
	 * 通过存储过程实现获取温室的信息：温室id,温室名称，以及温室的报警信息情况
	 * @author wujian
	 */
	private class GetGreenHouseInfoTask extends AsyncTask<Void, Void, List<GreenHouse>>{
		private int page;
		private int size;
		public GetGreenHouseInfoTask(int page,int size){
			this.page = page;
			this.size = size;
		}
		@Override
		protected List<GreenHouse> doInBackground(Void... params) {
			try {
				int count = 0;
				String sql = "call getAllGreenHouseInfo('"+Constant.CURRENT_USER+"','"+page*size+"','"+size+"')";
				dbPro = new DBPro(sql);
				ResultSet resultSet = dbPro.cs.getResultSet();
				while (resultSet != null && resultSet.next()) {
					GreenHouse greenHouse = new GreenHouse(resultSet.getString(1), resultSet.getString(2), resultSet.getInt(3));
					greenHouses.add(greenHouse);
					count++;
				}
				if (dbPro.cs.getMoreResults()) {//下一个结果集
					resultSet = dbPro.cs.getResultSet();
					while (resultSet.next()) {
						GreenHouse greenHouse = new GreenHouse(resultSet.getString(1), resultSet.getString(2), 0);
						greenHouses.add(greenHouse);
						count++;
					}
				}
				if (count < size || count == 0) {
					//如果这次加载的数据量小于size,或者没有加载到数据
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							greenhouse_list_gv.setMode(Mode.PULL_FROM_START);
							Toast.makeText(getApplicationContext(), "已加载全部", Toast.LENGTH_LONG).show();
						}
					});
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				//各种关闭
				dbPro.close();
			}
			return greenHouses;
		}
		@Override
		protected void onPostExecute(List<GreenHouse> result) {
			super.onPostExecute(result);
			if (result.size() > 0) {
				// 数据适配
				greenhouse_list_gv.setAdapter(greenHouseAdapter);
				greenhouse_list_gv.onRefreshComplete();
			}else {
				//当没有数据时显示的界面
				View emptyView = LayoutInflater.from(getApplicationContext()).inflate(
						R.layout.nodata_view, null);
				emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT));
				((ViewGroup) greenhouse_list_gv.getParent()).addView(emptyView);
				greenhouse_list_gv.setEmptyView(emptyView);
				greenhouse_list_gv.onRefreshComplete();
			}
		}
	}
	/**
	 * 从服务端获取的所有温室名称，进行适配显示
	 * @author wujian
	 */
	public class MyGreenHouseAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return greenHouses.size();
		}
		@Override
		public Object getItem(int position) {
			return greenHouses.get(position).getName();
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			//if (convertView == null) {加上这句，在滑动页面的时候容易出现错误
				holder = new MyHolder();
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.greenhouse_list_item, null);
				ViewUtils.inject(holder, convertView);
				//convertView.setTag(holder);
			//} else {
				//holder = (MyHolder) convertView.getTag();
			//}
			holder.greenhouse_item_name.setText(greenHouses.get(position).getName());
			//显示报警信息
			if (greenHouses.get(position).getAlarminfoNum()>0) {
				holder.greenhouse_item_alarminfo_num.setVisibility(View.VISIBLE);
				holder.greenhouse_item_state.setImageResource(R.drawable.gh_state_alarm);
				holder.greenhouse_item_alarminfo_num.setText(greenHouses.get(position).getAlarminfoNum()+"");
			}else {
				holder.greenhouse_item_state.setImageResource(R.drawable.gh_state_ok);
			}
			// 点击事件
			holder.greenhouse_item_ll.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//点击开启一个意图，也就是activity
					Intent intent = new Intent(getApplicationContext(),CoreActivity.class);
					//往下一个activity传递数据
					intent.putExtra("greenhouse", greenHouses.get(position));
					startActivity(intent);
				}
			});
			return convertView;
		}
	}
	/**
	 * 缓存view
	 * @author wujian
	 */
	public class MyHolder {
		@ViewInject(R.id.greenhouse_item_ll)
		private LinearLayout greenhouse_item_ll;
		@ViewInject(R.id.greenhouse_item_name)
		private TextView greenhouse_item_name;
		@ViewInject(R.id.greenhouse_item_state)
		private ImageView greenhouse_item_state;
		@ViewInject(R.id.greenhouse_item_alarminfo_num)
		private TextView greenhouse_item_alarminfo_num;
	}
	
}
