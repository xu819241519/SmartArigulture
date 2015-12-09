package com.nfschina.aiot.fragment;

import java.util.Comparator;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nfschina.aiot.activity.History;
import com.nfschina.aiot.activity.R;
import com.nfschina.aiot.adapter.InstructionsAdapter;
import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.constant.ConstantFun;
import com.nfschina.aiot.db.AccessDataBase;
import com.nfschina.aiot.entity.InstructionEntity;
import com.nfschina.aiot.util.NoDataViewUtil;

import android.os.AsyncTask;
import android.os.Bundle;
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
 * 指令记录 通过上拉控件实现展示数据
 * 
 * @author xu
 *
 */

public class InstructionsHistory extends Fragment implements OnClickListener {

	// 下拉刷新控件
	private PullToRefreshListView mPullRefreshListView;
	private ListView mListView;
	private View mView;

	// 温室ID
	private String GreenHouseID;
	// 指令id
	private TextView mID;
	// 发出时间
	private TextView mSendTime;
	// 执行时间
	private TextView mRunTime;
	// 控制内容
	private TextView mContent;
	// 温室
	private TextView mGreenHouse;
	// 用户
	private TextView mUser;
	// 执行周期
	private TextView mRunPeriod;

	// 当前排列类型
	public int mCurrentSortType;

	// 降序排列
	public int Type_DES = 0;
	// 升序排列
	public int Type_ASC = 1;

	private TextView[] mSortItems;

	// 当前排序项
	private TextView mCurrentSortItem = null;

	// 当前页码
	private int mPage = 0;
	// 每页的数量
	private int mSize = 15;

	// 适配器
	private InstructionsAdapter mInstructionsAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.instruction_history, null);
		return mView;
	}

	@Override
	public void onStart() {
		super.onStart();
		initUIControls();
		setListener();
	}

	private void setListener() {
		mID.setOnClickListener(this);
		mContent.setOnClickListener(this);
		mGreenHouse.setOnClickListener(this);
		mRunPeriod.setOnClickListener(this);
		mRunTime.setOnClickListener(this);
		mSendTime.setOnClickListener(this);
		mUser.setOnClickListener(this);
	}

	/**
	 * 初始化UI控件
	 */
	private void initUIControls() {
		mPullRefreshListView = (PullToRefreshListView) mView.findViewById(R.id.instructions_list);
		mID = (TextView) mView.findViewById(R.id.instructions_history_title_id);
		mContent = (TextView) mView.findViewById(R.id.instructions_history_title_content);
		mGreenHouse = (TextView) mView.findViewById(R.id.instructions_history_title_greenhouse);
		mRunPeriod = (TextView) mView.findViewById(R.id.instructions_history_title_runperiod);
		mRunTime = (TextView) mView.findViewById(R.id.instructions_history_title_runtime);
		mSendTime = (TextView) mView.findViewById(R.id.instructions_history_title_sendtime);
		mUser = (TextView) mView.findViewById(R.id.instructions_history_title_user);
		mSortItems = new TextView[] { mID, mContent, mGreenHouse, mRunPeriod, mRunTime, mSendTime, mUser };
		GreenHouseID = ((History) getActivity()).getGreenHouseID();
		mCurrentSortItem = mRunTime;
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
		mInstructionsAdapter = new InstructionsAdapter();
		mListView.setAdapter(mInstructionsAdapter);

		mPullRefreshListView.setRefreshing();
		// mPullRefreshListView.setRefreshing(true);
		new GetDataTask().execute(true);

		setCurrentSortItem();

	}

	/**
	 * 连接服务器获取指令数据
	 * 
	 * @author xu
	 *
	 */
	private class GetDataTask extends AsyncTask<Boolean, Void, Void> {

		// 获取到的数据的条数
		private int DataCount = 0;

		@Override
		protected Void doInBackground(Boolean... params) {
			DataCount = 0;
			// pull down
			if (params[0]) {
				mPage = 0;
				mInstructionsAdapter.clearData();
				List<InstructionEntity> data = AccessDataBase.getInstructionHistoryData(mPage, mSize, GreenHouseID);
				if (data != null && data.size() > 0) {
					mInstructionsAdapter.addData(data);
					DataCount = data.size();
				}
			} else {
				List<InstructionEntity> list = AccessDataBase.getInstructionHistoryData(mPage + 1, mSize, GreenHouseID);
				if (list != null && list.size() != 0) {
					mPage++;
					mInstructionsAdapter.addData(list);
					DataCount = list.size();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			if (DataCount != 0) {
				mInstructionsAdapter.notifyDataSetChanged();
				if (DataCount < mSize)
					Toast.makeText(getActivity(), Constant.END_OF_LIST, Toast.LENGTH_SHORT).show();
			} else {
				if (mInstructionsAdapter.getCount() == 0)
					NoDataViewUtil.setNoDataView(getActivity(), mPullRefreshListView);
				else
					Toast.makeText(getActivity(), Constant.END_OF_LIST, Toast.LENGTH_SHORT).show();

			}
			mPullRefreshListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}

	/**
	 * 设置当前排序类型
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

	/**
	 * 设置当前排序项
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
	 * 获取比较规则
	 * 
	 * @param textView
	 * @return 比较规则
	 */
	private Comparator<InstructionEntity> getComparator(TextView textView) {
		Comparator<InstructionEntity> comparator = null;
		if (mCurrentSortType == Type_DES) {
			if (textView == mID) {
				comparator = new Comparator<InstructionEntity>() {

					@Override
					public int compare(InstructionEntity lhs, InstructionEntity rhs) {
						if (lhs.getID() < rhs.getID())
							return 1;
						else if (lhs.getID() > rhs.getID())
							return -1;
						else
							return 0;
					}
				};
			} else if (textView == mContent) {
				comparator = new Comparator<InstructionEntity>() {

					@Override
					public int compare(InstructionEntity lhs, InstructionEntity rhs) {
						int result = lhs.getContent().compareTo(rhs.getContent());
						if (result == 0) {
							result = lhs.getRunTime().compareTo(rhs.getRunTime());
							if (result == 0)
								result = lhs.getSendTime().compareTo(rhs.getSendTime());
						}
						return -1 * result;
					}
				};

			} else if (textView == mRunPeriod) {
				comparator = new Comparator<InstructionEntity>() {

					@Override
					public int compare(InstructionEntity lhs, InstructionEntity rhs) {
						int result = lhs.getRunPeriod().compareTo(rhs.getRunPeriod());
						if (result == 0) {
							result = lhs.getRunTime().compareTo(rhs.getRunTime());
							if (result == 0)
								result = lhs.getSendTime().compareTo(rhs.getSendTime());

						}
						return -1 * result;
					}
				};

			} else if (textView == mRunTime) {
				comparator = new Comparator<InstructionEntity>() {

					@Override
					public int compare(InstructionEntity lhs, InstructionEntity rhs) {
						int result = lhs.getRunTime().compareTo(rhs.getRunTime());
						if (result == 0) {
							result = lhs.getSendTime().compareTo(rhs.getSendTime());
						}
						return -1 * result;
					}
				};

			} else if (textView == mSendTime) {
				comparator = new Comparator<InstructionEntity>() {

					@Override
					public int compare(InstructionEntity lhs, InstructionEntity rhs) {
						int result = lhs.getSendTime().compareTo(rhs.getSendTime());
						if (result == 0) {
							result = lhs.getRunTime().compareTo(rhs.getRunTime());
						}

						return -1 * result;
					}
				};

			} else if (textView == mUser) {
				comparator = new Comparator<InstructionEntity>() {

					@Override
					public int compare(InstructionEntity lhs, InstructionEntity rhs) {
						int result = lhs.getUserID().compareTo(rhs.getUserID());
						if (result == 0) {
							result = lhs.getRunTime().compareTo(rhs.getRunTime());
							if (result == 0)
								result = lhs.getSendTime().compareTo(rhs.getSendTime());

						}
						return -1 * result;
					}
				};

			} else if (textView == mGreenHouse) {
				comparator = new Comparator<InstructionEntity>() {

					@Override
					public int compare(InstructionEntity lhs, InstructionEntity rhs) {
						int result = lhs.getGreenHouseID().compareTo(rhs.getGreenHouseID());
						if (result == 0) {
							result = lhs.getRunTime().compareTo(rhs.getRunTime());
							if (result == 0)
								result = lhs.getSendTime().compareTo(rhs.getSendTime());

						}
						return -1 * result;
					}
				};

			}
		} else if (mCurrentSortType == Type_ASC) {
			if (textView == mID) {
				comparator = new Comparator<InstructionEntity>() {

					@Override
					public int compare(InstructionEntity lhs, InstructionEntity rhs) {
						if (lhs.getID() < rhs.getID())
							return -1;
						else if (lhs.getID() > rhs.getID())
							return 1;
						else
							return 0;
					}
				};
			} else if (textView == mContent) {
				comparator = new Comparator<InstructionEntity>() {

					@Override
					public int compare(InstructionEntity lhs, InstructionEntity rhs) {
						int result = lhs.getContent().compareTo(rhs.getContent());
						if (result == 0) {
							result = lhs.getRunTime().compareTo(rhs.getRunTime());
							if (result == 0)
								result = lhs.getSendTime().compareTo(rhs.getSendTime());
						}
						return result;
					}
				};

			} else if (textView == mRunPeriod) {
				comparator = new Comparator<InstructionEntity>() {

					@Override
					public int compare(InstructionEntity lhs, InstructionEntity rhs) {
						int result = lhs.getRunPeriod().compareTo(rhs.getRunPeriod());
						if (result == 0) {
							result = lhs.getRunTime().compareTo(rhs.getRunTime());
							if (result == 0)
								result = lhs.getSendTime().compareTo(rhs.getSendTime());

						}
						return result;
					}
				};

			} else if (textView == mRunTime) {
				comparator = new Comparator<InstructionEntity>() {

					@Override
					public int compare(InstructionEntity lhs, InstructionEntity rhs) {
						int result = lhs.getRunTime().compareTo(rhs.getRunTime());
						if (result == 0) {
							result = lhs.getSendTime().compareTo(rhs.getSendTime());
						}
						return result;
					}
				};

			} else if (textView == mSendTime) {
				comparator = new Comparator<InstructionEntity>() {

					@Override
					public int compare(InstructionEntity lhs, InstructionEntity rhs) {
						int result = lhs.getSendTime().compareTo(rhs.getSendTime());
						if (result == 0) {
							result = lhs.getRunTime().compareTo(rhs.getRunTime());
						}

						return result;
					}
				};

			} else if (textView == mUser) {
				comparator = new Comparator<InstructionEntity>() {

					@Override
					public int compare(InstructionEntity lhs, InstructionEntity rhs) {
						int result = lhs.getUserID().compareTo(rhs.getUserID());
						if (result == 0) {
							result = lhs.getRunTime().compareTo(rhs.getRunTime());
							if (result == 0)
								result = lhs.getSendTime().compareTo(rhs.getSendTime());

						}
						return result;
					}
				};

			} else if (textView == mGreenHouse) {
				comparator = new Comparator<InstructionEntity>() {

					@Override
					public int compare(InstructionEntity lhs, InstructionEntity rhs) {
						int result = lhs.getGreenHouseID().compareTo(rhs.getGreenHouseID());
						if (result == 0) {
							result = lhs.getRunTime().compareTo(rhs.getRunTime());
							if (result == 0)
								result = lhs.getSendTime().compareTo(rhs.getSendTime());

						}
						return result;
					}
				};

			}
		}
		return comparator;
	}

	@Override
	public void onClick(View v) {
		Comparator<InstructionEntity> comparator = null;
		switch (v.getId()) {
		case R.id.instructions_history_title_content:
			setCurrentSortType(mContent);
			comparator = getComparator(mContent);
			mCurrentSortItem = mContent;
			break;
		case R.id.instructions_history_title_greenhouse:
			setCurrentSortType(mGreenHouse);
			comparator = getComparator(mGreenHouse);
			mCurrentSortItem = mGreenHouse;
			break;
		case R.id.instructions_history_title_id:
			setCurrentSortType(mID);
			comparator = getComparator(mID);
			mCurrentSortItem = mID;
			break;
		case R.id.instructions_history_title_runperiod:
			setCurrentSortType(mRunPeriod);
			comparator = getComparator(mRunPeriod);
			mCurrentSortItem = mRunPeriod;
			break;
		case R.id.instructions_history_title_sendtime:
			setCurrentSortType(mSendTime);
			comparator = getComparator(mSendTime);
			mCurrentSortItem = mSendTime;
			break;
		case R.id.instructions_history_title_runtime:
			setCurrentSortType(mRunTime);
			comparator = getComparator(mRunTime);
			mCurrentSortItem = mRunTime;
			break;
		case R.id.instructions_history_title_user:
			setCurrentSortType(mUser);
			comparator = getComparator(mUser);
			mCurrentSortItem = mUser;
			break;
		default:
			break;
		}

		mInstructionsAdapter.sortData(comparator);
		setCurrentSortItem();

	}
}
