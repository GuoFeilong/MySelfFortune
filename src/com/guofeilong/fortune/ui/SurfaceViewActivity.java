package com.guofeilong.fortune.ui;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.ui.view.CustomPPTVLoading;
import com.guofeilong.fortune.ui.view.FlowWaterSurfaceView;
import com.guofeilong.fortune.utils.T;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SurfaceViewActivity extends BaseActivity implements OnClickListener {
	private int mColor;
	private String mTitle;
	private FlowWaterSurfaceView mWaterBottom;
	private int[] textColors;
	private int index;
	private CustomPPTVLoading mPPTVLoading1;
	private CustomPPTVLoading mPPTVLoading2;
	private CustomPPTVLoading mPPTVLoading3;
	private static final int STOP_LOADING = 10;
	private static final int UNIT_TIME = 10000;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case STOP_LOADING:
				mPPTVLoading1.stopLoading();
				mPPTVLoading1.setVisibility(View.INVISIBLE);
				T.show(SurfaceViewActivity.this, "第1个loading停止", 0);
				break;
			case STOP_LOADING * 2:
				mPPTVLoading2.stopLoading();
				;
				mPPTVLoading2.setVisibility(View.INVISIBLE);
				T.show(SurfaceViewActivity.this, "第2个loading停止", 0);

				break;
			case STOP_LOADING * 3:
				mPPTVLoading3.stopLoading();
				;
				mPPTVLoading3.setVisibility(View.INVISIBLE);
				T.show(SurfaceViewActivity.this, "第3个loading停止", 0);

				break;

			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_surface_view);
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

		mPPTVLoading1.setmFristColor(textColors[2]);
		mPPTVLoading2.setmFristColor(textColors[3]);
		mPPTVLoading3.setmFristColor(textColors[4]);

		mPPTVLoading1.setmSencondColor(textColors[5]);
		mPPTVLoading2.setmSencondColor(textColors[6]);
		mPPTVLoading3.setmSencondColor(textColors[7]);

		mWaterBottom.setOnClickListener(this);
		Message msg = mHandler.obtainMessage();
		msg.what = STOP_LOADING;
		mHandler.sendMessageDelayed(msg, UNIT_TIME);

		Message msg1 = mHandler.obtainMessage();
		msg1.what = STOP_LOADING * 2;
		mHandler.sendMessageDelayed(msg1, UNIT_TIME * 2);

		Message msg2 = mHandler.obtainMessage();
		msg2.what = STOP_LOADING * 3;

		mHandler.sendMessageDelayed(msg2, UNIT_TIME * 3);
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mWaterBottom = (FlowWaterSurfaceView) findViewById(R.id.homepage_bottle);
		mPPTVLoading1 = (CustomPPTVLoading) findViewById(R.id.pptv_1);
		mPPTVLoading2 = (CustomPPTVLoading) findViewById(R.id.pptv_2);
		mPPTVLoading3 = (CustomPPTVLoading) findViewById(R.id.pptv_3);

	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		textColors = getResources().getIntArray(R.array.water_colors);
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
		case R.id.homepage_bottle:
			changeBottomWater();
			break;
		default:
			break;
		}

	}

	private void changeBottomWater() {
		mWaterBottom.setWaterColor(textColors[index % textColors.length]);
		index++;
	}
}
