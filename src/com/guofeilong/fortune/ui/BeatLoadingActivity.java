package com.guofeilong.fortune.ui;

import com.guofeilong.fortune.R;
import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.ui.view.CustomBeatLoading;
import com.guofeilong.fortune.utils.T;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class BeatLoadingActivity extends BaseActivity implements OnClickListener {
	private static final int STOP_LOADING = 10;
	private static final int UNIT_TIME = 5000;
	private int mColor;
	private String mTitle;
	private CustomBeatLoading mBeatLoading;
	private CustomBeatLoading mBeatLoading1;
	private CustomBeatLoading mBeatLoading2;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case STOP_LOADING:
				mBeatLoading.stopAnimation();
				mBeatLoading.setVisibility(View.INVISIBLE);
				T.show(BeatLoadingActivity.this, "第一个loading停止", 0);
				break;
			case STOP_LOADING * 2:
				T.show(BeatLoadingActivity.this, "第二个loading停止", 0);
				mBeatLoading1.stopAnimation();
				mBeatLoading1.setVisibility(View.INVISIBLE);

				break;
			case STOP_LOADING * 3:
				T.show(BeatLoadingActivity.this, "第三个loading停止", 0);
				mBeatLoading2.stopAnimation();
				mBeatLoading2.setVisibility(View.INVISIBLE);

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
		setContentView(R.layout.activity_beat_loading);
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
		mBeatLoading.startLoading();
		mBeatLoading1.startLoading();
		mBeatLoading2.startLoading();

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
		mBeatLoading = (CustomBeatLoading) findViewById(R.id.beat_loading);
		mBeatLoading1 = (CustomBeatLoading) findViewById(R.id.beat_loading_1);
		mBeatLoading2 = (CustomBeatLoading) findViewById(R.id.beat_loading2);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {

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
		default:
			break;
		}

	}
}
