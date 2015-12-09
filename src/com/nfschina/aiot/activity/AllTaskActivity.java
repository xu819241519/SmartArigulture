package com.nfschina.aiot.activity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nfschina.aiot.animation.ExpandAnimation;
import com.nfschina.aiot.entity.Task;
import com.nfschina.aiot.util.DBConnectionUtil;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
/**
 * չʾ��ǰ�������Ҷ�Ӧ������
 * @author wujian
 *
 */
public class AllTaskActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.alltask_lv)
	private PullToRefreshListView alltask_lv;
	@ViewInject(R.id.alltask_back)
	private TextView alltask_back;
	private List<Task> tasks;
	private DBConnectionUtil connectionUtil;
	SimpleDateFormat format_ori = new SimpleDateFormat("yyyyMMddHHmmss");
	SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	SimpleDateFormat format_time = new SimpleDateFormat("HH:mm:ss");
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alltask);
		ViewUtils.inject(this);
		alltask_back.setOnClickListener(this);
		alltask_lv.setMode(Mode.PULL_FROM_START);
		
		alltask_lv.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				new GetAllTask().execute();
			}
		});
		// �״�����ҳ������Ҫ�Զ���������
		new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				alltask_lv.setRefreshing();// ˢ�¼�������
				return true;
			}
		}).sendEmptyMessageDelayed(0, 500);
		
	}
	/**
	 * ��ȡ���е�����
	 */
	private class GetAllTask extends AsyncTask<Void, Void, List<Task>>{
		@Override
		protected List<Task> doInBackground(Void... params) {
			try {//����Ҫ���һ��USER
				String sql = "select instructionid, instructioncontent,instructionsendtime,instructionruntime,greenhouseid,userid,runperiod " +
						"from instructiontb where runperiod = 1 or runperiod = 2 OR instructionruntime > NOW()";
				connectionUtil = new DBConnectionUtil(sql);
				ResultSet resultSet = connectionUtil.pst.executeQuery();
				tasks = new ArrayList<Task>();
				if (resultSet != null) {
					
					while (resultSet.next()) {
						Task task = new Task(resultSet.getInt("instructionid"),resultSet.getString("instructioncontent"),resultSet.getString("greenhouseid"),resultSet.getString("userid"),
								resultSet.getString("instructionsendtime"),resultSet.getString("instructionruntime"),resultSet.getString("runperiod"));
						tasks.add(task);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				//�ر����ݿ�����
				connectionUtil.close();
			}
			return tasks;
		}
		@Override
		protected void onPostExecute(List<Task> result) {
			super.onPostExecute(result);
			if (result != null ) {
				alltask_lv.setAdapter(new MyAllTaskAdapter());
				alltask_lv.onRefreshComplete();
			}else {
				//��û������ʱ��ʾ�Ľ���
				View emptyView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.notask_view, null);
				emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));  
				((ViewGroup)alltask_lv.getParent()).addView(emptyView);  
				alltask_lv.setEmptyView(emptyView);
				alltask_lv.onRefreshComplete();
			}
		}
	}
	/**
	 * ���������������
	 * @author wujian
	 */
	private class MyAllTaskAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return tasks.size();
		}
		@Override
		public Object getItem(int position) {
			return tasks.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			MyHolder holder;
			if (convertView == null) {
				holder = new MyHolder();
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.alltask_item, null);
				ViewUtils.inject(holder, convertView);
				convertView.setTag(holder);
			}else {
				holder = (MyHolder) convertView.getTag();
			}
			switch (tasks.get(position).getContent()) {
			case "A1":
				holder.alltask_content.setText("�򿪹�Ȼ�");
				break;
			case "A0":
				holder.alltask_content.setText("�رչ�Ȼ�");
				break;
			case "B1":
				holder.alltask_content.setText("�򿪾�����");
				break;
			case "B0":
				holder.alltask_content.setText("�رվ�����");
				break;
			case "C1":
				holder.alltask_content.setText("��CO2������");
				break;
			case "C0":
				holder.alltask_content.setText("�ر�CO2������");
				break;
			case "D1":
				holder.alltask_content.setText("���ȷ��");
				break;
			case "D0":
				holder.alltask_content.setText("�ر��ȷ��");
				break;
			case "E1":
				holder.alltask_content.setText("���ȷ��");
				break;
			case "E0":
				holder.alltask_content.setText("�ر�ͨ����");
				break;
			}
//			if (tasks.get(position).getContent().equals("A1")) {
//				holder.alltask_content.setText("�򿪹�Ȼ�");
//			}else if (tasks.get(position).getContent().equals("A0")) {
//				holder.alltask_content.setText("�رչ�Ȼ�");
//			}else if (tasks.get(position).getContent().equals("B1")) {
//				holder.alltask_content.setText("�򿪾�����");
//			}else if (tasks.get(position).getContent().equals("B0")) {
//				holder.alltask_content.setText("�رվ�����");
//			}else if (tasks.get(position).getContent().equals("C1")) {
//				holder.alltask_content.setText("��CO2������");
//			}else if (tasks.get(position).getContent().equals("C0")) {
//				holder.alltask_content.setText("�ر�CO2������");
//			}else if (tasks.get(position).getContent().equals("D1")) {
//				holder.alltask_content.setText("���ȷ��");
//			}else if (tasks.get(position).getContent().equals("D0")) {
//				holder.alltask_content.setText("�ر��ȷ��");
//			}else if (tasks.get(position).getContent().equals("E1")) {
//				holder.alltask_content.setText("��ͨ����");
//			}else if (tasks.get(position).getContent().equals("E0")) {
//				holder.alltask_content.setText("�ر�ͨ����");
//			}
			holder.alltask_greenhouse.setText(tasks.get(position).getGreenhouseId());
//			if (tasks.get(position).getExeperiod().equals("1")) {
//				holder.alltask_period.setText("ÿ��һ��");
//				holder.task_item_switch.setChecked(true);
//				try {
//					holder.alltask_createtime.setText(format.format(format_ori.parse(tasks.get(position).getSendTime())));
//					holder.alltask_exetime.setText(format.format(format_ori.parse(tasks.get(position).getExecuteTime())));
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}
//			}else if (tasks.get(position).getExeperiod().equals("0")) {
//				holder.alltask_period.setText("ִ��һ��");
//				holder.task_item_switch.setChecked(true);
//				try {
//					holder.alltask_createtime.setText(format_time.format(format_ori.parse(tasks.get(position).getSendTime())));
//					holder.alltask_exetime.setText(format_time.format(format_ori.parse(tasks.get(position).getExecuteTime())));
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}
//			}else if (tasks.get(position).getExeperiod().equals("0")) {
//				holder.alltask_period.setText("�Ѿ�ȡ������ִ��");
//				holder.task_item_switch.setChecked(false);
//				try {
//					holder.alltask_createtime.setText(format.format(format_ori.parse(tasks.get(position).getSendTime())));
//					holder.alltask_exetime.setText(format.format(format_ori.parse(tasks.get(position).getExecuteTime())));
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}
//			}
			switch (tasks.get(position).getExeperiod()) {
			case "1":
				holder.alltask_period.setText("ÿ��һ��");
				holder.task_item_switch.setChecked(true);
				try {
					holder.alltask_createtime.setText(format.format(format_ori.parse(tasks.get(position).getSendTime())));
					holder.alltask_exetime.setText(format.format(format_ori.parse(tasks.get(position).getExecuteTime())));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				break;

			case "0":
				holder.alltask_period.setText("ִ��һ��");
				holder.task_item_switch.setChecked(true);
				try {
					holder.alltask_createtime.setText(format_time.format(format_ori.parse(tasks.get(position).getSendTime())));
					holder.alltask_exetime.setText(format_time.format(format_ori.parse(tasks.get(position).getExecuteTime())));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				break;
			case "2":
				holder.alltask_period.setText("�Ѿ�ȡ������ִ��");
				holder.task_item_switch.setChecked(false);
				try {
					holder.alltask_createtime.setText(format.format(format_ori.parse(tasks.get(position).getSendTime())));
					holder.alltask_exetime.setText(format.format(format_ori.parse(tasks.get(position).getExecuteTime())));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				break;
			}
			holder.alltask_user.setText(tasks.get(position).getUserId());
			
			//Ϊ�˵���¼�
			View alltask_item_ll = (LinearLayout) convertView.findViewById(R.id.alltask_item_ll);
			alltask_item_ll.measure(0, 0);
			((LinearLayout.LayoutParams)alltask_item_ll.getLayoutParams()).bottomMargin = (-1)*alltask_item_ll.getMeasuredHeight();
			alltask_item_ll.setVisibility(View.GONE);
			//ֱ�ӽ�����¼�ע����convertView
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					upAndDownAnimation(v);
				}
			});
			holder.task_item_switch.setTag(position);
			//�������ߴ�����
			holder.task_item_switch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						//������
						if (tasks.get(position).getExeperiod().length()<14) {
							//�������ִ��
							new UpdateTaskState(tasks.get(position).getId(), "1").execute();
						}else {
							//�������ִ��һ��
							new UpdateTaskState(tasks.get(position).getId(), "0").execute();
						}
					}else {
						//�ر�����
						new UpdateTaskState(tasks.get(position).getId(), "2").execute();
					}
					alltask_lv.setRefreshing();// ˢ�¼�������
				}
			});
			return convertView;
		}
	}
	/**
	 * ��������״̬���첽����
	 * @author wujian
	 */
	private class  UpdateTaskState extends AsyncTask<Void, Void, Void>{
		private int id;
		private String state;
		public UpdateTaskState(int id,String state){
			this.id = id;
			this.state = state;
		}
		@Override
		protected Void doInBackground(Void... params) {
			try {
				String sql = "update instructiontb set runperiod = '"+state+"' where instructionid = '"+id+"'";
				connectionUtil = new DBConnectionUtil(sql);
				connectionUtil.pst.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				connectionUtil.close();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			Toast.makeText(getApplicationContext(), "ִ�гɹ�", 0).show();
		}
		
	}
	/**
	 * ������
	 * @author My
	 */
	private class MyHolder {
		@ViewInject(R.id.alltask_greenhouse)
		private TextView alltask_greenhouse;
		@ViewInject(R.id.alltask_content)
		private TextView alltask_content;
		@ViewInject(R.id.alltask_period)
		private TextView alltask_period;
		@ViewInject(R.id.alltask_exetime)
		private TextView alltask_exetime;
		@ViewInject(R.id.alltask_user)
		private TextView alltask_user;
		@ViewInject(R.id.alltask_createtime)
		private TextView alltask_createtime;
		@ViewInject(R.id.task_item_switch)
		private ToggleButton task_item_switch;
	}
	/**
	 * ʵ�����¼�ͷת���Ķ���
	 * @param v
	 */
	public void upAndDownAnimation(View v){
		//Ϊ����Ŀ�еļ�ͷ��ת��Ч��
		TextView isFirstClick = (TextView) v.findViewById(R.id.isFirstClick_alltask);
		//�����ж��Ƿ��ǵ�һ�ε����Ŀ
		boolean firstClick_tv = isFirstClick.getText().toString().equals("true");
		ImageView alarminfo_item_upordown_iv = (ImageView) v.findViewById(R.id.alltask_item_upordown_iv);
		if (firstClick_tv) {	//�ж��ǲ��ǵ�һ�ε��listview  item
			//Ϊͼ����һ������
			Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.alarminfo_updown_iv);
			System.out.println("true");
			alarminfo_item_upordown_iv.startAnimation(animation);
			animation.setFillAfter(true);//������ת�������
			isFirstClick.setText("false");//��ʾ��һ���Ѿ�������
		} else if (!firstClick_tv) {
			Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.alarminfo_downup_iv);
			System.out.println("false");
			alarminfo_item_upordown_iv.startAnimation(animation);
			animation.setFillAfter(true);
			isFirstClick.setText("true");
		}
		//�����ǵ����Ŀչ���Ķ���Ч��
		LinearLayout alltask_item_ll = (LinearLayout) v.findViewById(R.id.alltask_item_ll);
		ExpandAnimation expandAni = new ExpandAnimation(alltask_item_ll, 500);
		alltask_item_ll.startAnimation(expandAni);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.alltask_back:
			finish();
			break;

		default:
			break;
		}
	}
	

}
