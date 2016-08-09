package com.guofeilong.fortune.ui;

import java.util.Random;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.ui.view.LotterySurfaceView;
import com.guofeilong.fortune.ui.view.LotterySurfaceView.OnMyItemClickListener;
import com.guofeilong.fortune.utils.T;

public class LotteryActivity extends BaseActivity implements OnClickListener {
	private int mColor;
	private String mTitle;

	private ImageView mStart;
	private LotterySurfaceView mLotteryView;
	private LotterySurfaceView.OnMyItemClickListener mClickListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_lottery);
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
		// TODO Auto-generated method stub

	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mStart = (ImageView) findViewById(R.id.iv_start);
		mLotteryView = (LotterySurfaceView) findViewById(R.id.sfv_lottery);
		mClickListener = new OnMyItemClickListener() {

			@Override
			public void showMyToast() {
				T.show(LotteryActivity.this, mLotteryView.getmDesc(), 0);
			}
		};
		
		mStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!mLotteryView.isStart()) {
					mStart.setImageResource(R.drawable.luck_stop);
					int index = new Random().nextInt(5) + 1;
					mLotteryView.luckyStart(index);
					T.show(LotteryActivity.this, "random No=" + index, 0);
				} else {
					if (!mLotteryView.isShouldEnd())

					{
						mStart.setImageResource(R.drawable.luck_start);
						mLotteryView.setmClickListener(mClickListener);
						mLotteryView.luckyEnd();
					}
				}

			}
		});
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
