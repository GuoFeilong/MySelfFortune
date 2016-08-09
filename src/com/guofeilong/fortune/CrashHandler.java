package com.guofeilong.fortune;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

/**
 * 
 * 崩溃捕获
 * 
 * @author lvming
 */
public class CrashHandler implements UncaughtExceptionHandler {

	public static CrashHandler mCrashHandler = new CrashHandler();

	private Context mContext;

	private CrashHandler() {

	}

	public void init(Context context) {
		try {
			mContext = context.getApplicationContext();
			Thread.setDefaultUncaughtExceptionHandler(this);
		} catch (Exception e) {
			Console.printThrowable(e);
		}
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		try {
			ex.printStackTrace();
			Map<String, String> infos = new HashMap<String, String>();

			collectDeviceInfo(infos);
			saveCrashInfo2File(infos, ex);
			android.os.Process.killProcess(android.os.Process.myPid());
		} catch (Exception e) {
			Console.printThrowable(e);
		}
	}

	/**
	 * 收集设备参数信息
	 * 
	 * @param ctx
	 */
	private void collectDeviceInfo(Map<String, String> infos) {
		try {
			PackageManager pm = mContext.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (null != pi) {
				String versionName = pi.versionName == null ? "null" : pi.versionName;
				String versionCode = String.valueOf(pi.versionCode);
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}

			Field[] fields = Build.class.getDeclaredFields();
			for (Field field : fields) {
				try {
					field.setAccessible(true);
					infos.put(field.getName(), field.get(null).toString());

					Console.debug(String.format("CrashHandler::collectDeviceInfo:%s=%s", field.getName(), field.get(null)));
				} catch (Exception e) {
					Console.printThrowable(e);
				}
			}
		} catch (Exception e) {
			Console.printThrowable(e);
		}
	}

	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return 返回文件名称,便于将文件传送到服务器
	 */
	private void saveCrashInfo2File(Map<String, String> infos, Throwable ex) {

		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}

		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();

		String result = writer.toString();
		sb.append(result);
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

			long timestamp = System.currentTimeMillis();
			String time = formatter.format(new Date());
			String fileName = "crash-" + time + "-" + timestamp + ".log";
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				String path = Environment.getExternalStorageDirectory().getPath() + "/crash/";
				File dir = new File(path);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(path + fileName);
				fos.write(sb.toString().getBytes());
				fos.close();
			}
		} catch (Exception e) {
			Console.printThrowable(e);
		}
	}
}
