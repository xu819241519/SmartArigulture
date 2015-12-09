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
 * ����һ����ʱ����
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
	//��������������ʾ�Ѿ�ѡ������Һ��豸
	@ViewInject(R.id.show_house_selected_timed)
	private MyGridView show_house_selected_timed;
	@ViewInject(R.id.show_device_selected_timed)
	private MyGridView show_device_selected_timed;
	//�������
	private List<GreenHouse> greenHouses = new ArrayList<GreenHouse>();
	private List<String> greenHouseNames ;
	private Map<String, String> greenhouseMap = new HashMap<String, String>();//�������ֻ��Ϊ���ڷ�ָ���ʱ���ȡ�û�ѡ������ҵ�ID
	private List<String> house_selecting ;//��ű�ѡ�е���������
	private List<String> house_selected ;//������ձ�ѡ�е���������
	private List<String> devices;//������е��豸����
	private List<String> devices_selecting;
	private List<String> devices_selected;//������ձ�ѡ�е��豸����
	private MyHouseSelectedAdapter houseSelectedAdapter;
	private MyDeviceSelectedAdapter deviceSelectedAdapter;
	private Map<String, String> devicesMap = new HashMap<String, String>();//����豸
	private MyNormalDialog myDialog;
	private MyProgressDialog myProgressDialog;
	private MyDateTimePickDialog dateTimePickDialog;
	private List<InstructionMsg> instructions;//��Ҫ����Ķ�ʱָ��
	private DBConnectionUtil connectionUtil;
	private boolean isOpen = true;//�������򿪻��߹ر�
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
		case R.id.timedtask_greenhouse_rl://ѡ������
			new GetGreenHouseTask().execute();
			break;
		case R.id.timedtask_time_rl://ѡ��ʱ��
			chooseTime();
			break;
		case R.id.timedtask_device_rl://ѡ���豸
			if (devicesMap.size() == 0) {
				new GetAllDeviceTask().execute();
			}else {
				chooseDevice();
			}
			break;
		case R.id.task_save_btn://��������
			if (myProgressDialog == null) {
				myProgressDialog = new MyProgressDialog(getActivity());
			}
			myProgressDialog.setCanceledOnTouchOutside(false);
			myProgressDialog.setMessage("���ڱ��棬���Ժ�...");
			new Thread(new Runnable() {
				@Override
				public void run() {
					//ע��ǰ�����仰
					getActivity().getMainLooper().prepare();
					myProgressDialog.show();
					getActivity().getMainLooper().loop();
				}
			}).start();
			
			//ִ���첽�������,ֱ�ӽ�list���
			new SavaInstructionTask().execute();
			break;
		case R.id.timedtask_gh_selected://�鿴��ѡ����
			showHouseSelected(house_selected,v);
			break;
		case R.id.timedtask_device_selected://�鿴��ѡ�豸
			showDeviceSelected(devices_selected,v);
			break;
		}
	}
	/**
	 * ��ȡ���ݿ���ȫ���豸id������
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
			devices = new ArrayList<String>(devicesMap.values());//ֱ�ӻ�ȡMap��value��ת����һ��list�ķ���
			chooseDevice();
		}
	}
	/**
	 * ���涨ʱ����,���һ��list
	 * @author wujian
	 */
	private class SavaInstructionTask extends AsyncTask<Void, Void, Void>{
		@Override
		protected Void doInBackground(Void... params) {
			synchronized (this) {
				instructions = new ArrayList<InstructionMsg>();
				if (house_selected.size()>0 && devices_selected.size()>0 && time_selected.length() > 0) {
					
							InstructionMsg imsg = new InstructionMsg();
							//��䱨��
							//imsg.setHeadMark("TTLK");
							//imsg.setType("I");
							//imsg.setFromMark("A");
							imsg.setUserId(Constant.CURRENT_USER);
							Date date = new Date();
							for (String greenhouse : house_selected) {
								imsg.setGreenhouseId(greenhouseMap.get(greenhouse));//��ȡ����ID
								for (String device : devices_selected) {
									if (isOpen) {
										//���豸����
										switch (device) {
										case "��Ȼ�":
											imsg.setContent("A1");
											break;
										case "������":
											imsg.setContent("B1");
											break;
										case "CO2������":
											imsg.setContent("C1");
											break;
										case "�ȷ��":
											imsg.setContent("D1");
											break;
										case "ͨ����":
											imsg.setContent("E1");
											break;
										}
									}else {
										//�ر��豸����
										switch (device) {
										case "��Ȼ�":
											imsg.setContent("A0");
											break;
										case "������":
											imsg.setContent("B0");
											break;
										case "CO2������":
											imsg.setContent("C0");
											break;
										case "�ȷ��":
											imsg.setContent("D0");
											break;
										case "ͨ����":
											imsg.setContent("E0");
											break;
										}
									}
									imsg.setSendTime(format_ori.format(date));
									imsg.setExecuteTime(time_selected);
									if (exeTimes.equals("once")) {
										imsg.setExeperiod("0");//ִ��һ��
									}else if (exeTimes.equals("everyday")) {
										imsg.setExeperiod("1");//����ִ�У�ÿ��һ��
									}
									//imsg.setCheckNum(CRC16.getCRC16(imsg.getMessageEntity().getBytes()));
									instructions.add(imsg);//����һ�������ַ���
								}
							}
							try {
								//ֻ����һ�����ݿ������˲��������������һ��һ���Ĳ��붼����һ�����ݿ����ӣ����������ܡ�
								String sql = "insert into instructiontb (instructioncontent,instructionsendtime,instructionruntime,greenhouseid,userid,runperiod) values ";
								for (InstructionMsg instruction : instructions) {
									String values = " ('"+instruction.getContent()+"','"+instruction.getSendTime()+"','"+instruction.getExecuteTime()+"'," +
											"'"+instruction.getGreenhouseId()+"','"+instruction.getUserId()+"','"+instruction.getExeperiod()+"'),";
									sql = sql + values;
								}//����һ����,��
								System.out.println(sql);
								connectionUtil = new DBConnectionUtil(sql.substring(0, sql.length()-1));
								connectionUtil.pst.execute();
							} catch (SQLException e) {
								e.printStackTrace();
							}finally{
								connectionUtil.close();
							}//ȫ��������Ҫ�������Ϣ
					
				}else {
					Toast.makeText(getActivity(), "���ҡ��豸��ʱ�仹δѡ����ѡ�������", 1).show();
				}
			}
			
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			myProgressDialog.dismiss();
			
			Toast.makeText(getActivity(), "�����������", 0).show();
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
	 * �첽��ʽ��ȡ���ҵķ�ʽ
	 * @author wujian
	 */
	private class GetGreenHouseTask extends AsyncTask<Void, Void, List<GreenHouse>>{
		@Override
		protected List<GreenHouse> doInBackground(Void... params) {
			//��ҳ��ѯ
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
	 * Ϊ����ʾ�Ѿ�ѡ�������
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
	 * Ϊ����ʾ�Ѿ�ѡ����豸
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
	 * �鿴�Ѿ�ѡ����豸�ķ���
	 * @param v ���ǵ����view
	 * @param devices_selected ����ѡ����豸
	 */
	private void showDeviceSelected(List<String> devicesSelected, View v) {
		if (devicesSelected != null) {//��ѡ�Ϊ�յ�ʱ�򣬲Ż���ʾ����
			ActionItem actionItem ;
			//ʵ��������
			myPopup = new MyPopup(getActivity(),parent.getWidth()-20, LayoutParams.WRAP_CONTENT);
			for (int i = 0; i < devicesSelected.size(); i++) {
				actionItem = new ActionItem(devicesSelected.get(i));
				myPopup.addAction(actionItem);
			}
			myPopup.show(v);
		}else {
			Toast.makeText(getActivity(), "��δѡ���豸", 0).show();
		}
	}
	/**
	 * �鿴�Ѿ�ѡ�������
	 * @param v ���ǵ����view
	 * @param house_selected ����ѡ�������
	 */
	private void showHouseSelected(List<String> houseSelected, View v) {
		if (houseSelected != null) {//��ѡ�Ϊ�յ�ʱ�򣬲Ż���ʾ����
			ActionItem actionItem ;
			//ʵ��������
			myPopup = new MyPopup(getActivity(),parent.getWidth()-20, LayoutParams.WRAP_CONTENT);
			for (int i = 0; i < houseSelected.size(); i++) {
				actionItem = new ActionItem(houseSelected.get(i));
				myPopup.addAction(actionItem);
			}
			myPopup.show(v);
		}else {
			Toast.makeText(getActivity(), "��δѡ������", 0).show();
		}
	}
	/**
	 * ʵ��ѡ��ʱ��ķ���
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
			dateTimePickDialog.setTitle("ѡ��ʱ��");
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
	 * ʵ�ֶԻ���ѡ�����ҵķ���
	 */
	private void chooseHouse() {
		if (myDialog == null) {
			myDialog = new MyNormalDialog(getActivity(), "ѡ������",new MyDialogListener() {
				//ÿ��ѡ��һ��checkbox��ʱ�򣬴����������
				@Override
				public void onListItemChecked(int position, String house) {
					System.out.println("onListItemChecked in choosehouse");
					house_selected = null;
					if (house_selecting == null) {//����ģʽ
						house_selecting = new ArrayList<String>();
					}
					//�Ѿ�ѡ�еĴ��������
					house_selecting.add(house);
					System.out.println(house_selecting.toString());
				}
				@Override
				public void onCancel() {
					//����û����ȡ����������Ѿ�ѡ���ȫ��
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
					//�û�ѡ��һ����Ȼ���ֲ�ѡ�ˣ���Ҫɾ��
					if (house_selecting.size() >0) {
						house_selecting.remove(house);
					}
					
					//System.out.println(house_selected.toString());
				}
				//����������ȫѡ��ȫ��ѡ
				@Override
				public void onListItemAllChecked() {
					house_selected = null;
					house_selecting = null;
					//ȫѡ
					if (house_selecting == null) {//����ģʽ
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
					//ȫ��ѡ
					if (house_selecting.size()!=0) {
						house_selecting.clear();
					}
					System.out.println(house_selecting.toString());
				}
				//���ȷ����ť
				@Override
				public void onSure() {
					myDialog = null;
					if (house_selecting != null && house_selecting.size()>0) {
						Toast.makeText(getActivity(), "��ѡ������", 0).show();
						house_selected = house_selecting;
						house_selecting = null;
						//��ѡ������ҽ�������
						houseSelectedAdapter = new MyHouseSelectedAdapter();
						show_house_selected_timed.setAdapter(houseSelectedAdapter);
						timedtask_gh_selected.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//�»���
						timedtask_gh_selected.setVisibility(View.GONE);
						//�ж���������Ŀ�Ƿ�ѡ�����
						if (devices_selected != null && devices_selected.size()>0 ) {
							task_save_btn.setEnabled(true);
						}
					}
				}
			});
		}else {
			myDialog.setTitle("ѡ������");
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
	 * ʵ�ֶԻ����ȡ�豸������
	 */
	private void chooseDevice() {
		if (myDialog == null) {
			myDialog = new MyNormalDialog(getActivity(), "ѡ���豸",new MyDialogListener() {
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
					myDialog = null;//���Ի��������Ϊ�գ���ֹ��һ��ʹ�õ�ʱ�򣬳����ظ�����
					devices_selecting = null;//�����ȡ����ʱ���ͷ���ռ���ڴ�	
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
					devices_selecting = null;//�����ȡ����ʱ���ͷ���ռ���ڴ�	
					devices_selected = null;
					//ȫѡ
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
					//ȫ��ѡ
					if (devices_selecting.size()!=0) {
						devices_selecting.clear();
					}
					System.out.println(devices_selecting.toString());
				}
				//���ȷ����ť
				@Override
				public void onSure() {
					myDialog = null;
					if (devices_selecting != null && devices_selecting.size()>0) {
						Toast.makeText(getActivity(), "��ѡ���豸", 0).show();
						devices_selected = devices_selecting;
						devices_selecting = null;
						deviceSelectedAdapter = new MyDeviceSelectedAdapter();
						show_device_selected_timed.setAdapter(deviceSelectedAdapter);
						timedtask_device_selected.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//�»���
						timedtask_device_selected.setVisibility(View.GONE);
						if (house_selected != null && house_selected.size()>0 ) {
							task_save_btn.setEnabled(true);
						}
					}
				}
			});
		}
		else {
			myDialog.setTitle("ѡ���豸");
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
