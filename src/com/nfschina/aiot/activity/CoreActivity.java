package com.nfschina.aiot.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nfschina.aiot.adapter.MyViewPagerAdapter;
import com.nfschina.aiot.fragment.InfoFragment;
import com.nfschina.aiot.fragment.MonitorFragment;
import com.nfschina.aiot.fragment.RemotectrlFragment;
import com.nfschina.aiot.fragment.ThresholdFragment;
/**
 * 控制中心核心类，包括实时监控，报警信息，阀值设置，远程控制四个fragment
 * @author wujian
 */
public class CoreActivity extends FragmentActivity implements OnPageChangeListener{
	@ViewInject(R.id.viewpager)
	private ViewPager viewPager;
	@ViewInject(R.id.core_tabs_rg)
	private RadioGroup core_tabs_rg;
	@ViewInject(R.id.core_tabs_monitor)
	private RadioButton core_tabs_monitor;
	@ViewInject(R.id.core_tabs_info)
	private RadioButton core_tabs_info;
	@ViewInject(R.id.core_tabs_threshold)
	private RadioButton core_tabs_threshold;
	@ViewInject(R.id.core_tabs_remotectrl)
	private RadioButton core_tabs_remotectrl;
	//初始化需要的fragment
	private InfoFragment infoFragment;
	private MonitorFragment monitorFragment;
	private RemotectrlFragment remotectrlFragment;
	private ThresholdFragment thresholdFragment;
	//存放fragment，用来适配
	private List<Fragment> fragments = new ArrayList<Fragment>();
	private PagerAdapter pagerAdapter;
	
//	private  ISocketService binder;//绑定服务的中介
//	private  MyConn conn;//与服务建立连接
//	private BroadcastReceiver receiver;//广播接收者
//	private boolean isSendMsg = false;//表示当前是否发送了报文
//	SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
//	private int year,month,day,hour,minute;
//	private Handler handler = new Handler(){
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case 1:
//				System.out.println("send success");
//				break;
//			case 0:
//				System.out.println("send fail");
//				break;
//			}
//		};
//	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.core);
		ViewUtils.inject(this);
		
		initClick();
		initViews();
		//监听界面改变的事件
		viewPager.setOnPageChangeListener(this);
		
		//这一句为了刚进入选中第一个
		viewPager.setCurrentItem(0);
		((RadioButton) core_tabs_rg.getChildAt(0)).setChecked(true);
		//bindServiceAndRegisterReceiver();
	}

	/**
	 * 首先会初始化各个fragment，然后添加到fragments中
	 */
	private void initViews(){
		infoFragment = new InfoFragment();
		monitorFragment = new MonitorFragment();
		remotectrlFragment = new RemotectrlFragment();
		thresholdFragment = new ThresholdFragment();
		//再添加的时候，注意顺序需要和core.xml中，顺序一样
		fragments.add(monitorFragment);
		fragments.add(infoFragment);
		fragments.add(thresholdFragment);
		fragments.add(remotectrlFragment);
		
		pagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(),fragments);
		viewPager.setAdapter(pagerAdapter);
	}
	/**
	 * 为底下4个radiobutton添加点击事件，初始化
	 */
	private void initClick(){
		core_tabs_monitor.setOnClickListener(new MyRBClickListener(0));
		core_tabs_info.setOnClickListener(new MyRBClickListener(1));
		core_tabs_threshold.setOnClickListener(new MyRBClickListener(2));
		core_tabs_remotectrl.setOnClickListener(new MyRBClickListener(3));
	}
	/**
	 * 为radioButton设置的点击事件的类
	 * @author wujian
	 *
	 */
	public class MyRBClickListener implements View.OnClickListener{
		private int index;
		public MyRBClickListener(int index) {
			this.index = index ;
		}
		@Override
		public void onClick(View v) {
			viewPager.setCurrentItem(index);
		}
		
	}//end MyRBClickListener
	//以下三个方法，是实现OnPageChangeListener所需的
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}
	@Override
	public void onPageSelected(int position) {
		((RadioButton) core_tabs_rg.getChildAt(position)).setChecked(true);
	}//end OnPageChangeListener

//	@Override
//	public void alarmShow(boolean isHavaInfo) {
//		if (isHavaInfo) {
//			Toast.makeText(getApplicationContext(), "有报警信息", 0).show();
//		}
//	}
	
	/**
	 * 绑定服务，注册广播接收者，实现登陆之后同步服务端时间的操作
	 *//*
	public void bindServiceAndRegisterReceiver(){
		//绑定服务
		Intent intent = new Intent(this, SocketService.class);
		conn = new MyConn();
		this.bindService(intent, conn, Context.BIND_AUTO_CREATE);
		// 广播接收者，这里是专门接收阀值设置完成后，服务端反馈报文的。
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {// 当接收到广播之后
				if (isSendMsg) {// 并且是当前fragment发送了报文，服务端反馈的报文
					isSendMsg = false;
					String value = intent.getStringExtra("response");
					System.out.println(value);
					// 判断是否是服务端对时请求的反馈报文//CRC验证码
					if (value.substring(0, 7).equals("TTLKRZV")
							&& value.length() == 50
							&& CRC16.getCRC16(value.substring(0, 46).getBytes())
									.equals(value.substring(46))) {
						String dateString = value.substring(7, 21);
						System.out.println(dateString);
						try {
							Calendar calendar = Calendar.getInstance();  
							calendar.setTime(dateformat.parse(dateString));
							year = calendar.get(Calendar.YEAR);      
					        month = calendar.get(Calendar.MONTH)+1;      
					        day = calendar.get(Calendar.DATE);      
					        minute = calendar.get(Calendar.MINUTE);      
					        hour = calendar.get(Calendar.HOUR)+8;  
					        System.out.println(year+""+month+""+day+""+hour+""+minute);
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				}
			}
		};
		// 广播过滤器，表示将接收什么样的广播
		IntentFilter intentToReceiveFilter = new IntentFilter();
		intentToReceiveFilter.addAction(Const.BC);
		this.registerReceiver(receiver, intentToReceiveFilter);
	}
	*//**
	 * 服务连接事件
	 * @author wujian
	 *//*
	private class MyConn implements ServiceConnection{
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			//获得到了服务中介，可以调用服务中的方法了
			binder = (ISocketService) service;
			binder.sentMsg(("TTLKQAT000000000000000000000"+CRC16.getCRC16("TTLKQAT000000000000000000000".getBytes())).getBytes(), handler);
			isSendMsg = true;
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(receiver != null){
			unregisterReceiver(receiver);
		}
		unbindService(conn);
	}*/
	
}
