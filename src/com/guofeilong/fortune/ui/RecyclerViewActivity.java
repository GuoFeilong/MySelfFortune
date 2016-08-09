package com.guofeilong.fortune.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.ui.MyReAdapter.OnItemClickLitener;
import com.guofeilong.fortune.ui.view.DividerGridItemDecoration;
import com.guofeilong.fortune.ui.view.DividerItemDecoration;

public class RecyclerViewActivity extends BaseActivity implements OnClickListener {
	private int mColor;
	private String mTitle;
	private RecyclerView mRecyclerView;
	private MyReAdapter myReAdapter;
	private RecyclerView.LayoutManager mRManager;
	private RecyclerView.ViewHolder mRHolder;
	private ArrayList<String> mDatas;
	private int[] mColors;
	private int lenth;
	private TextView mListStyle;
	private TextView mGrideStyle;
	private TextView mWaterFullStyle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_recycle_view);
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
		mListStyle.setOnClickListener(this);
		mGrideStyle.setOnClickListener(this);
		mWaterFullStyle.setOnClickListener(this);

		// 设置布局管理器
		mRecyclerView.setLayoutManager(mRManager);
		// 设置adapter
		mRecyclerView.setAdapter(myReAdapter);
		// 设置Item增加、移除动画
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		// TODO 添加分割线
		mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
		// 设置item动画
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
		mListStyle = (TextView) findViewById(R.id.tv_1);
		mGrideStyle = (TextView) findViewById(R.id.tv_2);
		mWaterFullStyle = (TextView) findViewById(R.id.tv_3);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mColors = getResources().getIntArray(R.array.water_colors);

		lenth = mColors.length;
		mDatas = new ArrayList<String>();
		for (int i = 'A'; i < 'Z'; i++) {
			mDatas.add("" + (char) i);
		}

		mRManager = new LinearLayoutManager(this);
		myReAdapter = new MyReAdapter();
		myReAdapter.setmColors(mColors);
		myReAdapter.setContext(this);
		myReAdapter.setListData(mDatas);
		myReAdapter.setOnItemClickLitener(new OnItemClickLitener() {

			@Override
			public void onItemLongClick(View view, int position) {
				mDatas.remove(position);
				// myReAdapter.notifyDataSetChanged();
				myReAdapter.notifyItemRemoved(position);

			}

			@Override
			public void onItemClick(View view, int position) {
				// TODO Auto-generated method stub
				mDatas.add("我是新来的...");
				// myReAdapter.notifyDataSetChanged();
				myReAdapter.notifyItemInserted(position);
			}
		});
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
		case R.id.tv_1:
			// TODO 切换list风格
			mRManager = new LinearLayoutManager(this);
			mRecyclerView.setLayoutManager(mRManager);
			// TODO 添加分割线
			mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
			myReAdapter.notifyDataSetChanged();
			break;

		case R.id.tv_2:
			// TODO 切换grid风格
			mRManager = new GridLayoutManager(this, 4);
			mRecyclerView.setLayoutManager(mRManager);
			// TODO 添加分割线
			mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));
			myReAdapter.notifyDataSetChanged();
			break;

		case R.id.tv_3:
			// TODO 切换瀑布流风格
			mRManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.HORIZONTAL);
			mRecyclerView.setLayoutManager(mRManager);
			myReAdapter.notifyDataSetChanged();
			break;

		default:
			break;
		}

	}

}
