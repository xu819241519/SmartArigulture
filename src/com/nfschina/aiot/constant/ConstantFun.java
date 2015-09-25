package com.nfschina.aiot.constant;

import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;
import com.nfschina.aiot.R;

import android.app.Activity;
import android.widget.ListView;
import android.widget.Toast;

public class ConstantFun {

	
	/**
	 * get the refresh sound listener
	 * @param activity the activity relate to the sound listener
	 * @return the soundlistener
	 */
	public static SoundPullEventListener<ListView> getSoundListener(Activity activity){
		SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(activity);
		soundListener.addSoundEvent(State.PULL_TO_REFRESH, R.raw.pull_event);
		soundListener.addSoundEvent(State.RESET, R.raw.reset_sound);
		soundListener.addSoundEvent(State.REFRESHING, R.raw.refreshing_sound);
		return soundListener;
	}
	
	public static OnLastItemVisibleListener getLastItemVisibleListener(final Activity activity){
		return new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				Toast.makeText(activity, Constant.END_OF_LIST, Toast.LENGTH_SHORT).show();
			}
		};
	}

}
