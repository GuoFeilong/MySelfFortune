package com.guofeilong.fortune.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.ui.view.MouthFlowRectView;
import com.guofeilong.fortune.ui.view.MyLineChartView;
import com.guofeilong.fortune.ui.view.MyLineChartView.Mstyle;
import com.guofeilong.fortune.ui.view.PieChatView;
import com.guofeilong.fortune.ui.view.PieChatView.OnChatChangeListener;
import com.guofeilong.fortune.ui.view.TrafficDrawView;
import com.guofeilong.fortune.ui.view.TrafficDrawView.TrafficData;
import com.guofeilong.fortune.utils.T;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChartActivity extends BaseActivity implements OnClickListener {
	/**
	 * 折线图数据
	 */
	private int mColor;
	private String mTitle;
	private MyLineChartView myLineChartView;
	private HashMap<Double, Double> item2LineChart;
	private TextView mChangeStyle;
	/**
	 * 柱状图数据
	 */
	private TrafficDrawView mTrafficDrawView;
	private LinearLayout mDrawViewTittleContiner;
	private HorizontalScrollView mScrollView;
	private Map<String, Map<String, TrafficData>> usageMap;
	private List<TrafficData> infoList;
	private ArrayList<String> dateList;
	private int[] colors;

	/**
	 * 扇形图数据
	 */
	private PieChatView mPieChartview;
	private ArrayList<String> arrayList;
	private long[] values;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chart);
		mTitle = getMyTitleAndColor();
		initNavigation();
		initData();
		initView();
		initEvent();
	}

	/**
	 * 初始化事件
	 */
	private void initEvent() {
		mChangeStyle.setOnClickListener(this);
		double yAVG = 20.0;
		double yMaxValue = 100.0;

		myLineChartView.SetTuView(item2LineChart, yMaxValue, yAVG, "日", "M", false);
		myLineChartView.setTotalvalue(yMaxValue);
		myLineChartView.setCurrentMouth(4);
		myLineChartView.setMargint(20);
		myLineChartView.setmColor(mColor);
		myLineChartView.setMarginb(getResources().getDimensionPixelSize(R.dimen.traffic_day_chart_view__x2bottom_hight));// x轴距离底边的距离
		myLineChartView.setMstyle(Mstyle.Curve);

		fillData();

		mPieChartview.setData(values, arrayList);
		mPieChartview.setOnChatChangeListener(new OnChatChangeListener() {

			@Override
			public void onChatChange(Object object) {
				T.show(getApplicationContext(), (String) object, 0);
			}
		});
	}

	/**
	 * 填充柱状图数据
	 */
	private void fillData() {

		for (int i = 0; i < infoList.size(); i++) {
			MouthFlowRectView mouthFlowRectView = new MouthFlowRectView(this);
			TrafficData trafficData = infoList.get(i);
			String name = trafficData.getName();
			String unitString = "";
			mouthFlowRectView.setRextColorAndName(colors[i % colors.length], name + " " + unitString);
			mDrawViewTittleContiner.addView(mouthFlowRectView);
		}

		// 设置柱状图数据
		mTrafficDrawView.setTrafficData(usageMap, infoList, dateList);
		mTrafficDrawView.postDelayed(new Runnable() {

			@Override
			public void run() {
				// 移动柱状图到最右边
				// mScrollView.fullScroll(View.FOCUS_RIGHT);

			}
		}, 0);

	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		myLineChartView = (MyLineChartView) findViewById(R.id.mc_dayuse_detail);
		mChangeStyle = (TextView) findViewById(R.id.tv_change_style);

		mTrafficDrawView = (TrafficDrawView) findViewById(R.id.trafficdrawview);
		mDrawViewTittleContiner = (LinearLayout) findViewById(R.id.ll_diagram_title);
		mScrollView = (HorizontalScrollView) findViewById(R.id.scroll_trafficview);

		mPieChartview = (PieChatView) findViewById(R.id.chatview);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		item2LineChart = new HashMap<Double, Double>();
		item2LineChart.put(1.0, 88.0);
		item2LineChart.put(2.0, 5.0);
		item2LineChart.put(3.0, 66.0);
		item2LineChart.put(4.0, 40.0);
		item2LineChart.put(5.0, 98.0);
		item2LineChart.put(6.0, 28.0);
		item2LineChart.put(7.0, 10.0);
		item2LineChart.put(8.0, 22.0);
		item2LineChart.put(9.0, 54.0);
		item2LineChart.put(10.0, 76.0);

		colors = getResources().getIntArray(R.array.water_colors);
		infoList = new ArrayList<TrafficDrawView.TrafficData>();
		dateList = new ArrayList<String>();
		usageMap = new HashMap<String, Map<String, TrafficData>>();

		TrafficData temp = new TrafficData();
		temp.setName("Min");
		temp.setGroupId(0 + "");
		temp.setData(150);

		infoList.add(temp);
		temp = new TrafficData();
		temp.setName("MB");
		temp.setGroupId(1 + "");
		temp.setData(50);

		infoList.add(temp);
		temp = new TrafficData();
		temp.setName("Msg");
		temp.setGroupId(2 + "");
		temp.setData(100);

		infoList.add(temp);

		dateList.add("21/04");
		dateList.add("22/04");
		dateList.add("23/04");
		dateList.add("24/04");
		dateList.add("25/04");
		dateList.add("26/04");
		dateList.add("27/04");
		dateList.add("28/04");
		dateList.add("29/04");
		dateList.add("30/04");

		for (int i = 0; i < dateList.size(); i++) {
			Map<String, TrafficData> value = new HashMap<String, TrafficDrawView.TrafficData>();
			TrafficData tempData = new TrafficData();
			tempData.setCurrentHeight(10.0f);
			tempData.setData(50);
			value.put("0", tempData);
			tempData = new TrafficData();
			tempData.setData(50);
			value.put("1", tempData);
			tempData = new TrafficData();
			tempData.setData(50);
			value.put("2", tempData);
			usageMap.put(dateList.get(i), value);
		}

		values = new long[] { 33, 66, 20 };
		arrayList = new ArrayList<String>();
		arrayList.add("1");
		arrayList.add("2");
		arrayList.add("3");

	}

	/**
	 * 获取标题
	 * 
	 * @return
	 */
	private String getMyTitleAndColor() {
		String title = "";

		Intent intent = getIntent();
		if (intent != null) {
			Bundle extras = intent.getExtras();
			if (extras != null) {
				title = extras.getString(AppConstants.INTENT_AMOUNT);
				mColor = extras.getInt(AppConstants.INTENT_NAME_FROM);
			}
		}
		return title;
	}

	/**
	 * 初始化通用标题栏
	 */
	private void initNavigation() {
		TextView nameTextView = (TextView) findViewById(R.id.commond_textview_title_name);
		nameTextView.setText(mTitle);
		nameTextView.setTextColor(mColor);
		RelativeLayout leftButton = (RelativeLayout) findViewById(R.id.commond_imagebutton_title_leftbutton);
		leftButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.commond_imagebutton_title_leftbutton:
			finish();
			break;
		case R.id.tv_change_style:
			if (myLineChartView.getMstyle() == Mstyle.Line) {
				myLineChartView.setMstyle(Mstyle.Curve);
				item2LineChart.clear();
				item2LineChart.put(1.0, 88.0);
				item2LineChart.put(2.0, 5.0);
				item2LineChart.put(3.0, 66.0);
				item2LineChart.put(4.0, 40.0);
				item2LineChart.put(5.0, 98.0);
				item2LineChart.put(6.0, 28.0);
				item2LineChart.put(7.0, 10.0);
				item2LineChart.put(8.0, 22.0);
				item2LineChart.put(9.0, 54.0);
				item2LineChart.put(10.0, 76.0);
				double yAVG = 20.0;
				double yMaxValue = 100.0;
				myLineChartView.setmColor(mColor);
				myLineChartView.SetTuView(item2LineChart, yMaxValue, yAVG, "日", "M", false);
				myLineChartView.postInvalidate();
			} else {
				myLineChartView.setMstyle(Mstyle.Line);
				item2LineChart.clear();
				item2LineChart.put(1.0, 11.0);
				item2LineChart.put(2.0, 3.0);
				item2LineChart.put(3.0, 66.0);
				item2LineChart.put(4.0, 60.0);
				item2LineChart.put(5.0, 21.0);
				item2LineChart.put(6.0, 55.0);
				item2LineChart.put(7.0, 98.0);
				item2LineChart.put(8.0, 70.0);
				item2LineChart.put(9.0, 34.0);
				item2LineChart.put(10.0, 77.0);
				double yAVG = 20.0;
				double yMaxValue = 100.0;
				myLineChartView.setmColor(getResources().getColor(R.color.conmmd_blue));
				myLineChartView.SetTuView(item2LineChart, yMaxValue, yAVG, "日", "M", false);
				myLineChartView.postInvalidate();
			}
			break;
		default:
			break;
		}

	}
}
