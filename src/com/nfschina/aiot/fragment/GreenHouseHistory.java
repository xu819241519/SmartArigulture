package com.nfschina.aiot.fragment;

import java.util.ArrayList;
import java.util.List;

import com.nfschina.aiot.R;
import com.nfschina.aiot.constant.Constant;

import android.graphics.Color;
import android.os.AsyncTask;
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

/**
 * ���Ҵ���������ʷ��¼
 * ͨ���������⣨AndroidSliderImage��������ͼ��ʵ��չʾ
 * @author xu
 *
 */
public class GreenHouseHistory extends Fragment implements OnClickListener {

	// ���հ�ť
	private RadioButton mSunshine;
	// ������̼��ť
	private RadioButton mCarbonDioxide;
	// ʪ�Ȱ�ť
	private RadioButton mHumidity;
	// �¶Ȱ�ť
	private RadioButton mTemperature;
	
	private View mView;

	// ��ǰѡ��鿴��ֵ
	private int mCurrentItem = Constant.CARBONDIOXIDE;

	// ����ͼ
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
	 * ��ʼ��UI�ؼ�
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
	 * ����UI�ؼ��ļ���
	 */
	private void setListener() {
		mSunshine.setOnClickListener(this);
		mCarbonDioxide.setOnClickListener(this);
		mHumidity.setOnClickListener(this);
		mTemperature.setOnClickListener(this);
	}

	/**
	 * ��������ͼ��ʾ��ʵ������
	 * 
	 * @param kind Ҫ��ʾ��ʵ������
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
	 * ����¶�����
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
		// ������
		Axis axisX = new Axis(); // X��
		axisX.setHasTiltedLabels(true);
		axisX.setTextColor(Color.BLUE);
		axisX.setName("�ɼ�ʱ��1");

		axisX.setMaxLabelChars(3);

		data.setAxisXBottom(axisX);

		Axis axisY = new Axis(); // Y��
		axisY.setMaxLabelChars(3);
		axisY.setName("����");
		data.setAxisYLeft(axisY);

		return data;
	}

	/**
	 * ���ʪ������
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
		// ������
		Axis axisX = new Axis(); // X��
		axisX.setHasTiltedLabels(true);
		axisX.setTextColor(Color.BLUE);
		axisX.setName("�ɼ�ʱ��2");

		axisX.setMaxLabelChars(3);

		data.setAxisXBottom(axisX);

		Axis axisY = new Axis(); // Y��
		axisY.setMaxLabelChars(3);
		axisY.setName("����");
		data.setAxisYLeft(axisY);

		return data;
	}

	/**
	 * ��ù�������
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
		// ������
		Axis axisX = new Axis(); // X��
		axisX.setHasTiltedLabels(true);
		axisX.setTextColor(Color.BLUE);
		axisX.setName("�ɼ�ʱ��3");

		axisX.setMaxLabelChars(3);

		data.setAxisXBottom(axisX);

		Axis axisY = new Axis(); // Y��
		axisY.setMaxLabelChars(3);
		axisY.setName("����");
		data.setAxisYLeft(axisY);

		return data;
	}

	/**
	 * ��ö�����̼����
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
		// ������
		Axis axisX = new Axis(); // X��
		axisX.setHasTiltedLabels(true);
		axisX.setTextColor(Color.BLUE);
		axisX.setName("�ɼ�ʱ��4");

		axisX.setMaxLabelChars(3);

		data.setAxisXBottom(axisX);

		Axis axisY = new Axis(); // Y��
		axisY.setMaxLabelChars(3);
		axisY.setName("����");
		data.setAxisYLeft(axisY);

		return data;
	}

	/**
	 * ��ť����¼�
	 */
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
	
	
	
	/**
	 * @author xu
	 *
	 */
	public class GetDataTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}
	}
}
