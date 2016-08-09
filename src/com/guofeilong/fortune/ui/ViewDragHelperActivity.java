package com.guofeilong.fortune.ui;

import java.util.ArrayList;
import java.util.Random;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.ui.view.ViewDragHelperLayout;
import com.guofeilong.fortune.utils.ViewUtils;

import android.R.array;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class ViewDragHelperActivity extends BaseActivity implements OnClickListener {
	private int mColor;
	private String mTitle;
	private ViewDragHelperLayout mVdhLayout;
	private ArrayList<View> mAllViews;
	private DisplayMetrics mDm;
	private int[] mBgs = { R.drawable.bg_demo_circle1, R.drawable.bg_demo_circle2, R.drawable.bg_demo_circle3, R.drawable.bg_demo_circle4, R.drawable.bg_demo_circle5, R.drawable.bg_demo_circle6, R.drawable.bg_demo_circle7,
			R.drawable.bg_demo_circle8, };
	private String[] mDesc = { "敢脱吗?", "优衣库买衣服?", "拖一下?", "三里屯?", "优衣库新款?", "你懂的?", "....讨厌.", "什么意思啊?" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_view_draghelper);
		mTitle = getMyTitleAndColor();
		initNavigation();
		initData();
		initView();
		initEvent();
	}

	/**
	 * 添加生成的view到容器中
	 * 
	 * @param view
	 * @param container
	 */
	public void addView2Container(ArrayList<View> viewList, RelativeLayout container) {
		for (int i = 0; i < viewList.size(); i++) {
			View view = viewList.get(i);
			
//			int applyDimension = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
//			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(applyDimension, applyDimension);
//			int topMar = new Random().nextInt(mDm.heightPixels);
//			int leftMar = new Random().nextInt(mDm.widthPixels);
//			params.topMargin = topMar;
//			params.leftMargin = leftMar;
//			view.setLayoutParams(params);
			
			container.addView(view);

		}
	}

	/**
	 * 随机生成一个textview
	 * 
	 * @return
	 */
	@SuppressLint("NewApi")
	public void generateChildView(int bgID, String text) {
		TextView temp = new TextView(this);
		temp.setHeight(ViewUtils.dpToPx(getResources(), 100));
		temp.setWidth(ViewUtils.dpToPx(getResources(), 100));
		temp.setBackground(getResources().getDrawable(bgID));
		temp.setText(text);
		temp.setTextColor(getResources().getColor(R.color.white));
		temp.setGravity(Gravity.CENTER);
		temp.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 4, getResources().getDisplayMetrics()));
		mAllViews.add(temp);
	}

	/**
	 * 初始化事件
	 */
	private void initEvent() {
		int nextInt;
		int nextInt2;
		int count = new Random().nextInt(mBgs.length) + 3 < mBgs.length ? new Random().nextInt(mBgs.length) + 3 : mBgs.length;

		for (int i = 0; i < count; i++) {
			nextInt = new Random().nextInt(mBgs.length);
			nextInt2 = new Random().nextInt(mDesc.length);
			generateChildView(mBgs[nextInt], mDesc[nextInt2]);
		}

		addView2Container(mAllViews, mVdhLayout);
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mVdhLayout = (ViewDragHelperLayout) findViewById(R.id.vdhl_container);
	}

	/** 获取屏幕的宽度 */
	public DisplayMetrics getWindowsWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm;
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mDm = getWindowsWidth(this);
		mAllViews = new ArrayList<View>();
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
