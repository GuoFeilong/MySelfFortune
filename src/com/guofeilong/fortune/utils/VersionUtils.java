package com.guofeilong.fortune.utils;

import java.io.File;
import java.util.HashMap;



import com.guofeilong.fortune.utils.ViewUtils.AutoDismissOnClickListener;

import android.R.string;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

public class VersionUtils {

	/**
	 * get application version code
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		int versionCode = 0;
		try {
			versionCode = context.getPackageManager().getPackageInfo("com.asiainfo.rtss", 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * install application package
	 * 
	 * @param context
	 * @param file
	 */
	protected void installApk(Context context, File file) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		// data and type
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");// 编者按：此处Android应为android，否则造成安装不了
		context.startActivity(intent);
	}

	public static void checkVersion(String oldVersion, String os, Message m) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("md5", "md5");
		map.put("type", "2");
		map.put("url", "http://124.207.3.44/upload/temp/Rtss_India_B115_high_version.apk");
		map.put("serverVersion", "123");
		m.obj = map;
		m.sendToTarget();
		// TODO 调用服务接口拿到final String md5, final int type, final String url

	}

	public static void updateWhichVersion(final String md5, final int type, final String url, final Context context) {
		switch (type) {
		// 强制更新
		case 1:
			ViewUtils.showYesDialogAutoDismiss(context, "Version Update", "Update", new AutoDismissOnClickListener() {

				@Override
				public void onYesClick() {
					updateVersion(md5, type, url, context);
				}

				@Override
				public void onNoClick() {
				}
			});
			break;
		// 非强制更新
		case 2:
			ViewUtils.showYesNoDialogAutoDismiss(context, "Version Update", "Update", "Cancel", new AutoDismissOnClickListener() {

				@Override
				public void onYesClick() {
					updateVersion(md5, type, url, context);
				}

				@Override
				public void onNoClick() {
				}
			});
			break;
		case 3:

			break;
		case 4:

			break;

		default:
			break;
		}

	}

	public static void updateVersion(String md5, int type, String url, Context context) {
		VersionUpdateTask task = new VersionUpdateTask();
		task.setContext(context);
		task.execute(url, md5, type);
	}
}
