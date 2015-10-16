package com.nfschina.aiot.activity;

import com.nfschina.aiot.R;
import com.nfschina.aiot.adapter.AllGreenAdapter;
import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.constant.ConstantFun;
import com.nfschina.aiot.db.AccessDataBase;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

/**
 * ��ʾ������Ȩ�޵Ĵ��ѡ�������룬�鿴��ʷ��¼
 * ���д��ﶼ�ӷ�������ȡ����ʾ��GridView��
 * @author xu
 *
 */

public class AllGreenActivity extends Activity {

	//���ذ�ť
	private TextView mGoBack;
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
		mGoBack = (TextView)findViewById(R.id.greenhouse_list_back);
		//���÷��ذ�ť�����¼�
		mGoBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
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
}
