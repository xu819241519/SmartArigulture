package com.nfschina.aiot.fragment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nfschina.aiot.activity.Home;
import com.nfschina.aiot.activity.R;
import com.nfschina.aiot.animation.ExpandAnimation;
import com.nfschina.aiot.entity.GreenHouse;
import com.nfschina.aiot.entity.WarningInfo;
import com.nfschina.aiot.util.DBConnectionUtil;
/**
 * 报警信息的fragment
 * @author wujian
 */
public class InfoFragment extends Fragment implements OnClickListener {
	@ViewInject(R.id.core_info_lv)
	private PullToRefreshListView core_info_lv;
	@ViewInject(R.id.info_back)
	private ImageButton info_back;
	@ViewInject(R.id.info_gohome)
	private ImageButton info_gohome;
	private TextView pop_ignore_tv, pop_handler_tv;
	private MyInfoListAdapter infoListAdapter;
	private GreenHouse greenHouse;//当前温室实体
	private List<WarningInfo> warningInfos = new ArrayList<WarningInfo>();//保存报警信息
	private PopupWindow popupWindow;//长按弹出的对话框
	private LayoutInflater inflater = null;
	MyHolder holder;
	private int page,size = 10;
	private boolean isHavaInfo = false;
	//private InfoListener infoListener;
	private DBConnectionUtil connectionUtil ;
	//将时间字符串先转化为format_ori格式的Date，然后再格式化为format的字符串
	SimpleDateFormat format_ori = new SimpleDateFormat("yyyyMMddhhmmss");
	SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Date date = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.core_info, null);
		ViewUtils.inject(this, view);
		// 以下为了获取当前在哪个温室
		Intent intent = getActivity().getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			greenHouse = (GreenHouse) bundle.get("greenhouse");
		}
		info_back.setOnClickListener(this);
		info_gohome.setOnClickListener(this);
		core_info_lv.setMode(Mode.BOTH);
		//core_info_lv.setOnItemClickListener(this);//可以直接将listview条目的点击事件直接用在适配器中的convertView
		core_info_lv.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				loadDatas(core_info_lv.getScrollY()<0);
			}
		});
		// 首次来到页面是需要自动加载数据
		new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				core_info_lv.setRefreshing();// 刷新加载数据
				return true;
			}
		}).sendEmptyMessageDelayed(0, 1000);
		return view;
	}
	/**
	 * 实现下拉刷新、下拉加载更多的方法
	 * @param isRefresh
	 */
	protected void loadDatas(boolean isRefresh) {
		if (isRefresh) {
			page = 0;//下拉刷新，如果是刷新，就把页面设为第一个
			warningInfos.clear();
			new GetAlarmInfoByIdTask(page,size).execute();
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					core_info_lv.setMode(Mode.BOTH);
				}
			});
		}else {
			page++;//上拉加载更多，加载下一个页面
			new GetAlarmInfoByIdTask(page,size).execute();
			core_info_lv.setAdapter(infoListAdapter);
			//infoListAdapter.notifyDataSetChanged();
		}
	}
	/**
	 * 根据当前温室id获取报警信息
	 * @author wujian
	 */
	private class GetAlarmInfoByIdTask extends AsyncTask<Void, Void, List<WarningInfo>>{
		private int page;
		private int size;
		public GetAlarmInfoByIdTask(int page,int size){
			this.page = page;
			this.size = size;
		}
		@Override
		protected List<WarningInfo> doInBackground(Void... params) {
			try {
				int count = 0;
				//根据温室ID以及报警信息状态
				String sql = "select * from warningtb where warningstate = 0 and greenhouseid = '"+greenHouse.getId()+"' limit "+page*size+" , "+size+"";
				connectionUtil = new DBConnectionUtil(sql);
				ResultSet resultSet = connectionUtil.pst.executeQuery();
				if (resultSet != null) {
					warningInfos = new ArrayList<WarningInfo>();
					isHavaInfo = true;
					while (resultSet.next()) {
						WarningInfo warningInfo = new WarningInfo(resultSet.getInt("Warningid"), resultSet.getString("greenhouseid"), 
								resultSet.getFloat("temperature"), resultSet.getInt("co2"), resultSet.getFloat("humidity"),
								resultSet.getInt("illuminance"), resultSet.getString("warningtime"), resultSet.getInt("warningstate"));
						warningInfos.add(warningInfo);
						count++;
					}
				}
				if (count < size || count == 0) {
					//如果这次加载的数据量小于size,或者没有加载到数据
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							core_info_lv.setMode(Mode.PULL_FROM_START);
							Toast.makeText(getActivity(), "已加载全部", Toast.LENGTH_LONG).show();
						}
					});
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				connectionUtil.close();
			}
			return warningInfos;
		}
		@Override
		protected void onPostExecute(List<WarningInfo> result) {
			super.onPostExecute(result);
			if (result != null && result.size() > 0) {
				//数据适配
				infoListAdapter = new MyInfoListAdapter();
				core_info_lv.setAdapter(infoListAdapter);
				//停止刷新
				core_info_lv.onRefreshComplete();
				//就是给activity发送消息。
				//infoListener.alarmShow(isHavaInfo);
			}
			else {
				//当没有数据时显示的界面
				View emptyView = LayoutInflater.from(getActivity()).inflate(R.layout.nodata_view, null);
				emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));  
				((ViewGroup)core_info_lv.getParent()).addView(emptyView);  
				core_info_lv.setEmptyView(emptyView);
				core_info_lv.onRefreshComplete();//停止刷新
			}
		}
	}
	/**
	 * 适配报警信息的适配器
	 * @author wujian
	 */
	public class MyInfoListAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return warningInfos.size();
		}
		@Override
		public Object getItem(int position) {
			return warningInfos.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(final int position,  View convertView, ViewGroup parent) {
			//if (convertView == null) {
				holder = new MyHolder();
				convertView = LayoutInflater.from(parent.getContext()).inflate(
						R.layout.alarminfo_item, null);
				ViewUtils.inject(holder, convertView);
//				convertView.setTag(holder);
//			} else {
//				holder = (MyHolder) convertView.getTag();
			//}
			holder.alarminfo_greenhouse_name.setText(greenHouse.getName());
			//用于生成报警信息内容
			String infoContent = "";
			int infoCount = 1;
			if (warningInfos.get(position).getCo2() < 0) {
				infoContent += (infoCount++)+"、CO2低于最小阀值"+(-warningInfos.get(position).getCo2())+"ppm;\n";
			}else if (warningInfos.get(position).getCo2() > 0) {
				infoContent +=(infoCount++)+"、CO2高于最大阀值"+warningInfos.get(position).getCo2()+"ppm;\n";
			}
			if (warningInfos.get(position).getTemperature() < 0) {
				infoContent +=(infoCount++)+"、温度低于最小阀值"+(-warningInfos.get(position).getTemperature())+"℃;\n";
			}else if (warningInfos.get(position).getTemperature() > 0) {
				infoContent +=(infoCount++)+"、温度高于最大阀值"+warningInfos.get(position).getTemperature()+"℃;\n";
			}
			if (warningInfos.get(position).getHumidity() < 0 ) {
				infoContent +=(infoCount++)+"、湿度低于最小阀值"+(-warningInfos.get(position).getHumidity())+"%;\n";
			}else if(warningInfos.get(position).getHumidity() > 0 ){
				infoContent +=(infoCount++)+"、湿度高于最大阀值"+warningInfos.get(position).getHumidity()+"%;\n";
			}
			if (warningInfos.get(position).getIlluminance() < 0) {
				infoContent +=(infoCount++)+"、光照低于最小阀值"+(-warningInfos.get(position).getIlluminance())+"xl;\n";
			}else if (warningInfos.get(position).getIlluminance() > 0) {
				infoContent +=(infoCount++)+"、光照高于最大阀值"+warningInfos.get(position).getIlluminance()+"xl;\n";
			}
			
			holder.alarminfo_item_content.setText(infoContent);
			try {//生成格式化的时间
				date = format_ori.parse(warningInfos.get(position).getWarningtime());
				String formatTime = format.format(date);
				holder.alarminfo_item_time.setText(formatTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			//报警信息级别的生成
			String infoLevel = getWarningLevel();
			switch (infoLevel) {
			case "需要注意":
				holder.alarminfo_item_level.setBackgroundColor(getResources().getColor(R.color.green_light));
				holder.alarminfo_item_level.setText(infoLevel);
				break;
			case "立即处理":
				holder.alarminfo_item_level.setBackgroundColor(getResources().getColor(R.color.orange));
				holder.alarminfo_item_level.setText(infoLevel);
				break;
			case "可以忽略":
				holder.alarminfo_item_level.setBackgroundColor(getResources().getColor(R.color.gray));
				holder.alarminfo_item_level.setText(infoLevel);
				break;
			}
			//为了点击事件
			View alarminfo_item_ll = (LinearLayout) convertView.findViewById(R.id.alarminfo_item_ll);
			alarminfo_item_ll.measure(0, 0);
			((LinearLayout.LayoutParams)alarminfo_item_ll.getLayoutParams()).bottomMargin = (-1)*alarminfo_item_ll.getMeasuredHeight();
			alarminfo_item_ll.setVisibility(View.GONE);
			//直接将点击事件注册在convertView
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					upAndDownAnimation(v,position);
				}
			});
			//暂时去掉下面功能
			//直接将点击事件注册在convertView（在PullToRefreshListView没有onItemLongClick这个方法的时候）
//			convertView.setOnLongClickListener(new OnLongClickListener() {
//				@Override
//				public boolean onLongClick(View v) {
//					inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//					initPopupWindow(inflater);
//					showPop(v);
//					pop_ignore_tv.setOnClickListener(new TextViewOnClick(position));
//					pop_handler_tv.setOnClickListener(new TextViewOnClick(position));
//					return true;//返回true让OnClickListener不再执行
//				}
//			});
			return convertView;
		}
		
	}
	/**
	 * 暂时忽略  和  立即处理的点击事件
	 * @author My
	 */
	private class TextViewOnClick implements OnClickListener{
		private int position;
		
		public TextViewOnClick(int position){
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.pop_ignore_tv://暂时忽略
				//执行忽略操作
				new IgnoreWarningTask(position).execute();
				//关掉
				if (popupWindow != null) {
					popupWindow.dismiss();
				}
				Toast.makeText(getActivity(), "已经忽略当前报警信息", 1).show();
				//这个时候数据库中的数据已经发生改变，需要直接显示给用户，刷新
				core_info_lv.setRefreshing();
				break;

			case R.id.pop_handler_tv://立刻处理
				
				break;
			}
		}
	}
	/**
	 * 忽略报警信息或者将报警信息置为已处理
	 * @author wujian
	 */
	public class IgnoreWarningTask extends AsyncTask<Integer, Void, Void>{
		private int position;
		public IgnoreWarningTask(int position){
			this.position = position;
		}
		@Override
		protected Void doInBackground(Integer... params) {
			try {
				//更新值
				String sql = "update warningtb set warningstate = 1 where warningid = '"+warningInfos.get(position).getId()+"'";
				connectionUtil = new DBConnectionUtil(sql);
				connectionUtil.pst.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				connectionUtil.close();
			}
			return null;
		}
	}
	/**
	 * 初始化Popupwindow
	 * @param inflater
	 */
	private void initPopupWindow(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.pop_item_layout, null);
		popupWindow = new PopupWindow(view, 350, 100);
		pop_ignore_tv = (TextView) view.findViewById(R.id.pop_ignore_tv);
		pop_handler_tv = (TextView) view.findViewById(R.id.pop_handler_tv);
	}
	/**
	 * Popupwindow显示
	 * @param v
	 */
	@SuppressWarnings("deprecation")
	private void showPop(View v) {
		popupWindow.setFocusable(false);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());// 设置此项可点击Popupwindow外区域消失，注释则不消失
		// 设置出现位置
		int[] location = new int[2];
		v.getLocationOnScreen(location);
		popupWindow.showAtLocation(v, Gravity.NO_GRAVITY,
				location[0] + v.getWidth() / 2 - popupWindow.getWidth() / 2,
				location[1] - popupWindow.getHeight());
	}
	/**
	 * 为了缓控件
	 * @author wujian
	 */
	private final class MyHolder {
		@ViewInject(R.id.alarminfo_greenhouse_name)
		private TextView alarminfo_greenhouse_name;
		@ViewInject(R.id.alarminfo_item_content)
		private  TextView alarminfo_item_content;
		@ViewInject(R.id.alarminfo_item_time)
		private TextView alarminfo_item_time;
		@ViewInject(R.id.alarminfo_item_level)
		private  TextView alarminfo_item_level;
	}
	/**
	 * 实现上下箭头转换的动画
	 * @param v
	 */
	public void upAndDownAnimation(View v,int position){
		//为了确保第一次查看报警信息将报警信息状态改变
		TextView isFirstClick_other = (TextView) v.findViewById(R.id.isFirstClick_other);
		System.out.println(isFirstClick_other.getText());
		if (isFirstClick_other.getText().equals("true")) {
			new IgnoreWarningTask(position).execute();
			isFirstClick_other.setText("false");
		}
		//为了条目中的箭头旋转的效果
		TextView isFirstClick = (TextView) v.findViewById(R.id.isFirstClick);
		//用来判断是否是第一次点击条目
		boolean firstClick_tv = isFirstClick.getText().toString().equals("true");
		ImageView alarminfo_item_upordown_iv = (ImageView) v.findViewById(R.id.alarminfo_item_upordown_iv);
		if (firstClick_tv) {	//判断是不是第一次点击listview  item
			//为图标审定一个动画
			Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.alarminfo_updown_iv);
			alarminfo_item_upordown_iv.startAnimation(animation);
			animation.setFillAfter(true);//保持旋转后的样子
			isFirstClick.setText("false");//表示第一次已经点击完毕
		} else if (!firstClick_tv) {
			Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.alarminfo_downup_iv);
			alarminfo_item_upordown_iv.startAnimation(animation);
			animation.setFillAfter(true);
			isFirstClick.setText("true");
		}
		//下面是点击条目展开的动画效果
		LinearLayout alarminfo_item_ll = (LinearLayout) v.findViewById(R.id.alarminfo_item_ll);
		ExpandAnimation expandAni = new ExpandAnimation(alarminfo_item_ll, 500);
		alarminfo_item_ll.startAnimation(expandAni);
	}
	/**
	 * 随机获取报警信息级别
	 * @return
	 */
	public String getWarningLevel(){
		String level = "";
		int random = (int)(3 * Math.random())+1;
		switch (random) {
		case 1:
			level =  "需要注意";
			break;
		case 2:
			level =  "立即处理";
			break;
		case 3:
			level = "可以忽略";
			break;
		}
		return level;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.info_back:
			getActivity().finish();
			break;

		case R.id.info_gohome:
			Intent intent = new Intent(getActivity(), Home.class);
			startActivity(intent);
			getActivity().finish();
			break;
		}
		
	}
//	/**
//	 * 和activity进行通信的接口
//	 * @author wujian'
//	 */
//	public interface InfoListener{
//		public void alarmShow(boolean isHavaInfo);
//	}
//	@Override
//	public void onAttach(Activity activity) {
//		super.onAttach(activity);
//		infoListener = (InfoListener) activity;
//	}
	
}
