package com.guofeilong.fortune.ui;

import java.util.List;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.ui.view.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OneByOneTransferAniaActivity extends BaseActivity implements OnClickListener {
	private int mColor;
	private String mTitle;
	private LinearLayout mViewContainer;
	private int[] mTempData;
	private int mSize;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_onebyone_animation);
		mTitle = getMyTitleAndColor();
		initNavigation();
		initData();
		initView();
		initEvent();
		containerAnimation(3500, 250);
	}

	/**
	 * 初始化事件
	 */
	private void initEvent() {
		addViews(mViewContainer, mSize);
	}

	/**
	 * 添加控件
	 * 
	 * @param mViewContainer2
	 */
	private void addViews(LinearLayout mViewContainer2, int viewCount) {
		for (int i = 0; i < viewCount; i++) {
			CircleImageView childView = getChildView(mTempData[i]);
			mViewContainer2.addView(childView);
		}
	}

	/**
	 * 生成子view
	 * 
	 * @return
	 */
	private CircleImageView getChildView(int resId) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100, 100);
		params.width = 260;
		params.height = 260;
		params.leftMargin = 15;
		params.rightMargin = 15;
		CircleImageView tempView = new CircleImageView(this);
		tempView.setImageResource(resId);
		tempView.setLayoutParams(params);
		return tempView;
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mViewContainer = (LinearLayout) findViewById(R.id.ll_view_container);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mTempData = new int[] { R.drawable.bg_start, R.drawable.ic_launcher, R.drawable.bg_guanxi_icon, R.drawable.bg_guanxi_icon, R.drawable.bg_always_smile };
		mSize = mTempData.length;

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

	/**
	 * 容器的位移动画
	 */
	private void containerAnimation(int totalTime, int unitTime) {
		int childCount = mViewContainer.getChildCount();
		for (int i = 0; i < childCount; i++) {

			final View child = mViewContainer.getChildAt(i);
			child.setVisibility(View.INVISIBLE);
			final TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_SELF, 0);
			translateAnimation.setFillAfter(true);
			int time = i * 200;
			if (time > totalTime) {
				time = totalTime;
			}
			translateAnimation.setDuration(totalTime - time);
			translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
			translateAnimation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					child.setVisibility(View.VISIBLE);
				}
			});
			child.postDelayed(new Runnable() {

				@Override
				public void run() {
					child.startAnimation(translateAnimation);

				}
			}, 0);
		}
	}
}
