package com.nfschina.aiot.activity;

import java.util.ArrayList;
import java.util.List;
import com.nfschina.aiot.R;
import com.nfschina.aiot.adapter.FragmentAdapter;
import com.nfschina.aiot.fragment.Guide_1;
import com.nfschina.aiot.fragment.Guide_2;
import com.nfschina.aiot.fragment.Guide_3;
import com.nfschina.aiot.fragment.Guide_4;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 
 * @author xu
 *
 */
public class Guide extends FragmentActivity implements OnPageChangeListener {

	//ViewPager�ؼ�
	private ViewPager mpager;
	//fragment������
	private FragmentPagerAdapter mAdapter;
	//���fragment��list
	private List<Fragment> mFragments;
	//����ҳ���СԲ��
	private ImageView[] mdots;
	//����ҳ��ĵ�ǰҳ������
	private int mcurrentIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide);

		initFragments();

		initDots();
	}

	/**
	 * ��ʼ��fragments
	 */
	private void initFragments() {
		mFragments = new ArrayList<Fragment>();
		mFragments.add(new Guide_1());
		mFragments.add(new Guide_2());
		mFragments.add(new Guide_3());
		mFragments.add(new Guide_4());
		
		mAdapter = new FragmentAdapter(getSupportFragmentManager(),mFragments, this);

		mpager = (ViewPager) findViewById(R.id.guide_viewpager);
		mpager.setAdapter(mAdapter);
		mpager.setOnPageChangeListener(Guide.this);

	}

	/*
	 * ��ʼ��СԲ��
	 */
	private void initDots() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

		mdots = new ImageView[mFragments.size()];

		// ѭ��ȡ��С��ͼƬ
		for (int i = 0; i < mFragments.size(); i++) {
			mdots[i] = (ImageView) ll.getChildAt(i);
			mdots[i].setEnabled(true);// ����Ϊ��ɫ
		}

		mcurrentIndex = 0;
		mdots[mcurrentIndex].setEnabled(false);// ����Ϊ��ɫ����ѡ��״̬
	}

	/**
	 * ����СԲ��ĵ�ǰҳ��
	 * @param position ��ǰҳ�������ֵ
	 */
	private void setCurrentDot(int position) {
		if (position < 0 || position > mFragments.size() - 1 || mcurrentIndex == position) {
			return;
		}

		mdots[position].setEnabled(false);
		mdots[mcurrentIndex].setEnabled(true);

		mcurrentIndex = position;
	}

	// ������״̬�ı�ʱ����
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	// ����ǰҳ�汻����ʱ����
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	// ���µ�ҳ�汻ѡ��ʱ����
	@Override
	public void onPageSelected(int arg0) {
		// ���õײ�С��ѡ��״̬
		setCurrentDot(arg0);
	}

}
