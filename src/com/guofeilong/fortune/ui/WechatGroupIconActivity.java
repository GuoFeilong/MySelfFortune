package com.guofeilong.fortune.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.utils.BitmapUtil;
import com.guofeilong.fortune.utils.BitmapUtil.MyBitmapEntity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WechatGroupIconActivity extends BaseActivity implements OnClickListener {
	private static final int CHANGE_UI = 11;
	private int mColor;
	private String mTitle;
	private TextView mStart;
	private ImageView mGroupIcon1;
	private ImageView mGroupIcon2;
	private ImageView mGroupIcon3;
	private ArrayList<Integer> mGroupIcons;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CHANGE_UI:
				Bitmap[] bps = (Bitmap[]) msg.obj;
				mGroupIcon1.setImageBitmap(bps[0]);
				mGroupIcon2.setImageBitmap(bps[1]);
				mGroupIcon3.setImageBitmap(bps[2]);
				break;

			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_wechat_group_icon);
		mTitle = getMyTitleAndColor();
		initNavigation();
		initData();
		initView();
		initEvent();

		startRandom();
	}

	/**
	 * 初始化事件
	 */
	private void initEvent() {
		mStart.setOnClickListener(this);
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mStart = (TextView) findViewById(R.id.tv_start_created_icon);
		mGroupIcon1 = (ImageView) findViewById(R.id.iv_group_icon1);
		mGroupIcon2 = (ImageView) findViewById(R.id.iv_group_icon2);
		mGroupIcon3 = (ImageView) findViewById(R.id.iv_group_icon3);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mGroupIcons = new ArrayList<Integer>();
		mGroupIcons.add(R.drawable.gong);
		mGroupIcons.add(R.drawable.xi);
		mGroupIcons.add(R.drawable.fa);
		mGroupIcons.add(R.drawable.cai);
		mGroupIcons.add(R.drawable.hah);
		mGroupIcons.add(R.drawable.gong);
		mGroupIcons.add(R.drawable.xi);
		mGroupIcons.add(R.drawable.fa);
		mGroupIcons.add(R.drawable.cai);
	}

	/**
	 * 获得原始数据
	 * 
	 * @return
	 */
	private ArrayList<Integer> getSourceData() {
		int random = new Random().nextInt(8) + 1;
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for (int i = 0; i < random; i++) {
			temp.add(mGroupIcons.get(i));
		}

		return temp;
	}

	/**
	 * 获取bitmap数组
	 * 
	 * @param drables
	 * @return
	 */
	private Bitmap[] getBitmapsFromDrables(ArrayList<Integer> drables) {
		Bitmap[] bitmaps = null;
		ArrayList<Bitmap> arrayBs = new ArrayList<Bitmap>();
		for (int i = 0; i < drables.size(); i++) {
			Drawable drawable = getResources().getDrawable(drables.get(i));
			BitmapDrawable b = (BitmapDrawable) drawable;
			Bitmap bitmap = b.getBitmap();
			arrayBs.add(bitmap);
		}
		bitmaps = arrayBs.toArray(new Bitmap[arrayBs.size()]);
		return bitmaps;
	}

	/**
	 * 生成群头像
	 * 
	 * @param mGroupMembers2
	 */
	public Bitmap generateGroupProtrait(Bitmap[] bitmaps) {
		int protraitCount = bitmaps.length > 9 ? 9 : bitmaps.length;
		List<MyBitmapEntity> bitmapEntitys = BitmapUtil.getBitmapEntitys(protraitCount, this);
		Bitmap combineBitmap = BitmapUtil.getCombineBitmaps(bitmapEntitys, bitmaps);
		return combineBitmap;
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
		case R.id.tv_start_created_icon:
			startRandom();
			break;
		default:
			break;
		}

	}

	/**
	 * 开始拼图
	 */
	private void startRandom() {

		new Thread(new Runnable() {

			@Override
			public void run() {

				ArrayList<Integer> sourceData1 = getSourceData();
				ArrayList<Integer> sourceData2 = getSourceData();
				ArrayList<Integer> sourceData3 = getSourceData();

				Bitmap[] fromDrables1 = getBitmapsFromDrables(sourceData1);
				Bitmap[] fromDrables2 = getBitmapsFromDrables(sourceData2);
				Bitmap[] fromDrables3 = getBitmapsFromDrables(sourceData3);

				Bitmap groupProtrait1 = generateGroupProtrait(fromDrables1);
				Bitmap groupProtrait2 = generateGroupProtrait(fromDrables2);
				Bitmap groupProtrait3 = generateGroupProtrait(fromDrables3);

				Message msg = mHandler.obtainMessage();
				Bitmap[] bps = { groupProtrait1, groupProtrait2, groupProtrait3 };

				msg.what = CHANGE_UI;
				msg.obj = bps;

				mHandler.sendMessage(msg);

			}
		}).start();

	}
}
