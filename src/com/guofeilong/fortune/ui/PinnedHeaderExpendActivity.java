package com.guofeilong.fortune.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.ui.view.PinnedHeaderExpandableListView;
import com.guofeilong.fortune.ui.view.PinnedHeaderExpandableListView.OnHeaderUpdateListener;
import com.guofeilong.fortune.ui.view.StickyLayout;
import com.guofeilong.fortune.ui.view.StickyLayout.OnGiveUpTouchEventListener;
import com.guofeilong.fortune.utils.T;

public class PinnedHeaderExpendActivity extends BaseActivity implements OnClickListener, OnHeaderUpdateListener, OnChildClickListener, OnGroupClickListener, OnGiveUpTouchEventListener {
	private int mColor;
	private String mTitle;

	private PinnedHeaderExpandableListView expandableListView;
	private StickyLayout stickyLayout;
	private ArrayList<Group> groupList;
	private ArrayList<List<People>> childList;
	private MyexpandableListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_pinned_header);
		mTitle = getMyTitleAndColor();
		initNavigation();
		initData();
		initView();
		initEvent();
	}

	public class Group {

		private String title;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
	}

	public class People {

		private String name;
		private int age;
		private String address;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

	}

	class MyexpandableListAdapter extends BaseExpandableListAdapter {
		private Context context;
		private LayoutInflater inflater;

		public MyexpandableListAdapter(Context context) {
			this.context = context;
			inflater = LayoutInflater.from(context);
		}

		// 返回父列表个数
		@Override
		public int getGroupCount() {
			return groupList.size();
		}

		// 返回子列表个数
		@Override
		public int getChildrenCount(int groupPosition) {
			return childList.get(groupPosition).size();
		}

		@Override
		public Object getGroup(int groupPosition) {

			return groupList.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return childList.get(groupPosition).get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {

			return true;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			GroupHolder groupHolder = null;
			if (convertView == null) {
				groupHolder = new GroupHolder();
				convertView = inflater.inflate(R.layout.group, null);
				groupHolder.textView = (TextView) convertView.findViewById(R.id.group);
				groupHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
				convertView.setTag(groupHolder);
			} else {
				groupHolder = (GroupHolder) convertView.getTag();
			}

			groupHolder.textView.setText(((Group) getGroup(groupPosition)).getTitle());
			if (isExpanded)// ture is Expanded or false is not isExpanded
				groupHolder.imageView.setImageResource(R.drawable.bg_next);
			else
				groupHolder.imageView.setImageResource(R.drawable.bg_next1);
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			ChildHolder childHolder = null;
			if (convertView == null) {
				childHolder = new ChildHolder();
				convertView = inflater.inflate(R.layout.child, null);

				childHolder.textName = (TextView) convertView.findViewById(R.id.name);
				childHolder.textAge = (TextView) convertView.findViewById(R.id.age);
				childHolder.textAddress = (TextView) convertView.findViewById(R.id.address);
				childHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
				Button button = (Button) convertView.findViewById(R.id.button1);
				button.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						T.show(PinnedHeaderExpendActivity.this, "clicked pos=", 0);
					}
				});

				convertView.setTag(childHolder);
			} else {
				childHolder = (ChildHolder) convertView.getTag();
			}

			childHolder.textName.setText(((People) getChild(groupPosition, childPosition)).getName());
			childHolder.textAge.setText(String.valueOf(((People) getChild(groupPosition, childPosition)).getAge()));
			childHolder.textAddress.setText(((People) getChild(groupPosition, childPosition)).getAddress());

			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}

	@Override
	public boolean onGroupClick(final ExpandableListView parent, final View v, int groupPosition, final long id) {

		return false;
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		T.show(PinnedHeaderExpendActivity.this, childList.get(groupPosition).get(childPosition).getName(), 0);
		return false;
	}

	class GroupHolder {
		TextView textView;
		ImageView imageView;
	}

	class ChildHolder {
		TextView textName;
		TextView textAge;
		TextView textAddress;
		ImageView imageView;
	}

	@Override
	public View getPinnedHeader() {
		View headerView = (ViewGroup) getLayoutInflater().inflate(R.layout.group, null);
		headerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		return headerView;
	}

	@Override
	public void updatePinnedHeader(View headerView, int firstVisibleGroupPos) {
		Group firstVisibleGroup = (Group) adapter.getGroup(firstVisibleGroupPos);
		TextView textView = (TextView) headerView.findViewById(R.id.group);
		textView.setText(firstVisibleGroup.getTitle());
	}

	@Override
	public boolean giveUpTouchEvent(MotionEvent event) {
		if (expandableListView.getFirstVisiblePosition() == 0) {
			View view = expandableListView.getChildAt(0);
			if (view != null && view.getTop() >= 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 初始化事件
	 */
	private void initEvent() {
		adapter = new MyexpandableListAdapter(this);
		expandableListView.setAdapter(adapter);

		// 展开所有group
		for (int i = 0, count = expandableListView.getCount(); i < count; i++) {
			expandableListView.expandGroup(i);
		}

		expandableListView.setOnHeaderUpdateListener(this);
		expandableListView.setOnChildClickListener(this);
		expandableListView.setOnGroupClickListener(this);
		stickyLayout.setOnGiveUpTouchEventListener(this);
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		expandableListView = (PinnedHeaderExpandableListView) findViewById(R.id.expandablelist);
		stickyLayout = (StickyLayout) findViewById(R.id.sticky_layout);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {

		groupList = new ArrayList<Group>();
		Group group = null;
		for (int i = 0; i < 3; i++) {
			group = new Group();
			group.setTitle("group-" + i);
			groupList.add(group);
		}

		childList = new ArrayList<List<People>>();
		for (int i = 0; i < groupList.size(); i++) {
			ArrayList<People> childTemp;
			if (i == 0) {
				childTemp = new ArrayList<People>();
				for (int j = 0; j < 13; j++) {
					People people = new People();
					people.setName("yy-" + j);
					people.setAge(30);
					people.setAddress("sh-" + j);

					childTemp.add(people);
				}
			} else if (i == 1) {
				childTemp = new ArrayList<People>();
				for (int j = 0; j < 8; j++) {
					People people = new People();
					people.setName("ff-" + j);
					people.setAge(40);
					people.setAddress("sh-" + j);

					childTemp.add(people);
				}
			} else {
				childTemp = new ArrayList<People>();
				for (int j = 0; j < 23; j++) {
					People people = new People();
					people.setName("hh-" + j);
					people.setAge(20);
					people.setAddress("sh-" + j);

					childTemp.add(people);
				}
			}
			childList.add(childTemp);
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
}
