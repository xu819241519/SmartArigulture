package com.nfschina.aiot.fragment;

import java.util.ArrayList;
import java.util.List;

import com.nfschina.aiot.R;
import com.nfschina.aiot.constant.Constant;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class GreenHouseHistory extends Fragment implements OnClickListener {

	// the UI controls
	private RadioButton mSunshine;
	private RadioButton mCarbonDioxide;
	private RadioButton mHumidity;
	private RadioButton mTemperature;
	private View mView;

	// the current selected item
	private int mCurrentItem = Constant.CARBONDIOXIDE;

	// test controls
	private LineChartView mChart;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.greenhouse_history, null);
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
	 * Initialize the UI controls
	 */
	private void InitUIControls() {
		mSunshine = (RadioButton) mView.findViewById(R.id.greenhouse_history_sunshine);
		mCarbonDioxide = (RadioButton) mView.findViewById(R.id.greenhouse_history_carbondioxide);
		mHumidity = (RadioButton) mView.findViewById(R.id.greenhouse_history_humidity);
		mTemperature = (RadioButton) mView.findViewById(R.id.greenhouse_history_temperature);
		mCarbonDioxide.setChecked(true);
		mChart = (LineChartView) getActivity().findViewById(R.id.chart);
		mChart.setInteractive(true);
		mChart.setZoomType(ZoomType.HORIZONTAL);
		mChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
	}

	/**
	 * set the listener of the UI controls
	 */
	private void setListener() {
		mSunshine.setOnClickListener(this);
		mCarbonDioxide.setOnClickListener(this);
		mHumidity.setOnClickListener(this);
		mTemperature.setOnClickListener(this);
	}

	/**
	 * change the charts
	 * 
	 * @param kind
	 *            the kind of the chart to be display
	 */
	public void changeCharts(int kind) {
		LineChartData data = null;
		if (kind == Constant.CARBONDIOXIDE) {
			data = getCarbonDioxideData();
		} else if (kind == Constant.SUNSHINE) {
			data = getSunshineData();
		} else if (kind == Constant.TEMPERATURE) {
			data = getTemperatureData();
		} else if (kind == Constant.HUMIDITY) {
			data = getHumidityData();
		}
		if (data != null) {
			mChart.setLineChartData(data);
			mChart.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * get the temperature data
	 * 
	 * @return the data of temperature
	 */
	private LineChartData getTemperatureData() {
		List<PointValue> values = new ArrayList<PointValue>();
		values.add(new PointValue(10, 20));
		values.add(new PointValue(10, 40));
		values.add(new PointValue(20, 30));
		values.add(new PointValue(30, 40));
		values.add(new PointValue(40, 10));
		values.add(new PointValue(50, 10));
		values.add(new PointValue(60, 10));
		values.add(new PointValue(70, 30));

		// In most cased you can call data model methods in
		// builder-pattern-like manner.
		Line line = new Line(values).setColor(Color.BLUE).setCubic(true);
		List<Line> lines = new ArrayList<Line>();
		lines.add(line);

		LineChartData data = new LineChartData();
		data.setLines(lines);
		// 坐标轴
		Axis axisX = new Axis(); // X轴
		axisX.setHasTiltedLabels(true);
		axisX.setTextColor(Color.BLUE);
		axisX.setName("采集时间1");

		axisX.setMaxLabelChars(3);

		data.setAxisXBottom(axisX);

		Axis axisY = new Axis(); // Y轴
		axisY.setMaxLabelChars(3);
		axisY.setName("光照");
		data.setAxisYLeft(axisY);

		return data;
	}

	/**
	 * get the humidity data
	 * 
	 * @return the data of humidity
	 */
	private LineChartData getHumidityData() {
		List<PointValue> values = new ArrayList<PointValue>();
		values.add(new PointValue(10, 20));
		values.add(new PointValue(10, 40));
		values.add(new PointValue(20, 30));
		values.add(new PointValue(30, 40));
		values.add(new PointValue(40, 10));
		values.add(new PointValue(50, 10));
		values.add(new PointValue(60, 10));
		values.add(new PointValue(70, 30));

		// In most cased you can call data model methods in
		// builder-pattern-like manner.
		Line line = new Line(values).setColor(Color.BLUE).setCubic(true);
		List<Line> lines = new ArrayList<Line>();
		lines.add(line);

		LineChartData data = new LineChartData();
		data.setLines(lines);
		// 坐标轴
		Axis axisX = new Axis(); // X轴
		axisX.setHasTiltedLabels(true);
		axisX.setTextColor(Color.BLUE);
		axisX.setName("采集时间2");

		axisX.setMaxLabelChars(3);

		data.setAxisXBottom(axisX);

		Axis axisY = new Axis(); // Y轴
		axisY.setMaxLabelChars(3);
		axisY.setName("光照");
		data.setAxisYLeft(axisY);

		return data;
	}

	/**
	 * get the sunshine data
	 * 
	 * @return the data of the sunshine
	 */
	private LineChartData getSunshineData() {
		List<PointValue> values = new ArrayList<PointValue>();
		values.add(new PointValue(10, 20));
		values.add(new PointValue(10, 40));
		values.add(new PointValue(20, 30));
		values.add(new PointValue(30, 40));
		values.add(new PointValue(40, 10));
		values.add(new PointValue(50, 10));
		values.add(new PointValue(60, 10));
		values.add(new PointValue(70, 30));

		// In most cased you can call data model methods in
		// builder-pattern-like manner.
		Line line = new Line(values).setColor(Color.BLUE).setCubic(true);
		List<Line> lines = new ArrayList<Line>();
		lines.add(line);

		LineChartData data = new LineChartData();
		data.setLines(lines);
		// 坐标轴
		Axis axisX = new Axis(); // X轴
		axisX.setHasTiltedLabels(true);
		axisX.setTextColor(Color.BLUE);
		axisX.setName("采集时间3");

		axisX.setMaxLabelChars(3);

		data.setAxisXBottom(axisX);

		Axis axisY = new Axis(); // Y轴
		axisY.setMaxLabelChars(3);
		axisY.setName("光照");
		data.setAxisYLeft(axisY);

		return data;
	}

	/**
	 * get the carbon dioxide data
	 * 
	 * @return the data of carbon dioxide
	 */
	private LineChartData getCarbonDioxideData() {

		List<PointValue> values = new ArrayList<PointValue>();
		values.add(new PointValue(10, 20));
		values.add(new PointValue(10, 40));
		values.add(new PointValue(20, 30));
		values.add(new PointValue(30, 40));
		values.add(new PointValue(40, 10));
		values.add(new PointValue(50, 10));
		values.add(new PointValue(60, 10));
		values.add(new PointValue(70, 30));

		// In most cased you can call data model methods in
		// builder-pattern-like manner.
		Line line = new Line(values).setColor(Color.BLUE).setCubic(true);
		List<Line> lines = new ArrayList<Line>();
		lines.add(line);

		LineChartData data = new LineChartData();
		data.setLines(lines);
		// 坐标轴
		Axis axisX = new Axis(); // X轴
		axisX.setHasTiltedLabels(true);
		axisX.setTextColor(Color.BLUE);
		axisX.setName("采集时间4");

		axisX.setMaxLabelChars(3);

		data.setAxisXBottom(axisX);

		Axis axisY = new Axis(); // Y轴
		axisY.setMaxLabelChars(3);
		axisY.setName("光照");
		data.setAxisYLeft(axisY);

		return data;
	}

	@Override
	public void onClick(View v) {

		mCurrentItem = Constant.CARBONDIOXIDE;
		switch (v.getId()) {
		case R.id.greenhouse_history_carbondioxide:
			mCurrentItem = Constant.CARBONDIOXIDE;
			break;
		case R.id.greenhouse_history_humidity:
			mCurrentItem = Constant.HUMIDITY;
			break;

		case R.id.greenhouse_history_sunshine:
			mCurrentItem = Constant.SUNSHINE;
			break;
		case R.id.greenhouse_history_temperature:
			mCurrentItem = Constant.TEMPERATURE;
			break;

		default:
			Toast.makeText(getActivity(), Constant.UNDEF, Toast.LENGTH_SHORT).show();
			break;
		}
		changeCharts(mCurrentItem);
	}
}
