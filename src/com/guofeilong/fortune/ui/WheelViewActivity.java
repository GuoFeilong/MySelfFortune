package com.guofeilong.fortune.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.ui.view.WheelView;
import com.guofeilong.fortune.ui.view.wheelview.LoopListener;
import com.guofeilong.fortune.ui.view.wheelview.LoopView;
import com.guofeilong.fortune.utils.T;

import java.util.ArrayList;

public class WheelViewActivity extends BaseActivity implements OnClickListener {
	private int mColor;
	private String mTitle;

	private RelativeLayout mrl_coin_container;
	private RelativeLayout.LayoutParams layoutParams;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_wheelview);
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
		layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);


		LoopView loopView = new LoopView(this);
		ArrayList<String> list = new ArrayList();
		for (int i = 0; i < 15; i++) {
			list.add("item " + i);
		}
		//设置是否循环播放
		loopView.setNotLoop();
		//滚动监听
		loopView.setListener(new LoopListener() {
			@Override
			public void onItemSelect(int item) {
				T.show(WheelViewActivity.this,"--"+item,0);
			}
		});
		//设置原始数据
		loopView.setArrayList(list);
		//设置初始位置
		loopView.setPosition(5);
		//设置字体大小
		loopView.setTextSize(30);
		mrl_coin_container.addView(loopView, layoutParams);

	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		// TODO Auto-generated method stub
		mrl_coin_container = (RelativeLayout) findViewById(R.id.rl_coin_container);

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
