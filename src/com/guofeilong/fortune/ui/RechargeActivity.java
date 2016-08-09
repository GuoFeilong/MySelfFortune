package com.guofeilong.fortune.ui;

import java.util.ArrayList;
import java.util.List;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.utils.ViewUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RechargeActivity extends FragmentActivity implements OnClickListener {
	/**
	 * 指引线的宽度
	 */
	private static final int GUIDE_LINE_HIGHT = 3;
	private ImageView mIndicatorLine;
	private float mCurrentCheckedRadioLeft;// 当前被选中的RadioButton距离左侧的距离
	private HorizontalScrollView mHorizontalScrollView;// 上面的水平滚动控件
	private ViewPager mViewPager; // 下方的可横向拖动的控件
	private int windowsWidth;
	private int itemWidth;
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	private RadioGroup myRadioGroup;
	private int _id = 1000;
	private LinearLayout layout;
	private String mCurrentServiceType;
	private int mCurrentPosition;
	private List<String> mTestData;
	private int mColor;
	private String mTitle;

	/**
	 * 从服务器获取数据页签的数据
	 */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去标题
		setContentView(R.layout.activity_recharge_new);
		windowsWidth = getWindowsWidth(this);
		itemWidth = windowsWidth / 16;
		mTitle = getMyTitleAndColor();
		initNavigation();
		getMyRechargeData();
		initGroup();
		initFragment();
		iniListener();
		mViewPager.setCurrentItem(0);
	}

	/**
	 * 从服务器获取recharge数据
	 */
	private void getMyRechargeData() {
		mTestData = new ArrayList<String>();
		for (int i = 0; i < 5; i++) {
			mTestData.add("TestData" + i);
		}

	}

	/**
	 * translate animation
	 * 
	 * @param f
	 * @return
	 */
	private AnimationSet provideAnim(float f) {
		AnimationSet animationSet = new AnimationSet(true);
		TranslateAnimation translateAnimation;
		translateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, f, 0f, 0f);
		translateAnimation.setInterpolator(new AccelerateInterpolator());
		animationSet.addAnimation(translateAnimation);
		animationSet.setFillBefore(true);
		animationSet.setFillAfter(true);
		animationSet.setDuration(300);
		return animationSet;
	}

	private void initGroup() {
		layout = (LinearLayout) findViewById(R.id.lay);
		mIndicatorLine = (ImageView) findViewById(R.id.img1);
		mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		myRadioGroup = new RadioGroup(this);
		myRadioGroup.setLayoutParams(new LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		myRadioGroup.setOrientation(LinearLayout.HORIZONTAL);
		layout.addView(myRadioGroup);

		if (mTestData != null) {

			for (int i = 0; i < mTestData.size(); i++) {

				RadioButton radio = new RadioButton(this);

				radio.setButtonDrawable(android.R.color.transparent);
				LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, Gravity.CENTER);
				radio.setLayoutParams(l);
				radio.setGravity(Gravity.CENTER);
				int dpToTop = ViewUtils.dpToPx(getResources(), 20);
				int dpToBottom = ViewUtils.dpToPx(getResources(), 20);
				radio.setPadding(itemWidth, dpToTop, itemWidth, dpToBottom);
				radio.setId(_id + i);
				radio.setText(mTestData.get(i));
				radio.setTextColor(getResources().getColor(R.color.conmmd_title_arrow_color));

				if (i == 0) {
					radio.setChecked(true);
					radio.setTextColor(mColor);
					int itemWidth = (int) radio.getPaint().measureText(mTestData.get(i));
					mIndicatorLine.setLayoutParams(new LinearLayout.LayoutParams(itemWidth + radio.getPaddingLeft() + radio.getPaddingRight(), ViewUtils.dpToPx(getResources(), GUIDE_LINE_HIGHT)));
				}
				myRadioGroup.addView(radio);
			}
		}
		myRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int radioButtonId = group.getCheckedRadioButtonId();
				// 根据ID获取RadioButton的实例
				RadioButton rb = (RadioButton) findViewById(radioButtonId);
				AnimationSet animationSet = provideAnim(rb.getLeft());

				mIndicatorLine.startAnimation(animationSet);// 开始上面蓝色横条图片的动画切换
				mViewPager.setCurrentItem(radioButtonId - _id);// 让下方ViewPager跟随上面的HorizontalScrollView切换
				mCurrentCheckedRadioLeft = rb.getLeft();// 更新当前蓝色横条距离左边的距离
				mHorizontalScrollView.smoothScrollTo((int) mCurrentCheckedRadioLeft - (int) getResources().getDimension(R.dimen.recharge_title_instance), 0);

				mIndicatorLine.setLayoutParams(new LinearLayout.LayoutParams(rb.getRight() - rb.getLeft(), ViewUtils.dpToPx(getResources(), GUIDE_LINE_HIGHT)));

				for (int i = 0; i < group.getChildCount(); i++) {
					RadioButton tempRB = (RadioButton) group.getChildAt(i);
					tempRB.setTextColor(getResources().getColor(R.color.conmmd_title_arrow_color));
				}

				rb.setTextColor(mColor);
				// 拿到当前的serviceID ,发消息去数据
				mCurrentPosition = rb.getId() - _id;
			}

		});

	}

	/**
	 * 初始化叶签
	 */
	private void initFragment() {
		/**
		 * activity初始化的时候拿第一页serviceid取第一页的数据设置到fragment里面 剩下的activity创建出空的
		 * fragment碎片等待填充数据
		 */
		if (mTestData != null) {
			mCurrentPosition = 0;

			for (int i = 0; i < mTestData.size(); i++) {

				// 初始化fragment集合
				RechargeFragment rechargeFragment = new RechargeFragment();
				rechargeFragment.setmTestData(mTestData.get(i),mColor);
				fragments.add(rechargeFragment);

			}

		}
		MyFragmentPagerAdapter mAdapetr = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments);
		mViewPager.setAdapter(mAdapetr);// 设置ViewPager的适配器

	}

	private void iniListener() {
		mViewPager.setOnPageChangeListener(new MyPagerOnPageChangeListener());
	}

	/**
	 * ViewPager的PageChangeListener(页面改变的监听器)
	 */
	private class MyPagerOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			switch (arg0) {
			case 1:
				break;

			default:
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// mIndicatorLine.startAnimation(provideAnim(mCurrentCheckedRadioLeft+arg2));
			switch (arg0) {
			case 1:
				break;

			default:
				break;
			}
		}

		/**
		 * 滑动ViewPager的时候,让上方的HorizontalScrollView自动切换
		 */
		@Override
		public void onPageSelected(int position) {
			mCurrentPosition = position;
			RadioButton radioButton = (RadioButton) findViewById(_id + position);
			radioButton.performClick();

		}
	}

	/** 获取屏幕的宽度 */
	public final static int getWindowsWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
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
		ImageView line = (ImageView) findViewById(R.id.iv_line);
		line.setBackgroundDrawable(null);
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
