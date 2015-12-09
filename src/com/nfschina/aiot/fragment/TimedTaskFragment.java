package com.nfschina.aiot.fragment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import com.nfschina.aiot.listener.MyTimePickerListener;
import com.nfschina.aiot.util.DBConnectionUtil;
import com.nfschina.aiot.view.ActionItem;
import com.nfschina.aiot.view.MyDateTimePickDialog;
import com.nfschina.aiot.view.MyGridView;
import com.nfschina.aiot.view.MyNormalDialog;
import com.nfschina.aiot.view.MyPopup;
import com.nfschina.aiot.view.MyProgressDialog;
/**
 * 创建一个定时任务
 * @author wujian
 */
public class TimedTaskFragment extends Fragment implements OnClickListener,OnCheckedChangeListener{
	@ViewInject(R.id.timedtask_greenhouse_rl)
	private RelativeLayout timedtask_greenhouse_rl;
	@ViewInject(R.id.timedtask_device_rl)
	private RelativeLayout timedtask_device_rl;
	@ViewInject(R.id.timedtask_time_rl)
	private RelativeLayout timedtask_time_rl;
	@ViewInject(R.id.task_save_btn)
	private Button task_save_btn;
	@ViewInject(R.id.timedtask_gh_selected)
	private TextView timedtask_gh_selected;
	@ViewInject(R.id.timedtask_time_selected)
	private TextView timedtask_time_selected;
	@ViewInject(R.id.timedtask_device_selected)
	private TextView timedtask_device_selected;
	@ViewInject(R.id.timedtask_time)
	private TextView timedtask_time;
	@ViewInject(R.id.timedtask_tasktype_rg)
	private RadioGroup timedtask_tasktype_rg;
	@ViewInject(R.id.timedtask_taskdo_rg)
	private RadioGroup timedtask_taskdo_rg;
	//以下两个用来显示已经选择的温室和设备
	@ViewInject(R.id.show_house_selected_timed)
	private MyGridView show_house_selected_timed;
	@ViewInject(R.id.show_device_selected_timed)
	private MyGridView show_device_selected_timed;
	//存放温室
	private List<GreenHouse> greenHouses = new ArrayList<GreenHouse>();
	private List<String> greenHouseNames ;
	private Map<String, String> greenhouseMap = new HashMap<String, String>();//这个属性只是为了在发指令的时候获取用户选择的温室的ID
	private List<String> house_selecting ;//存放被选中的温室名称
	private List<String> house_selected ;//存放最终被选中的温室名称
	private List<String> devices;//存放所有的设备名称
	private List<String> devices_selecting;
	private List<String> devices_selected;//存放最终被选中的设备名称
	private MyHouseSelectedAdapter houseSelectedAdapter;
	private MyDeviceSelectedAdapter deviceSelectedAdapter;
	private Map<String, String> devicesMap = new HashMap<String, String>();//存放设备
	private MyNormalDialog myDialog;
	private MyProgressDialog myProgressDialog;
	private MyDateTimePickDialog dateTimePickDialog;
	private List<InstructionMsg> instructions;//需要保存的定时指令
	private DBConnectionUtil connectionUtil;
	private boolean isOpen = true;//操作，打开或者关闭
	private String exeTimes = "once";
	private MyPopup myPopup;
	View parent;
	private Myholder holder;
	private String time_selected;
	SimpleDateFormat format_ori = new SimpleDateFormat("yyyyMMddHHmmss");
	SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	SimpleDateFormat format_hms = new SimpleDateFormat("HH:mm:ss");
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.createtimedtask, null, false);
		ViewUtils.inject(this,view);
		timedtask_gh_selected.setOnClickListener(this);
		timedtask_time_selected.setOnClickListener(this);
		timedtask_device_selected.setOnClickListener(this);
		timedtask_greenhouse_rl.setOnClickListener(this);
		timedtask_device_rl.setOnClickListener(this);
		timedtask_time_rl.setOnClickListener(this);
		task_save_btn.setOnClickListener(this);
		
		timedtask_tasktype_rg.setOnCheckedChangeListener(this);
		timedtask_taskdo_rg.setOnCheckedChangeListener(this);
		
		parent = view;
		return view;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.timedtask_greenhouse_rl://选择温室
			new GetGreenHouseTask().execute();
			break;
		case R.id.timedtask_time_rl://选择时间
			chooseTime();
			break;
		case R.id.timedtask_device_rl://选择设备
			if (devicesMap.size() == 0) {
				new GetAllDeviceTask().execute();
			}else {
				chooseDevice();
			}
			break;
		case R.id.task_save_btn://保存任务
			if (myProgressDialog == null) {
				myProgressDialog = new MyProgressDialog(getActivity());
			}
			myProgressDialog.setCanceledOnTouchOutside(false);
			myProgressDialog.setMessage("正在保存，请稍后...");
			new Thread(new Runnable() {
				@Override
				public void run() {
					//注意前后两句话
					getActivity().getMainLooper().prepare();
					myProgressDialog.show();
					getActivity().getMainLooper().loop();
				}
			}).start();
			
			//执行异步保存操作,直接将list入参
			new SavaInstructionTask().execute();
			break;
		case R.id.timedtask_gh_selected://查看已选温室
			showHouseSelected(house_selected,v);
			break;
		case R.id.timedtask_device_selected://查看已选设备
			showDeviceSelected(devices_selected,v);
			break;
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
	/**
	 * 保存定时任务,针对一个list
	 * @author wujian
	 */
	private class SavaInstructionTask extends AsyncTask<Void, Void, Void>{
		@Override
		protected Void doInBackground(Void... params) {
			synchronized (this) {
				instructions = new ArrayList<InstructionMsg>();
				if (house_selected.size()>0 && devices_selected.size()>0 && time_selected.length() > 0) {
					
							InstructionMsg imsg = new InstructionMsg();
							//填充报文
							//imsg.setHeadMark("TTLK");
							//imsg.setType("I");
							//imsg.setFromMark("A");
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
									imsg.setSendTime(format_ori.format(date));
									imsg.setExecuteTime(time_selected);
									if (exeTimes.equals("once")) {
										imsg.setExeperiod("0");//执行一次
									}else if (exeTimes.equals("everyday")) {
										imsg.setExeperiod("1");//周期执行，每天一次
									}
									//imsg.setCheckNum(CRC16.getCRC16(imsg.getMessageEntity().getBytes()));
									instructions.add(imsg);//生成一条报文字符串
								}
							}
							try {
								//只访问一次数据库就完成了插入操作，避免了一条一条的插入都建立一次数据库连接，提升了性能。
								String sql = "insert into instructiontb (instructioncontent,instructionsendtime,instructionruntime,greenhouseid,userid,runperiod) values ";
								for (InstructionMsg instruction : instructions) {
									String values = " ('"+instruction.getContent()+"','"+instruction.getSendTime()+"','"+instruction.getExecuteTime()+"'," +
											"'"+instruction.getGreenhouseId()+"','"+instruction.getUserId()+"','"+instruction.getExeperiod()+"'),";
									sql = sql + values;
								}//多了一个“,”
								System.out.println(sql);
								connectionUtil = new DBConnectionUtil(sql.substring(0, sql.length()-1));
								connectionUtil.pst.execute();
							} catch (SQLException e) {
								e.printStackTrace();
							}finally{
								connectionUtil.close();
							}//全部生成需要保存的信息
					
				}else {
					Toast.makeText(getActivity(), "温室、设备或时间还未选择，请选择后重试", 1).show();
				}
			}
			
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			myProgressDialog.dismiss();
			
			Toast.makeText(getActivity(), "保存任务完成", 0).show();
			if (house_selected != null) {
				house_selected.clear();
				houseSelectedAdapter.notifyDataSetChanged();
			}
			if (devices_selected != null) {
				devices_selected.clear();
				deviceSelectedAdapter.notifyDataSetChanged();
			}
			if (time_selected.length() > 0) {
				timedtask_time.setVisibility(View.GONE);
			}
			task_save_btn.setEnabled(false);
			timedtask_gh_selected.setVisibility(View.VISIBLE);
			timedtask_time_selected.setVisibility(View.VISIBLE);
			timedtask_device_selected.setVisibility(View.VISIBLE);
			
		}
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
	 * @param devices_selected 最终选择的设备
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
	 * @param house_selected 最终选择的温室
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
	/**
	 * 实现选择时间的方法
	 */
	private void chooseTime() {
		if (dateTimePickDialog == null) {
			dateTimePickDialog = new MyDateTimePickDialog(getActivity(),new MyTimePickerListener(){
				@Override
				public void onCancel() {
					time_selected = "";
				}
				@Override
				public void onSure(String time) {
					if (time != null && time.length() >0) {
						time_selected = time;
						Date date;
						try {
							if (exeTimes.equals("once")) {
								date = format_ori.parse(time_selected);
								 timedtask_time.setVisibility(View.VISIBLE);
								 timedtask_time.setText(format.format(date));
							}else if(exeTimes.equals("everyday")){
								date = format_ori.parse(time_selected);
								 timedtask_time.setVisibility(View.VISIBLE);
								 timedtask_time.setText(format_hms.format(date));
							}
							 
						} catch (ParseException e) {
							e.printStackTrace();
						}
						timedtask_time_selected.setVisibility(View.GONE);
						System.out.println(time_selected);
						if (devices_selected != null && devices_selected.size()>0 && house_selecting != null && house_selecting.size()>0) {
							task_save_btn.setEnabled(true);
						}
					}
				}
			});
			dateTimePickDialog.setTitle("选择时间");
		}
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				dateTimePickDialog.initListViewData();
			}
		}, 1000);
		dateTimePickDialog.show();
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
						timedtask_gh_selected.setVisibility(View.VISIBLE);
						task_save_btn.setEnabled(false);
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
						show_house_selected_timed.setAdapter(houseSelectedAdapter);
						timedtask_gh_selected.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
						timedtask_gh_selected.setVisibility(View.GONE);
						//判断其他的项目是否都选择完成
						if (devices_selected != null && devices_selected.size()>0 ) {
							task_save_btn.setEnabled(true);
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
						timedtask_device_selected.setVisibility(View.VISIBLE);
						task_save_btn.setEnabled(false);
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
						show_device_selected_timed.setAdapter(deviceSelectedAdapter);
						timedtask_device_selected.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
						timedtask_device_selected.setVisibility(View.GONE);
						if (house_selected != null && house_selected.size()>0 ) {
							task_save_btn.setEnabled(true);
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
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.timedtask_on_rb:
			isOpen = true;
			break;

		case R.id.timedtask_off_rb:
			isOpen = false;
			break;
		case R.id.timedtask_onetime_rb:
			exeTimes = "once";
			break;
		case R.id.timedtask_everyday_rb:
			exeTimes = "everyday";
			break;
		
		}
	}
}
