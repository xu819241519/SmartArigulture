package com.nfschina.aiot.animation;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout.LayoutParams;
/**
 * ���listview��չ���Ķ���
 * @author wujian
 */
public class ExpandAnimation extends Animation {

	private static final String TAG = "ExpandAnimation";
	private View mAnimatedView;
	private LayoutParams mViewLayoutParams;
	private int mMarginStart, mMarginEnd;
	private boolean mIsVisibleAfter = false;
	private boolean mWasEndedAlready = false;

	public ExpandAnimation(View view, int duration) {

		setDuration(duration);//���ö���ִ�е�ʱ�䳤��
		mAnimatedView = view;
		mViewLayoutParams = (LayoutParams) view.getLayoutParams();//Ҳ���ǻ�����view�������Ϣ�������ߵ�

		// if the bottom margin is 0,
		// then after the animation will end it'll be negative, and invisible.
		mIsVisibleAfter = (mViewLayoutParams.bottomMargin == 0);//true ==���ɼ�

		mMarginStart = mViewLayoutParams.bottomMargin;//��ȡ���ھ���ײ��ľ��룬��Ϊ��Ҫ��ʾ�Ĳ��ֵĿ�ʼλ��
		mAnimatedView.measure(0,0);
		mMarginEnd = (mMarginStart == 0 ? (0 - mAnimatedView.getMeasuredHeight()) : 0);//��ȡ��ʾ���ֵĽ���λ��

		view.setVisibility(View.VISIBLE);
		
		
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		super.applyTransformation(interpolatedTime, t);
		
		if (interpolatedTime < 1.0f) {//��0---1�����ϸı䵱ǰ��ͼ��

			// Calculating the new bottom margin, and setting it
			mViewLayoutParams.bottomMargin = mMarginStart + (int) ((mMarginEnd - mMarginStart) * interpolatedTime);
			
			Log.i(TAG,"mViewLayoutParams.bottomMargin =" + mViewLayoutParams.bottomMargin);

			// Invalidating the layout, making us seeing the changes we made
			mAnimatedView.requestLayout();//���²���һ��view
			// Making sure we didn't run the ending before (it happens!)
		}
		else if (!mWasEndedAlready) {//���mWasEndedAlready==false����ִ����һ��
			//�����������Ϊ�˵ڶ��ε���ָ�����֮ǰ������
			mViewLayoutParams.bottomMargin = mMarginEnd;
			mAnimatedView.requestLayout();

			if (mIsVisibleAfter) {
				mAnimatedView.setVisibility(View.GONE);
			}
			mWasEndedAlready = true;
		}
	}

}
