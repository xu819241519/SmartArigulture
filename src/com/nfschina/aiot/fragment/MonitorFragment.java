package com.nfschina.aiot.fragment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nfschina.aiot.activity.Home;
import com.nfschina.aiot.activity.R;
import com.nfschina.aiot.constant.MyConstant;
import com.nfschina.aiot.entity.Environment;
import com.nfschina.aiot.entity.GreenHouse;
import com.nfschina.aiot.entity.Threshold;
import com.nfschina.aiot.util.DBConnectionUtil;

public class MonitorFragment extends Fragment implements OnClickListener {
	@ViewInject(R.id.core_monitor_tv)
	private TextView core_monitor_tv;
	@ViewInject(R.id.core_monitor_back)
	private TextView core_monitor_back;
	@ViewInject(R.id.environment_list_gv)
	private PullToRefreshGridView environment_list_gv;
	@ViewInject(R.id.monitor_gohome)
	private ImageButton monitor_gohome;
	//保存环境因素名称
	private String[] environmentsList ;
	//保存获取的环境实时数据
	private List<Environment> environments = new ArrayList<Environment>(); 
	private DBConnectionUtil connectionUtil;
	//温室实体
	private GreenHouse greenHouse;
	//环境实时数据实体
	private Environment environment;
	//阀值实体
	public static Threshold threshold;//static是为了fragment之间的数据传递
	public static Threshold getThreshold() {
		return threshold;
	}//为了fragment之间的数据传递
	public static void setThreshold(Threshold threshold) {
		MonitorFragment.threshold = threshold;
	}
	/*private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			environment_list_gv.setRefreshing();
		};
	};//这两段代码，可以实现界面出现在用户眼前才进行相关操作
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (isVisibleToUser) {
			handler.sendEmptyMessageDelayed(1, 300);// 300毫秒后主动加载数据
		}
		super.setUserVisibleHint(isVisibleToUser);
	}*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.core_monitor, null);
		ViewUtils.inject(this,view);
		//从Intent中获取温室信息
		Bundle bundle = getActivity().getIntent().getExtras();
		if (bundle != null) {
			greenHouse = (GreenHouse) bundle.get("greenhouse");
			core_monitor_tv.setText(greenHouse.getName());
		}
		//获取阀值
		new GetThresholdTask().execute();
		
		environmentsList = MyConstant.environmentlist;
		core_monitor_back.setOnClickListener(this);
		monitor_gohome.setOnClickListener(this);
		//以下，为PullToRefreshGridView设置
		environment_list_gv.setMode(Mode.PULL_FROM_START);//设置为只能下拉刷新
		environment_list_gv.setOnRefreshListener(new OnRefreshListener<GridView>() {
			//下拉的时候会执行
					@Override
					public void onRefresh(PullToRefreshBase<GridView> refreshView) {
						//实现下拉刷新
						new GetEnvironmentTask().execute();
					}
		});
		//首次来到页面是需要自动加载数据
		new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				environment_list_gv.setRefreshing();// 刷新加载数据
				return true;
			}
		}).sendEmptyMessageDelayed(0, 1000);// 300毫秒后主动加载数据
		return view;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.core_monitor_back:
			getActivity().finish();
			break;
		case R.id.monitor_gohome:
			Intent intent = new Intent(getActivity(), Home.class);
			startActivity(intent);
			getActivity().finish();
			break;
		}
	}
	/**
	 * 通过网络获取实时环境数据
	 * @author wujian
	 */
	private class GetEnvironmentTask extends AsyncTask<Void, Void, List<Environment>>{
		@Override
		protected List<Environment> doInBackground(Void... params) {
			try {
				//查询最新的数据
				String sql = "select environmentparaid,temperature,co2,humidity,illuminance,recordtime from environmentparatb " +
						"where greenhouseid = '"+greenHouse.getId()+"'order by recordtime desc limit 0 , 1";
				connectionUtil = new DBConnectionUtil(sql);
				ResultSet resultSet = connectionUtil.pst.executeQuery();
				while (resultSet.next()) {
					environment = new Environment(resultSet.getInt("environmentparaid"), resultSet.getFloat("temperature")/10, 
							resultSet.getInt("co2"), resultSet.getFloat("humidity"), resultSet.getInt("illuminance"), resultSet.getString("recordtime"));
					environments.add(environment);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				connectionUtil.close();
			}
			return environments;
		}
		//在主线程里面执行
		@Override
		protected void onPostExecute(List<Environment> result) {
			super.onPostExecute(result);
			if (result.size()>0) {
				//数据适配
				environment_list_gv.setAdapter(new MyEnvironmentAdapter());
				environment_list_gv.onRefreshComplete();//停止刷新
			}else {
				//当没有数据时显示的界面
				View emptyView = LayoutInflater.from(getActivity()).inflate(R.layout.nodata_view, null);
				emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));  
				((ViewGroup)environment_list_gv.getParent()).addView(emptyView);  
				environment_list_gv.setEmptyView(emptyView);
				environment_list_gv.onRefreshComplete();//停止刷新
			}
		}
	}
	/**
	 * 异步获取阀值，也就是适宜范围
	 * @author wujian
	 */
	private class GetThresholdTask extends AsyncTask<Void, Void, Threshold>{
		@Override
		protected Threshold doInBackground(Void... params) {
			//取最新的阀值，并且thresholdflag = 256
			try {
				String sql ="SELECT uptem,lowtem,upco2,lowco2,uphum,lowhum,upillum,lowillum,updatetime FROM " +
						"thresholdtb WHERE thresholdflag = 256 ORDER BY updatetime DESC LIMIT 0,1;";
				connectionUtil = new DBConnectionUtil(sql);
				ResultSet resultSet = connectionUtil.pst.executeQuery();
				while (resultSet.next()) {
					threshold = new Threshold(resultSet.getFloat("uptem"), resultSet.getFloat("lowtem"),
							resultSet.getInt("upco2"), resultSet.getInt("lowco2"), resultSet.getFloat("uphum"), resultSet.getFloat("lowhum"),
							resultSet.getInt("upillum"), resultSet.getInt("lowillum"),resultSet.getString("updatetime"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				connectionUtil.close();
			}
			return threshold;
		}
	}
	/**
	 * 为环境参数的显示创建的一个适配器
	 * @author wujian
	 */
	public class MyEnvironmentAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return environmentsList.length;
		}
		@Override
		public Object getItem(int position) {
			return environmentsList[position];
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MyHolder holder ;
			if (convertView == null) {
				holder = new MyHolder();
				convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.monitor_environment_value, null);
				ViewUtils.inject(holder, convertView);
				convertView.setTag(holder);
			}else {
				holder = (MyHolder) convertView.getTag();
			}
			holder.environment_name.setText(environmentsList[position]);
			switch (environmentsList[position]) {
			case "二氧化碳":
				holder.environment_lastvalue.setText(environment.getCo2()+"");
				holder.environment_fitvalue.setText("适宜范围："+threshold.getLowco2()+"~"+threshold.getUpco2()+"ppm");
				break;
			case "温度":
				holder.environment_lastvalue.setText(environment.getTemperature()+"");
				holder.environment_fitvalue.setText("适宜范围："+threshold.getLowtem()+"~"+threshold.getUptem()+"℃");
				break;
			case "湿度":
				holder.environment_lastvalue.setText(environment.getHumidity()+"");
				holder.environment_fitvalue.setText("适宜范围："+threshold.getLowhum()+"~"+threshold.getUphum()+"%");
				break;
			case "光照":
				holder.environment_lastvalue.setText(environment.getIlluminance()+"");
				holder.environment_fitvalue.setText("适宜范围："+threshold.getLowillum()+"~"+threshold.getUpillum()+"xl");
				break;
			}
			return convertView;
		}
	}
	/**
	 * 为了缓存控件
	 * @author wujian
	 */
	public class MyHolder {
		@ViewInject(R.id.environment_name)
		private TextView environment_name;
		@ViewInject(R.id.environment_lastvalue)
		private TextView environment_lastvalue;
		@ViewInject(R.id.environment_fitvalue)
		private TextView environment_fitvalue;
	}
}
