package com.guofeilong.fortune.ui;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.ui.view.CustomCircleIcon;
import com.guofeilong.fortune.utils.T;

public class AnimationAndAnimatorActivity extends BaseActivity implements OnClickListener {
	private static final int UNIT_Y = 220;
	private int mColor;
	private String mTitle;
	private CustomCircleIcon mCircleIcon1;
	private CustomCircleIcon mCircleIcon2;
	private TextView mStart1;
	private TextView mStrat2;
	private int width;
	private float toXDelta;
	private TextView mMenu;
	private CustomCircleIcon mMenu1;
	private CustomCircleIcon mMenu2;
	private CustomCircleIcon mMenu3;
	private CustomCircleIcon mMenu4;
	private List<CustomCircleIcon> mMenus;
	private boolean isExpendable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_animation_and_animator);
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
		mCircleIcon1.setOnClickListener(this);
		mCircleIcon2.setOnClickListener(this);
		mStart1.setOnClickListener(this);
		mStrat2.setOnClickListener(this);
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mCircleIcon1 = (CustomCircleIcon) findViewById(R.id.circleicon_1);
		mCircleIcon2 = (CustomCircleIcon) findViewById(R.id.circleicon_2);
		mStart1 = (TextView) findViewById(R.id.tv_start_1);
		mStrat2 = (TextView) findViewById(R.id.tv_start_2);

		mMenu = (TextView) findViewById(R.id.tv_menu);
		mMenu1 = (CustomCircleIcon) findViewById(R.id.menu1);
		mMenu2 = (CustomCircleIcon) findViewById(R.id.menu2);
		mMenu3 = (CustomCircleIcon) findViewById(R.id.menu3);
		mMenu4 = (CustomCircleIcon) findViewById(R.id.menu4);

		mMenus.add(mMenu1);
		mMenus.add(mMenu2);
		mMenus.add(mMenu3);
		mMenus.add(mMenu4);

		mMenu.setOnClickListener(this);
		for (int i = 0; i < mMenus.size(); i++) {
			mMenus.get(i).setOnClickListener(this);
		}

	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		WindowManager windowManager = getWindowManager();
		Display defaultDisplay = windowManager.getDefaultDisplay();
		width = defaultDisplay.getWidth();
		toXDelta = (width / 4) * 2.5f;
		mMenus = new ArrayList<CustomCircleIcon>();
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
		case R.id.circleicon_1:
			T.show(this, "点击Animation___中的View", 0);
			break;
		case R.id.circleicon_2:
			T.show(this, "点击Animator___中的View", 0);
			break;
		case R.id.tv_start_1:
			startAnimator();
			break;
		case R.id.tv_start_2:
			startAniamtion();
			break;
		case R.id.tv_menu:
			if (isExpendable) {
				startCloaeUpAni();
			} else {
				startExpandAni();
			}
			break;
		case R.id.menu1:
			startMenuAni(v);
			break;
		case R.id.menu2:
			startMenuAni(v);
			break;
		case R.id.menu3:
			startMenuAni(v);
			break;

		case R.id.menu4:
			startMenuAni(v);
			break;
		default:
			break;
		}

	}

	/**
	 * 开始菜单动画
	 * 
	 * @param v
	 */
	private void startMenuAni(final View v) {
		PropertyValuesHolder valph = PropertyValuesHolder.ofFloat("alpha", 1f, 0);
		PropertyValuesHolder vscrolX = PropertyValuesHolder.ofFloat("scaleX", 1f, 2f, 1f);
		PropertyValuesHolder vscrolY = PropertyValuesHolder.ofFloat("scaleY", 1f, 2f, 1f);
		ObjectAnimator oa = ObjectAnimator.ofPropertyValuesHolder(v, valph, vscrolX, vscrolY);
		oa.setDuration(1000);
		oa.start();
		oa.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				startCloaeUpAni();
				ObjectAnimator oaV = ObjectAnimator.ofFloat(v, "alpha", 0, 1f);
				oaV.start();
				T.show(AnimationAndAnimatorActivity.this, v.getId() + "被点击..", 0);
			}
		});

	}

	/**
	 * 开始展开动画
	 */
	private void startExpandAni() {
		ObjectAnimator oaMenu = ObjectAnimator.ofFloat(mMenu, "rotation", 0, 90f);
		oaMenu.setDuration(1000);
		oaMenu.start();
		for (int i = 0; i < mMenus.size(); i++) {
			CustomCircleIcon currentMenu = mMenus.get(i);
			ObjectAnimator oa = ObjectAnimator.ofFloat(currentMenu, "translationY", 0, UNIT_Y * (1 + i));
			oa.setDuration(1000);
			oa.setInterpolator(new BounceInterpolator());
			oa.setCurrentPlayTime(500);
			oa.start();
		}
		isExpendable = true;
	}

	/**
	 * 开始收拢动画
	 */
	private void startCloaeUpAni() {
		ObjectAnimator oaMenu = ObjectAnimator.ofFloat(mMenu, "rotation", 90f, 0);
		oaMenu.setDuration(1000);
		oaMenu.start();
		for (int i = 0; i < mMenus.size(); i++) {
			CustomCircleIcon currentMenu = mMenus.get(i);
			ObjectAnimator oa = ObjectAnimator.ofFloat(currentMenu, "translationY", UNIT_Y * (i + 1), 0);
			oa.setDuration(1000);
			oa.setInterpolator(new BounceInterpolator());
			oa.setCurrentPlayTime(500);
			oa.start();
		}
		isExpendable = false;
	}

	/**
	 * 普通动画实现
	 */
	private void startAnimator() {

		TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF, toXDelta, Animation.RELATIVE_TO_SELF, Animation.RELATIVE_TO_SELF);
		ta.setDuration(1000);
		ta.setFillAfter(true);
		ta.setInterpolator(new BounceInterpolator());
		ta.setAnimationListener(new AnimationListener() {

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
				T.show(AnimationAndAnimatorActivity.this, "我是Tween动画我还在原来位置..", 0);
			}
		});
		mCircleIcon1.startAnimation(ta);

	}

	/**
	 * 属性动画实现
	 */
	private void startAniamtion() {
		ObjectAnimator oa = ObjectAnimator.ofFloat(mCircleIcon2, "translationX", 0, toXDelta);
		oa.setDuration(1000);
		oa.setInterpolator(new BounceInterpolator());
		oa.start();
		oa.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				super.onAnimationEnd(animation);
				T.show(AnimationAndAnimatorActivity.this, "我是属性动画,位置真改变了..", 0);
			}
		});
	}
}
