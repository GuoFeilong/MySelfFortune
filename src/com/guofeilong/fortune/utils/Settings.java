package com.guofeilong.fortune.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Settings {

	/**
	 * 手势密码保护
	 */
	public static final String SETTING_PATTERN_PASSWORD = "SETTING_PATTERN_PASSWORD";
	/**
	 * 自动登录
	 */
	public static final String SETTING_AUTO_LOGIN = "SETTING_AUTO_LOGIN";

	/**
	 * 是否保存呢用户名密码
	 */
	public static final String SETTING_ISMEMORY_LOGIN = "SETTING_ISMEMORY_LOGIN";

	private static final String SETTING_NAME = "setting";

	private Context context;

	private SharedPreferences settings;
	private Editor settingEditor;

	private static Settings instance;

	public Settings(Context context) {
		this.context = context;
		settings = context.getSharedPreferences(SETTING_NAME, Context.MODE_PRIVATE);
		settingEditor = settings.edit();
	}

	synchronized public static Settings getSettings(Context context) {
		if (null == instance) {
			instance = new Settings(context);

			instance.init();
		}

		return instance;
	}

	private void init() {
		settings = context.getSharedPreferences(SETTING_NAME, Context.MODE_PRIVATE);
		settingEditor = settings.edit();
	}

	/**
	 * 手势密码保护
	 * 
	 * @return
	 */
	public boolean isPatternPasswordEnabled() {
		return settings.getBoolean(SETTING_PATTERN_PASSWORD, false);
	}

	public void enablePatternPassword() {
		settingEditor.putBoolean(SETTING_PATTERN_PASSWORD, true);
		settingEditor.commit();
	}

	public void disablePatternPassword() {
		settingEditor.putBoolean(SETTING_PATTERN_PASSWORD, false);
		settingEditor.commit();
	}

	/**
	 * 自动登录
	 */
	public boolean readAutoLoginStatus() {
		return settings.getBoolean(SETTING_AUTO_LOGIN, false);
	}

	public void writeAutoLoginStatus(boolean status) {
		settingEditor.putBoolean(SETTING_AUTO_LOGIN, status);
		settingEditor.commit();
	}

	/**
	 * 保存密码
	 */
	public void savePerson(String username, String password) {
		settingEditor.putString("username", username);
		settingEditor.putString("password", password);
		settingEditor.commit();
	}

	public String getUserName() {
		return settings.getString("username", null);
	}

	public String getPassWord() {
		return settings.getString("password", null);
	}

	/**
	 * 是否记住密码
	 */
	public boolean isMemoryLoginEnabled() {
		return settings.getBoolean(SETTING_ISMEMORY_LOGIN, false);
	}

	public void saveMemoryLogin(boolean isLogin) {
		settingEditor.putBoolean(SETTING_ISMEMORY_LOGIN, isLogin);
		settingEditor.commit();
	}

	/**
	 * 存储和读取更改后的九宫格菜单名称
	 */
	public void saveDargedMenu(String key, Object object) {
		Tools.writeObject(context, key, object);
	}

	public Object readDragedMenu(String key) {
		Object readObject = Tools.readObject(context, key);
		return readObject;
	}
}
