package com.nfschina.aiot.fragment;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nfschina.aiot.activity.Home;
import com.nfschina.aiot.activity.R;
import com.nfschina.aiot.constant.MyConstant;
import com.nfschina.aiot.entity.GreenHouse;
import com.nfschina.aiot.entity.Threshold;
import com.nfschina.aiot.view.NoScrollViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
/**
 * ��ֵ����fragment
 * @author wujian
 */
public class ThresholdFragment extends Fragment implements OnClickListener {
	@ViewInject(R.id.create_threshold_sv)
	private ScrollView create_threshold_sv;
	@ViewInject(R.id.create_threshold_sv_ll)
	private LinearLayout create_threshold_sv_ll;
	@ViewInject(R.id.create_threshold_vp)
	private NoScrollViewPager create_threshold_vp;
	@ViewInject(R.id.threshold_save_btn)
	private Button threshold_save_btn;
	@ViewInject(R.id.threshold_back)
	private ImageButton threshold_back;
	@ViewInject(R.id.threshold_gohome)
	private ImageButton threshold_gohome;
	
	private String[] environment;
	private TextView textViews[];//Ϊ�˴��ScrollView�е���Ŀ
	private View views[];
	private LayoutInflater inflater;//���������
	private int currentItem=0;
	private int scrllViewWidth = 0, scrollViewMiddle = 0;
	private EnvironmentAdapter environmentAdapter;//������
	private Threshold threshold;
	private boolean isFirst = true;//�жϵ�ǰ�ǲ��ǵ�һ�ν������fragment ҳ��
	//��ʱ���ַ�����ת��Ϊformat_ori��ʽ��Date��Ȼ���ٸ�ʽ��Ϊformat���ַ���
	SimpleDateFormat format_ori = new SimpleDateFormat("yyyyMMddhhmmss");
	SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Date date = null;
	private GreenHouse greenhouse;
	private Handler handler = new Handler() {
	@Override
		public void handleMessage(Message msg) {
			// ��һ�ν������fragmentҳ�棬��ִ����Щ����
			if (create_threshold_vp == null) {
				// ��ʼ��������
				environmentAdapter = new EnvironmentAdapter(getFragmentManager());
				showScrollView();
				initPager();
			}
			create_threshold_vp.setCurrentItem(0);
			create_threshold_vp.setAdapter(environmentAdapter);
			
		};
	};// �����δ��룬����ʵ�ֽ���������û���ǰ�Ž�����ز���
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (isVisibleToUser && !isFirst) {
			//�����ǽ��fragmentǶ�׳��ֵ���ʾ���⣨��ʵʱ���fragment��ת����ֵ����fragment�������ʾ��ȫ���⣩
			handler.sendEmptyMessageDelayed(1, 300);// 300�����������������
		}
		super.setUserVisibleHint(isVisibleToUser);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("CreateView in threshold");
		View view = inflater.inflate(R.layout.createthreshold, null);
		ViewUtils.inject(this,view);
		Bundle bundle = getActivity().getIntent().getExtras();
		if (bundle != null) {
			greenhouse = (GreenHouse) bundle.get("greenhouse");
		}
		threshold_gohome.setOnClickListener(this);
		threshold_back.setOnClickListener(this);
		threshold = MonitorFragment.threshold;//ֱ�ӵ���
		this.inflater = getActivity().getLayoutInflater();// ��ʼ�����������
		// ��ʼ��������
		environmentAdapter = new EnvironmentAdapter(getFragmentManager());
		showScrollView();
		initPager();
		isFirst = false;
		
		//����ҳ���ˢ��
		new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				return true;
			}
		}).sendEmptyMessageDelayed(0, 1000);
		return view;
	}
	/*@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (isVisibleToUser && !isFirst) {
			//��һ�ν������fragmentҳ�棬��ִ����Щ����
			if (create_threshold_vp == null) {
				//��ʼ��������
				environmentAdapter=new EnvironmentAdapter(getFragmentManager());
				showScrollView();
				initPager();
			}
			create_threshold_vp.setCurrentItem(0);
			create_threshold_vp.setAdapter(environmentAdapter);		
		}
		super.setUserVisibleHint(isVisibleToUser);
	}*/
	/**
	 * ��������ʾ�������ص�ScrollView
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
			view = null;
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
			bundle.putSerializable("threshold", (Serializable) threshold);
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
				textViews[i].setTextColor(getResources().getColor(R.color.gray_darker));
			}
		}
		textViews[id].setBackgroundResource(R.drawable.choose_item_selected);
		textViews[id].setTextColor(getResources().getColor(R.color.green));
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
	 * @return
	 */
	private int getScrollViewheight() {
		if (scrllViewWidth == 0)
			scrllViewWidth = create_threshold_sv.getBottom() - create_threshold_sv.getTop();
		return scrllViewWidth;
	}
	/**
	 * ����view�ĸ߶�
	 * @param view
	 * @return
	 */
	private int getViewheight(View view) {
		return view.getBottom() - view.getTop();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.threshold_back:
			getActivity().finish();
			break;
		case R.id.threshold_gohome:
			Intent intent = new Intent(getActivity(), Home.class);
			startActivity(intent);
			getActivity().finish();
			break;
		}
	}
	@Override
	public void onDestroy() {
		System.out.println("destory in threshold");
		super.onDestroy();
	}
	@Override
	public void onDestroyView() {
		System.out.println("destoryView in threshold");
		super.onDestroyView();
	}
	@Override
	public void onPause() {
		System.out.println("Pause in threshold");
		super.onPause();
	}
	@Override
	public void onResume() {
		System.out.println("Resume in threshold");
		super.onResume();
	}
	@Override
	public void onStart() {
		System.out.println("Start in threshold");
		super.onStart();
	}
	@Override
	public void onStop() {
		System.out.println("Stop in threshold");
		super.onStop();
	}
	
}
