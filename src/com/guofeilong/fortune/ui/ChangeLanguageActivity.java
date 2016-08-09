package com.guofeilong.fortune.ui;

import java.util.ArrayList;
import java.util.Locale;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.UserConfig;
import com.guofeilong.fortune.business.LanguageModel;
import com.guofeilong.fortune.utils.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChangeLanguageActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
	private int mColor;
	private String mTitle;
	private static final String LANGUAGE_LIST = "language_list";
	private ListView mChangeLanguageList;
	private ArrayList<LanguageModel> mLanguageData;
	private LanguageAdapter mLanguageAdapter;
	private LanguageModel mCurrentLanguage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_change_language);
		mTitle = getMyTitleAndColor();
		initNavigation();
		initData();
		initView();
		initEvent();
	}

	/**
	 * 初始化界面控件
	 */
	private void initEvent() {
		if (mLanguageData != null) {
			mLanguageAdapter = new LanguageAdapter(mLanguageData);
		}
		mChangeLanguageList.setAdapter(mLanguageAdapter);
		mChangeLanguageList.setOnItemClickListener(this);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mLanguageData = (ArrayList<LanguageModel>) Settings.getSettings(this).readDragedMenu(LANGUAGE_LIST);
		if (mLanguageData == null) {
			Locale locale = Locale.CHINA;
			mLanguageData = new ArrayList<LanguageModel>();
			LanguageModel model = new LanguageModel();
			model.setLanguageName("简体中文");
			model.setLocale(locale);
			model.setCheckedLanguageFlag(true);
			mLanguageData.add(model);

			locale = Locale.US;
			model = new LanguageModel();
			model.setLocale(locale);
			model.setLanguageName("English");
			model.setCheckedLanguageFlag(false);
			mLanguageData.add(model);

		}
	}

	/**
	 * 初始化事件
	 */
	private void initView() {
		mChangeLanguageList = (ListView) findViewById(R.id.lv_language_list);
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
		nameTextView.setText(getResources().getString(R.string.change_language));
		Button rightButton = (Button) findViewById(R.id.commond_imagebutton_title_Rightbutton);
		rightButton.setText(getResources().getString(R.string.change_language_save));
		rightButton.setOnClickListener(this);
		RelativeLayout leftButton = (RelativeLayout) findViewById(R.id.commond_imagebutton_title_leftbutton);
		leftButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.commond_imagebutton_title_leftbutton:
			finish();
			break;
		case R.id.commond_imagebutton_title_Rightbutton:
			if (mCurrentLanguage != null) {
				// 如果选择的语言和当前存储的语言一致直接关闭不发送广播
				String currentLanguage = UserConfig.getInstance(this).readCurrentLanguage();

				if (currentLanguage.equals(mCurrentLanguage.getLocale().getCountry())) {
					finish();
				} else {

					UserConfig.getInstance(this).saveCurrentLanguage(mCurrentLanguage.getLocale().getCountry());
					// 存储更改后的顺序
					Settings.getSettings(this).saveDargedMenu(LANGUAGE_LIST, mLanguageData);
					Intent intent = new Intent(BaseActivity.BROADCAST_FINISH);
					sendBroadcast(intent);
					startActivity(new Intent(ChangeLanguageActivity.this, MainActivity.class));
				}
			}
			finish();

			break;
		default:
			break;
		}
	}

	class LanguageHolder {
		TextView tvLanguageName;
		ImageView ivcheckedLanguageFlag;
	}

	/**
	 * 语言列表适配器
	 * 
	 * @author guofl
	 * 
	 */
	class LanguageAdapter extends BaseAdapter {
		private ArrayList<LanguageModel> languages;

		public LanguageAdapter(ArrayList<LanguageModel> languages) {
			super();
			this.languages = languages;
		}

		@Override
		public int getCount() {
			return languages.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LanguageHolder holder = null;
			if (convertView == null) {
				holder = new LanguageHolder();
				convertView = getLayoutInflater().inflate(R.layout.item_change_language, null);
				holder.tvLanguageName = (TextView) convertView.findViewById(R.id.tv_language_name);
				holder.ivcheckedLanguageFlag = (ImageView) convertView.findViewById(R.id.iv_language_check_flag);
				convertView.setTag(holder);
			} else {
				holder = (LanguageHolder) convertView.getTag();
			}
			setData2Position(languages, holder, position);
			return convertView;
		}

		private void setData2Position(ArrayList<LanguageModel> languages2, LanguageHolder holder, int position) {
			LanguageModel languageModel = languages2.get(position);
			holder.tvLanguageName.setText(languageModel.getLanguageName());
			boolean checkedLanguageFlag = languageModel.checkedLanguageFlag;
			if (checkedLanguageFlag) {
				holder.ivcheckedLanguageFlag.setVisibility(View.VISIBLE);
			} else {
				holder.ivcheckedLanguageFlag.setVisibility(View.GONE);
			}
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		for (int i = 0; i < mLanguageData.size(); i++) {
			mLanguageData.get(i).setCheckedLanguageFlag(false);
		}
		mCurrentLanguage = mLanguageData.get(position);
		mCurrentLanguage.setCheckedLanguageFlag(true);
		String country = mCurrentLanguage.getLocale().getCountry();
		mLanguageAdapter.notifyDataSetChanged();
	}
}
