package com.nfschina.aiot.fragment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Paint;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nfschina.aiot.activity.R;
import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.entity.GreenHouse;
import com.nfschina.aiot.entity.InstructionMsg;
import com.nfschina.aiot.listener.MyDialogListener;
import com.nfschina.aiot.socket.Const;
import com.nfschina.aiot.socket.ISocketService;
import com.nfschina.aiot.socket.SocketService;
import com.nfschina.aiot.util.CRC16;
import com.nfschina.aiot.util.DBConnectionUtil;
import com.nfschina.aiot.view.ActionItem;
import com.nfschina.aiot.view.MyGridView;
import com.nfschina.aiot.view.MyNormalDialog;
import com.nfschina.aiot.view.MyPopup;
import com.nfschina.aiot.view.MyProgressDialog;
/**
 * 创建一个实时批量任务
 * @author wujian
 */
public class RealTimeTaskFragment extends Fragment implements OnClickListener,OnCheckedChangeListener{
	@ViewInject(R.id.realtimetask_choosehouse_iv)
	private ImageView realtimetask_choosehouse_iv;
	@ViewInject(R.id.realtimetask_choosedevice_iv)
	private ImageView realtimetask_choosedevice_iv;
	@ViewInject(R.id.realtimetask_greenhouse_rl)
	private RelativeLayout realtimetask_greenhouse_rl;
	@ViewInject(R.id.realtimetask_device_rl)
	private RelativeLayout realtimetask_device_rl;
	@ViewInject(R.id.createtask_taskdo_rg)
	private RadioGroup createtask_taskdo_rg;
	@ViewInject(R.id.createtask_on_rb)
	private RadioButton createtask_on_rb;
	@ViewInject(R.id.createtask_off_rb)
	private RadioButton createtask_off_rb;
	@ViewInject(R.id.task_do_btn)
	private Button task_do_btn;
	@ViewInject(R.id.realtimetask_device_selected)
	private TextView realtimetask_device_selected;
	@ViewInject(R.id.realtimetask_gh_selected)
	private TextView realtimetask_gh_selected;
	//以下两个用来显示已经选择的温室和设备
	@ViewInject(R.id.show_house_selected)
	private MyGridView show_house_selected;
	@ViewInject(R.id.show_device_selected)
	private MyGridView show_device_selected;
	//存放温室
	private List<GreenHouse> greenHouses = new ArrayList<GreenHouse>();
	private Map<String, String> greenhouseMap = new HashMap<String, String>();//这个属性只是为了在发指令的时候获取用户选择的温室的ID
	private List<String> greenHouseNames ;
	private List<String> house_selecting ;//存放被选中的温室名称
	private List<String> house_selected ;//存放最终被选中的温室名称
	private List<String> devices;//存放所有的设备名称
	private Map<String, String> devicesMap = new HashMap<String, String>();//存放设备
	private List<String> devices_selecting;
	private List<String> devices_selected;//存放最终被选中的设备名称
	private List<String> messages;//报文
	private DBConnectionUtil connectionUtil;
	private boolean isOpen = true;//操作打开或者关闭
	private MyNormalDialog myDialog;
	private MyHouseSelectedAdapter houseSelectedAdapter;
	private MyDeviceSelectedAdapter deviceSelectedAdapter;
	
	private MyPopup myPopup;
	private View parent;
	private Myholder holder;
	SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
	private MyConn conn;
	private ISocketService binder;
	private BroadcastReceiver receiver;//广播接收者
	private boolean isSendMsg = false;//是否发送了报文
	private MyProgressDialog myProgressDialog;
	int receivecount = 0;
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
				if (myProgressDialog != null) {
					myProgressDialog.dismiss();
				}
				Toast.makeText(getActivity(), "发送失败，请检查服务器连接情况！", 0).show();
				break;
			}
		}
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.createrealtimetask, null, false);
		ViewUtils.inject(this,view);
		parent = view;
		realtimetask_choosehouse_iv.setOnClickListener(this);
		realtimetask_choosedevice_iv.setOnClickListener(this);
		realtimetask_greenhouse_rl.setOnClickListener(this);
		realtimetask_device_rl.setOnClickListener(this);
		task_do_btn.setOnClickListener(this);
		realtimetask_gh_selected.setOnClickListener(this);
		realtimetask_device_selected.setOnClickListener(this);
		
		createtask_taskdo_rg.setOnCheckedChangeListener(this);
		return view;
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// 绑定服务
		Intent intent = new Intent(getActivity(), SocketService.class);
		conn = new MyConn();
		getActivity().bindService(intent, conn, 0);
		//广播接收者，这里是专门接收阀值设置完成后，服务端反馈报文的。
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(final Context context, Intent intent) {// 当接收到广播之后
				if (isSendMsg) {// 并且是当前fragment发送了报文，服务端反馈的报文
					
					String value = intent.getStringExtra("response");
					System.out.println(value+"in realtime");
					// 判断是否是服务端反馈报文//CRC验证码
					if (value.substring(0, 6).equals("TTLKRZ")
							&& value.length() == 50
							&& CRC16.getCRC16(value.substring(0, 46).getBytes()).equals(value.substring(46))) {
						receivecount++;
						System.out.println("接收数量："+receivecount);
						System.out.println("应该数量："+(house_selected.size() * devices_selected.size()));
						if (receivecount == (house_selected.size() * devices_selected.size())) {
							System.out.println(receivecount+"in realtime");
							// 在主线程里面更新界面
							getActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
									myProgressDialog.dismiss();// 收到服务器端的回复消息之后再消失
									Toast.makeText(context, "执行完成", 0).show();
									receivecount = 0;
									isSendMsg = false;
									//发送完成后，清空已选列表
									house_selected.clear();
									devices_selected.clear();
									houseSelectedAdapter.notifyDataSetChanged();
									deviceSelectedAdapter.notifyDataSetChanged();
									realtimetask_gh_selected.setVisibility(View.VISIBLE);
									realtimetask_device_selected.setVisibility(View.VISIBLE);
									task_do_btn.setEnabled(false);
								}
							});
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
		if (receiver != null) {
			getActivity().unregisterReceiver(receiver);
		}
		getActivity().unbindService(conn);
	}
	/**
	 * 异步方式获取温室的方式
	 * @author wujian
	 */
	private class GetGreenHouseTask extends AsyncTask<Void, Void, List<GreenHouse>>{
		@Override
		protected List<GreenHouse> doInBackground(Void... params) {
			//分页查询
			String sql = "select GreenHouseId,GreenHouseName from greenhouseinfotb ";
			connectionUtil = new DBConnectionUtil(sql);
			try {
				ResultSet resultSet = connectionUtil.pst.executeQuery();
				if (resultSet != null) {
					greenhouseMap.clear();
					while (resultSet.next()) {
						GreenHouse greenHouse = new GreenHouse(resultSet.getString("GreenHouseId"),
								resultSet.getString("GreenHouseName"));
						greenHouses.add(greenHouse);
						greenhouseMap.put(resultSet.getString("GreenHouseName"), resultSet.getString("GreenHouseId"));
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				connectionUtil.close();
			}
			return greenHouses;
		}
		@Override
		protected void onPostExecute(List<GreenHouse> result) {
			super.onPostExecute(result);
			greenHouseNames = new ArrayList<String>();
			for (int i = 0; i < result.size(); i++) {
				greenHouseNames.add(result.get(i).getName());
			}
			chooseHouse();
		}
	}
	/**
	 * 获取数据库中全部设备id和名称
	 * @author wujian
	 */
	private class GetAllDeviceTask extends AsyncTask<Void, Void, Map<String, String>>{
		@Override
		protected Map<String, String> doInBackground(Void... params) {
			try {
				String sql = "select DeviceId,DeviceName from greenhousedevicetb";
				connectionUtil = new DBConnectionUtil(sql);
				ResultSet resultSet = connectionUtil.pst.executeQuery();
				if (resultSet != null) {
					devicesMap = new HashMap<String, String>();
					while (resultSet.next()) {
						devicesMap.put(resultSet.getString("DeviceId"), resultSet.getString("DeviceName"));
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				connectionUtil.close();
			}
			return devicesMap;
		}
		@Override
		protected void onPostExecute(Map<String, String> result) {
			super.onPostExecute(result);
			devices = new ArrayList<String>(devicesMap.values());//直接获取Map的value并转化成一个list的方法
			chooseDevice();
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.realtimetask_choosehouse_iv://选择温室
			if (greenHouses.size()==0 && greenhouseMap.size()==0) {
				new GetGreenHouseTask().execute();
			}else {
				chooseHouse();
			}
			
			break;
		case R.id.realtimetask_choosedevice_iv://选择设备
			if (devicesMap.size() == 0) {
				new GetAllDeviceTask().execute();
			}else {
				chooseDevice();
			}
			
			break;
		case R.id.realtimetask_greenhouse_rl:
			if (greenHouses.size()==0 && greenhouseMap.size()==0) {
				new GetGreenHouseTask().execute();
			}else {
				chooseHouse();
			}
			break;
		case R.id.realtimetask_device_rl://选择设备
			if (devicesMap.size() == 0) {
				new GetAllDeviceTask().execute();
			}else {
				chooseDevice();
			}
			break;
		case R.id.realtimetask_gh_selected://查看已选温室
			showHouseSelected(house_selected,v);
			break;
		case R.id.realtimetask_device_selected://查看已选设备
			showDeviceSelected(devices_selected,v);
			break;
		case R.id.task_do_btn://执行任务
			if (myProgressDialog == null) {
				myProgressDialog = new MyProgressDialog(getActivity());
			}
			myProgressDialog.setCanceledOnTouchOutside(false);
			myProgressDialog.setMessage("正在执行，请稍后...");
			new Thread(new Runnable() {
				@Override
				public void run() {
					//注意前后两句话
					getActivity().getMainLooper().prepare();
					myProgressDialog.show();
					getActivity().getMainLooper().loop();
				}
			}).start();
			//耗时的操作放到非主线程里面
			new Thread(new Runnable() {
				@Override
				public void run() {
						messages = new ArrayList<String>();
						if (house_selected.size()>0 && devices_selected.size()>0) {
							InstructionMsg imsg = new InstructionMsg();
							//填充报文
							imsg.setHeadMark("TTLK");
							imsg.setType("I");
							imsg.setFromMark("A");
							imsg.setUserId(Constant.CURRENT_USER);
							Date date = new Date();
							for (String greenhouse : house_selected) {
								imsg.setGreenhouseId(greenhouseMap.get(greenhouse));//获取温室ID
								for (String device : devices_selected) {
									if (isOpen) {
										//打开设备操作
										switch (device) {
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
									}else {
										//关闭设备操作
										switch (device) {
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
									}
									imsg.setSendTime(dateformat.format(date));
									imsg.setExecuteTime(dateformat.format(date));
									imsg.setExeperiod("0");
									imsg.setCheckNum(CRC16.getCRC16(imsg.getMessageEntity().getBytes()));
									messages.add(imsg.toString());//生成一条报文字符串
								}
							}
							for (String message : messages) {
								//开始逐一发送报文
								binder.sentMsg(message.getBytes(), handler);
								System.out.println(message);
								isSendMsg = true;//发送了
								
								try {
									//发完一次，稍停一下
									Thread.sleep(500);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
							
						}else {
							Toast.makeText(getActivity(), "温室或者设备还未选择，请选择后重试", 1).show();
						}
					}
			}).start();
			
			break;
		}
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
	/**
	 * 为了显示已经选择的温室
	 * @author wujian
	 */
	private class MyHouseSelectedAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return house_selected.size();
		}

		@Override
		public Object getItem(int position) {
			return house_selected.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				holder = new Myholder();
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.deviceorhouse_selected_item, null);
				ViewUtils.inject(holder, convertView);
				convertView.setTag(holder);
			}else {
				holder = (Myholder) convertView.getTag();
			}
			holder.deviceorhouse_selected_name.setText(house_selected.get(position));
			return convertView;
		}
		
	}
	/**
	 * 为了显示已经选择的设备
	 * @author wujian
	 */
	private class MyDeviceSelectedAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return devices_selected.size();
		}

		@Override
		public Object getItem(int position) {
			return devices_selected.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				holder = new Myholder();
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.deviceorhouse_selected_item, null);
				ViewUtils.inject(holder, convertView);
				convertView.setTag(holder);
			}else {
				holder = (Myholder) convertView.getTag();
			}
			holder.deviceorhouse_selected_name.setText(devices_selected.get(position));
			return convertView;
		}
		
	}
	private class Myholder {
		@ViewInject(R.id.deviceorhouse_selected_name)
		private TextView deviceorhouse_selected_name;
	}
	/**
	 * 查看已经选择的设备的方法
	 * @param v 就是点击的view
	 * @param devices_selected2
	 */
	private void showDeviceSelected(List<String> devicesSelected, View v) {
		if (devicesSelected != null) {//所选项不为空的时候，才会显示弹窗
			ActionItem actionItem ;
			//实例化弹窗
			myPopup = new MyPopup(getActivity(),parent.getWidth()-20, LayoutParams.WRAP_CONTENT);
			for (int i = 0; i < devicesSelected.size(); i++) {
				actionItem = new ActionItem(devicesSelected.get(i));
				myPopup.addAction(actionItem);
			}
			myPopup.show(v);
		}else {
			Toast.makeText(getActivity(), "还未选择设备", 0).show();
		}
	}
	/**
	 * 查看已经选择的温室
	 * @param v 就是点击的view
	 * @param house_selected2
	 */
	private void showHouseSelected(List<String> houseSelected, View v) {
		if (houseSelected != null) {//所选项不为空的时候，才会显示弹窗
			ActionItem actionItem ;
			//实例化弹窗
			myPopup = new MyPopup(getActivity(),parent.getWidth()-20, LayoutParams.WRAP_CONTENT);
			for (int i = 0; i < houseSelected.size(); i++) {
				actionItem = new ActionItem(houseSelected.get(i));
				myPopup.addAction(actionItem);
			}
			myPopup.show(v);
		}else {
			Toast.makeText(getActivity(), "还未选择温室", 0).show();
		}
	}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.createtask_on_rb://打开
			isOpen = true;
			break;
		case R.id.createtask_off_rb://关闭
			isOpen = false;
			break;
		}
	}
	/**
	 * 实现对话框选择温室的方法
	 */
	private void chooseHouse() {
		if (myDialog == null) {
			myDialog = new MyNormalDialog(getActivity(), "选择温室",new MyDialogListener() {
				//每次选中一个checkbox的时候，触发这个函数
				@Override
				public void onListItemChecked(int position, String house) {
					System.out.println("onListItemChecked in choosehouse");
					house_selected = null;
					if (house_selecting == null) {//单例模式
						house_selecting = new ArrayList<String>();
					}
					//已经选中的存放在这里
					house_selecting.add(house);
					System.out.println(house_selecting.toString());
				}
				@Override
				public void onCancel() {
					//如果用户点击取消，就清除已经选则的全部
					if (house_selecting != null && !"".equals(house_selected)) {
						house_selecting.clear();
					}
					myDialog = null;
					house_selecting = null;
					if (house_selected != null) {
						house_selected.clear();
						houseSelectedAdapter.notifyDataSetChanged();
						realtimetask_gh_selected.setVisibility(View.VISIBLE);
						task_do_btn.setEnabled(false);
					}else {
						house_selected = null;
					}
					//System.out.println(house_selected.toString());
				}
				@Override
				public void onListItemUnChecked(int position, String house) {
					house_selected = null;
					//用户选择一个后，然后又不选了，需要删除
					if (house_selecting.size() >0) {
						house_selecting.remove(house);
					}
					
					//System.out.println(house_selected.toString());
				}
				//以下两个是全选和全不选
				@Override
				public void onListItemAllChecked() {
					house_selected = null;
					house_selecting = null;
					//全选
					if (house_selecting == null) {//单例模式
						house_selecting = new ArrayList<String>();
					}
					for (int i = 0; i < greenHouseNames.size(); i++) {
						house_selecting.add(greenHouseNames.get(i));
					}
					System.out.println(house_selecting.toString());
				}
				@Override
				public void onListItemAllUnChecked() {
					house_selected = null;
					//全不选
					if (house_selecting.size()!=0) {
						house_selecting.clear();
					}
					System.out.println(house_selecting.toString());
				}
				//点击确定按钮
				@Override
				public void onSure() {
					myDialog = null;
					if (house_selecting != null && house_selecting.size()>0) {
						Toast.makeText(getActivity(), "已选择温室", 0).show();
						house_selected = house_selecting;
						house_selecting = null;
						//对选择的温室进行适配
						houseSelectedAdapter = new MyHouseSelectedAdapter();
						show_house_selected.setAdapter(houseSelectedAdapter);
						realtimetask_gh_selected.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
						realtimetask_gh_selected.setVisibility(View.GONE);
						//判断其他的项目是否都选择完成
						if (devices_selected != null && devices_selected.size()>0 ) {
							task_do_btn.setEnabled(true);
						}
					}
					
				}
			});
		}else {
			myDialog.setTitle("选择温室");
		}
		myDialog.show();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				myDialog.initListViewData(greenHouseNames);
			}
		}, 1000);
	}
	/**
	 * 实现对话框获取设备的名称
	 */
	private void chooseDevice() {
		if (myDialog == null) {
			myDialog = new MyNormalDialog(getActivity(), "选择设备",new MyDialogListener() {
				@Override
				public void onListItemChecked(int position, String device) {
					System.out.println("onListItemChecked in choosedevice");
					devices_selected = null;
					if (devices_selecting == null) {
						devices_selecting = new ArrayList<String>();
					}
					devices_selecting.add(device);
					//System.out.println(devices_selected.toString());
				}
				@Override
				public void onListItemUnChecked(int position, String string) {
					devices_selected = null;
					if (devices_selecting.size()>0) {
						devices_selecting.remove(position);
					}
					
					//System.out.println(devices_selected.toString());
				}
				@Override
				public void onCancel() {
					if (devices_selecting != null && !"".equals(devices_selected)) {
						devices_selecting.clear();
						System.out.println(devices_selecting.toString());
					}
					myDialog = null;//将对话框对象置为空，防止下一个使用的时候，出现重复错误
					devices_selecting = null;//当点击取消的时候，释放所占的内存	
					if (devices_selected != null) {
						devices_selected.clear();
						deviceSelectedAdapter.notifyDataSetChanged();
						realtimetask_device_selected.setVisibility(View.VISIBLE);
						task_do_btn.setEnabled(false);
					}else {
						devices_selected = null;
					}
				}
				
				@Override
				public void onListItemAllChecked() {
					devices_selecting = null;//当点击取消的时候，释放所占的内存	
					devices_selected = null;
					//全选
					if (devices_selecting == null) {
						devices_selecting = new ArrayList<String>();
					}
					for (int i = 0; i < devices.size(); i++) {
						devices_selecting.add(devices.get(i));
					}
					System.out.println(devices_selecting.toString());
				}
				@Override
				public void onListItemAllUnChecked() {
					devices_selected = null;
					//全不选
					if (devices_selecting.size()!=0) {
						devices_selecting.clear();
					}
					System.out.println(devices_selecting.toString());
				}
				//点击确定按钮
				@Override
				public void onSure() {
					myDialog = null;
					if (devices_selecting != null && devices_selecting.size()>0) {
						Toast.makeText(getActivity(), "已选择设备", 0).show();
						devices_selected = devices_selecting;
						devices_selecting = null;
						deviceSelectedAdapter = new MyDeviceSelectedAdapter();
						show_device_selected.setAdapter(deviceSelectedAdapter);
						realtimetask_device_selected.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
						realtimetask_device_selected.setVisibility(View.GONE);
						if (house_selected != null && house_selected.size()>0 ) {
							task_do_btn.setEnabled(true);
						}
					}
					
				}
			});
		}
		else {
			myDialog.setTitle("选择设备");
		}
		myDialog.show();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				myDialog.initListViewData(devices);
			}
		}, 1000);
	}
}
