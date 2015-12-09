package com.nfschina.aiot.fragment;

import com.nfschina.aiot.activity.R;
import com.nfschina.aiot.adapter.FragmentAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.internal.view.menu.MenuView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * ����ҳ��3
 * 
 * @author xu
 *
 */

public class Guide_Three extends Fragment {

	private View mView;

	// ��ʼ��ť
	private ImageView imageStart;

	// ������
	private FragmentAdapter fAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.what_new_three, null);

		imageStart = (ImageView) mView.findViewById(R.id.start);
		// ���ÿ�ʼ��ť���¼��������ʼ������ҳ��
		imageStart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (fAdapter != null) {
					fAdapter.setGuided();
					fAdapter.goLogin();
				}
			}
		});
		return mView;
	}

	/**
	 * ����������
	 * 
	 * @param fAdapter
	 *            �������Ķ���
	 */
	public void SetAdapter(FragmentAdapter fAdapter) {
		this.fAdapter = fAdapter;
	}

}
