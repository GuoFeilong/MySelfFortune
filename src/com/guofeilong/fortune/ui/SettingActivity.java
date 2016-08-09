package com.guofeilong.fortune.ui;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.UserConfig;
import com.guofeilong.fortune.ui.view.SettingModulePanel;
import com.guofeilong.fortune.ui.view.SettingModulePanel.CheckOptionForBudget;
import com.guofeilong.fortune.ui.view.SettingModulePanel.ImageOptionForBudget;
import com.guofeilong.fortune.ui.view.SettingModulePanel.SumOption;
import com.guofeilong.fortune.ui.view.SettingModulePanel.SwitchOnClickListener;
import com.guofeilong.fortune.ui.view.SettingModulePanel.SwitchOption;
import com.guofeilong.fortune.ui.view.SettingModulePanel.TextOptionForBudget;
import com.guofeilong.fortune.utils.T;
import com.guofeilong.fortune.utils.ViewUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class SettingActivity extends BaseActivity implements OnClickListener {
	private int mColor;
	private String mTitle;
	private LinearLayout mMainLayout;
	private int mCommondViewMartinTop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting);
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
		initFirstModulePanel();
		initSecondModulePanel();
		initThirdModulePanel();
		initStyleModulePanel();
	}

	private void initStyleModulePanel() {

		final SettingModulePanel s4 = new SettingModulePanel(this);
		final TextOptionForBudget style2 = new TextOptionForBudget(true);
		style2.setName("style2_name");
		style2.setValue("Setting Panne Style");

		final TextOptionForBudget style3 = new TextOptionForBudget(false);
		style3.setName("style3_name");
		style3.setValue("style3_value");

		final CheckOptionForBudget style4 = new CheckOptionForBudget(true);
		style4.setChecked(true);
		style4.setName("style4_name");

		ImageOptionForBudget style5 = new ImageOptionForBudget(true);
		style5.setName("style5_name");

		s4.addOption(style2);
		s4.addOption(style3);
		s4.addOption(style4);
		s4.addOption(style5);
		mMainLayout.addView(s4.getView(), getCommondParams());

	}

	private void initFirstModulePanel() {
		final SettingModulePanel s1 = new SettingModulePanel(this);

		final SumOption versionUpdate = new SumOption();
		versionUpdate.setName(getResources().getString(R.string.setting_version_update));
		versionUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// changeActivity(VersionUpdateActivity.class);
			}
		});
		s1.addOption(versionUpdate);
		mMainLayout.addView(s1.getView(), getCommondParams());
	}

	private void initSecondModulePanel() {
		final SettingModulePanel s2 = new SettingModulePanel(this);
		final SumOption languageData = new SumOption();
		languageData.setName(getResources().getString(R.string.setting_language));
		s2.addOption(languageData);
		languageData.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ViewUtils.changeActivity(SettingActivity.this, ChangeLanguageActivity.class);
			}
		});
		final SumOption dndData = new SumOption();
		dndData.setName(getResources().getString(R.string.setting_dnd));
		s2.addOption(dndData);
		mMainLayout.addView(s2.getView(), getCommondParams());

	}

	private void initThirdModulePanel() {
		final SettingModulePanel s3 = new SettingModulePanel(this);

		final SwitchOption patternSwitch = new SwitchOption();
		patternSwitch.selected = UserConfig.getInstance(this).readLockPatternFlag();
		patternSwitch.setName(getResources().getString(R.string.lockpattern));
		patternSwitch.setOnClickListener(new SettingModulePanel.SwitchOnClickListener(patternSwitch) {

			@Override
			public void onClick(View v) {
				super.onClick(v);
				if (UserConfig.getInstance(SettingActivity.this).readSetLockPatternFlag()) {
					UserConfig.getInstance(SettingActivity.this).saveLockPatternFlag(patternSwitch.selected);
				} else if (!UserConfig.getInstance(SettingActivity.this).readSetLockPatternFlag() && pannel.selected) {
					T.show(SettingActivity.this, "please set pattern first", 0);
					patternSwitch.getView().performClick();
				}
			}
		});

		final SumOption about = new SumOption();
		about.setName(getResources().getString(R.string.setting_about));
		about.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ViewUtils.changeActivity(SettingActivity.this, PatternSettingActivity.class);
			}
		});

		s3.addOption(about);
		s3.addOption(patternSwitch);
		mMainLayout.addView(s3.getView(), getCommondParams());

	}

	private LayoutParams getCommondParams() {
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.topMargin = mCommondViewMartinTop;
		return layoutParams;
	}

	/**
	 * 初始化控件
	 */
	private void initView() {

		mMainLayout = (LinearLayout) findViewById(R.id.linearlayout_main);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mCommondViewMartinTop = (int) getResources().getDimension(R.dimen.protect_setting_margin);
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
		nameTextView.setText(getResources().getString(R.string.setting_title));
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
