package com.nfschina.aiot.fragment;

import java.util.Comparator;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nfschina.aiot.activity.History;
import com.nfschina.aiot.activity.R;
import com.nfschina.aiot.adapter.AlarmAdapter;
import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.constant.ConstantFun;
import com.nfschina.aiot.db.AccessDataBase;
import com.nfschina.aiot.entity.AlarmEntity;
import com.nfschina.aiot.util.NoDataViewUtil;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ������¼ �������ؼ�ʵ��չʾ
 * 
 * @author xu
 *
 */

public class AlarmHistory extends Fragment implements OnClickListener {

	// ����ˢ�¿ؼ�
	private PullToRefreshListView mPullRefreshListView;
	private ListView mListView;
	private View mView;

	// id
	private TextView mID;
	// ����ʱ��
	private TextView mTime;
	// ��������
	private TextView mContent;
	// ����
	private TextView mGreenHouse;
	// �û�
	private TextView mLevel;
	// ִ������
	private TextView mState;
	// ��ǰ������
	private TextView mCurrentSortItem;

	private TextView[] mSortItems;

	// ��ǰ����ID
	private String GreenHouseID;
	// ��ǰҳ��
	private int mPage = 0;
	// һҳ������
	private int mSize = 15;

	// ��������
	public int Type_DES = 0;
	// ��������
	public int Type_ASC = 1;
	// ��ǰ��������
	public int mCurrentSortType;
	// ������
	private AlarmAdapter mAlarmAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.alarm_history, null);
		return mView;
	}

	@Override
	public void onStart() {
		super.onStart();
		initUIControls();
		setListener();

	}
	/**
	 * ���ü�����
	 */
	public void setListener() {
		mID.setOnClickListener(this);
		mTime.setOnClickListener(this);
		mContent.setOnClickListener(this);
		mGreenHouse.setOnClickListener(this);
		mState.setOnClickListener(this);
		mLevel.setOnClickListener(this);
	}

	/**
	 * ��ʼ��UI�ؼ�
	 */
	private void initUIControls() {

		mPullRefreshListView = (PullToRefreshListView) mView.findViewById(R.id.alarm_list);
		mID = (TextView) mView.findViewById(R.id.alarm_history_title_id);
		mTime = (TextView) mView.findViewById(R.id.alarm_history_title_time);
		mContent = (TextView) mView.findViewById(R.id.alarm_history_title_content);
		mGreenHouse = (TextView) mView.findViewById(R.id.alarm_history_title_greenhouse);
		mState = (TextView) mView.findViewById(R.id.alarm_history_title_state);
		mLevel = (TextView) mView.findViewById(R.id.alarm_history_title_level);
		mCurrentSortItem = mTime;
		mSortItems = new TextView[] { mID, mTime, mContent, mGreenHouse, mState, mLevel };

		GreenHouseID = ((History) getActivity()).getGreenHouseID();
		mPullRefreshListView.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.BOTH);
		mPullRefreshListView.setScrollingWhileRefreshingEnabled(true);
		mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				new GetDataTask().execute(true);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				new GetDataTask().execute(false);
			}

		});
		mPullRefreshListView.setOnPullEventListener(ConstantFun.getSoundListener(getActivity()));
		mListView = mPullRefreshListView.getRefreshableView();
		mAlarmAdapter = new AlarmAdapter();
		mListView.setAdapter(mAlarmAdapter);

		new Handler(new Handler.Callback() {

			@Override
			public boolean handleMessage(Message msg) {
				mPullRefreshListView.setRefreshing();

				return true;
			}
		}).sendEmptyMessageDelayed(0, 300);
		// mPullRefreshListView.setRefreshing(true);
		new GetDataTask().execute(true);

		setCurrentSortItem();
		mCurrentSortType = Type_DES;
	}

	/**
	 * �ӷ�������ȡ������¼
	 * 
	 * @author xu
	 *
	 */
	private class GetDataTask extends AsyncTask<Boolean, Void, Void> {

		// ��ȡ���ݵ�����
		private int DataCount = 0;

		@Override
		protected Void doInBackground(Boolean... params) {
			DataCount = 0;
			// ����
			if (params[0]) {
				mPage = 0;
				mAlarmAdapter.clearData();
				List<AlarmEntity> data = AccessDataBase.getAlarmHistoryData(mPage, mSize, GreenHouseID);
				if (data != null && data.size() > 0) {
					mAlarmAdapter.addData(data);
					DataCount = data.size();
				}
			}
			// ����
			else {
				List<AlarmEntity> list = AccessDataBase.getAlarmHistoryData(mPage + 1, mSize, GreenHouseID);
				if (list != null && list.size() != 0) {
					mPage++;
					mAlarmAdapter.addData(list);
					DataCount = list.size();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (DataCount != 0) {
				mAlarmAdapter.notifyDataSetChanged();
				if (DataCount < mSize)
					Toast.makeText(getActivity(), Constant.END_OF_LIST, Toast.LENGTH_SHORT).show();
			}

			else {
				if (mAlarmAdapter.getCount() == 0)
					NoDataViewUtil.setNoDataView(getActivity(), mPullRefreshListView);
				else
					Toast.makeText(getActivity(), Constant.END_OF_LIST, Toast.LENGTH_SHORT).show();
			}

			mPullRefreshListView.onRefreshComplete();
		}
	}

	@Override
	public void onClick(View v) {
		Comparator<AlarmEntity> comparator = null;
		switch (v.getId()) {
		case R.id.alarm_history_title_id:
			setCurrentSortType(mID);
			comparator = getComparator(mID);
			mCurrentSortItem = mID;
			break;
		case R.id.alarm_history_title_time:
			setCurrentSortType(mTime);
			comparator = getComparator(mTime);
			mCurrentSortItem = mTime;
			break;
		case R.id.alarm_history_title_content:
			setCurrentSortType(mContent);
			comparator = getComparator(mContent);
			mCurrentSortItem = mContent;
			break;
		case R.id.alarm_history_title_greenhouse:
			setCurrentSortType(mGreenHouse);
			comparator = getComparator(mGreenHouse);
			mCurrentSortItem = mGreenHouse;
			break;
		case R.id.alarm_history_title_level:
			setCurrentSortType(mLevel);
			comparator = getComparator(mLevel);
			mCurrentSortItem = mLevel;
			break;
		case R.id.alarm_history_title_state:
			setCurrentSortType(mState);
			comparator = getComparator(mState);
			mCurrentSortItem = mState;
			break;

		default:
			break;
		}

		mAlarmAdapter.sortData(comparator);
		setCurrentSortItem();

	}

	/**
	 * ���õ�ǰ������
	 * 
	 * @param textView
	 */
	private void setCurrentSortItem() {
		for (int i = 0; i < mSortItems.length; ++i) {
			if (mSortItems[i] == mCurrentSortItem) {
				mSortItems[i].setTextColor(getResources().getColor(R.color.nav_bg));
			} else {
				mSortItems[i].setTextColor(getResources().getColor(R.color.white));
			}
		}
	}

	/**
	 * ��ñȽ���
	 * 
	 * @param textView
	 * @param type
	 * @return
	 */
	private Comparator<AlarmEntity> getComparator(TextView textView) {
		Comparator<AlarmEntity> comparator = null;
		if (mCurrentSortType == Type_ASC) {
			if (textView == mID) {
				comparator = new Comparator<AlarmEntity>() {

					@Override
					public int compare(AlarmEntity lhs, AlarmEntity rhs) {
						if (lhs.getID() > rhs.getID())
							return 1;
						else
							return -1;
					}
				};
			} else if (textView == mTime || textView == mGreenHouse) {
				comparator = new Comparator<AlarmEntity>() {

					@Override
					public int compare(AlarmEntity lhs, AlarmEntity rhs) {
						return lhs.getTime().compareTo(rhs.getTime());
					}
				};
			} else if (textView == mLevel) {
				comparator = new Comparator<AlarmEntity>() {

					@Override
					public int compare(AlarmEntity lhs, AlarmEntity rhs) {
						int compareResult = lhs.getLevel().compareTo(rhs.getLevel());
						if (compareResult == 0) {
							compareResult = lhs.getTime().compareTo(rhs.getTime());
						}
						return compareResult;

					}
				};
			} else if (textView == mContent) {
				comparator = new Comparator<AlarmEntity>() {

					@Override
					public int compare(AlarmEntity lhs, AlarmEntity rhs) {
						int compareResult = lhs.getContent().compareTo(rhs.getContent());
						if (compareResult == 0) {
							compareResult = lhs.getTime().compareTo(rhs.getTime());
						}

						return compareResult;

					}
				};
			} else if (textView == mState) {
				comparator = new Comparator<AlarmEntity>() {

					@Override
					public int compare(AlarmEntity lhs, AlarmEntity rhs) {
						int compareResult = lhs.getState().compareTo(rhs.getState());
						if (compareResult == 0) {
							compareResult = lhs.getTime().compareTo(rhs.getTime());
						}
						return compareResult;

					}
				};
			}
		} else if (mCurrentSortType == Type_DES) {
			if (textView == mID) {
				comparator = new Comparator<AlarmEntity>() {

					@Override
					public int compare(AlarmEntity lhs, AlarmEntity rhs) {
						if (lhs.getID() <= rhs.getID())
							return 1;
						else
							return -1;
					}
				};
			} else if (textView == mTime || textView == mGreenHouse) {
				comparator = new Comparator<AlarmEntity>() {

					@Override
					public int compare(AlarmEntity lhs, AlarmEntity rhs) {
						return -1*lhs.getTime().compareTo(rhs.getTime());
					}
				};
			} else if (textView == mLevel) {
				comparator = new Comparator<AlarmEntity>() {

					@Override
					public int compare(AlarmEntity lhs, AlarmEntity rhs) {
						int compareResult = lhs.getLevel().compareTo(rhs.getLevel());
						if (compareResult == 0) {
							compareResult = lhs.getTime().compareTo(rhs.getTime());
						}
						return -1*compareResult;

					}
				};
			} else if (textView == mContent) {
				comparator = new Comparator<AlarmEntity>() {

					@Override
					public int compare(AlarmEntity lhs, AlarmEntity rhs) {
						int compareResult = lhs.getContent().compareTo(rhs.getContent());
						if (compareResult == 0) {
							compareResult = lhs.getTime().compareTo(rhs.getTime());
						}

						return -1*compareResult;

					}
				};
			} else if (textView == mState) {
				comparator = new Comparator<AlarmEntity>() {

					@Override
					public int compare(AlarmEntity lhs, AlarmEntity rhs) {
						int compareResult = lhs.getState().compareTo(rhs.getState());
						if (compareResult == 0) {
							compareResult = lhs.getTime().compareTo(rhs.getTime());
						}
						return -1*compareResult;

					}
				};
			}
		}
		return comparator;
	}

	/**
	 * ���õ�ǰ��������
	 * 
	 * @param textView
	 */
	private void setCurrentSortType(TextView textView) {
		if (mCurrentSortItem == textView) {
			if (mCurrentSortType == Type_DES) {
				mCurrentSortType = Type_ASC;
			} else {
				mCurrentSortType = Type_DES;
			}
		} else
			mCurrentSortType = Type_DES;
	}

}
