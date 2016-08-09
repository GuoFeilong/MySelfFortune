package com.guofeilong.fortune.ui;

import java.io.IOException;
import java.util.HashMap;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.ui.ShakeListener.OnShakeListener;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;

public class WechartShakeActivity extends BaseActivity implements OnClickListener {
	private int mColor;
	private String mTitleString;

	
	private static final long ANIMATION_TIME = 500;
	ShakeListener mShakeListener = null;
	Vibrator mVibrator;
	private RelativeLayout mImgUp;
	private RelativeLayout mImgDn;
	private RelativeLayout mTitle;
	private SlidingDrawer mDrawer;
	private Button mDrawerBtn;
	private SoundPool sndPool;
	private HashMap<Integer, Integer> soundPoolMap = new HashMap<Integer, Integer>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_shake);
		mTitleString = getMyTitleAndColor();
		initNavigation();
		initData();
		initView();
		initEvent();
	}

	/**
	 * 初始化事件
	 */
	private void initEvent() {
		mDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			public void onDrawerOpened() {
				mDrawerBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.shake_report_dragger_down));
				TranslateAnimation titleup = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -1.0f);
				titleup.setDuration(200);
				titleup.setFillAfter(true);
				mTitle.startAnimation(titleup);
			}
		});
		/* 设定SlidingDrawer被关闭的事件处理 */
		mDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			public void onDrawerClosed() {
				mDrawerBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.shake_report_dragger_up));
				TranslateAnimation titledn = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0f);
				titledn.setDuration(200);
				titledn.setFillAfter(false);
				mTitle.startAnimation(titledn);
			}
		});
		loadSound();
		
		
		mShakeListener = new ShakeListener(this);
		mShakeListener.setOnShakeListener(new OnShakeListener() {
			public void onShake() {
				// Toast.makeText(getApplicationContext(),
				// "抱歉，暂时没有找到在同一时刻摇一摇的人。\n再试一次吧！", Toast.LENGTH_SHORT).show();
				startAnim(); // 开始 摇一摇手掌动画
				mShakeListener.stop();
				sndPool.play(soundPoolMap.get(0), (float) 1, (float) 1, 0, 0, (float) 1.2);
				new Handler().postDelayed(new Runnable() {
					public void run() {
						// Toast.makeText(getApplicationContext(),
						// "抱歉，暂时没有找到\n在同一时刻摇一摇的人。\n再试一次吧！",
						// ANIMATION_TIME).setGravity(Gravity.CENTER,0,0).show();
						sndPool.play(soundPoolMap.get(1), (float) 1, (float) 1, 0, 0, (float) 1.0);
						Toast mtoast;
						mtoast = Toast.makeText(getApplicationContext(), "抱歉，暂时没有找到\n在同一时刻摇一摇的人。\n再试一次吧！", 10);
						// mtoast.setGravity(Gravity.CENTER, 0, 0);
						mtoast.show();
						mVibrator.cancel();
						mShakeListener.start();

					}
				}, 2000);
			}
		});
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mVibrator = (Vibrator) getApplication().getSystemService(VIBRATOR_SERVICE);

		mImgUp = (RelativeLayout) findViewById(R.id.shakeImgUp);
		mImgDn = (RelativeLayout) findViewById(R.id.shakeImgDown);
		mTitle = (RelativeLayout) findViewById(R.id.shake_title_bar);
		mDrawer = (SlidingDrawer) findViewById(R.id.slidingDrawer1);
		mDrawerBtn = (Button) findViewById(R.id.handle);
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
		nameTextView.setText(mTitleString);
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
	
	private void loadSound() {

		sndPool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 5);
		new Thread() {
			public void run() {
				try {
					soundPoolMap.put(0, sndPool.load(getAssets().openFd("sound/shake_sound_male.mp3"), 1));

					soundPoolMap.put(1, sndPool.load(getAssets().openFd("sound/shake_match.mp3"), 1));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void startAnim() { // 定义摇一摇动画动画
		AnimationSet animup = new AnimationSet(true);
		TranslateAnimation mytranslateanimup0 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -0.5f);
		mytranslateanimup0.setDuration(ANIMATION_TIME);
		TranslateAnimation mytranslateanimup1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, +0.5f);
		mytranslateanimup1.setDuration(ANIMATION_TIME);
		mytranslateanimup1.setStartOffset(ANIMATION_TIME);
		animup.addAnimation(mytranslateanimup0);
		animup.addAnimation(mytranslateanimup1);
		mImgUp.startAnimation(animup);

		AnimationSet animdn = new AnimationSet(true);
		TranslateAnimation mytranslateanimdn0 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, +0.5f);
		mytranslateanimdn0.setDuration(ANIMATION_TIME);
		TranslateAnimation mytranslateanimdn1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -0.5f);
		mytranslateanimdn1.setDuration(ANIMATION_TIME);
		mytranslateanimdn1.setStartOffset(ANIMATION_TIME);
		animdn.addAnimation(mytranslateanimdn0);
		animdn.addAnimation(mytranslateanimdn1);
		mImgDn.startAnimation(animdn);
	}

	public void startVibrato() { // 定义震动
		mVibrator.vibrate(new long[] { ANIMATION_TIME, 200, ANIMATION_TIME, 200 }, -1); // 第一个｛｝里面是节奏数组，
																	// 第二个参数是重复次数，-1为不重复，非-1俄日从pattern的指定下标开始重复
	}

	public void shake_activity_back(View v) { // 标题栏 返回按钮
		this.finish();
	}

	public void linshi(View v) { // 标题栏
		startAnim();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mShakeListener != null) {
			mShakeListener.stop();
		}
	}
}
