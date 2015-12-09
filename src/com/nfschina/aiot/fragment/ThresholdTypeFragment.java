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
 * 动态生成的fragment，在创建阀值的时候，动态生成对应的阀值设置界面
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
	private String typename;//温度，二氧化碳。。。。。
	private GreenHouse greenhouse;//接收上一个Activity传递的温室信息
	private Threshold threshold;
	private ISocketService binder;//绑定服务的中介
	private MyConn conn;//与服务建立连接
	private BroadcastReceiver receiver;//广播接收者
	private ThresholdMsg thresholdMsg = new ThresholdMsg();//阀值设置报文
	private boolean isSendMsg = false;//是否发送了报文
	private String timefromservice;//用于记录从服务端反馈回来的时间
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
				Toast.makeText(getActivity(), "发送失败，请检查服务器连接情况！", 0).show();
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
		case "温度":
			threshold_max_et.setText(threshold.getUptem()+"");
			threshold_min_et.setText(threshold.getLowtem()+"");
			threshold_min_recommend.setText("单位：℃；适宜范围：0~90");
			threshold_max_recommend.setText("单位：℃；适宜范围：0~90");
			break;
		case "二氧化碳":
			threshold_max_et.setText(threshold.getUpco2()+"");
			threshold_min_et.setText(threshold.getLowco2()+"");
			threshold_min_recommend.setText("单位：ppm；适宜范围：0~5000");
			threshold_max_recommend.setText("单位：ppm；适宜范围：0~5000");
			break;
		case "湿度":
			threshold_max_et.setText(threshold.getUphum()+"");
			threshold_min_et.setText(threshold.getLowhum()+"");
			threshold_min_recommend.setText("单位：%；适宜范围：0~90");
			threshold_max_recommend.setText("单位：%；适宜范围：0~90");
			break;
		case "光照":
			threshold_max_et.setText(threshold.getUpillum()+"");
			threshold_min_et.setText(threshold.getLowillum()+"");
			threshold_min_recommend.setText("单位：xl；适宜范围：0~20000");
			threshold_max_recommend.setText("单位：xl；适宜范围：0~20000");
			break;
		}
		//获取温室信息
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
		case R.id.threshold_btn_sure://确定修改
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
			Pattern pattern = Pattern.compile("[\\d]+\\.[\\d]+");//判断是否为浮点数
			String max_threshold = threshold_max_et.getText().toString().trim();
			String min_threshold = threshold_min_et.getText().toString().trim();
			if (pattern.matcher(max_threshold).matches()) {
				max_threshold = (int)Float.parseFloat(max_threshold)+"";
			}//浮点数转化成整形，在转化成字符串
			if (pattern.matcher(min_threshold).matches()) {
				min_threshold = (int)Float.parseFloat(min_threshold)+"";
			}
			if (Integer.parseInt(max_threshold)<=Integer.parseInt(min_threshold)) {
				Toast.makeText(getActivity(), "最大阀值不能小于或者等于最小阀值", 1).show();
				break;
			}
			if (max_threshold.length() == 0 || min_threshold.length() == 0) {
				Toast.makeText(getActivity(), "最大和最小阀值都不能为空", 1).show();
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
			case "温度":
				//阀值最大值
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
				//阀值最小值
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
				//将其他环境因素的阀值报文位都置为NNNN
				thresholdMsg.setCo2Max("NNNN");
				thresholdMsg.setCo2Min("NNNN");
				thresholdMsg.setLightMax("NNNNN");
				thresholdMsg.setLightMin("NNNNN");
				thresholdMsg.setWaterMax("NN");
				thresholdMsg.setWaterMin("NN");
				break;
			case "二氧化碳":
				//阀值最大值
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
				//阀值最小值
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
				//将其他环境因素的阀值报文位都置为NNNN
				thresholdMsg.setTemperatureMax("NNNN");
				thresholdMsg.setTemperatureMin("NNNN");
				thresholdMsg.setLightMax("NNNNN");
				thresholdMsg.setLightMin("NNNNN");
				thresholdMsg.setWaterMax("NN");
				thresholdMsg.setWaterMin("NN");
				break;
			case "湿度":
				//阀值最大值
				if (max_threshold.length() >= 2) {
					thresholdMsg.setWaterMax(max_threshold.substring(0, 2));
				}else if (max_threshold.length() == 1) {
					thresholdMsg.setWaterMax("0"+max_threshold);
				}else if (max_threshold.length() == 0) {
					thresholdMsg.setWaterMax("NN");
				}
				//阀值最小值
				if (min_threshold.length()>=2) {
					thresholdMsg.setWaterMin(min_threshold.substring(0, 2));
				}else if (min_threshold.length() == 1) {
					thresholdMsg.setWaterMin("0"+min_threshold);
				}else if (min_threshold.length() == 0) {
					thresholdMsg.setWaterMin("NN");
				}
				//将其他环境因素的阀值报文位都置为NNNN
				thresholdMsg.setTemperatureMax("NNNN");
				thresholdMsg.setTemperatureMin("NNNN");
				thresholdMsg.setLightMax("NNNNN");
				thresholdMsg.setLightMin("NNNNN");
				thresholdMsg.setCo2Max("NNNN");
				thresholdMsg.setCo2Min("NNNN");
				break;
			case "光照":
				//阀值最大值
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
				//阀值最小值
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
				//将其他环境因素的阀值报文位都置为NNNN
				thresholdMsg.setTemperatureMax("NNNN");
				thresholdMsg.setTemperatureMin("NNNN");
				thresholdMsg.setCo2Max("NNNN");
				thresholdMsg.setCo2Min("NNNN");
				thresholdMsg.setWaterMax("NN");
				thresholdMsg.setWaterMin("NN");
				break;
				
			}
			
			thresholdMsg.setCheckNum(CRC16.getCRC16(thresholdMsg.getMsgEntity().getBytes()));
			//发送报文
			System.out.println(thresholdMsg.toString()+"   长度："+thresholdMsg.toString().length());
			binder.sentMsg(thresholdMsg.toString().getBytes(), handler);
			isSendMsg = true;
			//再设置为不可点
			threshold_max_et.setEnabled(false);
			threshold_min_et.setEnabled(false);
			threshold_btn_sure.setEnabled(false);
			break;
		}
		
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		//绑定服务
		Intent intent = new Intent(getActivity(), SocketService.class);
		conn = new MyConn();
		getActivity().bindService(intent, conn, 0);
		//广播接收者，这里是专门接收阀值设置完成后，服务端反馈报文的。
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(final Context context, Intent intent) {// 当接收到广播之后
				if (isSendMsg) {//并且是当前fragment发送了报文，服务端反馈的报文
					isSendMsg = false;
					String value = intent.getStringExtra("response");
					System.out.println(value   +"   at threshold ");
					// 判断是否是服务端反馈报文//CRC验证码
					if (value.length() > 0 && value.length() == 50 && CRC16.getCRC16(value.substring(0, 46).getBytes()).equals(value.substring(46))) {
						if (value.substring(0, 7).equals("TTLKRZ0")) {//远程控制反馈报文
							// 在主线程里面更新界面
							getActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									myProgressDialog.dismiss();//收到服务器端的回复消息之后再消失
									Toast.makeText(context, "阀值设置成功", 0).show();
								}
							});
						}else if (value.substring(0, 7).equals("TTLKRZV")) {//时间反馈报文
							timefromservice = value.substring(7, 21);
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
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(receiver != null){
			getActivity().unregisterReceiver(receiver);
		}
		//解绑服务
		getActivity().unbindService(conn);
	}
}
