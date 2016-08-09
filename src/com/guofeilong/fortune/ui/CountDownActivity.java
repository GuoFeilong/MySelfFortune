package com.guofeilong.fortune.ui;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.ui.view.CountDownTextView;
import com.guofeilong.fortune.ui.view.CountDownTextView.IBackwardsOverListener;
import com.guofeilong.fortune.utils.T;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CountDownActivity extends BaseActivity implements OnClickListener {
	private int mColor;
	private String mTitle;
	private CountDownTextView mCuntDown1;
	private CountDownTextView mCuntDown2;
	private CountDownTextView mCuntDown3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_count_down);
		mTitle = getMyTitleAndColor();
		initNavigation();
		initData();
		initView();
		initEvent();
	}

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		mCuntDown1.stoppingBackwardsThread();
		mCuntDown2.stoppingBackwardsThread();
		mCuntDown3.stoppingBackwardsThread();
		super.onFinish();
	}

	/**
	 * 初始化事件
	 */
	private void initEvent() {
		mCuntDown1.setBackwardsOverListener(new IBackwardsOverListener() {

			@Override
			public void onBackwardsStart() {
				T.show(CountDownActivity.this, "1--倒计时开始", 0);
			}

			@Override
			public void onBackwardsOver() {

			}
		});
		mCuntDown1.startBackwardsThread();
		mCuntDown2.setBackwardsOverListener(new IBackwardsOverListener() {

			@Override
			public void onBackwardsStart() {
			}

			@Override
			public void onBackwardsOver() {

			}
		});
		mCuntDown2.startBackwardsThread();
		mCuntDown3.setBackwardsOverListener(new IBackwardsOverListener() {

			@Override
			public void onBackwardsStart() {
			}

			@Override
			public void onBackwardsOver() {
				T.show(CountDownActivity.this, "2--倒计时结束", 0);
			}
		});
		mCuntDown3.startBackwardsThread();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mCuntDown1 = (CountDownTextView) findViewById(R.id.tv_countdowm1);
		mCuntDown2 = (CountDownTextView) findViewById(R.id.tv_countdowm2);
		mCuntDown3 = (CountDownTextView) findViewById(R.id.tv_countdowm3);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		// TODO Auto-generated method stub

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
