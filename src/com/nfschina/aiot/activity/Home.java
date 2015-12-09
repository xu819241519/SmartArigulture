/**
 * 
 */
package com.nfschina.aiot.activity;

import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.BaseSliderView.ImageLoadListener;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.nfschina.aiot.broadcastreceiver.MyReceiver;
import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.constant.ConstantFun;
import com.nfschina.aiot.socket.NetManager;
import com.nfschina.aiot.socket.SocketService;
import com.nfschina.aiot.util.TimeRequest;

/**
 * 主页面，可以通过此页面进入相应的选项页面
 * 
 * @author xu
 *
 */
public class Home extends FragmentActivity implements OnClickListener {

	// 按钮
	private LinearLayout[] mBtnItem;

	// 点击返回键，提示再按一次退出程序。两次返回键之间的时间间隔
	private long mExitTime;
	// 广告页面控件，第三方控件（AndroidImageSlider）
	private SliderLayout mSliderLayout;
	
	public static Intent intent;//开启服务的意图
	private TimeRequest timeRequest;
	public static Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		context = this;
		//传入context
		NetManager.instance().init(this);
		InitUIControls();
		setListener();

		mExitTime = 0;
	}
	@Override
	protected void onResume() {
		super.onResume();
		//开启后台服务，接收服务端推送信息
		intent = new Intent(Home.this, SocketService.class);
		startService(intent);
	}
	@Override
	protected void onStart() {
		super.onStart();
		mSliderLayout.startAutoCycle();
	}
	
	/**
	 * 初始化UI控件
	 */
	private void InitUIControls() {
		mBtnItem = new LinearLayout[Constant.HOME_BTN_COUNT];
		mBtnItem[0] = (LinearLayout) findViewById(R.id.btn_history);
		mBtnItem[1] = (LinearLayout) findViewById(R.id.btn_monitor_center);
		mBtnItem[2] = (LinearLayout) findViewById(R.id.btn_news);
		mBtnItem[3] = (LinearLayout) findViewById(R.id.btn_other);
		mSliderLayout = (SliderLayout) findViewById(R.id.slider);

		TextSliderView textSliderView = new TextSliderView(this);
		// textSliderView.image("http://bbs.unpcn.com/attachment.aspx?attachmentid=4341481");
		textSliderView.image(R.drawable.index_banner);
		TextSliderView textSliderView2 = new TextSliderView(this);
		textSliderView2.image(R.drawable.greenhouse);
		mSliderLayout.addSlider(textSliderView);
		mSliderLayout.addSlider(textSliderView2);
		textSliderView.setOnImageLoadListener(new ImageLoadListener() {

			@Override
			public void onStart(BaseSliderView target) {
				mSliderLayout.setPresetTransformer(new Random().nextInt(16));
			}

			@Override
			public void onEnd(boolean result, BaseSliderView target) {

			}
		});
	}

	/**
	 * 设置UI控件的监听
	 */
	private void setListener() {
		for (int i = 0; i < Constant.HOME_BTN_COUNT; ++i)
			mBtnItem[i].setOnClickListener(this);
	}

	/**
	 * 判断点击的按钮，进入相应的处理
	 */
	@Override
	public void onClick(View v) {
		Intent intent = null;
		if (v.getId() != R.id.btn_other && !ConstantFun.netStateAvailable(this)) {
			return;
		}
		switch (v.getId()) {
		case R.id.btn_history:
			intent = new Intent(Home.this, AllGreenActivity.class);
			break;
		case R.id.btn_monitor_center:
			intent = new Intent(Home.this, AllGreenHouseActivity.class);
			break;
		case R.id.btn_news:
			intent = new Intent(Home.this, News.class);
			break;
		case R.id.btn_other:
			intent = new Intent(Home.this, Others.class);
			break;
		default:
			Toast.makeText(this, Constant.UNDEF, Toast.LENGTH_SHORT).show();
			break;
		}
		if (intent != null) {
			startActivity(intent);
		}
	}
	/**
	 * 点击返回键，提示再点一次返回键退出，两次点击的时间间隔不超过2s
	 */
	@Override
	public void onBackPressed() {
		if (mExitTime == 0 || (mExitTime != 0 && System.currentTimeMillis() - mExitTime > 2000)) {
			Toast.makeText(this, Constant.CONFIRM_EXIT, Toast.LENGTH_SHORT).show();
			mExitTime = System.currentTimeMillis();
		} else{
			//退出应用程序的同时也要停止服务。
			stopService(intent);
			finish();
			
		}
	}
	/**
	 * 在页面停止时，停止广告页面的滚动，放置内存泄露
	 */
	@Override
	protected void onStop() {
		mSliderLayout.stopAutoCycle();
		super.onStop();
	}
//	public static boolean isWorked() {  
//		  ActivityManager myManager=(ActivityManager)context.getSystemService(Co                 ntext.ACTIVITY_SERVICE);  
//		  ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServi                   ceInfo>) myManager.getRunningServices(30);  
//		  for(int i = 0 ; i<runningService.size();i++)  
//		  {  
//		    if(runningService.get(i).service.getClassName().toString().equals("c         om.android.controlAddFunctions.PhoneService"))  
//		  {  
//		    return true;  
//		   }  
//		  }  
//		  return false;  
//		 }
	
}
