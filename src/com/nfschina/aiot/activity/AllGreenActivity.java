package com.nfschina.aiot.activity;

import com.nfschina.aiot.R;
import com.nfschina.aiot.adapter.AllGreenAdapter;
import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.constant.ConstantFun;
import com.nfschina.aiot.db.AccessDataBase;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * ��ʾ������Ȩ�޵Ĵ��ѡ�������룬�鿴��ʷ��¼
 * ���д��ﶼ�ӷ�������ȡ����ʾ��GridView��
 * @author xu
 *
 */

public class AllGreenActivity extends Activity implements OnClickListener {

	//���ذ�ť
	private ImageButton mGoBack;
	//��ҳ��ť
	private ImageButton mGoHome;
	//��Ŵ��������ؼ�
	private GridView mGridView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.greenhouse_list);
		InitUIControls();
	}

	/**
	 * ��ʼ��UI�ؼ�
	 */
	private void InitUIControls(){
		//�󶨿ؼ�
		mGridView = (GridView)findViewById(R.id.greenhouse_list_gv);
		mGoBack = (ImageButton)findViewById(R.id.greenhouse_back);
		mGoHome = (ImageButton)findViewById(R.id.greenhouse_gohome);
		//���÷��ذ�ť�����¼�
		mGoBack.setOnClickListener(this);
		//������ҳ��ť�����¼�
		mGoHome.setOnClickListener(this);
		
		//ִ�в�ѯ
		new GetDataTask().execute();
	}
	
	
	/**
	 * �ӷ�������ȡӵ��Ȩ�޵Ĵ���
	 * @author xu
	 *
	 */
	public class GetDataTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			//��ȡ���������
			Constant.GreenHouseName = AccessDataBase.getGreenHouseName();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			//����������
			mGridView.setAdapter(new AllGreenAdapter(AllGreenActivity.this));
		}
	}


	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.greenhouse_back){
			finish();
		}else if(v.getId() == R.id.greenhouse_gohome){
			Intent intent = new Intent(this,Home.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
	}
}
