package com.nfschina.aiot.crash;

import com.nfschina.aiot.constant.Constant;

import android.os.Bundle;

public class SavedInstanceState {

	/**
	 * 保存主要的状态参数
	 * 
	 * @param savedInstanceState
	 */
	public static void saveState(Bundle savedInstanceState) {
		savedInstanceState.putString("user_id", Constant.CURRENT_USER);
		savedInstanceState.putString("user_pwd", Constant.CURRENT_PASSWORD);
	}

	public static void restoreState(Bundle savedInstanceState) {
		Constant.CURRENT_USER = savedInstanceState.getString("user_id");
		Constant.CURRENT_PASSWORD = savedInstanceState.getString("user_pwd");
	}
}
