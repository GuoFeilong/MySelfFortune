package com.guofeilong.fortune.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.ui.view.CustomRunningProgressDialog;
import com.guofeilong.fortune.ui.view.LoadingDialog;
import com.guofeilong.fortune.ui.view.WaterWaveLoadingView;

public class CustomerLoadingActivity extends Activity implements OnClickListener {
	private int mColor;
	private String mTitle;
	private TextView mShowMeiTuan;
	private TextView mShowShunFeng;
	private TextView mShowCommon;
	private List<TextView> mTVContainer;
	private CustomRunningProgressDialog meituanDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_customer_loading);
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
		mShowMeiTuan.setOnClickListener(this);
		mShowShunFeng.setOnClickListener(this);
		mShowCommon.setOnClickListener(this);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mTVContainer = new ArrayList<TextView>();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mShowMeiTuan = (TextView) findViewById(R.id.tv_show_meituan);
		mShowShunFeng = (TextView) findViewById(R.id.tv_show_shunfeng);
		mShowCommon = (TextView) findViewById(R.id.tv_show_commen);
		mTVContainer.add(mShowMeiTuan);
		mTVContainer.add(mShowShunFeng);
		mTVContainer.add(mShowCommon);
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
	public boolean onTouchEvent(MotionEvent event) {
		if (meituanDialog != null && meituanDialog.isShowing()) {
			setButtonState(mTVContainer, View.INVISIBLE);
		} else if (mShowMeiTuan.getVisibility() != View.VISIBLE) {

			setButtonState(mTVContainer, View.VISIBLE);
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.commond_imagebutton_title_leftbutton:
			finish();
			break;
		case R.id.tv_show_meituan:
			meituanDialog = new CustomRunningProgressDialog(this, "loading...", R.anim.frame);
			meituanDialog.show();
			if (meituanDialog.isShowing()) {
				setButtonState(mTVContainer, View.INVISIBLE);
			} else {
				setButtonState(mTVContainer, View.VISIBLE);
			}
			break;
		case R.id.tv_show_shunfeng:
			CustomRunningProgressDialog shunFengDialog = new CustomRunningProgressDialog(this, "loading...", R.anim.frame2);
			shunFengDialog.show();
			if (shunFengDialog.isShowing()) {
				setButtonState(mTVContainer, View.INVISIBLE);
			} else {
				setButtonState(mTVContainer, View.VISIBLE);
			}
			break;
		case R.id.tv_show_commen:
			LoadingDialog dialog = new LoadingDialog(this, true, "loading...");
			dialog.show();
			if (dialog.isShowing()) {
				setButtonState(mTVContainer, View.INVISIBLE);
			} else {
				setButtonState(mTVContainer, View.VISIBLE);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 设置按钮状态
	 * 
	 * @param mTVContainer2
	 * @param invisible
	 */
	private void setButtonState(List<TextView> mTVContainer2, int invisible) {
		for (int i = 0; i < mTVContainer2.size(); i++) {
			TextView textView = mTVContainer2.get(i);
			textView.setVisibility(invisible);
		}
	}

}
