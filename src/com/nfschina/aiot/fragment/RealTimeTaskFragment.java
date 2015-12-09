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
 * ����һ��ʵʱ��������
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
	//��������������ʾ�Ѿ�ѡ������Һ��豸
	@ViewInject(R.id.show_house_selected)
	private MyGridView show_house_selected;
	@ViewInject(R.id.show_device_selected)
	private MyGridView show_device_selected;
	//�������
	private List<GreenHouse> greenHouses = new ArrayList<GreenHouse>();
	private Map<String, String> greenhouseMap = new HashMap<String, String>();//�������ֻ��Ϊ���ڷ�ָ���ʱ���ȡ�û�ѡ������ҵ�ID
	private List<String> greenHouseNames ;
	private List<String> house_selecting ;//��ű�ѡ�е���������
	private List<String> house_selected ;//������ձ�ѡ�е���������
	private List<String> devices;//������е��豸����
	private Map<String, String> devicesMap = new HashMap<String, String>();//����豸
	private List<String> devices_selecting;
	private List<String> devices_selected;//������ձ�ѡ�е��豸����
	private List<String> messages;//����
	private DBConnectionUtil connectionUtil;
	private boolean isOpen = true;//�����򿪻��߹ر�
	private MyNormalDialog myDialog;
	private MyHouseSelectedAdapter houseSelectedAdapter;
	private MyDeviceSelectedAdapter deviceSelectedAdapter;
	
	private MyPopup myPopup;
	private View parent;
	private Myholder holder;
	SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
	private MyConn conn;
	private ISocketService binder;
	private BroadcastReceiver receiver;//�㲥������
	private boolean isSendMsg = false;//�Ƿ����˱���
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
				Toast.makeText(getActivity(), "����ʧ�ܣ�������������������", 0).show();
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
		// �󶨷���
		Intent intent = new Intent(getActivity(), SocketService.class);
		conn = new MyConn();
		getActivity().bindService(intent, conn, 0);
		//�㲥�����ߣ�������ר�Ž��շ�ֵ������ɺ󣬷���˷������ĵġ�
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(final Context context, Intent intent) {// �����յ��㲥֮��
				if (isSendMsg) {// �����ǵ�ǰfragment�����˱��ģ�����˷����ı���
					
					String value = intent.getStringExtra("response");
					System.out.println(value+"in realtime");
					// �ж��Ƿ��Ƿ���˷�������//CRC��֤��
					if (value.substring(0, 6).equals("TTLKRZ")
							&& value.length() == 50
							&& CRC16.getCRC16(value.substring(0, 46).getBytes()).equals(value.substring(46))) {
						receivecount++;
						System.out.println("����������"+receivecount);
						System.out.println("Ӧ��������"+(house_selected.size() * devices_selected.size()));
						if (receivecount == (house_selected.size() * devices_selected.size())) {
							System.out.println(receivecount+"in realtime");
							// �����߳�������½���
							getActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
									myProgressDialog.dismiss();// �յ��������˵Ļظ���Ϣ֮������ʧ
									Toast.makeText(context, "ִ�����", 0).show();
									receivecount = 0;
									isSendMsg = false;
									//������ɺ������ѡ�б�
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
		// �㲥����������ʾ������ʲô���Ĺ㲥
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
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.realtimetask_choosehouse_iv://ѡ������
			if (greenHouses.size()==0 && greenhouseMap.size()==0) {
				new GetGreenHouseTask().execute();
			}else {
				chooseHouse();
			}
			
			break;
		case R.id.realtimetask_choosedevice_iv://ѡ���豸
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
		case R.id.realtimetask_device_rl://ѡ���豸
			if (devicesMap.size() == 0) {
				new GetAllDeviceTask().execute();
			}else {
				chooseDevice();
			}
			break;
		case R.id.realtimetask_gh_selected://�鿴��ѡ����
			showHouseSelected(house_selected,v);
			break;
		case R.id.realtimetask_device_selected://�鿴��ѡ�豸
			showDeviceSelected(devices_selected,v);
			break;
		case R.id.task_do_btn://ִ������
			if (myProgressDialog == null) {
				myProgressDialog = new MyProgressDialog(getActivity());
			}
			myProgressDialog.setCanceledOnTouchOutside(false);
			myProgressDialog.setMessage("����ִ�У����Ժ�...");
			new Thread(new Runnable() {
				@Override
				public void run() {
					//ע��ǰ�����仰
					getActivity().getMainLooper().prepare();
					myProgressDialog.show();
					getActivity().getMainLooper().loop();
				}
			}).start();
			//��ʱ�Ĳ����ŵ������߳�����
			new Thread(new Runnable() {
				@Override
				public void run() {
						messages = new ArrayList<String>();
						if (house_selected.size()>0 && devices_selected.size()>0) {
							InstructionMsg imsg = new InstructionMsg();
							//��䱨��
							imsg.setHeadMark("TTLK");
							imsg.setType("I");
							imsg.setFromMark("A");
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
									imsg.setSendTime(dateformat.format(date));
									imsg.setExecuteTime(dateformat.format(date));
									imsg.setExeperiod("0");
									imsg.setCheckNum(CRC16.getCRC16(imsg.getMessageEntity().getBytes()));
									messages.add(imsg.toString());//����һ�������ַ���
								}
							}
							for (String message : messages) {
								//��ʼ��һ���ͱ���
								binder.sentMsg(message.getBytes(), handler);
								System.out.println(message);
								isSendMsg = true;//������
								
								try {
									//����һ�Σ���ͣһ��
									Thread.sleep(500);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
							
						}else {
							Toast.makeText(getActivity(), "���һ����豸��δѡ����ѡ�������", 1).show();
						}
					}
			}).start();
			
			break;
		}
	}
	/**
	 * ���������¼�
	 * @author wujian
	 */
	private class MyConn implements ServiceConnection{
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			//��õ��˷����н飬���Ե��÷����еķ�����
			binder = (ISocketService) service;
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {
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
	 * @param devices_selected2
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
	 * @param house_selected2
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
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.createtask_on_rb://��
			isOpen = true;
			break;
		case R.id.createtask_off_rb://�ر�
			isOpen = false;
			break;
		}
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
						show_house_selected.setAdapter(houseSelectedAdapter);
						realtimetask_gh_selected.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//�»���
						realtimetask_gh_selected.setVisibility(View.GONE);
						//�ж���������Ŀ�Ƿ�ѡ�����
						if (devices_selected != null && devices_selected.size()>0 ) {
							task_do_btn.setEnabled(true);
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
						realtimetask_device_selected.setVisibility(View.VISIBLE);
						task_do_btn.setEnabled(false);
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
						show_device_selected.setAdapter(deviceSelectedAdapter);
						realtimetask_device_selected.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//�»���
						realtimetask_device_selected.setVisibility(View.GONE);
						if (house_selected != null && house_selected.size()>0 ) {
							task_do_btn.setEnabled(true);
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
}
