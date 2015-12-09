package com.nfschina.aiot.fragment;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nfschina.aiot.activity.R;
import com.nfschina.aiot.constant.MyConstant;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class CreateThresholdFragment extends Fragment {
	@ViewInject(R.id.create_threshold_sv)
	private ScrollView create_threshold_sv;
	@ViewInject(R.id.create_threshold_sv_ll)
	private LinearLayout create_threshold_sv_ll;
	@ViewInject(R.id.create_threshold_vp)
	private ViewPager create_threshold_vp;
	@ViewInject(R.id.threshold_save_btn)
	private Button threshold_save_btn;
	
	private String[] environment;
	private TextView textViews[];//Ϊ�˴��ScrollView�е���Ŀ
	private View views[];
	private LayoutInflater inflater;
	private int currentItem=0;
	private int scrllViewWidth = 0, scrollViewMiddle = 0;
	private EnvironmentAdapter environmentAdapter;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			environmentAdapter.getItem(1);
		};
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.createthreshold, null);
		ViewUtils.inject(this,view);
		
		this.inflater=LayoutInflater.from(getActivity());//��ʼ�����������
		//��ʼ��������
		environmentAdapter=new EnvironmentAdapter(getActivity().getSupportFragmentManager());
		showScrollView();
		initPager();
		return view;
	}
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (isVisibleToUser) {
			handler.sendEmptyMessageDelayed(1, 1000);
		}
		super.setUserVisibleHint(isVisibleToUser);
	}
	/**
	 * ��ʾ�������ص�ScrollView
	 */
	private void showScrollView() {
		environment = getData();
		textViews=new TextView[environment.length];
		views=new View[environment.length];
		
		for (int i = 0; i < environment.length; i++) {
			View view=inflater.inflate(R.layout.scrollview_item_layout, null);
			TextView textView=(TextView) view.findViewById(R.id.scrollview_tv);
			view.setId(i);//����һ��view����һ��item
			view.setOnClickListener(scrollItemListener);
			textView.setText(environment[i]);//����item����
			create_threshold_sv_ll.addView(view);//��ӵ���ǰ��LinearLayout
			textViews[i]=textView;
			views[i]=view;
			view = null;//ʹview����null��Ϊ�����ܿ���
		}
		changeItemColor(0);
	}
	
	private View.OnClickListener scrollItemListener =new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			//�����һ����Ŀ��ʱ��viewpager���õ�ǰ��ҳ�棬���ݵ�ǰ��������
			create_threshold_vp.setCurrentItem(v.getId());
		}
	};
	
	/**
     * initPager<br/>
     * ��ʼ��ViewPager�ؼ��������
     */
	private void initPager() {
		create_threshold_vp.setAdapter(environmentAdapter);		
		create_threshold_vp.setOnPageChangeListener(onPageChangeListener);
	}
	
	/**
	 * OnPageChangeListener<br/>
	 * ����ViewPagerѡ��仯�µ��¼�
	 */
	private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int position) {
			
			if(create_threshold_vp.getCurrentItem() != position)
				create_threshold_vp.setCurrentItem(position);
			if(currentItem!=position){
				changeItemColor(position);
				changeItemLocation(position);
				
			}
			currentItem=position;
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};
	
	
	/**
	 * ViewPager ����fragment
	 * @author wujian
	 */
	private class EnvironmentAdapter extends FragmentPagerAdapter {
		public EnvironmentAdapter(FragmentManager fm) {
			super(fm);
		}
		//viewpager�ı�ʱ�͵������
		@Override
		public Fragment getItem(int position) {
			Fragment fragment =new ThresholdTypeFragment();
			Bundle bundle=new Bundle();//�൱��һ��map
			String str=environment[position];
			bundle.putString("typename",str);
			fragment.setArguments(bundle);
			return fragment;
		}
		
		@Override
		public int getCount() {
			return environment.length;
		}
	}
	
	/**
	 * ��ȡ�������ط���
	 * @return
	 */
	public String[] getData(){
		return MyConstant.environmentlist;
	}
	
	/**
	 * �ı�textView����ɫ
	 * @param id
	 */
	private void changeItemColor(int id) {
		for (int i = 0; i < textViews.length; i++) {
			if(i!=id){
				textViews[i].setBackgroundResource(android.R.color.transparent);
				textViews[i].setTextColor(0xff000000);
			}
		}
		textViews[id].setBackgroundResource(R.drawable.choose_item_selected);
		textViews[id].setTextColor(0xffff5d5e);
	}
	
	/**
	 * �ı���Ŀλ��
	 * @param clickPosition ���λ��
	 */
	private void changeItemLocation(int clickPosition) {
		
		int y = (views[clickPosition].getTop() - getScrollViewMiddle() + (getViewheight(views[clickPosition]) / 2));
		create_threshold_sv.smoothScrollTo(0, y);
	}
	
	/**
	 * ����scrollview���м�λ��
	 * @return
	 */
	private int getScrollViewMiddle() {
		if (scrollViewMiddle == 0)
			scrollViewMiddle = getScrollViewheight() / 2;
		return scrollViewMiddle;
	}
	
	/**
	 * ����ScrollView�ĸ߶�
	 * 
	 * @return
	 */
	private int getScrollViewheight() {
		if (scrllViewWidth == 0)
			scrllViewWidth = create_threshold_sv.getBottom() - create_threshold_sv.getTop();
		return scrllViewWidth;
	}
	
	/**
	 * ����view�ĸ߶�
	 * 
	 * @param view
	 * @return
	 */
	private int getViewheight(View view) {
		return view.getBottom() - view.getTop();
	}

}
