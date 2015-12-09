package com.nfschina.aiot.animation;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout.LayoutParams;
/**
 * 点击listview会展开的动画
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

		setDuration(duration);//设置动画执行的时间长度
		mAnimatedView = view;
		mViewLayoutParams = (LayoutParams) view.getLayoutParams();//也就是获得这个view的相关信息，比如宽高等

		// if the bottom margin is 0,
		// then after the animation will end it'll be negative, and invisible.
		mIsVisibleAfter = (mViewLayoutParams.bottomMargin == 0);//true ==不可见

		mMarginStart = mViewLayoutParams.bottomMargin;//获取现在距离底部的距离，作为将要显示的部分的开始位置
		mAnimatedView.measure(0,0);
		mMarginEnd = (mMarginStart == 0 ? (0 - mAnimatedView.getMeasuredHeight()) : 0);//获取显示部分的结束位置

		view.setVisibility(View.VISIBLE);
		
		
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		super.applyTransformation(interpolatedTime, t);
		
		if (interpolatedTime < 1.0f) {//从0---1，不断改变当前的图形

			// Calculating the new bottom margin, and setting it
			mViewLayoutParams.bottomMargin = mMarginStart + (int) ((mMarginEnd - mMarginStart) * interpolatedTime);
			
			Log.i(TAG,"mViewLayoutParams.bottomMargin =" + mViewLayoutParams.bottomMargin);

			// Invalidating the layout, making us seeing the changes we made
			mAnimatedView.requestLayout();//重新布局一个view
			// Making sure we didn't run the ending before (it happens!)
		}
		else if (!mWasEndedAlready) {//如果mWasEndedAlready==false，就执行这一句
			//以下两句就是为了第二次点击恢复动画之前的样子
			mViewLayoutParams.bottomMargin = mMarginEnd;
			mAnimatedView.requestLayout();

			if (mIsVisibleAfter) {
				mAnimatedView.setVisibility(View.GONE);
			}
			mWasEndedAlready = true;
		}
	}

}
