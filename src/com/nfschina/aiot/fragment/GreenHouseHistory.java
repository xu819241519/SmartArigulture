package com.nfschina.aiot.fragment;

import java.util.ArrayList;
import java.util.List;

import com.nfschina.aiot.activity.History;
import com.nfschina.aiot.activity.R;
import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.db.AccessDataBase;
import com.nfschina.aiot.entity.CarbonDioxideEntity;
import com.nfschina.aiot.entity.EnvironmentParameterEntity;
import com.nfschina.aiot.entity.HumidityEntity;
import com.nfschina.aiot.entity.IlluminanceEntity;
import com.nfschina.aiot.entity.TemperatureEntity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * 温室大棚数据历史记录 通过第三方库（AndroidSliderImage）的折线图来实现展示
 * 
 * @author xu
 *
 */
public class GreenHouseHistory extends Fragment implements OnClickListener {

	// 光照按钮
	private RadioButton mSunshine;
	// 二氧化碳按钮
	private RadioButton mCarbonDioxide;
	// 湿度按钮
	private RadioButton mHumidity;
	// 温度按钮
	private RadioButton mTemperature;

	private View mView;

	// 下拉刷新组件
	private SwipeRefreshLayout mRefreshLayout;

	// 当前游标
	private ImageView mCursor;
	// 当前温室ID
	private String GreenHouseID;

	// 当前选择查看的值
	private int mCurrentItem = Constant.CARBONDIOXIDE;

	// 折线图
	private LineChartView mChart;
	// 报表数据
	private LineChartData mLineChartData;

	// 等待对话框
	private ProgressDialog mDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.greenhouse_history, null);
		mRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.history_swiperefresh);
		mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				changeCharts(mCurrentItem);

			}
		});
		return mView;
	}

	@Override
	public void onStart() {
		super.onStart();
		InitUIControls();
		setListener();
		changeCharts(mCurrentItem);
	}

	/**
	 * 显示或关闭等待对话框
	 * 
	 * @param show
	 */
	private void showDialog(boolean show) {
		if (show) {
			if (mDialog == null) {
				mDialog = new ProgressDialog(getActivity());
				mDialog.setTitle("正在获取数据...");
				mDialog.show();
			}
		} else {
			if (mDialog != null) {
				mDialog.dismiss();
				mDialog = null;
			}
		}
	}

	/**
	 * 初始化UI控件
	 */
	private void InitUIControls() {
		mSunshine = (RadioButton) mView.findViewById(R.id.greenhouse_history_sunshine);
		GreenHouseID = ((History) getActivity()).getGreenHouseID();
		mCarbonDioxide = (RadioButton) mView.findViewById(R.id.greenhouse_history_carbondioxide);
		mHumidity = (RadioButton) mView.findViewById(R.id.greenhouse_history_humidity);
		mTemperature = (RadioButton) mView.findViewById(R.id.greenhouse_history_temperature);
		mCursor = (ImageView) mView.findViewById(R.id.history_cursor);
		android.view.ViewGroup.LayoutParams para;
		para = mCursor.getLayoutParams();
		para.width = getResources().getDisplayMetrics().widthPixels / 4;
		mCursor.setLayoutParams(para);

		mCarbonDioxide.setChecked(true);
		mChart = (LineChartView) getActivity().findViewById(R.id.chart);
		mChart.setInteractive(true);
		mChart.setZoomType(ZoomType.HORIZONTAL);
		mChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
		showDialog(true);
	}

	/**
	 * 设置UI控件的监听
	 */
	private void setListener() {
		mSunshine.setOnClickListener(this);
		mCarbonDioxide.setOnClickListener(this);
		mHumidity.setOnClickListener(this);
		mTemperature.setOnClickListener(this);
	}

	/**
	 * 更换折线图显示的实体种类
	 * 
	 * @param kind
	 *            要显示的实体种类
	 */
	public void changeCharts(int kind) {

		new GetDataTask().execute(kind);
	}

	/**
	 * 按钮点击事件
	 */
	@Override
	public void onClick(View v) {

		android.view.ViewGroup.LayoutParams para;
		para = mCursor.getLayoutParams();
		int lastItem = mCurrentItem;

		mCurrentItem = Constant.CARBONDIOXIDE;
		switch (v.getId()) {
		case R.id.greenhouse_history_carbondioxide:
			mCurrentItem = Constant.CARBONDIOXIDE;
			break;
		case R.id.greenhouse_history_humidity:
			mCurrentItem = Constant.HUMIDITY;
			break;

		case R.id.greenhouse_history_sunshine:
			mCurrentItem = Constant.ILLUMINANCE;
			break;
		case R.id.greenhouse_history_temperature:
			mCurrentItem = Constant.TEMPERATURE;
			break;

		default:
			Toast.makeText(getActivity(), Constant.UNDEF, Toast.LENGTH_SHORT).show();
			break;
		}
		// showDialog(true);
		if (lastItem != mCurrentItem) {
			showDialog(true);
			changeCharts(mCurrentItem);

		}

		TranslateAnimation translateAnimation = new TranslateAnimation(lastItem * para.width, mCurrentItem * para.width,
				0, 0);
		translateAnimation.setDuration(200);
		translateAnimation.setFillAfter(true);
		mCursor.startAnimation(translateAnimation);

	}

	/**
	 * 转换显示x坐标的时间
	 * 
	 * @param time
	 *            时间字符串
	 * @return 要显示的时间字符串
	 */
	private String convertTime(String time) {
		String result = "";
		time = time.substring(6);
		result += time.substring(0, 2);
		result += "日";
		result += time.substring(2, 4);
		result += ":";
		result += time.substring(4, 6);

		return result;
	}

	/**
	 * 更新界面显示
	 * 
	 * @param entities
	 * @param kind
	 */
	private void updateDisplay(List<EnvironmentParameterEntity> entities, int kind) {
		List<PointValue> values = new ArrayList<PointValue>();
		List<AxisValue> axisValues = new ArrayList<AxisValue>();
		for (int i = 0; i < entities.size(); ++i) {
			if (kind == Constant.CARBONDIOXIDE) {
				CarbonDioxideEntity carbonDioxideEntity = (CarbonDioxideEntity) entities.get(i);
				values.add(new PointValue(i, carbonDioxideEntity.getData()));
				axisValues.add(new AxisValue(i).setLabel(convertTime(carbonDioxideEntity.getTime())));
			} else if (kind == Constant.TEMPERATURE) {
				TemperatureEntity temperatureEntity = (TemperatureEntity) entities.get(i);
				values.add(new PointValue(i, temperatureEntity.getData()));
				axisValues.add(new AxisValue(i).setLabel(convertTime(temperatureEntity.getTime())));
			} else if (kind == Constant.HUMIDITY) {
				HumidityEntity humidityEntity = (HumidityEntity) entities.get(i);
				values.add(new PointValue(i, humidityEntity.getData()));
				axisValues.add(new AxisValue(i).setLabel(convertTime(humidityEntity.getTime())));
			} else if (kind == Constant.ILLUMINANCE) {
				IlluminanceEntity illuminanceEntity = (IlluminanceEntity) entities.get(i);
				values.add(new PointValue(i, illuminanceEntity.getData()));
				axisValues.add(new AxisValue(i).setLabel(convertTime(illuminanceEntity.getTime())));
			}
		}

		// In most cased you can call data model methods in
		// builder-pattern-like manner.
		Line line = new Line(values).setColor(Color.WHITE).setCubic(true);
		line.setPointRadius(1);
		line.setStrokeWidth(1);
		// line.setHasPoints(false);
		//line.setHasLines(false);
		List<Line> lines = new ArrayList<Line>();
		lines.add(line);

		boolean bFirst = false;
		if (mLineChartData == null) {
			bFirst = true;
			mLineChartData = new LineChartData();
		}

		mLineChartData.setLines(lines);
		// 坐标轴
		Axis axisX = new Axis(); // X轴
		axisX.setHasTiltedLabels(true);
		axisX.setTextColor(Color.WHITE);
		axisX.setName("时间");
		axisX.setTextSize(10);
		axisX.setHasLines(false);

		axisX.setMaxLabelChars(3);
		axisX.setValues(axisValues);

		mLineChartData.setAxisXBottom(axisX);

		Axis axisY = new Axis(); // Y轴
		axisY.setTextColor(Color.WHITE);
		axisY.setMaxLabelChars(3);
		if (kind == Constant.TEMPERATURE)
			axisY.setName("温度");
		else if (kind == Constant.HUMIDITY)
			axisY.setName("湿度");
		else if (kind == Constant.CARBONDIOXIDE)
			axisY.setName("二氧化碳浓度");
		else if (kind == Constant.ILLUMINANCE)
			axisY.setName("光照");
		axisY.setTextSize(10);
		mLineChartData.setAxisYLeft(axisY);
		if (bFirst)
			mChart.setLineChartData(mLineChartData);
		else {
			mChart.startDataAnimation();
		}
		mChart.setVisibility(View.VISIBLE);
		showDialog(false);
		mRefreshLayout.setRefreshing(false);
	}

	/**
	 * @author xu
	 *
	 */
	public class GetDataTask extends AsyncTask<Integer, Void, List<EnvironmentParameterEntity>> {

		@Override
		protected List<EnvironmentParameterEntity> doInBackground(Integer... params) {
			List<EnvironmentParameterEntity> result = null;
			if (params[0] == Constant.CARBONDIOXIDE) {
				result = AccessDataBase.getCarbonDioxideData(GreenHouseID);
			} else if (params[0] == Constant.HUMIDITY) {
				result = AccessDataBase.getHumidityData(GreenHouseID);
			} else if (params[0] == Constant.TEMPERATURE) {
				result = AccessDataBase.getTemperatureData(GreenHouseID);
			} else if (params[0] == Constant.ILLUMINANCE) {
				result = AccessDataBase.getIlluminanceData(GreenHouseID);
			}
			return result;
		}

		@Override
		protected void onPostExecute(List<EnvironmentParameterEntity> result) {
			if (result != null && result.size() != 0) {
				EnvironmentParameterEntity entity = result.get(0);
				updateDisplay(result, entity.getKind());
			} else {
				showDialog(false);
				mRefreshLayout.setRefreshing(false);
				Toast.makeText(getActivity(), "未获取到数据更新", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
