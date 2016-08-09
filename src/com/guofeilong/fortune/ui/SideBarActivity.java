package com.guofeilong.fortune.ui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.business.User;
import com.guofeilong.fortune.ui.view.CircleImageView;
import com.guofeilong.fortune.ui.view.ClearEditText;
import com.guofeilong.fortune.ui.view.SideBar;
import com.guofeilong.fortune.ui.view.SideBar.OnTouchingLetterChangedListener;
import com.guofeilong.fortune.utils.ViewUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class SideBarActivity extends BaseActivity implements OnClickListener, TextWatcher, OnTouchingLetterChangedListener {
	private int mColor;
	private String mTitle;
	private ListView mSLideList;
	private SideBar mSideBar;
	private TextView mIndexLetter;
	private ClearEditText mClearSerch;
	private FriendsAdapter mAdapter;

	private SortIgnoreCaseComparator mComparator;
	private List<String> mFriendsSortLetters;
	private List<User> mFriendsList;
	private List<User> filterDataList;
	private int[] mTempData;
	private String mSearchStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_slide_bar);
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
		mSideBar.setTextView(mIndexLetter);
		// 设置右侧触摸监听
		mSideBar.setOnTouchingLetterChangedListener(this);
		// 根据输入框输入值的改变来过滤搜索
		mClearSerch.addTextChangedListener(this);

		// 如果数据不为空,设置给adapter设置数据
		if (mAdapter != null) {
			mSLideList.setAdapter(mAdapter);
		}

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		if (ev.getAction() == MotionEvent.ACTION_DOWN && isShouldHideInput(getCurrentFocus(), ev)) {
			ViewUtils.hideSolftInput(this);
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 是否隐藏键盘
	 * 
	 * @param v
	 * @param event
	 * @return
	 */
	private boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] leftTop = { 0, 0 };
			// 获取输入框当前的location位置
			v.getLocationInWindow(leftTop);
			int left = leftTop[0];
			int top = leftTop[1];
			int bottom = top + v.getHeight();
			int right = left + v.getWidth();
			if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
				// 点击的是输入框区域，保留点击EditText的事件
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mSLideList = (ListView) findViewById(R.id.friends_lv_friends);
		mSideBar = (SideBar) findViewById(R.id.friends_side_bar);
		mIndexLetter = (TextView) findViewById(R.id.friends_index_letter_dialog);
		mClearSerch = (ClearEditText) findViewById(R.id.filter_edit);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mTempData = new int[] { R.drawable.ic_launcher, R.drawable.bg_guanxi_icon, R.drawable.bg_guanxi_icon, R.drawable.bg_always_smile };
		mFriendsList = new ArrayList<User>();
		// TODO 制造假数据填充朋友列表
		addUser();
		mComparator = new SortIgnoreCaseComparator();
		mFriendsSortLetters = getSortLettersList(mFriendsList);
		mAdapter = new FriendsAdapter(this, mFriendsList, mFriendsSortLetters, mSearchStr);

	}

	private void addUser() {
		int baseNum = 65;
		String baseName = "_Android_Test";
		for (int i = 0; i < 26; i++) {
			char c = (char) (baseNum + i);
			User user = new User();
			user.setPortrait(mTempData[i % mTempData.length]);
			user.setUserId(c + baseName);
			mFriendsList.add(user);
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

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		mSearchStr = mClearSerch.getText().toString();
		filterData(s.toString());
	}

	/**
	 * 模拟刷新数据
	 * 
	 * @param string
	 */
	private void filterData(String string) {
		if (TextUtils.isEmpty(string)) {
			addUser();
			mAdapter.notifyDataSetChanged();
		} else {
			mFriendsList.clear();
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTouchingLetterChanged(String s) {
		// 该字母首次出现的位置
		if (mAdapter != null) {
			int position = mAdapter.getPositionForSection(s.charAt(0));
			if (position != -1) {
				mSLideList.setSelection(position + 1);
			}
		}
	}

	/**
	 * 得到集合元素name的首字母的集合
	 * 
	 * @param mFriendsList
	 */
	@SuppressLint("DefaultLocale")
	private List<String> getSortLettersList(List<User> friendsList) {
		List<String> sortLettersList = new ArrayList<String>();
		for (int i = 0; i < friendsList.size(); i++) {
			User user = friendsList.get(i);
			String sortString = user.getUserId().substring(0, 1).toUpperCase();
			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				sortLettersList.add(sortString);
			} else {
				sortLettersList.add("#");
			}
		}
		return sortLettersList;
	}

	/**
	 * 比较器
	 * 
	 * @author guofl
	 * 
	 */
	private class SortIgnoreCaseComparator implements Comparator<User> {

		@SuppressLint("DefaultLocale")
		public int compare(User o1, User o2) {
			String sortStr1 = "#";
			String sortStr2 = "#";
			String sortLetter1 = o1.getUserId().substring(0, 1).toUpperCase();
			String sortLetter2 = o2.getUserId().substring(0, 1).toUpperCase();
			String name1 = o1.getUserId().toLowerCase();
			String name2 = o2.getUserId().toLowerCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortLetter1.matches("[A-Z]")) {
				sortStr1 = sortLetter1;
			}
			if (sortLetter2.matches("[A-Z]")) {
				sortStr2 = sortLetter2;
			}

			if ((!"#".equals(sortStr1)) && "#".equals(sortStr2)) {
				return -1;
			} else if ("#".equals(sortStr1) && (!"#".equals(sortStr2))) {
				return 1;
			} else {
				return name1.compareTo(name2);
			}
		}

	}

	class FriendsAdapter extends BaseAdapter implements SectionIndexer {
		private Handler mHandle = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
			}
		};
		private List<User> mFriendsList = null;
		private List<String> mFriendsSortLetters;
		private Context mContext;
		private String mSearchStr;
		private SpannableString mSpannableString;
		private int mSearchStrColor;
		private Bitmap defaultPortrait;
		private Bitmap bitmap;
		private int bitmapWidth;
		private int bitmapHeight;
		private boolean mBusy = false;

		public FriendsAdapter(Context mContext, List<User> list, List<String> friendsSortLetters, String searchStr) {
			this.mContext = mContext;
			this.mFriendsList = list;
			this.mFriendsSortLetters = friendsSortLetters;
			this.mSearchStr = searchStr;
			mSearchStrColor = mContext.getResources().getColor(R.color.qingse);
			bitmapWidth = ViewUtils.dpToPx(mContext.getResources(), 50);
			bitmapHeight = ViewUtils.dpToPx(mContext.getResources(), 50);
			defaultPortrait = ViewUtils.decodeSampledBitmapFromResource(mContext.getResources(), R.drawable.default_portrait, bitmapWidth, bitmapHeight);
		}

		public void setFlagBusy(boolean busy) {
			this.mBusy = busy;
		}

		public int getCount() {
			return this.mFriendsList.size();
		}

		public Object getItem(int position) {
			return mFriendsList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("DefaultLocale")
		public View getView(final int position, View convertView, ViewGroup arg2) {
			ViewHolder holder = null;
			final User friend = mFriendsList.get(position);
			final String sortLetter = mFriendsSortLetters.get(position);
			if (convertView != null && convertView instanceof LinearLayout) {
				holder = (ViewHolder) convertView.getTag();
			} else {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_friends_list_member, null);
				holder.name = (TextView) convertView.findViewById(R.id.item_friends_name);
				holder.portrait = (CircleImageView) convertView.findViewById(R.id.item_friends_iv_portrait);
				holder.sortLetters = (TextView) convertView.findViewById(R.id.catalog);
				holder.line = (ImageView) convertView.findViewById(R.id.friends_item_iv_line_top);
				convertView.setTag(holder);
			}

			// 根据position获取分类的首字母的Char ascii值
			int section = getSectionForPosition(position);
			// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
			if (position == getPositionForSection(section)) {
				holder.line.setVisibility(View.VISIBLE);
				holder.sortLetters.setVisibility(View.VISIBLE);
				holder.sortLetters.setText(sortLetter);
			} else {
				holder.line.setVisibility(View.GONE);
				holder.sortLetters.setVisibility(View.GONE);
			}

			int portrait = friend.getPortrait();

			holder.portrait.setImageBitmap(ViewUtils.drawable2Bitmap(SideBarActivity.this, portrait));
			String name = mFriendsList.get(position).getUserId();
			mSpannableString = new SpannableString(name);
			if (mSearchStr == null) {
				holder.name.setText(name);
			} else {
				int searchStrIndex = name.toUpperCase().indexOf(mSearchStr.toUpperCase());
				mSpannableString.setSpan(new ForegroundColorSpan(mSearchStrColor), searchStrIndex, searchStrIndex + mSearchStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				holder.name.setText(mSpannableString);
			}

			return convertView;
		}

		class ViewHolder {
			CircleImageView portrait;
			TextView name;
			// TextView phoneNum;
			TextView sortLetters;
			ImageView line;

		}

		/**
		 * 根据ListView的当前位置获取分类的首字母的Char ascii值
		 */
		public int getSectionForPosition(int position) {
			return mFriendsSortLetters.get(position).charAt(0);
		}

		/**
		 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
		 */
		@SuppressLint("DefaultLocale")
		public int getPositionForSection(int section) {
			for (int i = 0; i < getCount(); i++) {
				String sortStr = mFriendsSortLetters.get(i);
				char firstChar = sortStr.toUpperCase().charAt(0);
				if (firstChar == section) {
					return i;
				}
			}
			return -1;
		}

		/**
		 * 提取英文的首字母，非英文字母用#代替。
		 */
		@SuppressLint("DefaultLocale")
		private String getAlpha(String str) {
			String sortStr = str.trim().substring(0, 1).toUpperCase();
			// 正则表达式，判断首字母是否是英文字母
			if (sortStr.matches("[A-Z]")) {
				return sortStr;
			} else {
				return "#";
			}
		}

		@Override
		public Object[] getSections() {
			return null;
		}
	}
}
