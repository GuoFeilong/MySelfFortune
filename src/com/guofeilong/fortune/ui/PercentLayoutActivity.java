package com.guofeilong.fortune.ui;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;

public class PercentLayoutActivity extends BaseActivity implements OnClickListener {
	private int mColor;
	private String mTitle;
	private RelativeLayout mPercentLayout;
	private TextView mRotation;

	private TextView mContent;
	private TextView mShowFullContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_percentlayout);
		mTitle = getMyTitleAndColor();
		initNavigation();
		initData();
		initView();
		initEvent();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);

		int lineCount = mContent.getLineCount();

		if (lineCount >= 2) {
			mShowFullContent.setVisibility(View.VISIBLE);
		} else {
			mShowFullContent.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 初始化事件
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void initEvent() {
		PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f, 0.5f, 1f);
		PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.5f, 1f);
		PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f, 0.5f, 1f);
		// PropertyValuesHolder pvhXX =
		// PropertyValuesHolder.ofFloat("rotationX", 0f, 360f);
		// PropertyValuesHolder pvhYY= PropertyValuesHolder.ofFloat("rotationY",
		// 0f, 360f);

		ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mPercentLayout, pvhX, pvhY, pvhZ).setDuration(1000);
		animator.setRepeatCount(-1);
		animator.start();

		LayoutAnimationController controller = new LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.custom_item_anim));
		// controller.setOrder(LayoutAnimationController.ORDER_RANDOM);
		controller.setDelay(0.8f);
		mPercentLayout.setLayoutAnimation(controller);

		Animation loadAnimation = AnimationUtils.loadAnimation(this, R.anim.custom_item_anim2);
		loadAnimation.setRepeatCount(1000);
		loadAnimation.setDuration(10000);
		mRotation.startAnimation(loadAnimation);

	}

	/**
	 * 初始化控件
	 */
	@SuppressLint("NewApi")
	private void initView() {
		mPercentLayout = (RelativeLayout) findViewById(R.id.rl_container);
		mRotation = (TextView) findViewById(R.id.tv_7);

		mContent = (TextView) findViewById(R.id.tv_mytextview);
		mContent.setText("测试微信全文点击效果,测试微信全文点击效果测试微信全文点击效果,测试微信全文点击效果测试微信全文点击效果,测试微信全文点击效果测试微信全文点击效果,测试微信全文点击效果测试微信全文点击效果,测试微信全文点击效果测试微信全文点击效果,测试微信全文点击效果");
		// mContent.setText("文字少点...");

		mShowFullContent = (TextView) findViewById(R.id.tv_full_content);
		mShowFullContent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int maxLines = mContent.getMaxLines();
				if (maxLines == 3) {

					mContent.setMaxLines(1000);
				} else if (maxLines == 1000) {
					mContent.setMaxLines(3);
					mContent.setEllipsize(TruncateAt.END);
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
