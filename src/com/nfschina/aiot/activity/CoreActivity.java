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
 * �������ĺ����࣬����ʵʱ��أ�������Ϣ����ֵ���ã�Զ�̿����ĸ�fragment
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
	//��ʼ����Ҫ��fragment
	private InfoFragment infoFragment;
	private MonitorFragment monitorFragment;
	private RemotectrlFragment remotectrlFragment;
	private ThresholdFragment thresholdFragment;
	//���fragment����������
	private List<Fragment> fragments = new ArrayList<Fragment>();
	private PagerAdapter pagerAdapter;
	
//	private  ISocketService binder;//�󶨷�����н�
//	private  MyConn conn;//�����������
//	private BroadcastReceiver receiver;//�㲥������
//	private boolean isSendMsg = false;//��ʾ��ǰ�Ƿ����˱���
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
		//��������ı���¼�
		viewPager.setOnPageChangeListener(this);
		
		//��һ��Ϊ�˸ս���ѡ�е�һ��
		viewPager.setCurrentItem(0);
		((RadioButton) core_tabs_rg.getChildAt(0)).setChecked(true);
		//bindServiceAndRegisterReceiver();
	}

	/**
	 * ���Ȼ��ʼ������fragment��Ȼ����ӵ�fragments��
	 */
	private void initViews(){
		infoFragment = new InfoFragment();
		monitorFragment = new MonitorFragment();
		remotectrlFragment = new RemotectrlFragment();
		thresholdFragment = new ThresholdFragment();
		//����ӵ�ʱ��ע��˳����Ҫ��core.xml�У�˳��һ��
		fragments.add(monitorFragment);
		fragments.add(infoFragment);
		fragments.add(thresholdFragment);
		fragments.add(remotectrlFragment);
		
		pagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(),fragments);
		viewPager.setAdapter(pagerAdapter);
	}
	/**
	 * Ϊ����4��radiobutton��ӵ���¼�����ʼ��
	 */
	private void initClick(){
		core_tabs_monitor.setOnClickListener(new MyRBClickListener(0));
		core_tabs_info.setOnClickListener(new MyRBClickListener(1));
		core_tabs_threshold.setOnClickListener(new MyRBClickListener(2));
		core_tabs_remotectrl.setOnClickListener(new MyRBClickListener(3));
	}
	/**
	 * ΪradioButton���õĵ���¼�����
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
	//����������������ʵ��OnPageChangeListener�����
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
//			Toast.makeText(getApplicationContext(), "�б�����Ϣ", 0).show();
//		}
//	}
	
	/**
	 * �󶨷���ע��㲥�����ߣ�ʵ�ֵ�½֮��ͬ�������ʱ��Ĳ���
	 *//*
	public void bindServiceAndRegisterReceiver(){
		//�󶨷���
		Intent intent = new Intent(this, SocketService.class);
		conn = new MyConn();
		this.bindService(intent, conn, Context.BIND_AUTO_CREATE);
		// �㲥�����ߣ�������ר�Ž��շ�ֵ������ɺ󣬷���˷������ĵġ�
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {// �����յ��㲥֮��
				if (isSendMsg) {// �����ǵ�ǰfragment�����˱��ģ�����˷����ı���
					isSendMsg = false;
					String value = intent.getStringExtra("response");
					System.out.println(value);
					// �ж��Ƿ��Ƿ���˶�ʱ����ķ�������//CRC��֤��
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
		// �㲥����������ʾ������ʲô���Ĺ㲥
		IntentFilter intentToReceiveFilter = new IntentFilter();
		intentToReceiveFilter.addAction(Const.BC);
		this.registerReceiver(receiver, intentToReceiveFilter);
	}
	*//**
	 * ���������¼�
	 * @author wujian
	 *//*
	private class MyConn implements ServiceConnection{
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			//��õ��˷����н飬���Ե��÷����еķ�����
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
