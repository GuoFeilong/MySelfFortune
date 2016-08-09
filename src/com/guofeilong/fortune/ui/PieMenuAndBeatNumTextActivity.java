package com.guofeilong.fortune.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.ui.view.NumberBeatTextView;
import com.guofeilong.fortune.ui.view.PathAnimImageViewItem;
import com.guofeilong.fortune.ui.view.PathAnimMenu;
import com.guofeilong.fortune.ui.view.PathAnimMenu.PathAnimMenuLinster;
import com.guofeilong.fortune.ui.view.PathAnimTextViewItem;
import com.guofeilong.fortune.utils.T;
import com.guofeilong.fortune.utils.ViewUtils;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class PieMenuAndBeatNumTextActivity extends BaseActivity implements OnClickListener {
	private int mColor;
	private String mTitle;
	/**
	 * 扇形菜单
	 */
	private PathAnimMenu mPathMenu;
	/**
	 * gift
	 */
	private static final int MENU_ITEM_GIVE = 2;
	/**
	 * 接收
	 */
	private static final int MENU_ITEM_RECEIVE = 1;
	/**
	 * 历史信息
	 */
	private static final int MENU_ITEM_HISTORY = 0;
	private NumberBeatTextView mBeatTextView1, mBeatTextView2, mBeatTextView3;
	private TextView mChangeNum;
	private int[] mCicleBG;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setellite_menu);
		mTitle = getMyTitleAndColor();
		initNavigation();
		initData();
		initView();
		initPathMenu();
		initEvent();
	}

	/**
	 * 初始化扇形菜单
	 */
	private void initPathMenu() {
		List<PathAnimTextViewItem> otherAnimItemList = new ArrayList<PathAnimTextViewItem>();

		PathAnimTextViewItem otherItem = new PathAnimTextViewItem(this, null);
		otherItem.setText(getResources().getString(R.string.main_path_menu_friends));
		otherItem.setBackgroundResource(R.drawable.pathmenu_bg_round_gray);
		otherAnimItemList.add(otherItem);

		otherItem = new PathAnimTextViewItem(this, null);
		otherItem.setBackgroundResource(R.drawable.pathmenu_bg_round_gray);
		otherItem.setText(getResources().getString(R.string.main_path_menu_reciver));
		otherAnimItemList.add(otherItem);

		otherItem = new PathAnimTextViewItem(this, null);
		otherItem.setBackgroundResource(R.drawable.pathmenu_bg_round_gray);
		otherItem.setText(getResources().getString(R.string.main_path_menu_gift));
		otherAnimItemList.add(otherItem);

		// add按钮
		PathAnimImageViewItem addItem = new PathAnimImageViewItem(this, null);
		addItem.setBackgroundResource(R.drawable.pathmenu_bg_round_white);
		addItem.setImageResource(R.drawable.item_pathmenu_transfer);

		mPathMenu.addAllItems(otherAnimItemList, addItem);
		mPathMenu.setPathAnimMenuLinster(new PathAnimMenuLinster() {

			@Override
			public void didSelectedItem(View item, int index) {
				switch (index) {
				case MENU_ITEM_GIVE:
					T.show(PieMenuAndBeatNumTextActivity.this, "click" + index, 0);
					break;
				case MENU_ITEM_RECEIVE:
					T.show(PieMenuAndBeatNumTextActivity.this, "click" + index, 0);
					break;
				case MENU_ITEM_HISTORY:
					T.show(PieMenuAndBeatNumTextActivity.this, "click" + index, 0);
					break;
				default:
					break;
				}

			}

			@Override
			public void onPathMenuClosed() {
			}

			@Override
			public void onPathMenuOpened() {
			}

		});

	}

	/**
	 * 初始化事件
	 */
	private void initEvent() {
		setRandomNum();
		mChangeNum.setOnClickListener(this);
	}

	private void setRandomNum() {
		float nextFloat = new Random().nextFloat()+0.1f;
		int nextInt = new Random().nextInt(300) + 10;
		mBeatTextView1.beatNumber(nextFloat * nextInt);
		setRandomBG(mBeatTextView1);
		nextFloat = new Random().nextFloat();
		nextInt = new Random().nextInt(300) + 10;
		mBeatTextView2.beatNumber(nextFloat * nextInt);
		setRandomBG(mBeatTextView2);
		nextFloat = new Random().nextFloat();
		nextInt = new Random().nextInt(300) + 10;
		mBeatTextView3.beatNumber(nextFloat * nextInt);
		setRandomBG(mBeatTextView3);
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mPathMenu = (PathAnimMenu) findViewById(R.id.pathmenu);
		mBeatTextView1 = (NumberBeatTextView) findViewById(R.id.tv_beat_num1);
		setRandomBG(mBeatTextView1);
		mBeatTextView2 = (NumberBeatTextView) findViewById(R.id.tv_beat_num2);
		setRandomBG(mBeatTextView2);
		mBeatTextView3 = (NumberBeatTextView) findViewById(R.id.tv_beat_num3);
		setRandomBG(mBeatTextView3);
		mChangeNum = (TextView) findViewById(R.id.tv_change_num);
	}

	private void setRandomBG(NumberBeatTextView mBeatTextView12) {
		mBeatTextView12.setBackground(getResources().getDrawable(mCicleBG[new Random().nextInt(mCicleBG.length)]));
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mCicleBG = new int[] { R.drawable.bg_demo_circle1, R.drawable.bg_demo_circle2, R.drawable.bg_demo_circle2, R.drawable.bg_demo_circle3, R.drawable.bg_demo_circle4, R.drawable.bg_demo_circle5, R.drawable.bg_demo_circle6,
				R.drawable.bg_demo_circle7, R.drawable.bg_demo_circle8 };
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
		case R.id.tv_change_num:
			setRandomNum();
			mChangeNum.setBackground(getResources().getDrawable(mCicleBG[new Random().nextInt(mCicleBG.length)]));
			break;
		default:
			break;
		}

	}
}
