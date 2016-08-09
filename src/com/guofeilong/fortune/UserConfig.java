package com.guofeilong.fortune;

import java.util.Locale;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserConfig {
	private static final String LOACLE_LANGUAGE = "loacle_language";
	private static final String GUIDE_FLAG = "guide_flag";
	private static final String LOCK_PATTERN_FLAG = "lock_pattern_flag";
	private static final String LOCK_SET_PATTERN_FLAG = "lock_set_pattern_flag";
	private String SPName_UserConfig = "my_police";
	private Context mContext;
	private SharedPreferences sharedPreferences;
	private Editor edit;
	private String PER_RECEIVE = "per_receive";
	private static UserConfig mUserConfig;

	public static synchronized UserConfig getInstance(Context paramContext) {
		if (null == mUserConfig) {
			mUserConfig = new UserConfig(paramContext);
		}
		return mUserConfig;
	}

	private UserConfig(Context paramContext) {
		this.mContext = paramContext;
		this.sharedPreferences = this.mContext.getSharedPreferences(this.SPName_UserConfig, Context.MODE_WORLD_WRITEABLE | Context.MODE_WORLD_READABLE);
		this.edit = this.sharedPreferences.edit();
	}

	public boolean saveUserPerReceive(String resouceId, int unit, long per) {
		this.edit.putLong(resouceId + unit, per);
		return this.edit.commit();
	}

	public Object readOpenNotifiBarStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getCurrentSim() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void saveCurrentLanguage(String lan) {
		edit.putString(LOACLE_LANGUAGE, lan);
		edit.commit();
	}

	public String readCurrentLanguage() {
		String country = sharedPreferences.getString(LOACLE_LANGUAGE, Locale.CHINA.getCountry());
		return country;
	}

	public void saveGuideFlag(boolean flag) {
		edit.putBoolean(GUIDE_FLAG, flag);
		edit.commit();
	}

	public boolean readGuideFlag() {
		boolean flag = sharedPreferences.getBoolean(GUIDE_FLAG, false);
		return flag;
	}

	public void saveLockPatternFlag(boolean flag) {
		edit.putBoolean(LOCK_PATTERN_FLAG, flag);
		edit.commit();
	}

	public boolean readLockPatternFlag() {
		boolean flag = sharedPreferences.getBoolean(LOCK_PATTERN_FLAG, false);
		return flag;
	}
	
	public void saveSetLockPatternFlag(boolean flag) {
		edit.putBoolean(LOCK_SET_PATTERN_FLAG, flag);
		edit.commit();
	}
	
	public boolean readSetLockPatternFlag() {
		boolean flag = sharedPreferences.getBoolean(LOCK_SET_PATTERN_FLAG, false);
		return flag;
	}
	

}
