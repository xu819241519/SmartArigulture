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
 * 对话框里面的List适配器
 * @author wujian
 */
public class DialogListViewAdapter extends BaseAdapter{
	private Context context;
	private List<String> list=new ArrayList<String>();
	private MyDialogListener dialogListener;
	private Boolean isAllChecked = false;//为了判断是否是全选操作，防止全选的时候，单选的回调函数执行
	private Boolean isAllUnChecked = false;
	Map<Integer, Boolean> map_state = new HashMap<Integer, Boolean>();
	Holder holder =null;
	public DialogListViewAdapter(Context context, List<String> list,MyDialogListener dialogListener){
		this.context=context;
		this.list=list;
		this.dialogListener = dialogListener;
	}
	/**
	 * 全选
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
	 * 全不选
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
				if (isChecked && !isAllChecked) {//当用户选中,并且不是全选的时候
					map_state.put(position, isChecked);//暂时保存选中状态
						if (dialogListener !=null ) {
							dialogListener.onListItemChecked(position, list.get(position));
						}
					}else {
						if (!isChecked && !isAllUnChecked) {
							//当用户选中后，又再次点击，使之未选中
							map_state.remove(position);//删除
							if (dialogListener !=null) {
								dialogListener.onListItemUnChecked(position, list.get(position));
							}
						}
					}
			}
		});
		//根据保存的状态，来确定现在的状态，以改变样式
		holder.ad_item_cb.setChecked(map_state.get(position) == null ? false : true);
		
		return convertView;
	}
	/**
	 * 缓存convertView，提高效率
	 * @author My
	 */
	private final class Holder{
		@ViewInject(R.id.ad_item_name)
		private TextView ad_item_name;
		@ViewInject(R.id.ad_item_cb)
		private CheckBox ad_item_cb;

	}

}
