package com.guofeilong.fortune.ui;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.ui.view.BidirSlidingLayout;
import com.guofeilong.fortune.ui.view.CustomPPTVLoading;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BidirSlidingLayoutActivity extends BaseActivity implements OnClickListener {
	private int mColor;
	private String mTitle;

	/**
	 * 双向滑动菜单布局
	 */
	private BidirSlidingLayout bidirSldingLayout;

	/**
	 * 在内容布局上显示的ListView
	 */
	private ListView contentList;
	// private RelativeLayout contentList;

	/**
	 * ListView的适配器
	 */
	private MybidAdapter contentListAdapter;
	// private ArrayAdapter<String> contentListAdapter;

	/**
	 * 用于填充contentListAdapter的数据源。
	 */
	private String[] contentItems = { "Content Item 1", "Content Item 2", "Content Item 3", "Content Item 4", "Content Item 5", "Content Item 6", "Content Item 7", "Content Item 8", "Content Item 9", "Content Item 10", "Content Item 11",
			"Content Item 12", "Content Item 13", "Content Item 14", "Content Item 15", "Content Item 16" };
	private int[] mAllClass;

	// private List<Integer> mAllClass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bidir_sliding_layout);
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
		// TODO Auto-generated method stub

	}

	class MybidAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return contentItems.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = getLayoutInflater().inflate(R.layout.item_bidir, null);
			CustomPPTVLoading pptvL = (CustomPPTVLoading) convertView.findViewById(R.id.pptv_dir);
			int i = position % mAllClass.length;
			int j = (position + 1) % mAllClass.length;
			pptvL.setmFristColor(mAllClass[i]);
			pptvL.setmSencondColor(mAllClass[j]);
			pptvL.startLoading();
			return convertView;
		}
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		bidirSldingLayout = (BidirSlidingLayout) findViewById(R.id.bid_sliding_layout);
		Button showLeftButton = (Button) findViewById(R.id.show_left_button);
		Button showRightButton = (Button) findViewById(R.id.show_right_button);
		contentList = (ListView) findViewById(R.id.contentList);
		// contentListAdapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, contentItems);
		contentListAdapter = new MybidAdapter();
		contentList.setAdapter(contentListAdapter);
		bidirSldingLayout.setScrollEvent(contentList);
		showLeftButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (bidirSldingLayout.isLeftLayoutVisible()) {
					bidirSldingLayout.scrollToContentFromLeftMenu();
				} else {
					bidirSldingLayout.initShowLeftState();
					bidirSldingLayout.scrollToLeftMenu();
				}
			}
		});
		showRightButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (bidirSldingLayout.isRightLayoutVisible()) {
					bidirSldingLayout.scrollToContentFromRightMenu();
				} else {
					bidirSldingLayout.initShowRightState();
					bidirSldingLayout.scrollToRightMenu();
				}
			}
		});
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		// mAllClass =
		// getResources().getStringArray(R.array.title_name_list_activity);
		mAllClass = getResources().getIntArray(R.array.water_colors);
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
