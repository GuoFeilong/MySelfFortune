package com.guofeilong.fortune.ui;

import java.util.List;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.ui.view.CircleImageView;
import com.guofeilong.fortune.ui.view.PatternSettingView;
import com.guofeilong.fortune.ui.view.PatternSettingView.Cell;
import com.guofeilong.fortune.ui.view.PatternSettingView.OnPatternListener;
import com.guofeilong.fortune.utils.PatternPasswordUtils;
import com.guofeilong.fortune.utils.Settings;
import com.guofeilong.fortune.utils.Tools;
import com.guofeilong.fortune.utils.ViewUtils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PatternPasswordActivity extends BaseActivity implements OnClickListener, AnimationListener, OnPatternListener {
	private CircleImageView mPatternUserIcon; // 手势解锁界面头像
	private TextView mPatternTitle;// 手势解锁title
	private PatternSettingView mPatternPasswordView;// 手势解锁的view
	private PatternPasswordUtils mLockPatternUtils; // 手势解锁的工具类
	private TextView mForgetPassword;// 忘记密码
	private int mColor;
	private String mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		String lockPattenString = new PatternPasswordUtils(this).getLockPaternString();
		if (lockPattenString == null) {
			finish();
			return;
		}

		setContentView(R.layout.activity_lockpattern);
		mTitle = getMyTitleAndColor();
		initNavigation();
		initView();
		// mLockPattern = LockPatternView.stringToPattern(lockPattenString);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// disable back key
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mPatternUserIcon = (CircleImageView) findViewById(R.id.civ_usericon);

		byte[] readDragedMenu = (byte[]) Settings.getSettings(this).readDragedMenu(AppConstants.USERPORTRINT);
		BitmapFactory.Options opts = new BitmapFactory.Options();
		if (readDragedMenu != null) {
			Bitmap mPortrait = Tools.getBitmapFromBytes(readDragedMenu, opts);
			mPatternUserIcon.setImageBitmap(mPortrait);
		} else {
			BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.default_portrait);
			Bitmap bitmap = drawable.getBitmap();
			mPatternUserIcon.setImageBitmap(bitmap);
		}

		mPatternTitle = (TextView) findViewById(R.id.tv_lock_pattern_title);
		mPatternPasswordView = (PatternSettingView) findViewById(R.id.lpv_lockPattern);
		mLockPatternUtils = new PatternPasswordUtils(this);
		mForgetPassword = (TextView) findViewById(R.id.tv_forget_password);
		mForgetPassword.setOnClickListener(this);
		mPatternPasswordView.setOnPatternListener(this);
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
		case R.id.tv_forget_password:
			mLockPatternUtils.clearLock();
			ViewUtils.changeActivity(this, MainActivity.class);
			finish();
			break;

		default:
			break;
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

	@Override
	public void onAnimationStart(Animation animation) {

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		mPatternPasswordView.setDisplayMode(PatternSettingView.DisplayMode.Wrong);
		mPatternPasswordView.clearPattern();
	}

	@Override
	public void onAnimationRepeat(Animation animation) {

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
		String patternToString = mLockPatternUtils.patternToString(pattern);
		String lockPaternString = mLockPatternUtils.getLockPaternString();
		if (patternToString.equals(lockPaternString)) {
			ViewUtils.changeActivity(this, MainActivity.class);
			finish();
			mPatternPasswordView.clearPattern();
		} else {
			shakeAnimation(mPatternTitle);
		}
	}

}
