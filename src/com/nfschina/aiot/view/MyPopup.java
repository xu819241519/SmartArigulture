package com.nfschina.aiot.view;

import java.util.ArrayList;

import com.nfschina.aiot.activity.R;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * @author wujian
 *	������������ʾ���ݵĵ������̳���PopupWindow��
 */
public class MyPopup extends PopupWindow {
	private Context mContext;
	
	private int width, height;

	//�б����ļ��
	protected final int LIST_PADDING = 10;
	
	//ʵ����һ������
	private Rect mRect = new Rect();
	
	//�����λ�ã�x��y��
	private final int[] mLocation = new int[2];
	
	//��Ļ�Ŀ�Ⱥ͸߶�
	private int mScreenWidth,mScreenHeight;

	//�ж��Ƿ���Ҫ��ӻ�����б�������
	private boolean mIsDirty;
	
	//λ�ò�������
	private int popupGravity = Gravity.NO_GRAVITY;	
	
	//����������ѡ��ʱ�ļ���
	private OnItemOnClickListener mItemOnClickListener;
	
	//�����б����
	private GridView mGridView;
	
	//���嵯���������б�
	private ArrayList<ActionItem> mActionItems = new ArrayList<ActionItem>();			
	
	public MyPopup(Context context){
		//���ò��ֵĲ���
		this(context, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}
	
	public MyPopup(Context context, int width, int height){
		this.mContext = context;
		this.width = width;
		this.height = height;
		
		//���ÿ��Ի�ý���
		setFocusable(true);
		//���õ����ڿɵ��
		setTouchable(true);	
		//���õ�����ɵ��
		setOutsideTouchable(true);
		
		//�����Ļ�Ŀ�Ⱥ͸߶�
		mScreenWidth = ScreenUtil.getScreenWidth(mContext);
		mScreenHeight = ScreenUtil.getScreenHeight(mContext);
		
		//���õ����Ŀ�Ⱥ͸߶�
		setWidth(width);
		setHeight(height);
		
		setBackgroundDrawable(new BitmapDrawable());
		
		//���õ����Ĳ��ֽ���
		setContentView(LayoutInflater.from(mContext).inflate(R.layout.mypopup, null));
		
		initUI();
	}
		
	/**
	 * ��ʼ�������б�
	 */
	private void initUI(){
		mGridView = (GridView) getContentView().findViewById(R.id.title_gv);
		
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,long arg3) {
				//���������󣬵�����ʧ
				dismiss();
				
				if(mItemOnClickListener != null)
					mItemOnClickListener.onItemClick(mActionItems.get(index), index);
			}
		}); 
	}
	
	/**
	 * ��ʾ�����б����
	 */
	public void show(View view){
		//��õ����Ļ��λ������
		view.getLocationOnScreen(mLocation);
		
		//���þ��εĴ�С
		mRect.set(mLocation[0], mLocation[1], mLocation[0] + view.getWidth(),mLocation[1] + view.getHeight());
		
		//�ж��Ƿ���Ҫ��ӻ�����б�������
		if(mIsDirty){
			populateActions();
		}
		//��ʾ������λ��
		showAtLocation(view, popupGravity, (mScreenWidth/2) - (width/2), mRect.bottom);
	}
	
	/**
	 * ���õ����б�����
	 */
	private void populateActions(){
		mIsDirty = false;
		
		//�����б��������
		mGridView.setAdapter(new BaseAdapter() {			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView textView = null;
				
				if(convertView == null){
					textView = new TextView(mContext);
					textView.setTextColor(mContext.getResources().getColor(R.color.white));
					textView.setTextSize(14);
					//�����ı�����
					textView.setGravity(Gravity.CENTER);
					//�����ı���ķ�Χ
					textView.setPadding(0, 5, 0, 5);
					//�����ı���һ������ʾ�������У�
					textView.setSingleLine(true);
				}else{
					textView = (TextView) convertView;
				}
				
				ActionItem item = mActionItems.get(position);
				
				//�����ı�����
				textView.setText(item.content);
//				//����������ͼ��ļ��
//				textView.setCompoundDrawablePadding(10);
//				//���������ֵ���߷�һ��ͼ��
//                textView.setCompoundDrawablesWithIntrinsicBounds(item.mDrawable, null , null, null);
				
                return textView;
			}
			
			@Override
			public long getItemId(int position) {
				return position;
			}
			
			@Override
			public Object getItem(int position) {
				return mActionItems.get(position);
			}
			
			@Override
			public int getCount() {
				return mActionItems.size();
			}
		}) ;
	}
	
	/**
	 * ���������
	 */
	public void addAction(ActionItem action){
		if(action != null){
			mActionItems.add(action);
			mIsDirty = true;
		}
	}
	
	/**
	 * ���������
	 */
	public void cleanAction(){
		if(mActionItems.isEmpty()){
			mActionItems.clear();
			mIsDirty = true;
		}
	}
	
	/**
	 * ����λ�õõ�������
	 */
	public ActionItem getAction(int position){
		if(position < 0 || position > mActionItems.size())
			return null;
		return mActionItems.get(position);
	}			
	
	/**
	 * ���ü����¼�
	 */
	public void setItemOnClickListener(OnItemOnClickListener onItemOnClickListener){
		this.mItemOnClickListener = onItemOnClickListener;
	}
	
	/**
	 * @author yangyu
	 *	�������������������ť�����¼�
	 */
	public static interface OnItemOnClickListener{
		public void onItemClick(ActionItem item , int position);
	}
}
