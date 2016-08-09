package com.guofeilong.fortune.ui;

import java.util.ArrayList;
import java.util.List;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
public class ChangeColorIconActivity extends BaseActivity implements OnClickListener {
	private static final int CHANGE_COLOR_MESSAGE = 111;
	private static final long DELAY_MILLIS = 1500;
	private int mCurrentID;
	private String mTitle;
	private int mColor;
	private LinearLayout mIconContainer;
	private List<ChangeColorIconView> mIcons;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CHANGE_COLOR_MESSAGE:
				// TODO 改变图标的颜色
				changeAllIconColor(mIcons);
				// mHandler.sendMessageDelayed(msg, DELAY_MILLIS);
				break;

			default:
				break;
			}
		}

	};
	private int[] textColors;
	private Message msg;

	/**
	 * 改变所有图标的颜色
	 * 
	 * @param mCurrentID
	 * @param mIcons
	 */
	private void changeAllIconColor(List<ChangeColorIconView> mIcons) {
		int changeColor = textColors[mCurrentID];
		for (int i = 0; i < mIcons.size(); i++) {
			mIcons.get(i).setColor(changeColor);
		}

		mCurrentID++;
		if (mCurrentID == mIcons.size()) {
			mCurrentID = 0;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_change_color_icon);
		mTitle = getMyTitleAndColor();
		initNavigation();
		initView();
		textColors = getResources().getIntArray(R.array.water_colors);
		mIcons = new ArrayList<ChangeColorIconView>();
		addColorfulIcon(mIconContainer, textColors);

		msg = mHandler.obtainMessage();
		msg.what = CHANGE_COLOR_MESSAGE;
		mHandler.sendMessageDelayed(msg, DELAY_MILLIS);
		
		
	}

	/**
	 * 添加图标
	 * 
	 * @param mIconContainer2
	 * @param textColor
	 */
	private void addColorfulIcon(LinearLayout mIconContainer2, int[] textColor) {
		for (int i = 0; i < textColor.length; i++) {

			ChangeColorIconView generateIconView = generateIconView(R.drawable.gridview_icon_08s_setting, textColor[i]);
			mIconContainer2.addView(generateIconView);
			mIcons.add(generateIconView);
		}
	}

	/**
	 * 动态生成一个自定义颜色的icon
	 * 
	 * @param gridviewIcon08sSetting
	 * @param mColor2
	 * @return
	 */
	private ChangeColorIconView generateIconView(int icon, int color) {
		ChangeColorIconView colorIconView = new ChangeColorIconView(this);
		colorIconView.setColor(color);
		colorIconView.setmResource(icon);
		Bitmap drawable2Bitmap = drawable2Bitmap(icon);
		// 不能直接写wropcontent因为bitmap没设置进去的时候,拿不到宽高导致控件不显示
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(drawable2Bitmap.getWidth(), drawable2Bitmap.getHeight());
		params.bottomMargin = 10;
		params.topMargin = 10;
		params.gravity = Gravity.CENTER_HORIZONTAL;
		colorIconView.setLayoutParams(params);
		return colorIconView;

	}

	/**
	 * drawable转换bitmap
	 * 
	 * @return bitmap
	 */
	private Bitmap drawable2Bitmap(int resourceID) {
		Drawable drawable = this.getResources().getDrawable(resourceID);
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 初始化界面控件
	 */
	private void initView() {
		mIconContainer = (LinearLayout) findViewById(R.id.rl_icon_container);
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
