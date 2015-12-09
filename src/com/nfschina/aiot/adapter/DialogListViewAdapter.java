package com.nfschina.aiot.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nfschina.aiot.activity.R;
import com.nfschina.aiot.listener.MyDialogListener;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
/**
 * �Ի��������List������
 * @author wujian
 */
public class DialogListViewAdapter extends BaseAdapter{
	private Context context;
	private List<String> list=new ArrayList<String>();
	private MyDialogListener dialogListener;
	private Boolean isAllChecked = false;//Ϊ���ж��Ƿ���ȫѡ��������ֹȫѡ��ʱ�򣬵�ѡ�Ļص�����ִ��
	private Boolean isAllUnChecked = false;
	Map<Integer, Boolean> map_state = new HashMap<Integer, Boolean>();
	Holder holder =null;
	public DialogListViewAdapter(Context context, List<String> list,MyDialogListener dialogListener){
		this.context=context;
		this.list=list;
		this.dialogListener = dialogListener;
	}
	/**
	 * ȫѡ
	 * @param isAllChecked 
	 */
	public void checkAll(Boolean isAllChecked) {
		this.isAllChecked = isAllChecked;
		for (int i = 0; i < list.size(); i++) {
			map_state.put(i, true);
		}
		notifyDataSetChanged();
		
	}
	/**
	 * ȫ��ѡ
	 */
	public void checkNone(Boolean isAllUnChecked) {
		this.isAllUnChecked = isAllUnChecked;
		for (int i = 0; i < list.size(); i++) {
			map_state.remove(i);
		}
		notifyDataSetChanged();
		
	}
	@Override
	public int getCount() {
		return list.size();
	}
	@Override
	public Object getItem(int position) {
		return list.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.alertdialog_view_item, null);
			holder=new Holder();
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		}else{
			holder=(Holder) convertView.getTag();
		}
		holder.ad_item_name.setText(list.get(position));
		holder.ad_item_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked && !isAllChecked) {//���û�ѡ��,���Ҳ���ȫѡ��ʱ��
					map_state.put(position, isChecked);//��ʱ����ѡ��״̬
						if (dialogListener !=null ) {
							dialogListener.onListItemChecked(position, list.get(position));
						}
					}else {
						if (!isChecked && !isAllUnChecked) {
							//���û�ѡ�к����ٴε����ʹ֮δѡ��
							map_state.remove(position);//ɾ��
							if (dialogListener !=null) {
								dialogListener.onListItemUnChecked(position, list.get(position));
							}
						}
					}
			}
		});
		//���ݱ����״̬����ȷ�����ڵ�״̬���Ըı���ʽ
		holder.ad_item_cb.setChecked(map_state.get(position) == null ? false : true);
		
		return convertView;
	}
	/**
	 * ����convertView�����Ч��
	 * @author My
	 */
	private final class Holder{
		@ViewInject(R.id.ad_item_name)
		private TextView ad_item_name;
		@ViewInject(R.id.ad_item_cb)
		private CheckBox ad_item_cb;

	}

}
