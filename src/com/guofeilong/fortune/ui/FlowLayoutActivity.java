package com.guofeilong.fortune.ui;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.ui.view.XCFlowLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class FlowLayoutActivity extends BaseActivity implements OnClickListener {
	private String mTitle;
	private int mColor;
	private int[] textColors;
	private String mNames[] = { "犯我中华者", "虽远", "必诛", "apple", "google", "android", "乔布斯", "apple watch", "viewgroup and layout", "雷军雷布斯", "华为P8 Max", "麦克格雷迪", "詹姆斯", "火箭哈登" };
	private XCFlowLayout mFlowLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_flow_layout);
		mTitle = getMyTitleAndColor();
		textColors = getResources().getIntArray(R.array.water_colors);
		initNavigation();
		initView();
		initChildViews();

	}

	private void initChildViews() {
		MarginLayoutParams lp = new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		// 左右的距离
		lp.leftMargin = 10;
		lp.rightMargin = 10;
		// 行高距离
		lp.topMargin = 20;
		lp.bottomMargin = 20;

		for (int i = 0; i < mNames.length; i++) {
			TextView view = new TextView(this);
			view.setText(mNames[i]);
			view.setTextSize(16);
			view.setTextColor(textColors[i % textColors.length]);
			view.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_yellow_selector));
			Animation anima = AnimationUtils.loadAnimation(this, R.anim.bottom_in);
			anima.setDuration(2000);
			view.startAnimation(anima);
			mFlowLayout.addView(view, lp);
		}

	}

	/**
	 * 初始化界面控件
	 */
	private void initView() {
		mFlowLayout = (XCFlowLayout) findViewById(R.id.flow_layout);
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
