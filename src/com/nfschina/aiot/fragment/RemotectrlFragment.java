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
 * Զ�̿��ƣ��������豸�Ŀ���
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
	private BroadcastReceiver receiver;//�㲥������
	private boolean isSendMsg = false;//�Ƿ����˱���
	private String device_ctrled;//�������û����ѵ�ǰ�Ĳ����Ƿ�ɹ�
	private boolean isOpen = false;//��ǰ�����Ǵ򿪻��ǹر�
	private int index ;//��¼��ǰ������ĸ��豸
	private DBPro dbPro ;//���ݿ�洢����
	private boolean isCheckChange = false;//�رջ��ߴ����豸
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
				Toast.makeText(getActivity(), "����ʧ�ܣ�������������������", 0).show();
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
		// ����Ϊ�˻�ȡ��ǰ���ĸ�����
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
					myProgressDialog_load.setMessage("���ڼ��أ����Ժ�...");
					myProgressDialog_load.show();
					//������������豸�����Լ����豸��״̬
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
		//�󶨷���
		Intent intent = new Intent(getActivity(), SocketService.class);
		conn = new MyConn();
		getActivity().bindService(intent, conn, 0);
		//�㲥�����ߣ�������ר�Ž��շ�ֵ������ɺ󣬷���˷������ĵġ�
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(final Context context, final Intent intent) {// �����յ��㲥֮��
			        	if (isSendMsg) {// �����ǵ�ǰfragment�����˱��ģ�����˷����ı���
							String value = intent.getStringExtra("response");
							System.out.println(value+"  at remotectrl");
							// �ж��Ƿ��Ƿ���˷�������//CRC��֤��
							if (value.length() > 0 && value.length() == 50 && CRC16.getCRC16(value.substring(0, 46).getBytes()).equals(value.substring(46))) {
								if (value.substring(0, 7).equals("TTLKRZ0")) {//Զ�̿��Ʒ�������
									isSendMsg = false;
											try {
												Thread.sleep(5000);
											} catch (InterruptedException e) {
												e.printStackTrace();
											}
									//��ѯ���ݿ��е�ʵʱ����
									new GetDeviceAndStateTask().execute();
								}else if (value.substring(0, 7).equals("TTLKRZV")) {//ʱ�䷴������
									//timefromservice = value.substring(7, 21);
									//notify();
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
		System.out.println("onDestroy");
		if(receiver != null){
			getActivity().unregisterReceiver(receiver);
		}
		getActivity().unbindService(conn);
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
	//ʵ�ֵ���¼�
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.task_look_tv://�鿴����
			Intent intent1 = new Intent(getActivity(), AllTaskActivity.class);
			startActivity(intent1);
			break;
		case R.id.task_create_tv://��������
			Intent intent = new Intent(getActivity(), CreateTaskActivity.class);
			startActivity(intent);
			break;
		}
	}
	/**
	 * ͨ�����ʴ洢���̣���ѯ��ǰ���ҵ��豸���豸״̬
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
				//���ֹر�
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
				//���Կ�ʼ����������
				remote_device_lv.setAdapter(new MyDeviceAdapter());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				myProgressDialog_load.dismiss();
			}else {
				//��û������ʱ��ʾ�Ľ���
				View emptyView = LayoutInflater.from(getActivity()).inflate(R.layout.nodata_view, null);
				emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));  
				((ViewGroup)remote_device_lv.getParent()).addView(emptyView);  
				remote_device_lv.setEmptyView(emptyView);
				myProgressDialog_load.dismiss();//ֹͣˢ��
			}
			if (isCheckChange) {
				myProgressDialog_do.dismiss();
				myProgressDialog_do = null;
				if (isOpen && result.get(index).getState() == 1) {
						//�Ǵ򿪣��������ݿ������״̬Ϊ1
						Toast.makeText(getActivity(), device_ctrled+"�Ѿ���", 0).show();
					}else if (isOpen && result.get(index).getState() == 0) {
						Toast.makeText(getActivity(), device_ctrled+"��ʧ��", 0).show();
					}else if (!isOpen && result.get(index).getState() == 0) {
						Toast.makeText(getActivity(), device_ctrled+"�Ѿ��ر�", 0).show();
					}else if (!isOpen && result.get(index).getState() == 1) {
						Toast.makeText(getActivity(), device_ctrled+"�ر�ʧ��", 0).show();
					}
				isCheckChange = false;
			}
		}
	}
	
	/**
	 * ��ʾ�������豸��������
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
			//�豸��������
			holder.device_item_name.setText(devices.get(position).getDeviceName());
			//�豸״̬����
			if (devices.get(position).getId().equals("1")) {
				holder.device_item_switch.setChecked(true);
			}
			holder.device_item_switch.setTag(position);//Ϊ��onclick��ȡ��ǰ�����λ��
			holder.device_item_switch.setOnCheckedChangeListener(this);//��Ҫ������������¼�
			
			return convertView;
		}
		//��ToggleButton device_item_switch��������ش����ĺ���
		@Override
		public void onCheckedChanged(final CompoundButton buttonView,
				final boolean isChecked) {
				isCheckChange = true;

				if (myProgressDialog_do == null) {
					myProgressDialog_do = new MyProgressDialog(getActivity());
			}
				myProgressDialog_do.setCanceledOnTouchOutside(false);
				myProgressDialog_do.setMessage("���ڲ��������Ժ�...");
			new Thread(new Runnable() {
				@Override
				public void run() {
					//ע��ǰ�����仰
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
					// ��䱨��
					imsg.setHeadMark("TTLK");
					imsg.setType("I");
					imsg.setFromMark("A");
					imsg.setGreenhouseId(greenHouse.getId());
					imsg.setUserId(Constant.CURRENT_USER);
					if (isChecked) {
						// ���豸����
						switch (devices.get(index).getDeviceName()) {
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
						isOpen = true;
					} else {
						// �ر��豸����
						switch (devices.get(index).getDeviceName()) {
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
					isSendMsg = true;// �ѷ�����Ϣ
					device_ctrled = devices.get(index).getDeviceName();
				}
			}).start();
		}
	}
	/**
	 * ��������,��Ҫ�Ƿ�������������ʹ��
	 * @author wujian
	 */
	private class MyHolder{
		@ViewInject(R.id.device_item_name)
		private TextView device_item_name;
		@ViewInject(R.id.device_item_switch)
		private ToggleButton device_item_switch;
	}
	
}
