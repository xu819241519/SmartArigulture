/**
 * 
 */
package com.nfschina.aiot.activity;

import com.nfschina.aiot.R;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author xu
 *
 */
public class Home extends Activity {

	/**
	 * The constructor function
	 */
	public Home() {
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		InitUIControls();
	}
	
	private void InitUIControls(){
		
	}

}
