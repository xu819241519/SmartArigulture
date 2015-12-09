package com.nfschina.aiot.listener;


public interface MyDialogListener {
//	public void onListItemClick(int position, String string);
//	public void onListItemLongClick(int position, String string);
	public void onCancel();
	public void onSure();
	public void onListItemChecked(int position, String string);
	public void onListItemUnChecked(int position, String string);
	public void onListItemAllChecked();
	public void onListItemAllUnChecked();
}
