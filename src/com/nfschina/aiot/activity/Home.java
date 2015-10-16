/**
 * 
 */
package com.nfschina.aiot.activity;

import com.nfschina.aiot.R;
import com.nfschina.aiot.constant.Constant;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Random;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.BaseSliderView.ImageLoadListener;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

/**
 * ��ҳ�棬����ͨ����ҳ�������Ӧ��ѡ��ҳ��
 * @author xu
 *
 */
public class Home extends FragmentActivity implements OnClickListener {

	// ��ť
	private LinearLayout[] mBtnItem;

	// ������ؼ�����ʾ�ٰ�һ���˳��������η��ؼ�֮���ʱ����
	private long mExitTime;
	// ���ҳ��ؼ����������ؼ���AndroidImageSlider��
	private SliderLayout mSliderLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		InitUIControls();
		setListener();

		mExitTime = 0;
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		mSliderLayout.startAutoCycle();
	}

	/**
	 * ��ʼ��UI�ؼ�
	 */
	private void InitUIControls() {
		mBtnItem = new LinearLayout[Constant.HOME_BTN_COUNT];
		mBtnItem[0] = (LinearLayout) findViewById(R.id.btn_history);
		mBtnItem[1] = (LinearLayout) findViewById(R.id.btn_monitor_center);
		mBtnItem[2] = (LinearLayout) findViewById(R.id.btn_news);
		mBtnItem[3] = (LinearLayout) findViewById(R.id.btn_other);
		mSliderLayout = (SliderLayout) findViewById(R.id.slider);


		TextSliderView textSliderView = new TextSliderView(this);
		textSliderView.image("http://bbs.unpcn.com/attachment.aspx?attachmentid=4341481");
		mSliderLayout.addSlider(textSliderView);
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
	 * ����UI�ؼ��ļ���
	 */
	private void setListener() {
		for (int i = 0; i < Constant.HOME_BTN_COUNT; ++i)
			mBtnItem[i].setOnClickListener(this);
	}

	
	/**
	 * �жϵ���İ�ť��������Ӧ�Ĵ���
	 */
	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.btn_history:
			intent = new Intent(Home.this, AllGreenActivity.class);
			break;
		case R.id.btn_monitor_center:
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
	 * ������ؼ�����ʾ�ٵ�һ�η��ؼ��˳������ε����ʱ����������2s
	 */
	@Override
	public void onBackPressed() {
		if (mExitTime == 0 || (mExitTime != 0 && System.currentTimeMillis() - mExitTime > 2000)) {
			Toast.makeText(this, Constant.CONFIRM_EXIT, Toast.LENGTH_SHORT).show();
			mExitTime = System.currentTimeMillis();
		} else
			finish();
	}
	
	
	/**
	 * ��ҳ��ֹͣʱ��ֹͣ���ҳ��Ĺ����������ڴ�й¶
	 */
	@Override
	protected void onStop() {
		mSliderLayout.stopAutoCycle();
		super.onStop();
	}

}
