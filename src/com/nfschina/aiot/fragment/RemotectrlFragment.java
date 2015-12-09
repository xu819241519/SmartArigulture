package com.nfschina.aiot.fragment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nfschina.aiot.activity.AllTaskActivity;
import com.nfschina.aiot.activity.CreateTaskActivity;
import com.nfschina.aiot.activity.R;
import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.entity.Device;
import com.nfschina.aiot.entity.GreenHouse;
import com.nfschina.aiot.entity.InstructionMsg;
import com.nfschina.aiot.socket.Const;
import com.nfschina.aiot.socket.ISocketService;
import com.nfschina.aiot.socket.SocketService;
import com.nfschina.aiot.util.CRC16;
import com.nfschina.aiot.util.DBPro;
import com.nfschina.aiot.view.MyProgressDialog;
/**
 * 远程控制，温室内设备的控制
 * @author wujian
 */
public class RemotectrlFragment extends Fragment implements OnClickListener{
	@ViewInject(R.id.remote_device_lv)
	private ListView remote_device_lv;
	@ViewInject(R.id.task_look_tv)
	private TextView task_look_tv;
	@ViewInject(R.id.task_create_tv)
	private TextView task_create_tv;
	@ViewInject(R.id.device_item_switch)
	private ToggleButton device_item_switch;
	
	private List<Device> devices ;
	private MyHolder holder;
	private GreenHouse greenHouse;
	private MyConn conn;
	private ISocketService binder;
	private BroadcastReceiver receiver;//广播接收者
	private boolean isSendMsg = false;//是否发送了报文
	private String device_ctrled;//用于向用户提醒当前的操作是否成功
	private boolean isOpen = false;//当前操作是打开还是关闭
	private int index ;//记录当前点击了哪个设备
	private DBPro dbPro ;//数据库存储过程
	private boolean isCheckChange = false;//关闭或者打开了设备
	private MyProgressDialog myProgressDialog_load;
	private MyProgressDialog myProgressDialog_do;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				System.out.println("send success");
				break;
			case 0:
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("send fail");
				if (myProgressDialog_do != null) {
					myProgressDialog_do.dismiss();
				}
				if (devices != null && devices.size()>0) {
					remote_device_lv.setAdapter(new MyDeviceAdapter());
				}
				Toast.makeText(getActivity(), "发送失败，请检查服务器连接情况！", 0).show();
				break;
			}
		}
	};
	SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.core_remotectrl, null);
		ViewUtils.inject(this,view);
		// 以下为了获取当前在哪个温室
		Intent intent = getActivity().getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			greenHouse = (GreenHouse) bundle.get("greenhouse");
		}
		task_look_tv.setOnClickListener(this);
		task_create_tv.setOnClickListener(this);	
		return view;
	}
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
			if (isVisibleToUser) {
				new Handler(new Handler.Callback() {
				@Override
				public boolean handleMessage(Message msg) {
					if (myProgressDialog_load == null) {
						myProgressDialog_load = new MyProgressDialog(getActivity());
					}
					myProgressDialog_load.setCanceledOnTouchOutside(false);
					myProgressDialog_load.setMessage("正在加载，请稍后...");
					myProgressDialog_load.show();
					//网络加载温室设备名称以及各设备的状态
					new GetDeviceAndStateTask().execute();
					return true;
				}
				}).sendEmptyMessageDelayed(0, 200);
			}
		super.setUserVisibleHint(isVisibleToUser);
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		System.out.println("onAttach");
		//绑定服务
		Intent intent = new Intent(getActivity(), SocketService.class);
		conn = new MyConn();
		getActivity().bindService(intent, conn, 0);
		//广播接收者，这里是专门接收阀值设置完成后，服务端反馈报文的。
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(final Context context, final Intent intent) {// 当接收到广播之后
			        	if (isSendMsg) {// 并且是当前fragment发送了报文，服务端反馈的报文
							String value = intent.getStringExtra("response");
							System.out.println(value+"  at remotectrl");
							// 判断是否是服务端反馈报文//CRC验证码
							if (value.length() > 0 && value.length() == 50 && CRC16.getCRC16(value.substring(0, 46).getBytes()).equals(value.substring(46))) {
								if (value.substring(0, 7).equals("TTLKRZ0")) {//远程控制反馈报文
									isSendMsg = false;
											try {
												Thread.sleep(5000);
											} catch (InterruptedException e) {
												e.printStackTrace();
											}
									//查询数据库中的实时数据
									new GetDeviceAndStateTask().execute();
								}else if (value.substring(0, 7).equals("TTLKRZV")) {//时间反馈报文
									//timefromservice = value.substring(7, 21);
									//notify();
								}
							}
						}
			}
		};
		// 广播过滤器，表示将接收什么样的广播
		IntentFilter intentToReceiveFilter = new IntentFilter();
		intentToReceiveFilter.addAction(Const.BC);
		getActivity().registerReceiver(receiver, intentToReceiveFilter);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("onDestroy");
		if(receiver != null){
			getActivity().unregisterReceiver(receiver);
		}
		getActivity().unbindService(conn);
	}
	/**
	 * 服务连接事件
	 * @author wujian
	 */
	private class MyConn implements ServiceConnection{
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			//获得到了服务中介，可以调用服务中的方法了
			binder = (ISocketService) service;
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {
		}
	}
	//实现点击事件
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.task_look_tv://查看任务
			Intent intent1 = new Intent(getActivity(), AllTaskActivity.class);
			startActivity(intent1);
			break;
		case R.id.task_create_tv://创建任务
			Intent intent = new Intent(getActivity(), CreateTaskActivity.class);
			startActivity(intent);
			break;
		}
	}
	/**
	 * 通过访问存储过程，查询当前温室的设备和设备状态
	 * @author wujian
	 *
	 */
	private class GetDeviceAndStateTask extends AsyncTask<Void, Void, List<Device>>{

		@Override
		protected List<Device> doInBackground(Void... params) {
			try {
				String sql = "call getDevices('"+greenHouse.getId()+"')";
				dbPro = new DBPro(sql);
				ResultSet resultSet = dbPro.cs.getResultSet();
				if (resultSet != null) {
					devices = new ArrayList<Device>();
				}
				while (resultSet != null && resultSet.next()) {
					Device device = new Device(resultSet.getString(1), resultSet.getString(2), resultSet.getInt(3));
					devices.add(device);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				//各种关闭
				if (dbPro != null) {
					dbPro.close();
				}
				
			}
			return devices;
		}
		@Override
		protected void onPostExecute(List<Device> result) {
			super.onPostExecute(result);
			if (result != null && result.size() >0) {
				//可以开始进行适配了
				remote_device_lv.setAdapter(new MyDeviceAdapter());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				myProgressDialog_load.dismiss();
			}else {
				//当没有数据时显示的界面
				View emptyView = LayoutInflater.from(getActivity()).inflate(R.layout.nodata_view, null);
				emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));  
				((ViewGroup)remote_device_lv.getParent()).addView(emptyView);  
				remote_device_lv.setEmptyView(emptyView);
				myProgressDialog_load.dismiss();//停止刷新
			}
			if (isCheckChange) {
				myProgressDialog_do.dismiss();
				myProgressDialog_do = null;
				if (isOpen && result.get(index).getState() == 1) {
						//是打开，并且数据库里面的状态为1
						Toast.makeText(getActivity(), device_ctrled+"已经打开", 0).show();
					}else if (isOpen && result.get(index).getState() == 0) {
						Toast.makeText(getActivity(), device_ctrled+"打开失败", 0).show();
					}else if (!isOpen && result.get(index).getState() == 0) {
						Toast.makeText(getActivity(), device_ctrled+"已经关闭", 0).show();
					}else if (!isOpen && result.get(index).getState() == 1) {
						Toast.makeText(getActivity(), device_ctrled+"关闭失败", 0).show();
					}
				isCheckChange = false;
			}
		}
	}
	
	/**
	 * 显示温室内设备的适配器
	 * @author wujian
	 */
	public class MyDeviceAdapter extends BaseAdapter implements OnCheckedChangeListener{
		@Override
		public int getCount() {
			return devices.size();
		}
		@Override
		public Object getItem(int position) {
			return devices.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				holder = new MyHolder();
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.remote_device_item, null);
				ViewUtils.inject(holder, convertView);
				convertView.setTag(holder);
			}else {
				holder = (MyHolder) convertView.getTag();
			}
			//设备名称适配
			holder.device_item_name.setText(devices.get(position).getDeviceName());
			//设备状态适配
			if (devices.get(position).getId().equals("1")) {
				holder.device_item_switch.setChecked(true);
			}
			holder.device_item_switch.setTag(position);//为了onclick获取当前点击的位置
			holder.device_item_switch.setOnCheckedChangeListener(this);//需要设置这个监听事件
			
			return convertView;
		}
		//当ToggleButton device_item_switch被点击开关触发的函数
		@Override
		public void onCheckedChanged(final CompoundButton buttonView,
				final boolean isChecked) {
				isCheckChange = true;

				if (myProgressDialog_do == null) {
					myProgressDialog_do = new MyProgressDialog(getActivity());
			}
				myProgressDialog_do.setCanceledOnTouchOutside(false);
				myProgressDialog_do.setMessage("正在操作，请稍后...");
			new Thread(new Runnable() {
				@Override
				public void run() {
					//注意前后两句话
					getActivity().getMainLooper().prepare();
					myProgressDialog_do.show();
					getActivity().getMainLooper().loop();
				}
			}).start();
				
			new Thread(new Runnable() {
				@Override
				public void run() {
					index = Integer.parseInt(buttonView.getTag().toString());
					InstructionMsg imsg = new InstructionMsg();
					// 填充报文
					imsg.setHeadMark("TTLK");
					imsg.setType("I");
					imsg.setFromMark("A");
					imsg.setGreenhouseId(greenHouse.getId());
					imsg.setUserId(Constant.CURRENT_USER);
					if (isChecked) {
						// 打开设备操作
						switch (devices.get(index).getDeviceName()) {
						case "灌溉机":
							imsg.setContent("A1");
							break;
						case "卷帘机":
							imsg.setContent("B1");
							break;
						case "CO2产生器":
							imsg.setContent("C1");
							break;
						case "热风机":
							imsg.setContent("D1");
							break;
						case "通风电机":
							imsg.setContent("E1");
							break;
						}
						isOpen = true;
					} else {
						// 关闭设备操作
						switch (devices.get(index).getDeviceName()) {
						case "灌溉机":
							imsg.setContent("A0");
							break;
						case "卷帘机":
							imsg.setContent("B0");
							break;
						case "CO2产生器":
							imsg.setContent("C0");
							break;
						case "热风机":
							imsg.setContent("D0");
							break;
						case "通风电机":
							imsg.setContent("E0");
							break;
						}
						isOpen = false;
					}
					Date date = new Date();
					imsg.setSendTime(dateformat.format(date));
					imsg.setExecuteTime(dateformat.format(date));
					imsg.setExeperiod("0");
					imsg.setCheckNum(CRC16.getCRC16(imsg.getMessageEntity()
							.getBytes()));
					System.out.println(imsg.toString());
					binder.sentMsg(imsg.toString().getBytes(), handler);
					isSendMsg = true;// 已发送信息
					device_ctrled = devices.get(index).getDeviceName();
				}
			}).start();
		}
	}
	/**
	 * 缓存作用,主要是放在适配器里面使用
	 * @author wujian
	 */
	private class MyHolder{
		@ViewInject(R.id.device_item_name)
		private TextView device_item_name;
		@ViewInject(R.id.device_item_switch)
		private ToggleButton device_item_switch;
	}
	
}
