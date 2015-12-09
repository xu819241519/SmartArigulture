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
 * ������Ϣ��fragment
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
	private GreenHouse greenHouse;//��ǰ����ʵ��
	private List<WarningInfo> warningInfos = new ArrayList<WarningInfo>();//���汨����Ϣ
	private PopupWindow popupWindow;//���������ĶԻ���
	private LayoutInflater inflater = null;
	MyHolder holder;
	private int page,size = 10;
	private boolean isHavaInfo = false;
	//private InfoListener infoListener;
	private DBConnectionUtil connectionUtil ;
	//��ʱ���ַ�����ת��Ϊformat_ori��ʽ��Date��Ȼ���ٸ�ʽ��Ϊformat���ַ���
	SimpleDateFormat format_ori = new SimpleDateFormat("yyyyMMddhhmmss");
	SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Date date = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.core_info, null);
		ViewUtils.inject(this, view);
		// ����Ϊ�˻�ȡ��ǰ���ĸ�����
		Intent intent = getActivity().getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			greenHouse = (GreenHouse) bundle.get("greenhouse");
		}
		info_back.setOnClickListener(this);
		info_gohome.setOnClickListener(this);
		core_info_lv.setMode(Mode.BOTH);
		//core_info_lv.setOnItemClickListener(this);//����ֱ�ӽ�listview��Ŀ�ĵ���¼�ֱ�������������е�convertView
		core_info_lv.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				loadDatas(core_info_lv.getScrollY()<0);
			}
		});
		// �״�����ҳ������Ҫ�Զ���������
		new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				core_info_lv.setRefreshing();// ˢ�¼�������
				return true;
			}
		}).sendEmptyMessageDelayed(0, 1000);
		return view;
	}
	/**
	 * ʵ������ˢ�¡��������ظ���ķ���
	 * @param isRefresh
	 */
	protected void loadDatas(boolean isRefresh) {
		if (isRefresh) {
			page = 0;//����ˢ�£������ˢ�£��Ͱ�ҳ����Ϊ��һ��
			warningInfos.clear();
			new GetAlarmInfoByIdTask(page,size).execute();
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					core_info_lv.setMode(Mode.BOTH);
				}
			});
		}else {
			page++;//�������ظ��࣬������һ��ҳ��
			new GetAlarmInfoByIdTask(page,size).execute();
			core_info_lv.setAdapter(infoListAdapter);
			//infoListAdapter.notifyDataSetChanged();
		}
	}
	/**
	 * ���ݵ�ǰ����id��ȡ������Ϣ
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
				//��������ID�Լ�������Ϣ״̬
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
					//�����μ��ص�������С��size,����û�м��ص�����
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							core_info_lv.setMode(Mode.PULL_FROM_START);
							Toast.makeText(getActivity(), "�Ѽ���ȫ��", Toast.LENGTH_LONG).show();
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
				//��������
				infoListAdapter = new MyInfoListAdapter();
				core_info_lv.setAdapter(infoListAdapter);
				//ֹͣˢ��
				core_info_lv.onRefreshComplete();
				//���Ǹ�activity������Ϣ��
				//infoListener.alarmShow(isHavaInfo);
			}
			else {
				//��û������ʱ��ʾ�Ľ���
				View emptyView = LayoutInflater.from(getActivity()).inflate(R.layout.nodata_view, null);
				emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));  
				((ViewGroup)core_info_lv.getParent()).addView(emptyView);  
				core_info_lv.setEmptyView(emptyView);
				core_info_lv.onRefreshComplete();//ֹͣˢ��
			}
		}
	}
	/**
	 * ���䱨����Ϣ��������
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
			//�������ɱ�����Ϣ����
			String infoContent = "";
			int infoCount = 1;
			if (warningInfos.get(position).getCo2() < 0) {
				infoContent += (infoCount++)+"��CO2������С��ֵ"+(-warningInfos.get(position).getCo2())+"ppm;\n";
			}else if (warningInfos.get(position).getCo2() > 0) {
				infoContent +=(infoCount++)+"��CO2�������ֵ"+warningInfos.get(position).getCo2()+"ppm;\n";
			}
			if (warningInfos.get(position).getTemperature() < 0) {
				infoContent +=(infoCount++)+"���¶ȵ�����С��ֵ"+(-warningInfos.get(position).getTemperature())+"��;\n";
			}else if (warningInfos.get(position).getTemperature() > 0) {
				infoContent +=(infoCount++)+"���¶ȸ������ֵ"+warningInfos.get(position).getTemperature()+"��;\n";
			}
			if (warningInfos.get(position).getHumidity() < 0 ) {
				infoContent +=(infoCount++)+"��ʪ�ȵ�����С��ֵ"+(-warningInfos.get(position).getHumidity())+"%;\n";
			}else if(warningInfos.get(position).getHumidity() > 0 ){
				infoContent +=(infoCount++)+"��ʪ�ȸ������ֵ"+warningInfos.get(position).getHumidity()+"%;\n";
			}
			if (warningInfos.get(position).getIlluminance() < 0) {
				infoContent +=(infoCount++)+"�����յ�����С��ֵ"+(-warningInfos.get(position).getIlluminance())+"xl;\n";
			}else if (warningInfos.get(position).getIlluminance() > 0) {
				infoContent +=(infoCount++)+"�����ո������ֵ"+warningInfos.get(position).getIlluminance()+"xl;\n";
			}
			
			holder.alarminfo_item_content.setText(infoContent);
			try {//���ɸ�ʽ����ʱ��
				date = format_ori.parse(warningInfos.get(position).getWarningtime());
				String formatTime = format.format(date);
				holder.alarminfo_item_time.setText(formatTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			//������Ϣ���������
			String infoLevel = getWarningLevel();
			switch (infoLevel) {
			case "��Ҫע��":
				holder.alarminfo_item_level.setBackgroundColor(getResources().getColor(R.color.green_light));
				holder.alarminfo_item_level.setText(infoLevel);
				break;
			case "��������":
				holder.alarminfo_item_level.setBackgroundColor(getResources().getColor(R.color.orange));
				holder.alarminfo_item_level.setText(infoLevel);
				break;
			case "���Ժ���":
				holder.alarminfo_item_level.setBackgroundColor(getResources().getColor(R.color.gray));
				holder.alarminfo_item_level.setText(infoLevel);
				break;
			}
			//Ϊ�˵���¼�
			View alarminfo_item_ll = (LinearLayout) convertView.findViewById(R.id.alarminfo_item_ll);
			alarminfo_item_ll.measure(0, 0);
			((LinearLayout.LayoutParams)alarminfo_item_ll.getLayoutParams()).bottomMargin = (-1)*alarminfo_item_ll.getMeasuredHeight();
			alarminfo_item_ll.setVisibility(View.GONE);
			//ֱ�ӽ�����¼�ע����convertView
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					upAndDownAnimation(v,position);
				}
			});
			//��ʱȥ�����湦��
			//ֱ�ӽ�����¼�ע����convertView����PullToRefreshListViewû��onItemLongClick���������ʱ��
//			convertView.setOnLongClickListener(new OnLongClickListener() {
//				@Override
//				public boolean onLongClick(View v) {
//					inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//					initPopupWindow(inflater);
//					showPop(v);
//					pop_ignore_tv.setOnClickListener(new TextViewOnClick(position));
//					pop_handler_tv.setOnClickListener(new TextViewOnClick(position));
//					return true;//����true��OnClickListener����ִ��
//				}
//			});
			return convertView;
		}
		
	}
	/**
	 * ��ʱ����  ��  ��������ĵ���¼�
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
			case R.id.pop_ignore_tv://��ʱ����
				//ִ�к��Բ���
				new IgnoreWarningTask(position).execute();
				//�ص�
				if (popupWindow != null) {
					popupWindow.dismiss();
				}
				Toast.makeText(getActivity(), "�Ѿ����Ե�ǰ������Ϣ", 1).show();
				//���ʱ�����ݿ��е������Ѿ������ı䣬��Ҫֱ����ʾ���û���ˢ��
				core_info_lv.setRefreshing();
				break;

			case R.id.pop_handler_tv://���̴���
				
				break;
			}
		}
	}
	/**
	 * ���Ա�����Ϣ���߽�������Ϣ��Ϊ�Ѵ���
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
				//����ֵ
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
	 * ��ʼ��Popupwindow
	 * @param inflater
	 */
	private void initPopupWindow(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.pop_item_layout, null);
		popupWindow = new PopupWindow(view, 350, 100);
		pop_ignore_tv = (TextView) view.findViewById(R.id.pop_ignore_tv);
		pop_handler_tv = (TextView) view.findViewById(R.id.pop_handler_tv);
	}
	/**
	 * Popupwindow��ʾ
	 * @param v
	 */
	@SuppressWarnings("deprecation")
	private void showPop(View v) {
		popupWindow.setFocusable(false);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());// ���ô���ɵ��Popupwindow��������ʧ��ע������ʧ
		// ���ó���λ��
		int[] location = new int[2];
		v.getLocationOnScreen(location);
		popupWindow.showAtLocation(v, Gravity.NO_GRAVITY,
				location[0] + v.getWidth() / 2 - popupWindow.getWidth() / 2,
				location[1] - popupWindow.getHeight());
	}
	/**
	 * Ϊ�˻��ؼ�
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
	 * ʵ�����¼�ͷת���Ķ���
	 * @param v
	 */
	public void upAndDownAnimation(View v,int position){
		//Ϊ��ȷ����һ�β鿴������Ϣ��������Ϣ״̬�ı�
		TextView isFirstClick_other = (TextView) v.findViewById(R.id.isFirstClick_other);
		System.out.println(isFirstClick_other.getText());
		if (isFirstClick_other.getText().equals("true")) {
			new IgnoreWarningTask(position).execute();
			isFirstClick_other.setText("false");
		}
		//Ϊ����Ŀ�еļ�ͷ��ת��Ч��
		TextView isFirstClick = (TextView) v.findViewById(R.id.isFirstClick);
		//�����ж��Ƿ��ǵ�һ�ε����Ŀ
		boolean firstClick_tv = isFirstClick.getText().toString().equals("true");
		ImageView alarminfo_item_upordown_iv = (ImageView) v.findViewById(R.id.alarminfo_item_upordown_iv);
		if (firstClick_tv) {	//�ж��ǲ��ǵ�һ�ε��listview  item
			//Ϊͼ����һ������
			Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.alarminfo_updown_iv);
			alarminfo_item_upordown_iv.startAnimation(animation);
			animation.setFillAfter(true);//������ת�������
			isFirstClick.setText("false");//��ʾ��һ���Ѿ�������
		} else if (!firstClick_tv) {
			Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.alarminfo_downup_iv);
			alarminfo_item_upordown_iv.startAnimation(animation);
			animation.setFillAfter(true);
			isFirstClick.setText("true");
		}
		//�����ǵ����Ŀչ���Ķ���Ч��
		LinearLayout alarminfo_item_ll = (LinearLayout) v.findViewById(R.id.alarminfo_item_ll);
		ExpandAnimation expandAni = new ExpandAnimation(alarminfo_item_ll, 500);
		alarminfo_item_ll.startAnimation(expandAni);
	}
	/**
	 * �����ȡ������Ϣ����
	 * @return
	 */
	public String getWarningLevel(){
		String level = "";
		int random = (int)(3 * Math.random())+1;
		switch (random) {
		case 1:
			level =  "��Ҫע��";
			break;
		case 2:
			level =  "��������";
			break;
		case 3:
			level = "���Ժ���";
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
//	 * ��activity����ͨ�ŵĽӿ�
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
