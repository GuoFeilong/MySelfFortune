package com.guofeilong.fortune.ui;

import java.util.ArrayList;
import java.util.HashMap;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.ui.view.CircleImageView;
import com.guofeilong.fortune.ui.view.DragGridView;
import com.guofeilong.fortune.ui.view.GridMenuDragAdapter;
import com.guofeilong.fortune.utils.Settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GridMenuActivity extends BaseActivity implements OnClickListener {
	private int mColor;
	private String mTitle;
	/**
	 * TYPE作为滑动界面的标记:1→第二页 2→第三页
	 */
	public int MENU_PAGE_TYPE;
	/**
	 * 预付费标记
	 */
	private static final int PRE_PAID_TYPE = 22;
	/**
	 * 后付费标记
	 */
	private static final int POST_PAID_TYPE = 23;
	/**
	 * 背景的标记
	 */
	private static final String GRID_MENU_BG = "grid_menu_bg";
	/**
	 * 存储拖拽的标记
	 */
	private static final String IS_HAS_DRAGED = "is_has_draged";
	private static final String IS_HAS_DRAGED_KEY = "is_has_draged_key";
	/**
	 * 这是调整后的九宫格菜单的标记
	 */
	private static final String NEW_GRIDVIEW_MENU = "new_gridview_menu";
	/**
	 * 标题的标记
	 */
	private static final String GRID_MENU_TITLE = "grid_menu_title";
	public static final String MENU_TYPE = "MENU_TYPE";
	private static final int SECOND_PAGE_TYPE = 1;
	private static final int THIRD_PAGE_TYPE = 2;

	protected static final int USAGE_OVERVIEW = 0;
	protected static final int USAGE_EXPLORE = 1;
	protected static final int DATA_USAGE = 2;
	protected static final int RECHARGE = 3;
	protected static final int QUICK_ORDER = 4;
	protected static final int TURBO_BOOST = 5;
	protected static final int TRANSFER = 6;
	protected static final int TRANSFEORM = 7;

	protected static final int WALLET = 0;
	protected static final int FRIENDS = 1;
	protected static final int BUDGET_CONTROL = 2;
	protected static final int DISCOVERY = 3;
	protected static final int MESSAGES = 4;
	protected static final int CUSTOM_SUPPORT = 5;
	protected static final int SERVICE_REQUEST = 6;
	protected static final int SETTINGS = 7;
	private DragGridView mGridMenu;
	private GridMenuDragAdapter mSecondPageAdapter;
	private GridMenuDragAdapter mThridPageAdapter;

	private int mPreOrPostPaid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		MENU_PAGE_TYPE = intent.getIntExtra(MENU_TYPE, 0);
		MENU_PAGE_TYPE =SECOND_PAGE_TYPE;
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_gridmenu);
		mTitle = getMyTitleAndColor();
		initNavigation();
		initGridMenuViews();
		mPreOrPostPaid = getPreOrPostPaidType();
		// 根据不同的type类型设置不同的适配器展示数据
		switch (MENU_PAGE_TYPE) {
		case SECOND_PAGE_TYPE:
			setSecondPageData();
			break;
		case THIRD_PAGE_TYPE:
			setThridPageData();

			break;
		default:
			break;
		}
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
	 * 获取拿到的用户标记,预付费or后付费
	 * 
	 * @return
	 */
	private int getPreOrPostPaidType() {
		// TODO 调用接口拿到用户的付费类型
		return POST_PAID_TYPE;
	}

	/**
	 * 初始化九宫格界面的控件
	 */
	private void initGridMenuViews() {
		mGridMenu = (DragGridView) findViewById(R.id.gv_nine_pattch);
	}

	/**
	 * 设置第三页九宫格数据
	 */
	@SuppressWarnings("unchecked")
	private void setThridPageData() {
		SharedPreferences sp = getSharedPreferences(IS_HAS_DRAGED + THIRD_PAGE_TYPE, Context.MODE_PRIVATE);
		boolean isDraged = sp.getBoolean(IS_HAS_DRAGED_KEY + THIRD_PAGE_TYPE, false);

		int arrayID = R.array.grid_menu_base_title;
		int arrayIcon = R.array.grid_menu_base_icon;
		int arrayCustomID = 0;

		if (PRE_PAID_TYPE == mPreOrPostPaid) {
			arrayCustomID = R.array.grid_menu_thrid_pre;
		} else if (POST_PAID_TYPE == mPreOrPostPaid) {
			arrayCustomID = R.array.grid_menu_thrid_post;
		}
		// 1.取基准顺序对应的数组
		String[] baseGvTitles = getResources().getStringArray(arrayID);
		// 2.根据基准顺序拿到对应的排序图标
		int[] gvNewBgs = getReferenceIcon(arrayIcon);
		// 3.建立集合存储基准顺序图标
		ArrayList<String> myBaseGvTitles = new ArrayList<String>();
		for (int i = 0; i < baseGvTitles.length; i++) {
			myBaseGvTitles.add(baseGvTitles[i]);
		}
		ArrayList<Integer> baseGvNewBgs = new ArrayList<Integer>();
		for (int i = 0; i < gvNewBgs.length; i++) {
			baseGvNewBgs.add(gvNewBgs[i]);
		}

		// 4.获取客户自定义顺序标题
		String[] gvTitles = getResources().getStringArray(arrayCustomID);
		// 5.根据客户自定义 标题顺序,得到新的排序图标
		ArrayList<Integer> myGvNewBgsArray = new ArrayList<Integer>();
		for (int i = 0; i < gvTitles.length; i++) {
			String customTitle = gvTitles[i];
			for (int j = 0; j < myBaseGvTitles.size(); j++) {
				if (customTitle.equals(myBaseGvTitles.get(j))) {
					// 添加客户调整的自定义顺序图标
					myGvNewBgsArray.add(baseGvNewBgs.get(j));
					break;
				}
			}
		}

		ArrayList<HashMap<String, Object>> gvMenuData;
		if (isDraged) {
			gvMenuData = (ArrayList<HashMap<String, Object>>) (Settings.getSettings(this).readDragedMenu(NEW_GRIDVIEW_MENU + THIRD_PAGE_TYPE));
		} else {
			gvMenuData = getGridMenuData(gvTitles, myGvNewBgsArray);
			// gvMenuData = getGridMenuData(gvTitles, gvNewBgs);

		}

		if (mThridPageAdapter == null) {
			// 传入参考的标题
			mThridPageAdapter = new GridMenuDragAdapter(this, gvMenuData, THIRD_PAGE_TYPE, baseGvTitles);
		}
		mGridMenu.setAdapter(mThridPageAdapter);
	}

	/**
	 * 获取九宫格数据
	 * 
	 * @return
	 */
	private ArrayList<HashMap<String, Object>> getGridMenuData(String[] gvTitles, int[] gvNewBgs) {
		ArrayList<HashMap<String, Object>> gvMenuData = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < gvNewBgs.length; i++) {
			HashMap<String, Object> tempData = new HashMap<String, Object>();
			tempData.put(GRID_MENU_BG, gvNewBgs[i]);
			tempData.put(GRID_MENU_TITLE, gvTitles[i]);
			gvMenuData.add(tempData);

		}
		return gvMenuData;
	}

	/**
	 * 获取九宫格数据
	 * 
	 * @return
	 */
	private ArrayList<HashMap<String, Object>> getGridMenuData(String[] gvTitles, ArrayList<Integer> gvBgs) {
		ArrayList<HashMap<String, Object>> gvMenuData = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < gvBgs.size(); i++) {
			HashMap<String, Object> tempData = new HashMap<String, Object>();
			tempData.put(GRID_MENU_BG, gvBgs.get(i));
			tempData.put(GRID_MENU_TITLE, gvTitles[i]);
			gvMenuData.add(tempData);

		}
		return gvMenuData;
	}

	/**
	 * 设置第二页九宫格数据
	 */
	@SuppressWarnings("unchecked")
	private void setSecondPageData() {
		SharedPreferences sp = getSharedPreferences(IS_HAS_DRAGED + SECOND_PAGE_TYPE, Context.MODE_PRIVATE);
		boolean isDraged = sp.getBoolean(IS_HAS_DRAGED_KEY + SECOND_PAGE_TYPE, false);

		int arrayID = R.array.grid_menu_base_title;
		int arrayIcon = R.array.grid_menu_base_icon;
		int arrayCustomID = 0;
		if (PRE_PAID_TYPE == mPreOrPostPaid) {
			arrayCustomID = R.array.grid_menu_second_pre;
		} else if (POST_PAID_TYPE == mPreOrPostPaid) {
			arrayCustomID = R.array.grid_menu_second_post;
		}

		// 1.取基准顺序对应的数组
		String[] baseGvTitles = getResources().getStringArray(arrayID);
		// 2.根据基准顺序对应的排序图标
		int[] gvNewBgs = getReferenceIcon(arrayIcon);
		// 3.建立集合存储基准顺序图标
		ArrayList<String> myBaseGvTitles = new ArrayList<String>();
		for (int i = 0; i < baseGvTitles.length; i++) {
			myBaseGvTitles.add(baseGvTitles[i]);
		}
		ArrayList<Integer> baseGvNewBgs = new ArrayList<Integer>();
		for (int i = 0; i < gvNewBgs.length; i++) {
			baseGvNewBgs.add(gvNewBgs[i]);
		}

		// 4.获取客户自定义顺序标题
		String[] gvTitles = getResources().getStringArray(arrayCustomID);
		// 5.根据客户自定义 标题顺序,得到新的排序图标
		ArrayList<Integer> myGvNewBgsArray = new ArrayList<Integer>();
		for (int i = 0; i < gvTitles.length; i++) {
			String customTitle = gvTitles[i];
			for (int j = 0; j < myBaseGvTitles.size(); j++) {
				if (customTitle.equals(myBaseGvTitles.get(j))) {
					// 添加客户调整的自定义顺序图标
					myGvNewBgsArray.add(baseGvNewBgs.get(j));
					break;
				}
			}
		}

		ArrayList<HashMap<String, Object>> gvMenuData;
		if (isDraged) {
			gvMenuData = (ArrayList<HashMap<String, Object>>) (Settings.getSettings(this).readDragedMenu(NEW_GRIDVIEW_MENU + SECOND_PAGE_TYPE));
		} else {
			gvMenuData = getGridMenuData(gvTitles, myGvNewBgsArray);
			// gvMenuData = getGridMenuData(gvTitles, gvNewBgs);

		}

		mGridMenu = (DragGridView) findViewById(R.id.gv_nine_pattch);
		if (mSecondPageAdapter == null) {
			// 传入参考的标题
			mSecondPageAdapter = new GridMenuDragAdapter(this, gvMenuData, SECOND_PAGE_TYPE, baseGvTitles);
		}
		mGridMenu.setAdapter(mSecondPageAdapter);
	}

	/**
	 * 根据传入的配置顺序获取正确的参考图标id数组
	 * 
	 * @param arrayIcon
	 * @return
	 */
	private int[] getReferenceIcon(int arrayIcon) {
		TypedArray obtainTypedArray = getResources().obtainTypedArray(arrayIcon);
		int length = obtainTypedArray.length();
		int arrayIds[] = new int[length];
		for (int i = 0; i < length; i++) {
			arrayIds[i] = obtainTypedArray.getResourceId(i, 0);
		}
		obtainTypedArray.recycle();
		return arrayIds;
	}

	/**
	 * 切换界面跳转
	 * 
	 * @param <T>
	 */
	private <T> void changeActivity(Class<T> clazz) {
		Intent intent = new Intent(getApplicationContext(), clazz);
		startActivity(intent);
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
