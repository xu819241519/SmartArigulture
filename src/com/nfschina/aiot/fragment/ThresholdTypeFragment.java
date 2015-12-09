package com.nfschina.aiot.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nfschina.aiot.activity.R;
import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.entity.GreenHouse;
import com.nfschina.aiot.entity.Threshold;
import com.nfschina.aiot.entity.ThresholdMsg;
import com.nfschina.aiot.socket.Const;
import com.nfschina.aiot.socket.ISocketService;
import com.nfschina.aiot.socket.SocketService;
import com.nfschina.aiot.util.CRC16;
import com.nfschina.aiot.view.MyProgressDialog;
/**
 * ��̬���ɵ�fragment���ڴ�����ֵ��ʱ�򣬶�̬���ɶ�Ӧ�ķ�ֵ���ý���
 * @author wujian
 */
public class ThresholdTypeFragment extends Fragment implements OnClickListener {
	@ViewInject(R.id.toptype)
	private TextView toptype;
	@ViewInject(R.id.threshold_max_et)
	private EditText threshold_max_et;
	@ViewInject(R.id.threshold_min_et)
	private EditText threshold_min_et;
	@ViewInject(R.id.threshold_min_recommend)
	private TextView threshold_min_recommend;
	@ViewInject(R.id.threshold_max_recommend)
	private TextView threshold_max_recommend;
	@ViewInject(R.id.threshold_btn_reset)
	private Button threshold_btn_reset;
	@ViewInject(R.id.threshold_btn_sure)
	private Button threshold_btn_sure;
	private String typename;//�¶ȣ�������̼����������
	private GreenHouse greenhouse;//������һ��Activity���ݵ�������Ϣ
	private Threshold threshold;
	private ISocketService binder;//�󶨷�����н�
	private MyConn conn;//�����������
	private BroadcastReceiver receiver;//�㲥������
	private ThresholdMsg thresholdMsg = new ThresholdMsg();//��ֵ���ñ���
	private boolean isSendMsg = false;//�Ƿ����˱���
	private String timefromservice;//���ڼ�¼�ӷ���˷���������ʱ��
	private MyProgressDialog myProgressDialog;
	SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
	private Handler handler = new Handler(){
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
				threshold_max_et.setEnabled(false);
				threshold_min_et.setEnabled(false);
				threshold_btn_sure.setEnabled(false);
				Toast.makeText(getActivity(), "����ʧ�ܣ�������������������", 0).show();
				break;
			}
		};
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_pro_type, null);
		ViewUtils.inject(this,view);
		typename=getArguments().getString("typename");
		threshold = (Threshold) getArguments().getSerializable("threshold");
		toptype.setText(typename);
		switch (typename) {
		case "�¶�":
			threshold_max_et.setText(threshold.getUptem()+"");
			threshold_min_et.setText(threshold.getLowtem()+"");
			threshold_min_recommend.setText("��λ���棻���˷�Χ��0~90");
			threshold_max_recommend.setText("��λ���棻���˷�Χ��0~90");
			break;
		case "������̼":
			threshold_max_et.setText(threshold.getUpco2()+"");
			threshold_min_et.setText(threshold.getLowco2()+"");
			threshold_min_recommend.setText("��λ��ppm�����˷�Χ��0~5000");
			threshold_max_recommend.setText("��λ��ppm�����˷�Χ��0~5000");
			break;
		case "ʪ��":
			threshold_max_et.setText(threshold.getUphum()+"");
			threshold_min_et.setText(threshold.getLowhum()+"");
			threshold_min_recommend.setText("��λ��%�����˷�Χ��0~90");
			threshold_max_recommend.setText("��λ��%�����˷�Χ��0~90");
			break;
		case "����":
			threshold_max_et.setText(threshold.getUpillum()+"");
			threshold_min_et.setText(threshold.getLowillum()+"");
			threshold_min_recommend.setText("��λ��xl�����˷�Χ��0~20000");
			threshold_max_recommend.setText("��λ��xl�����˷�Χ��0~20000");
			break;
		}
		//��ȡ������Ϣ
		Bundle bundle = getActivity().getIntent().getExtras();
		if (bundle != null) {
			greenhouse = (GreenHouse) bundle.get("greenhouse");
		}
		threshold_btn_reset.setOnClickListener(this);
		threshold_btn_sure.setOnClickListener(this);
		
		return view;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.threshold_btn_reset:
			threshold_max_et.setEnabled(true);
			threshold_min_et.setEnabled(true);
			threshold_btn_sure.setEnabled(true);
			break;
		case R.id.threshold_btn_sure://ȷ���޸�
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
			Pattern pattern = Pattern.compile("[\\d]+\\.[\\d]+");//�ж��Ƿ�Ϊ������
			String max_threshold = threshold_max_et.getText().toString().trim();
			String min_threshold = threshold_min_et.getText().toString().trim();
			if (pattern.matcher(max_threshold).matches()) {
				max_threshold = (int)Float.parseFloat(max_threshold)+"";
			}//������ת�������Σ���ת�����ַ���
			if (pattern.matcher(min_threshold).matches()) {
				min_threshold = (int)Float.parseFloat(min_threshold)+"";
			}
			if (Integer.parseInt(max_threshold)<=Integer.parseInt(min_threshold)) {
				Toast.makeText(getActivity(), "���ֵ����С�ڻ��ߵ�����С��ֵ", 1).show();
				break;
			}
			if (max_threshold.length() == 0 || min_threshold.length() == 0) {
				Toast.makeText(getActivity(), "������С��ֵ������Ϊ��", 1).show();
				break;
			}
			
			thresholdMsg.setHeadMark("TTLK");
			thresholdMsg.setType("T");
			thresholdMsg.setFromMark("A");
			thresholdMsg.setUserId(Constant.CURRENT_USER);
//			binder.sentMsg(("TTLKQAT000000000000000000000"+CRC16.getCRC16("TTLKQAT000000000000000000000".getBytes())).getBytes(), handler);
//			isSendMsg = true;
			Date date = new Date();
			thresholdMsg.setUpdateTime(dateformat.format(date));
			thresholdMsg.setExtendPart("0000000000000000");
			thresholdMsg.setGreenhouseId(greenhouse.getId());
			switch (typename) {
			case "�¶�":
				//��ֵ���ֵ
				if (max_threshold.length() >= 4) {
					thresholdMsg.setTemperatureMax(max_threshold.substring(0, 4));
				}else if (max_threshold.length() == 3) {
					thresholdMsg.setTemperatureMax("0"+max_threshold);
				}else if (max_threshold.length() == 2) {
					thresholdMsg.setTemperatureMax("00"+max_threshold);
				}else if (max_threshold.length() == 1) {
					thresholdMsg.setTemperatureMax("000"+max_threshold);
				}else if (max_threshold.length() == 0) {
					thresholdMsg.setTemperatureMax("NNNN");
				}
				//��ֵ��Сֵ
				if (min_threshold.length()>=4) {
					thresholdMsg.setTemperatureMin(min_threshold.substring(0, 4));
				}else if (min_threshold.length() == 3) {
					thresholdMsg.setTemperatureMin("0"+min_threshold);
				}else if (min_threshold.length() == 2) {
					thresholdMsg.setTemperatureMin("00"+min_threshold);
				}else if (min_threshold.length() == 1) {
					thresholdMsg.setTemperatureMin("000"+min_threshold);
				}else if (min_threshold.length() == 0) {
					thresholdMsg.setTemperatureMin("NNNN");
				}
				//�������������صķ�ֵ����λ����ΪNNNN
				thresholdMsg.setCo2Max("NNNN");
				thresholdMsg.setCo2Min("NNNN");
				thresholdMsg.setLightMax("NNNNN");
				thresholdMsg.setLightMin("NNNNN");
				thresholdMsg.setWaterMax("NN");
				thresholdMsg.setWaterMin("NN");
				break;
			case "������̼":
				//��ֵ���ֵ
				if (max_threshold.length() >= 4) {
					thresholdMsg.setCo2Max(max_threshold.substring(0, 4));
				}else if (max_threshold.length() == 3) {
					thresholdMsg.setCo2Max("0"+max_threshold);
				}else if (max_threshold.length() == 2) {
					thresholdMsg.setCo2Max("00"+max_threshold);
				}else if (max_threshold.length() == 1) {
					thresholdMsg.setCo2Max("000"+max_threshold);
				}else if (max_threshold.length() == 0) {
					thresholdMsg.setCo2Max("NNNN");
				}
				//��ֵ��Сֵ
				if (min_threshold.length()>=4) {
					thresholdMsg.setCo2Min(min_threshold.substring(0, 4));
				}else if (min_threshold.length() == 3) {
					thresholdMsg.setCo2Min("0"+min_threshold);
				}else if (min_threshold.length() == 2) {
					thresholdMsg.setCo2Min("00"+min_threshold);
				}else if (min_threshold.length() == 1) {
					thresholdMsg.setCo2Min("000"+min_threshold);
				}else if (min_threshold.length() == 0) {
					thresholdMsg.setCo2Min("NNNN");
				}
				//�������������صķ�ֵ����λ����ΪNNNN
				thresholdMsg.setTemperatureMax("NNNN");
				thresholdMsg.setTemperatureMin("NNNN");
				thresholdMsg.setLightMax("NNNNN");
				thresholdMsg.setLightMin("NNNNN");
				thresholdMsg.setWaterMax("NN");
				thresholdMsg.setWaterMin("NN");
				break;
			case "ʪ��":
				//��ֵ���ֵ
				if (max_threshold.length() >= 2) {
					thresholdMsg.setWaterMax(max_threshold.substring(0, 2));
				}else if (max_threshold.length() == 1) {
					thresholdMsg.setWaterMax("0"+max_threshold);
				}else if (max_threshold.length() == 0) {
					thresholdMsg.setWaterMax("NN");
				}
				//��ֵ��Сֵ
				if (min_threshold.length()>=2) {
					thresholdMsg.setWaterMin(min_threshold.substring(0, 2));
				}else if (min_threshold.length() == 1) {
					thresholdMsg.setWaterMin("0"+min_threshold);
				}else if (min_threshold.length() == 0) {
					thresholdMsg.setWaterMin("NN");
				}
				//�������������صķ�ֵ����λ����ΪNNNN
				thresholdMsg.setTemperatureMax("NNNN");
				thresholdMsg.setTemperatureMin("NNNN");
				thresholdMsg.setLightMax("NNNNN");
				thresholdMsg.setLightMin("NNNNN");
				thresholdMsg.setCo2Max("NNNN");
				thresholdMsg.setCo2Min("NNNN");
				break;
			case "����":
				//��ֵ���ֵ
				if (max_threshold.length() >= 5) {
					thresholdMsg.setLightMax(max_threshold.substring(0, 5));
				}else if (max_threshold.length() == 4) {
					thresholdMsg.setLightMax("0"+max_threshold);
				}else if (max_threshold.length() == 3) {
					thresholdMsg.setLightMax("00"+max_threshold);
				}else if (max_threshold.length() == 2) {
					thresholdMsg.setLightMax("000"+max_threshold);
				}else if (max_threshold.length() == 1) {
					thresholdMsg.setLightMax("0000"+max_threshold);
				}else if (max_threshold.length() == 0) {
					thresholdMsg.setLightMax("NNNNN");
				}
				//��ֵ��Сֵ
				if (min_threshold.length()>=5) {
					thresholdMsg.setLightMin(min_threshold.substring(0, 5));
				}else if (min_threshold.length() == 4) {
					thresholdMsg.setLightMin("0"+min_threshold);
				}else if (min_threshold.length() == 3) {
					thresholdMsg.setLightMin("00"+min_threshold);
				}else if (min_threshold.length() == 2) {
					thresholdMsg.setLightMin("000"+min_threshold);
				}else if (min_threshold.length() == 1) {
					thresholdMsg.setLightMin("0000"+min_threshold);
				}else if (min_threshold.length() == 0) {
					thresholdMsg.setLightMin("NNNNN");
				}
				//�������������صķ�ֵ����λ����ΪNNNN
				thresholdMsg.setTemperatureMax("NNNN");
				thresholdMsg.setTemperatureMin("NNNN");
				thresholdMsg.setCo2Max("NNNN");
				thresholdMsg.setCo2Min("NNNN");
				thresholdMsg.setWaterMax("NN");
				thresholdMsg.setWaterMin("NN");
				break;
				
			}
			
			thresholdMsg.setCheckNum(CRC16.getCRC16(thresholdMsg.getMsgEntity().getBytes()));
			//���ͱ���
			System.out.println(thresholdMsg.toString()+"   ���ȣ�"+thresholdMsg.toString().length());
			binder.sentMsg(thresholdMsg.toString().getBytes(), handler);
			isSendMsg = true;
			//������Ϊ���ɵ�
			threshold_max_et.setEnabled(false);
			threshold_min_et.setEnabled(false);
			threshold_btn_sure.setEnabled(false);
			break;
		}
		
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		//�󶨷���
		Intent intent = new Intent(getActivity(), SocketService.class);
		conn = new MyConn();
		getActivity().bindService(intent, conn, 0);
		//�㲥�����ߣ�������ר�Ž��շ�ֵ������ɺ󣬷���˷������ĵġ�
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(final Context context, Intent intent) {// �����յ��㲥֮��
				if (isSendMsg) {//�����ǵ�ǰfragment�����˱��ģ�����˷����ı���
					isSendMsg = false;
					String value = intent.getStringExtra("response");
					System.out.println(value   +"   at threshold ");
					// �ж��Ƿ��Ƿ���˷�������//CRC��֤��
					if (value.length() > 0 && value.length() == 50 && CRC16.getCRC16(value.substring(0, 46).getBytes()).equals(value.substring(46))) {
						if (value.substring(0, 7).equals("TTLKRZ0")) {//Զ�̿��Ʒ�������
							// �����߳�������½���
							getActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									myProgressDialog.dismiss();//�յ��������˵Ļظ���Ϣ֮������ʧ
									Toast.makeText(context, "��ֵ���óɹ�", 0).show();
								}
							});
						}else if (value.substring(0, 7).equals("TTLKRZV")) {//ʱ�䷴������
							timefromservice = value.substring(7, 21);
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
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(receiver != null){
			getActivity().unregisterReceiver(receiver);
		}
		//������
		getActivity().unbindService(conn);
	}
}
