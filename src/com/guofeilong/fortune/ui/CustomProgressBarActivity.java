package com.guofeilong.fortune.ui;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.ui.view.CanDragProgressView;
import com.guofeilong.fortune.ui.view.CustomProgressBar;
import com.guofeilong.fortune.ui.view.CustomRoundProgressBar;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomProgressBarActivity extends BaseActivity implements OnClickListener {
	private static final int POST_DELAY = 11;
	private int mColor;
	private String mTitle;
	private CustomProgressBar mProgressBar, mProgressBarPercent;
	private float mCurrentData, mMaxData;
	private Button mChangeData;
	private CustomRoundProgressBar mRoundProgressBar;
	private CanDragProgressView mDragProgressView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_customer_progress_bar);
		mTitle = getMyTitleAndColor();
		initNavigation();
		initData();
		initView();
		initEvent();
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case POST_DELAY:
				mRoundProgressBar.setVisibility(View.VISIBLE);
				mRoundProgressBar.setDataValue(mCurrentData / mMaxData);
				mRoundProgressBar.setCurrentSweep(0);
				mRoundProgressBar.startThreadAnimation();
				mRoundProgressBar.setSectorWidth(10.0f);
				break;

			default:
				break;
			}
		}

	};

	/**
	 * 初始化事件
	 */
	private void initEvent() {
		mProgressBar.setProBarColor(mColor, getResources().getColor(R.color.conmmd_background));
		mProgressBar.setDataProgress(mMaxData, mCurrentData);
		mProgressBar.setHideOrShowPercent(false);

		mProgressBarPercent.setProBarColor(getResources().getColor(R.color.conmmd_blue), mColor);
		mProgressBarPercent.setDataProgress(mMaxData, mCurrentData);
		mProgressBarPercent.setHideOrShowPercent(true);

		mChangeData.setOnClickListener(this);

		Message obtainMessage = mHandler.obtainMessage();
		obtainMessage.what = POST_DELAY;
		mHandler.sendMessageDelayed(obtainMessage, 1000);

		
		
		mDragProgressView.setmMaxValue(100.0f);
		mDragProgressView.setmSecondValue(39.0f);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mCurrentData = 50.0f;
		mMaxData = 100.0f;
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mRoundProgressBar = (CustomRoundProgressBar) findViewById(R.id.round_progressbar_test);
		mProgressBar = (CustomProgressBar) findViewById(R.id.custom_progress_bar);
		mProgressBarPercent = (CustomProgressBar) findViewById(R.id.custom_progress_bar_percent);
		mChangeData = (Button) findViewById(R.id.btn_change_data);
		mDragProgressView = (CanDragProgressView) findViewById(R.id.item_service_transfer_tpv2);
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
		case R.id.btn_change_data:

			break;
		default:
			break;
		}
	}

}
