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
 * ��ʾ���е�������
 * @author wujian
 */
public class AllGreenHouseActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.greenhouse_list_back)
	private TextView greenhouse_list_back;
	@ViewInject(R.id.greenhouse_list_home)
	private TextView greenhouse_list_home;
	@ViewInject(R.id.greenhouse_list_gv)
	private PullToRefreshGridView greenhouse_list_gv;
	//�������
	private List<GreenHouse> greenHouses = new ArrayList<GreenHouse>();
	private int page,size = 15;
	private DBPro dbPro ;//���ݿ�洢����
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
		//���ñ���ģʽ������ˢ�£��������ظ���
		greenhouse_list_gv.setMode(Mode.BOTH);
		greenhouse_list_gv.setScrollingWhileRefreshingEnabled(false);
		greenhouse_list_gv.setOnRefreshListener(new OnRefreshListener<GridView>() {
			@Override
			public void onRefresh(PullToRefreshBase<GridView> refreshView) {
				//ʵ������ˢ�£��������ظ���ķ���
				loadDatas(greenhouse_list_gv.getScrollY()<0);
			}
		});
		// �״�����ҳ������Ҫ�Զ���������
		new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				greenhouse_list_gv.setRefreshing();// ˢ�¼�������
				return true;
			}
		}).sendEmptyMessageDelayed(0, 1000);// 1000�����������������,����ط��������ʱ���Գ�һ�㣬��Ȼ��ûȡ������
	}
	/**
	 * ʵ������ˢ�¡��������ظ���ķ���
	 * @param isRefresh
	 */
	protected void loadDatas(boolean isRefresh) {
		if (isRefresh) {
			page = 0;//����ˢ�£������ˢ�£��Ͱ�ҳ����Ϊ��һ��
			greenHouses.clear();
			new GetGreenHouseInfoTask(page,size).execute();
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					greenhouse_list_gv.setMode(Mode.BOTH);
				}
			});
		}else {
			page++;//�������ظ��࣬������һ��ҳ��
			new GetGreenHouseInfoTask(page,size).execute();
			greenHouseAdapter.notifyDataSetChanged();
		}
	}
	//�������¼�
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
	 * ͨ���洢����ʵ�ֻ�ȡ���ҵ���Ϣ������id,�������ƣ��Լ����ҵı�����Ϣ���
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
				if (dbPro.cs.getMoreResults()) {//��һ�������
					resultSet = dbPro.cs.getResultSet();
					while (resultSet.next()) {
						GreenHouse greenHouse = new GreenHouse(resultSet.getString(1), resultSet.getString(2), 0);
						greenHouses.add(greenHouse);
						count++;
					}
				}
				if (count < size || count == 0) {
					//�����μ��ص�������С��size,����û�м��ص�����
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							greenhouse_list_gv.setMode(Mode.PULL_FROM_START);
							Toast.makeText(getApplicationContext(), "�Ѽ���ȫ��", Toast.LENGTH_LONG).show();
						}
					});
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				//���ֹر�
				dbPro.close();
			}
			return greenHouses;
		}
		@Override
		protected void onPostExecute(List<GreenHouse> result) {
			super.onPostExecute(result);
			if (result.size() > 0) {
				// ��������
				greenhouse_list_gv.setAdapter(greenHouseAdapter);
				greenhouse_list_gv.onRefreshComplete();
			}else {
				//��û������ʱ��ʾ�Ľ���
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
	 * �ӷ���˻�ȡ�������������ƣ�����������ʾ
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
			//if (convertView == null) {������䣬�ڻ���ҳ���ʱ�����׳��ִ���
				holder = new MyHolder();
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.greenhouse_list_item, null);
				ViewUtils.inject(holder, convertView);
				//convertView.setTag(holder);
			//} else {
				//holder = (MyHolder) convertView.getTag();
			//}
			holder.greenhouse_item_name.setText(greenHouses.get(position).getName());
			//��ʾ������Ϣ
			if (greenHouses.get(position).getAlarminfoNum()>0) {
				holder.greenhouse_item_alarminfo_num.setVisibility(View.VISIBLE);
				holder.greenhouse_item_state.setImageResource(R.drawable.gh_state_alarm);
				holder.greenhouse_item_alarminfo_num.setText(greenHouses.get(position).getAlarminfoNum()+"");
			}else {
				holder.greenhouse_item_state.setImageResource(R.drawable.gh_state_ok);
			}
			// ����¼�
			holder.greenhouse_item_ll.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//�������һ����ͼ��Ҳ����activity
					Intent intent = new Intent(getApplicationContext(),CoreActivity.class);
					//����һ��activity��������
					intent.putExtra("greenhouse", greenHouses.get(position));
					startActivity(intent);
				}
			});
			return convertView;
		}
	}
	/**
	 * ����view
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
