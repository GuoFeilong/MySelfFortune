package com.guofeilong.fortune.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.UserConfig;
import com.guofeilong.fortune.ui.view.CircleImageView;
import com.guofeilong.fortune.ui.view.DragImageView;
import com.guofeilong.fortune.ui.view.DragImageView.IDragImageViewOnclickListener;
import com.guofeilong.fortune.ui.view.LoadingDialog;
import com.guofeilong.fortune.utils.Settings;
import com.guofeilong.fortune.utils.T;
import com.guofeilong.fortune.utils.Tools;
import com.guofeilong.fortune.utils.VersionUtils;
import com.guofeilong.fortune.utils.ViewUtils;

import java.util.HashMap;

public class StartActivity extends BaseActivity implements OnClickListener {

	private DragImageView mStartPortrait;
	private RelativeLayout mStartPage;
	private CircleImageView mStartLogo;
	private ImageView translateUnder;
	private TextView mStartNotice;
	private TextView mWelcomeNotice;
	private boolean isTouch = true;
	private boolean isPageAnimEnd = false;
	private static final int HAND_SHAKE_OK = 0;
	private static final int NET_ERROR = 1;
	private boolean isUpdate = false;// 是否更新
	private static final int UPDATA_VERSION = 5;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATA_VERSION:
				HashMap<String, String> map = (HashMap<String, String>) msg.obj;
				VersionUtils.updateWhichVersion(map.get("md5"), Integer.parseInt(map.get("type")), map.get("url"), StartActivity.this);
				break;
			case HAND_SHAKE_OK:
				if (isPageAnimEnd) {
					circleAnim();
				}
				break;
			case NET_ERROR:
				T.showShort(StartActivity.this, getString(R.string.mapp_network_error));
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}

	};
	private LoadingDialog mloadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		StatusBarCompat.compat(this, getResources().getColor(R.color.white));
		// 程序启动的时候调用初始化推送服务
//		JPushInterface.setDebugMode(true);
//		JPushInterface.init(this);
		if (isUpdate) {
			Message uploadeMessage = mHandler.obtainMessage();
			uploadeMessage.what = UPDATA_VERSION;
			VersionUtils.checkVersion("" + VersionUtils.getVersionCode(this), "android ", uploadeMessage);
		}

		initViewsAndAnim();
	}

	private void initViewsAndAnim() {
		TranslateAnimation inAnimation = (TranslateAnimation) AnimationUtils.loadAnimation(getApplicationContext(), R.anim.in_from_top);
		initViews();
		mStartPage.startAnimation(inAnimation);
		inAnimation.setFillAfter(true);
		inAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				isPageAnimEnd = true;
				circleAnim();
			}
		});
		mStartPortrait.setIOnclickListener(new IDragImageViewOnclickListener() {

			@Override
			public void onDragImageViewOnclickListener(View v) {
				mStartPortrait.animToTarget();
			}

			@Override
			public void onDragImageViewMoveToTarget(View v) {
				startNext();
			}
		});
		mStartPortrait.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				isTouch = false;
				return false;
			}
		});
	}

	private void circleAnim() {
		mStartNotice.setVisibility(View.VISIBLE);
		mWelcomeNotice.setVisibility(View.VISIBLE);
		translateUnder.setVisibility(View.VISIBLE);
		mStartLogo.setVisibility(View.VISIBLE);
		mStartPortrait.setVisibility(View.VISIBLE);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(2000);
		alphaAnimation.setAnimationListener(new AnimationListener() {

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
				if (isTouch) {
					TranslateAnimation translateAnimation = new TranslateAnimation(0, 20, 0, 0);
					translateAnimation.setDuration(1000);
					translateAnimation.setInterpolator(new CycleInterpolator(2));
					mStartPortrait.startAnimation(translateAnimation);
				}
			}
		});
		translateUnder.setAnimation(alphaAnimation);
		mStartLogo.setAnimation(alphaAnimation);
		mStartNotice.startAnimation(alphaAnimation);
		mWelcomeNotice.startAnimation(alphaAnimation);
		mStartPortrait.setAnimation(alphaAnimation);
		alphaAnimation.setFillAfter(true);
	}

	private void initViews() {

		mloadingDialog = new LoadingDialog(this);
		mStartPortrait = (DragImageView) findViewById(R.id.iv_start_portrait);
		mStartLogo = (CircleImageView) findViewById(R.id.iv_start_logo);

		byte[] readDragedMenu = (byte[]) Settings.getSettings(this).readDragedMenu(AppConstants.USERPORTRINT);
		BitmapFactory.Options opts = new BitmapFactory.Options();
		if (readDragedMenu != null) {
			Bitmap mPortrait = Tools.getBitmapFromBytes(readDragedMenu, opts);
			mStartPortrait.setImageBitmap(mPortrait);
		} else {
			BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.default_portrait_for_login);
			Bitmap bitmap = drawable.getBitmap();
			mStartPortrait.setImageBitmap(bitmap);
		}
		mStartPage = (RelativeLayout) findViewById(R.id.rl_start);
		translateUnder = (ImageView) findViewById(R.id.iv_translate_under);
		mStartNotice = (TextView) findViewById(R.id.tv_notice_start);
		mWelcomeNotice = (TextView) findViewById(R.id.tv_welcome_start);
		mStartPortrait.setTargetView(mStartLogo);

	};

	private void startNext() {
		// 读取手势密码设置成功的标记和是否打开的标记
		boolean lockPatternFlag = UserConfig.getInstance(this).readLockPatternFlag();
		boolean setLockPatternFlag = UserConfig.getInstance(this).readSetLockPatternFlag();
		if (lockPatternFlag && setLockPatternFlag) {
			// 进入手势密码解锁界面
			ViewUtils.changeActivity(this, PatternPasswordActivity.class);

		} else {
			// 进入主页
			ViewUtils.changeActivity(this, MainActivity.class);
		}
		finish();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_start_portrait:
			startNext();
			break;
		default:
			break;
		}
	}
}
