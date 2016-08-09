package com.guofeilong.fortune;

import cn.jpush.android.api.JPushInterface;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;

public class MyFortuneApplication extends Application {

	public static String appId;

	public static String versionName = "1.0.0";

	public static String version = "100";

	public static String lang;

	public Typeface mNormalTypeface;

	@Override
	public void onCreate() {

		super.onCreate();
		setDebugOutput();
		CrashHandler.mCrashHandler.init(this);
		initApplication();
		
		// 程序启动的时候调用初始化推送服务
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
	}

	private void setDebugOutput() {
		try {
			ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
			int debugLevel = (Integer) appInfo.metaData.get("debug");
			if (0 == debugLevel) {
				Console.isPrint = false;
				Console.isPrintStackTrace = false;
			} else if (1 == debugLevel) {
				Console.isPrint = false;
				Console.isPrintStackTrace = true;
			} else if (2 == debugLevel) {
				Console.isPrint = true;
				Console.isPrintStackTrace = true;
			}
		} catch (Exception e) {
			Console.printThrowable(e);
		}
	}

	private void initApplication() {
		try {
			Context context = getApplicationContext();

			appId = context.getPackageName();

			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(appId, 0);
			versionName = packageInfo.versionName;
			version = String.valueOf(packageInfo.versionCode);

			ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
			lang = (String) appInfo.metaData.get("lang");

			mNormalTypeface = Typeface.createFromAsset(getAssets(), "fonts/lowvetica.ttf");
		} catch (Exception e) {
			Console.printThrowable(e);
		}
	}
}
