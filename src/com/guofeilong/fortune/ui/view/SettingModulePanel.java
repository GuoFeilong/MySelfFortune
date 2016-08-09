package com.guofeilong.fortune.ui.view;

import java.util.ArrayList;

import com.guofeilong.fortune.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * 设置小模块
 * 
 * @author Android_Tian
 */
public class SettingModulePanel {

	public static final String TAG = "SettingModuleLayout";
	public String mName;
	private LinearLayout mMainLayout;
	private static LayoutInflater mInflater;
	private Context mContext;
	private ArrayList<SettingOption> mPanelList = new ArrayList<SettingOption>();

	public SettingModulePanel(Context context) {
		mContext = context;
		createLayout();
	}

	private void createLayout() {
		mInflater = LayoutInflater.from(mContext);
		mMainLayout = (LinearLayout) mInflater.inflate(R.layout.commond_view_settingmodule, null);
	}

	public View getView() {
		return mMainLayout;
	}

	public void removeAllPanel() {
		mMainLayout.removeAllViews();
		mPanelList.clear();
	}

	public void removePanel(SettingOption panel) {
		mMainLayout.removeView(panel.getView());
		mPanelList.remove(panel);
	}

	private ImageView getLine() {
		Resources resources = mContext.getResources();
		ImageView imageView = new ImageView(mContext);
		imageView.setBackgroundColor(mContext.getResources().getColor(R.color.conmmd_line));
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, (int) resources.getDimension(R.dimen.commond_line_height));
		imageView.setLayoutParams(lp);
		return imageView;
	}

	public void addOption(SettingOption s) {
		mMainLayout.addView(s.getView());
		if (mMainLayout.getChildCount() > 1) {
			mMainLayout.addView(getLine());
		}
		mPanelList.add(s);
	}

	public SettingOption getOption(int index) {
		return mPanelList.get(index);
	}

	public int size() {
		return mPanelList.size();
	}

	public void refresh() {
		boolean enable = ((SwitchOption) mPanelList.get(0)).selected;
		for (int i = 1, size = mPanelList.size(); i < size; i++) {
			SettingOption option = mPanelList.get(i);
			option.setEnable(enable);
		}
	}

	public void setEnable(boolean enable) {
		for (int i = 0, size = mPanelList.size(); i < size; i++) {
			SettingOption option = mPanelList.get(i);
			option.setEnable(enable);
		}
	}

	public int getOptionSize() {
		return mPanelList.size();
	}

	public static class SwitchOption extends SettingOption {
		public boolean selected;

		private TextView name;

		public SwitchOption() {
			view = mInflater.inflate(R.layout.commond_view_setting_switch, null);
			name = (TextView) view.findViewById(R.id.textview_name);
		}

		public void setName(String n) {
			name.setText(n);
		}

		public View getView() {
			if (selected) {
				((ImageView) view.findViewById(R.id.imageview_switch)).setImageResource(R.drawable.switch_open);
			} else {
				((ImageView) view.findViewById(R.id.imageview_switch)).setImageResource(R.drawable.switch_close);
			}
			return view;
		}

		void onSwitch() {
			// 选择
			if (selected) {
				((ImageView) view.findViewById(R.id.imageview_switch)).setImageResource(R.drawable.switch_close);
			} else {
				((ImageView) view.findViewById(R.id.imageview_switch)).setImageResource(R.drawable.switch_open);
			}
			selected = !selected;
		}

	}

	public static class SwitchOnClickListener implements OnClickListener {
		public SwitchOption pannel;

		public SwitchOnClickListener(SwitchOption s) {
			pannel = s;
		}

		@Override
		public void onClick(View v) {
			pannel.onSwitch();
		}
	}

	public static class SumOption extends SettingOption {
		protected TextView name;
		private TextView value;

		public SumOption() {
			view = getLayoutView();
			name = (TextView) view.findViewById(R.id.textview_name);
			value = (TextView) view.findViewById(R.id.textview_value);
		}

		View getLayoutView() {
			return mInflater.inflate(R.layout.commond_view_setting_sum, null);
		}

		public void setName(String n) {
			name.setText(n);
		}

		public void setValue(String n) {
			value.setText(n);
		}

		public View getView() {
			return view;
		}

	}

	/**
	 * budgetcontrol文本模块
	 * 
	 * @author guofl
	 * 
	 */
	public static class TextOptionForBudget extends SettingOption {
		protected TextView name;
		private TextView value;
		private boolean isArrowFlag;
		private ImageView arrow;

		public TextOptionForBudget(boolean isArrowFlag) {
			view = getLayoutView();
			name = (TextView) view.findViewById(R.id.tv_budget_control_created_text_itle);
			value = (TextView) view.findViewById(R.id.tv_budget_control_created_item_content);
			arrow = (ImageView) view.findViewById(R.id.iv_arrow);
			this.isArrowFlag = isArrowFlag;
			if (!isArrowFlag) {
				arrow.setVisibility(View.INVISIBLE);
			}
		}

		View getLayoutView() {
			return mInflater.inflate(R.layout.item_budget_control_created_new_text, null);
		}

		public void setName(String n) {
			name.setText(n);
		}

		public void setValue(String n) {
			value.setText(n);
		}

		public String getValue() {
			return value.getText().toString();
		}

		public View getView() {
			return view;
		}

		public void setValueHint(String hint) {
			value.setHint(hint);
		}
	}

	/**
	 * budgetcontrol复选框模块
	 * 
	 * @author guofl
	 * 
	 */
	public static class CheckOptionForBudget extends SettingOption {
		protected TextView name;
		CheckBox bar;

		public CheckOptionForBudget(boolean isArrowFlag) {
			view = getLayoutView();
			name = (TextView) view.findViewById(R.id.tv_budget_control_created_text_itle);
			bar = (CheckBox) view.findViewById(R.id.cb_is_service);
		}

		View getLayoutView() {
			return mInflater.inflate(R.layout.item_budget_control_created_new_checked, null);
		}

		public void setName(String n) {
			name.setText(n);
		}

		public void setChecked(boolean isChecked) {
			bar.setChecked(isChecked);
		}

		public View getView() {
			return view;
		}

		public boolean getCheckState() {
			return bar.isChecked();
		}

	}

	/**
	 * budgetcontrol的小模块
	 * 
	 * @author guofl
	 * 
	 */
	public static class ImageOptionForBudget extends SettingOption {
		protected TextView name;
		private CircleImageView value;
		private boolean isArrowFlag;
		private ImageView arrow;

		public ImageOptionForBudget(boolean isArrowFlag) {
			view = getLayoutView();
			name = (TextView) view.findViewById(R.id.tv_budget_control_created_imagetitle);
			value = (CircleImageView) view.findViewById(R.id.civ_budget_control_created_item_image);
			arrow = (ImageView) view.findViewById(R.id.recovery_online_data);
			this.isArrowFlag = isArrowFlag;
			if (!isArrowFlag) {
				arrow.setVisibility(View.INVISIBLE);
			}

		}

		View getLayoutView() {
			return mInflater.inflate(R.layout.item_budget_control_created_new_image, null);
		}

		public void setName(String n) {
			name.setText(n);
		}

		public void setImage(Bitmap resId) {
			value.setImageBitmap(resId);
		}

		public void removeImage(int resID) {
			value.setImageResource(resID);
		}

		public View getView() {
			return view;
		}

	}

	public static abstract class SettingOption {
		protected View view;

		abstract View getView();

		public void setOnClickListener(OnClickListener listener) {
			if (null != view) {
				view.setOnClickListener(listener);
			}
		}

		public void setEnable(boolean enable) {
			if (view != null) {
				view.setEnabled(enable);
			}
		}
	}

}
