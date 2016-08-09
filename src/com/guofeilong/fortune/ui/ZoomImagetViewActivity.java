package com.guofeilong.fortune.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.ui.view.ExtendedViewPager;
import com.guofeilong.fortune.ui.view.TouchImageView;

public class ZoomImagetViewActivity extends BaseActivity implements OnClickListener {
	private int mColor;
	private String mTitle;
	private ExtendedViewPager mExtendedViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_zoom_imageview);
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
		mExtendedViewPager.setAdapter(new TouchImageAdapter());
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mExtendedViewPager = (ExtendedViewPager) findViewById(R.id.evp_viewpager);
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

	static class TouchImageAdapter extends PagerAdapter {

		private static int[] images = { R.drawable.nature_1, R.drawable.nature_2, R.drawable.nature_3, R.drawable.nature_4, R.drawable.nature_5 };

		@Override
		public int getCount() {
			return images.length;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			TouchImageView img = new TouchImageView(container.getContext());
			img.setImageResource(images[position]);
			container.addView(img, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
			return img;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}
}
