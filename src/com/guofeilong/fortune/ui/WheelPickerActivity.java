package com.guofeilong.fortune.ui;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.ui.view.ArrayWheelAdapter;
import com.guofeilong.fortune.ui.view.OnWheelChangedListener;
import com.guofeilong.fortune.ui.view.WheelView;
import com.guofeilong.fortune.utils.T;

import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class WheelPickerActivity extends BaseActivity implements OnClickListener {
	private static final int VISIBILITY_ITEMS = 7;
	private int mColor;
	private String mTitle;
	private Button mShowPicker;
	private String[] arrayAccountID;
	private int index;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_wheel_picker);
		mTitle = getMyTitleAndColor();
		initNavigation();
		initData();
		initView();
		initEvent();
	}

	private void initEvent() {
		mShowPicker.setOnClickListener(this);
	}

	private void initView() {
		mShowPicker = (Button) findViewById(R.id.btn_show_picker);
	}

	private void initData() {
		arrayAccountID = new String[] { "Android Cupcake", "Android Gingerbread", "Android JellyBean" };
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
		case R.id.btn_show_picker:
			showPicker();
			break;
		default:
			break;
		}
	}

	private PopupWindow mPopupWindow;

	@Override
	public void onBackPressed() {
		if (mPopupWindow != null) {
			if (mPopupWindow.isShowing()) {
				mPopupWindow.dismiss();
			} else {
				finish();
			}
		} else {
			finish();
		}
	}

	private void showPicker() {
		try {
			if (arrayAccountID != null && arrayAccountID.length != 0) {
				final int tempIndex = index;
				int mTextSize = (int) this.getResources().getDimension(R.dimen.set_notice_text_size);
				View view = getLayoutInflater().inflate(R.layout.balance_choice_box, null);
				TextView tv_cancel = (TextView) view.findViewById(R.id.tv_notice_cancel);
				TextView tv_complete = (TextView) view.findViewById(R.id.tv_notice_complete);
				// 初始化并且取值滚动控件的百分比
				WheelView wv_notice_percent = (WheelView) view.findViewById(R.id.wv_notice_percent);
				wv_notice_percent.setCurrentItem(index);
				wv_notice_percent.setVisibleItems(VISIBILITY_ITEMS);
				wv_notice_percent.setTextSize(mTextSize);
				wv_notice_percent.setAdapter(new ArrayWheelAdapter<String>(arrayAccountID));
				wv_notice_percent.addChangingListener(new OnWheelChangedListener() {

					// 滚动事件
					@Override
					public void onChanged(WheelView wheel, int oldValue, int newValue) {
						index = newValue;
					}
				});
				wv_notice_percent.setCurrentItem(index);

				mPopupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				mPopupWindow.setTouchable(true);
				mPopupWindow.setTouchInterceptor(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						return false;
					}
				});
				mPopupWindow.setOutsideTouchable(false);
				ColorDrawable dw_my = new ColorDrawable(0xb0000000);
				mPopupWindow.setBackgroundDrawable(dw_my);
				mPopupWindow.setAnimationStyle(R.style.PopupWindow_Animation);
				mPopupWindow.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
				tv_cancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						index = tempIndex;
						mPopupWindow.dismiss();
					}
				});
				tv_complete.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						T.show(getApplicationContext(), "你选中的内容是:" + arrayAccountID[index], 0);
						mPopupWindow.dismiss();
					}
				});
			}
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
	}

}
