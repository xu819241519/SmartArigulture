package com.nfschina.aiot.fragment;

import com.nfschina.aiot.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;

public class GreenHouseHistory extends Fragment implements OnClickListener {

	
	private RadioButton mSunshine;
	private RadioButton mCarbonDioxide;
	private RadioButton mHumidity;
	private RadioButton mTemperature;
	
	private View mView;
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
	}

	private void InitUIControls(){
		mSunshine = (RadioButton) mView.findViewById(R.id.greenhouse_history_sunshine);
		mCarbonDioxide = (RadioButton) mView.findViewById(R.id.greenhouse_history_carbondioxide);
		mHumidity = (RadioButton) mView.findViewById(R.id.greenhouse_history_humidity);
		mTemperature = (RadioButton) mView.findViewById(R.id.greenhouse_history_temperature);
		mCarbonDioxide.setChecked(true);
	}
	
	private void setListener(){
		mSunshine.setOnClickListener(this);
		mCarbonDioxide.setOnClickListener(this);
		mHumidity.setOnClickListener(this);
		mTemperature.setOnClickListener(this);
	}
	// mLinearLayout = (LinearLayout)findViewById(R.id.history_main);
	// mChart = new LineChartView(this);
	// mChart.setInteractive(true);
	// mChart.setZoomType(ZoomType.HORIZONTAL);
	// mChart.setContainerScrollEnabled(true,
	// ContainerScrollType.HORIZONTAL);
	//
	// List<PointValue> values = new ArrayList<PointValue>();
	// values.add(new PointValue(0, 2));
	// values.add(new PointValue(1, 4));
	// values.add(new PointValue(2, 3));
	// values.add(new PointValue(3, 4));
	// values.add(new PointValue(4, 1));
	// values.add(new PointValue(5, 1));
	//
	//
	// //In most cased you can call data model methods in
	// builder-pattern-like manner.
	// Line line = new Line(values).setColor(Color.BLUE).setCubic(true);
	// List<Line> lines = new ArrayList<Line>();
	// lines.add(line);
	//
	// LineChartData data = new LineChartData();
	// data.setLines(lines);
	// //坐标轴
	// Axis axisX = new Axis(); //X轴
	//// axisX.setHasTiltedLabels(true);
	//// axisX.setTextColor(Color.BLUE);
	//// axisX.setName("采集时间");
	// axisX.setMaxLabelChars(3);
	//
	// data.setAxisXBottom(axisX);
	//
	// Axis axisY = new Axis(); //Y轴
	// axisY.setMaxLabelChars(3);
	// data.setAxisYLeft(axisY);
	//// data.setBaseValue(baseValue)
	//
	//
	//
	// mChart.setLineChartData(data);
	// mChart.setVisibility(View.VISIBLE);
	// mLinearLayout.addView(mChart);

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.greenhouse_history_carbondioxide:
			
			break;
		case R.id.greenhouse_history_humidity:
			break;
			
		case R.id.greenhouse_history_sunshine:
			break;
		case R.id.greenhouse_history_temperature:
			break;

		default:
			break;
		}
	}
}
