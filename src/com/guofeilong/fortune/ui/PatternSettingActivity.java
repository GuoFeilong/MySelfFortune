package com.guofeilong.fortune.ui;

import java.util.ArrayList;
import java.util.List;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.UserConfig;
import com.guofeilong.fortune.ui.view.PatternSettingView;
import com.guofeilong.fortune.ui.view.PatternSettingView.Cell;
import com.guofeilong.fortune.ui.view.PatternSettingView.OnPatternListener;
import com.guofeilong.fortune.utils.PatternPasswordUtils;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PatternSettingActivity extends BaseActivity implements OnPatternListener, AnimationListener, OnClickListener {
	public static final int LOCK_PATTERN_COUNT = 9;
	private TextView mTv_lock_pattern_title;
	private PatternSettingView mLpv_lockPattern;
	private PatternPasswordUtils mLockPatternUtils;
	private List<Cell> choosePattern;
	private List<Cell> choosePatternBackup;
	private boolean isFirst;
	private ImageView mPatternPreviewZero;
	private ImageView mPatternPreviewOne;
	private ImageView mPatternPreviewTwo;
	private ImageView mPatternPreviewThree;
	private ImageView mPatternPreviewFour;
	private ImageView mPatternPreviewFive;
	private ImageView mPatternPreviewSix;
	private ImageView mPatternPreviewSeven;
	private ImageView mPatternPreviewEight;
	private ArrayList<ImageView> mPatternViewContainer;

	private int mColor;
	private String mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_lockpattern_setup);
		isFirst = true;
		mTitle = getMyTitleAndColor();
		initNavigation();
		initNavigation();
		initView();
		initContainer();
		setPreview();
	}

	/**
	 * 初始装控件的容器
	 */
	private void initContainer() {
		mPatternViewContainer = new ArrayList<ImageView>();
		mPatternViewContainer.add(mPatternPreviewZero);
		mPatternViewContainer.add(mPatternPreviewOne);
		mPatternViewContainer.add(mPatternPreviewTwo);
		mPatternViewContainer.add(mPatternPreviewThree);
		mPatternViewContainer.add(mPatternPreviewFour);
		mPatternViewContainer.add(mPatternPreviewFive);
		mPatternViewContainer.add(mPatternPreviewSix);
		mPatternViewContainer.add(mPatternPreviewSeven);
		mPatternViewContainer.add(mPatternPreviewEight);
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
		nameTextView.setText("Set pattern lock");
		nameTextView.setTextColor(mColor);
		RelativeLayout leftButton = (RelativeLayout) findViewById(R.id.commond_imagebutton_title_leftbutton);
		leftButton.setOnClickListener(this);
	}

	/**
	 * 设置手势密码
	 * 
	 * @param pattern
	 */
	private void setPatternPassword(List<Cell> pattern) {
		if (choosePattern == null) {
			choosePattern = new ArrayList<PatternSettingView.Cell>();
		}
		if (choosePatternBackup == null) {
			choosePatternBackup = new ArrayList<PatternSettingView.Cell>();
		}
		for (int i = 0; i < pattern.size(); i++) {
			Cell cell = pattern.get(i);
			if (cell != null) {
				choosePattern.add(cell);
				choosePatternBackup.add(cell);
			}
		}

		if (choosePattern.size() >= 3) {
			mLockPatternUtils = new PatternPasswordUtils(this);
			mLockPatternUtils.saveLockPattern(choosePattern);
			mLpv_lockPattern.clearPattern();
		} else {
			mLpv_lockPattern.clearPattern();
		}
	}

	/**
	 * 设置九宫格预览界面
	 * 
	 */
	private void setPreview() {
		if (choosePatternBackup == null || choosePatternBackup.size() == 0) {

		} else {
			for (int i = 0; i < choosePatternBackup.size(); i++) {
				Cell cell = choosePatternBackup.get(i);
				int mPosition = cell.row * 3 + cell.column;
				ImageView tempKeyView = mPatternViewContainer.get(mPosition);
				tempKeyView.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_gridview_pattern_key_h));
			}
		}
	}

	/**
	 * 初始化views
	 */
	private void initView() {
		mPatternPreviewZero = (ImageView) findViewById(R.id.iv_pattern_preview_zero);
		mPatternPreviewOne = (ImageView) findViewById(R.id.iv_pattern_preview_one);
		mPatternPreviewTwo = (ImageView) findViewById(R.id.iv_pattern_preview_two);
		mPatternPreviewThree = (ImageView) findViewById(R.id.iv_pattern_preview_three);
		mPatternPreviewFour = (ImageView) findViewById(R.id.iv_pattern_preview_four);
		mPatternPreviewFive = (ImageView) findViewById(R.id.iv_pattern_preview_five);
		mPatternPreviewSix = (ImageView) findViewById(R.id.iv_pattern_preview_six);
		mPatternPreviewSeven = (ImageView) findViewById(R.id.iv_pattern_preview_seven);
		mPatternPreviewEight = (ImageView) findViewById(R.id.iv_pattern_preview_eight);

		mTv_lock_pattern_title = (TextView) findViewById(R.id.tv_lock_pattern_title);
		mLpv_lockPattern = (PatternSettingView) findViewById(R.id.lpv_lockPattern);
		mLpv_lockPattern.setOnPatternListener(this);
	}

	@Override
	public void onPatternStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPatternCleared() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPatternCellAdded(List<Cell> pattern) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPatternDetected(List<Cell> pattern) {
		if (pattern.size() >= 3) {
			if (isFirst) {
				setPatternPassword(pattern);
				setPreview();
				changeTheTitleToConfirm("Draw pattern agin to confirm");
				isFirst = false;
			} else {
				int result = mLockPatternUtils.checkPattern(pattern);
				if (result != 1) {
					if (result == 0) {
						shakeAnimation(mTv_lock_pattern_title);
					}
				} else if (result == 1) {
					// 设置成功存储标记
					UserConfig.getInstance(this).saveSetLockPatternFlag(true);
					changeTheTitleToConfirm("Success");
					finish();
				}
			}
		} else {
			UserConfig.getInstance(this).saveSetLockPatternFlag(true);
			mLpv_lockPattern.clearPattern();
		}
	}

	/**
	 * textview抖动动画
	 * 
	 * @param mTv_lock_pattern_title2
	 */
	private void shakeAnimation(TextView iv) {

		TranslateAnimation anim = new TranslateAnimation(0, -15, 0, 0);
		anim.setInterpolator(new CycleInterpolator(3f));
		anim.setDuration(300);
		anim.setFillAfter(true);
		anim.setAnimationListener(this);
		iv.startAnimation(anim);
	}

	/**
	 * 
	 * @param string
	 */
	private void changeTheTitleToConfirm(String string) {
		mTv_lock_pattern_title.setText(string);
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		Canvas canvas = new Canvas();
		mLpv_lockPattern.setDisplayMode(PatternSettingView.DisplayMode.Wrong);
		mLpv_lockPattern.clearPattern();
		UserConfig.getInstance(this).saveSetLockPatternFlag(false);
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

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
