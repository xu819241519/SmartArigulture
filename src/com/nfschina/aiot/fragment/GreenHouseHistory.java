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
 * ���Ҵ���������ʷ��¼ ͨ���������⣨AndroidSliderImage��������ͼ��ʵ��չʾ
 * 
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

	// ����ˢ�����
	private SwipeRefreshLayout mRefreshLayout;

	// ��ǰ�α�
	private ImageView mCursor;
	// ��ǰ����ID
	private String GreenHouseID;

	// ��ǰѡ��鿴��ֵ
	private int mCurrentItem = Constant.CARBONDIOXIDE;

	// ����ͼ
	private LineChartView mChart;
	// ��������
	private LineChartData mLineChartData;

	// �ȴ��Ի���
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
	 * ��ʾ��رյȴ��Ի���
	 * 
	 * @param show
	 */
	private void showDialog(boolean show) {
		if (show) {
			if (mDialog == null) {
				mDialog = new ProgressDialog(getActivity());
				mDialog.setTitle("���ڻ�ȡ����...");
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
	 * ��ʼ��UI�ؼ�
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
	 * @param kind
	 *            Ҫ��ʾ��ʵ������
	 */
	public void changeCharts(int kind) {

		new GetDataTask().execute(kind);
	}

	/**
	 * ��ť����¼�
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
	 * ת����ʾx�����ʱ��
	 * 
	 * @param time
	 *            ʱ���ַ���
	 * @return Ҫ��ʾ��ʱ���ַ���
	 */
	private String convertTime(String time) {
		String result = "";
		time = time.substring(6);
		result += time.substring(0, 2);
		result += "��";
		result += time.substring(2, 4);
		result += ":";
		result += time.substring(4, 6);

		return result;
	}

	/**
	 * ���½�����ʾ
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
		// ������
		Axis axisX = new Axis(); // X��
		axisX.setHasTiltedLabels(true);
		axisX.setTextColor(Color.WHITE);
		axisX.setName("ʱ��");
		axisX.setTextSize(10);
		axisX.setHasLines(false);

		axisX.setMaxLabelChars(3);
		axisX.setValues(axisValues);

		mLineChartData.setAxisXBottom(axisX);

		Axis axisY = new Axis(); // Y��
		axisY.setTextColor(Color.WHITE);
		axisY.setMaxLabelChars(3);
		if (kind == Constant.TEMPERATURE)
			axisY.setName("�¶�");
		else if (kind == Constant.HUMIDITY)
			axisY.setName("ʪ��");
		else if (kind == Constant.CARBONDIOXIDE)
			axisY.setName("������̼Ũ��");
		else if (kind == Constant.ILLUMINANCE)
			axisY.setName("����");
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
				Toast.makeText(getActivity(), "δ��ȡ�����ݸ���", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
