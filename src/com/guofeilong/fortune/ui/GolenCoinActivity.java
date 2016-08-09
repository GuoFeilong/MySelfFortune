package com.guofeilong.fortune.ui;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.utils.MusicUtils;

public class GolenCoinActivity extends Activity implements OnClickListener {
	private static final int COINS_COUNT = 12;
	private RelativeLayout mGodenContainer;
	private DisplayMetrics mDm;
	private ArrayList<ImageView> mGodenViews;
	private Button mChange;
	private Button mStartAnim;
	private int mColor;
	private String mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_golden_coin);
		mTitle = getMyTitleAndColor();
		initNavigation();
		mDm = getWindowsWidth(this);
		mGodenContainer = (RelativeLayout) findViewById(R.id.rl_coin_container);
		mChange = (Button) findViewById(R.id.btn_change);
		mStartAnim = (Button) findViewById(R.id.btn_start_animation);
		mChange.setOnClickListener(this);
		mStartAnim.setOnClickListener(this);
		mGodenViews = new ArrayList<ImageView>();
		addCoins();
		dropCoins(mGodenViews);
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

	/**
	 * 添加金币
	 */
	private void addCoins() {
		for (int i = 0; i < COINS_COUNT; i++) {
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(150, 150);
			int topMar = new Random().nextInt(mDm.heightPixels);
			int leftMar = new Random().nextInt(mDm.widthPixels);
			params.topMargin = topMar;
			params.leftMargin = leftMar;
			ImageView coin = new ImageView(this);
			coin.setBackgroundDrawable(getResources().getDrawable(R.drawable.gold_coin));
			coin.setLayoutParams(params);
			mGodenContainer.addView(coin);
			mGodenViews.add(coin);
		}
	}

	/**
	 * 移除金币
	 */
	private void removeCoins() {
		for (int i = 0; i < mGodenViews.size(); i++) {
			mGodenContainer.removeView(mGodenViews.get(i));
			
		}
		mGodenViews.clear();
	}

	/** 获取屏幕的宽度 */
	public DisplayMetrics getWindowsWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_change:
			removeCoins();
			addCoins();
			break;
		case R.id.btn_start_animation:
			dropCoins(mGodenViews);
			break;
		case R.id.commond_imagebutton_title_leftbutton:
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 掉落金币的动画
	 * 
	 * @param mGodenViews
	 */
	private void dropCoins(ArrayList<ImageView> mGodenViews) {
		Animation anima = AnimationUtils.loadAnimation(this, R.anim.coin_top_in);
		anima.setDuration(3000);
		anima.setFillAfter(true);
		final MusicUtils musicUtils = new MusicUtils(this, R.raw.diaoluo_da);
		final int playMusic = musicUtils.playMusic(true);
		for (int i = 0; i < mGodenViews.size(); i++) {
			ImageView coin = mGodenViews.get(i);
			coin.startAnimation(anima);
			if (i == mGodenViews.size() - 1) {
				anima.setAnimationListener(new AnimationListener() {

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
						// TODO Auto-generated method stub
						musicUtils.stopMusic(playMusic);
					}
				});
			}
		}

	}
}
