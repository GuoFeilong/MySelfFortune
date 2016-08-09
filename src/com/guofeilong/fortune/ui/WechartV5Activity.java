package com.guofeilong.fortune.ui;

import java.util.ArrayList;
import java.util.List;

import com.guofeilong.fortune.R;
import com.guofeilong.fortune.utils.T;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WechartV5Activity extends FragmentActivity implements OnPageChangeListener, OnClickListener {
	private TextView mTitleChart;
	private TextView mTitleHistory;
	private TextView mTitleFriends;
	private TextView mTitleChartNum;
	private ImageView mTapLine;
	private ViewPager mVpContent;
	private List<TextView> mAllTitles;
	private WechartFragmentAdapter mFragmentAdapter;
	private List<WeChartFragment> mFragments;
	private int mScreenWith_3;
	private int mCurrentPageIndex;
	private LinearLayout mTitleContainer;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_wechart_v5);
		initData();
		initView();
		initEvent();
	}

	/**
	 * 初始化事件
	 */
	private void initEvent() {
		if (mFragmentAdapter != null) {
			mVpContent.setAdapter(mFragmentAdapter);
		}
		mVpContent.setOnPageChangeListener(this);

		for (int i = 0; i < mAllTitles.size(); i++) {
			mAllTitles.get(i).setOnClickListener(this);
		}

	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mTitleContainer = (LinearLayout) findViewById(R.id.include_top2);
		mTitleChart = (TextView) findViewById(R.id.tv_chart);
		mTitleChartNum = (TextView) findViewById(R.id.tv_chart_num);
		mTitleHistory = (TextView) findViewById(R.id.tv_history);
		mTitleFriends = (TextView) findViewById(R.id.tv_friends);
		mTapLine = (ImageView) findViewById(R.id.iv_table_line);
		mVpContent = (ViewPager) findViewById(R.id.vp_content);
		mVpContent.setPageMargin(20);
		mAllTitles.add(mTitleChart);
		mAllTitles.add(mTitleHistory);
		mAllTitles.add(mTitleFriends);

		initTapLine();
	}

	/**
	 * 初始化指示线
	 */
	private void initTapLine() {
		mScreenWith_3 = getWindowsWidth(this) / 3;
		LayoutParams lp = mTapLine.getLayoutParams();
		lp.width = mScreenWith_3;
		mTapLine.setLayoutParams(lp);
	}

	/** 获取屏幕的宽度 */
	public final static int getWindowsWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mAllTitles = new ArrayList<TextView>();
		mFragments = new ArrayList<WeChartFragment>();

		mFragments.add(new WeChartFragment("聊天"));
		mFragments.add(new WeChartFragment("历史"));
		mFragments.add(new WeChartFragment("朋友圈"));

		mFragmentAdapter = new WechartFragmentAdapter(getSupportFragmentManager(), (ArrayList<WeChartFragment>) mFragments);
	}

	/**
	 * reset
	 * 
	 * @param position
	 */
	private void restTopView(int position) {
		for (int i = 0; i < mAllTitles.size(); i++) {
			mAllTitles.get(i).setTextColor(getResources().getColor(R.color.conmmd_textcolor));
		}
		mAllTitles.get(position).setTextColor(getResources().getColor(R.color.demo_title6));
	}

	/**
	 * 适配器
	 * 
	 * @author guofl
	 * 
	 */
	class WechartFragmentAdapter extends FragmentPagerAdapter {
		private ArrayList<WeChartFragment> fragments;
		private FragmentManager fm;

		public WechartFragmentAdapter(FragmentManager fm) {
			super(fm);
			this.fm = fm;
		}

		public WechartFragmentAdapter(FragmentManager fm, ArrayList<WeChartFragment> fragments) {
			super(fm);
			this.fm = fm;
			this.fragments = fragments;
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void setFragments(ArrayList<WeChartFragment> fragments) {
			if (this.fragments != null) {
				FragmentTransaction ft = fm.beginTransaction();
				for (Fragment f : this.fragments) {
					ft.remove(f);
				}
				ft.commit();
				ft = null;
				fm.executePendingTransactions();
			}
			this.fragments = fragments;
			notifyDataSetChanged();
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			Object obj = super.instantiateItem(container, position);
			return obj;
		}

	}

	@Override
	public void onPageScrollStateChanged(int position) {

	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPx) {
		mCurrentPageIndex = position;
		restTopView(position);
		Log.e("TAG", position + " , " + positionOffset + " , " + positionOffsetPx);
		LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) mTapLine.getLayoutParams();

		if (mCurrentPageIndex == 0 && position == 0)// 0->1
		{
			lp.leftMargin = (int) (positionOffset * mScreenWith_3 + mCurrentPageIndex * mScreenWith_3);
		} else if (mCurrentPageIndex == 1 && position == 0)// 1->0
		{
			lp.leftMargin = (int) (mCurrentPageIndex * mScreenWith_3 + (positionOffset - 1) * mScreenWith_3);
		} else if (mCurrentPageIndex == 1 && position == 1) // 1->2
		{
			lp.leftMargin = (int) (mCurrentPageIndex * mScreenWith_3 + positionOffset * mScreenWith_3);
		} else if (mCurrentPageIndex == 2 && position == 1) // 2->1
		{
			lp.leftMargin = (int) (mCurrentPageIndex * mScreenWith_3 + (positionOffset - 1) * mScreenWith_3);
		}
		mTapLine.setLayoutParams(lp);

	}

	@Override
	public void onPageSelected(int arg0) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.tv_chart:
			changePostion(0);
			break;
		case R.id.tv_history:
			changePostion(1);
			break;
		case R.id.tv_friends:
			changePostion(2);
			break;

		default:
			break;
		}
	}

	private void changePostion(int i) {
		mVpContent.setCurrentItem(i);
		LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) mTapLine.getLayoutParams();
		lp.leftMargin = mScreenWith_3 * i;
		mTapLine.setLayoutParams(lp);
	}
}
