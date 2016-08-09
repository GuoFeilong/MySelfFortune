package com.guofeilong.fortune.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;

import com.ailk.android.jni.BspatchJNI;
import com.guofeilong.fortune.Console;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.ui.view.MyProgressDialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;


public class VersionUpdateTask extends AsyncTask<Object, Object, Integer> {
	private final String pathPrefix = (true == Environment.getExternalStorageState().equalsIgnoreCase(android.os.Environment.MEDIA_MOUNTED)) ? Environment.getExternalStorageDirectory().getPath() : "/data/data";

	private Context mContext;
	private MyProgressDialog mDialog;

	public void setContext(Context context) {
		mContext = context;
	}

	@Override
	protected void onPreExecute() {
		try {
			mDialog = ViewUtils.builderProgressDialog(mContext, mContext.getResources().getString(R.string.title_loading_client), mContext.getString(R.string.connecting), ProgressDialog.STYLE_HORIZONTAL, false, null);
			mDialog.show();
			try {
				String path = String.format("%s/%s/files", pathPrefix, mContext.getPackageName());
				File p = new File(path);
				if (true != p.exists()) {
					p.mkdirs();
				}
			} catch (Exception e) {
				Console.printThrowable(e);
			}

			try {
				String latestVersionFile = String.format("%s/%s/files/%s", pathPrefix, mContext.getPackageName(), "update.apk");
				File updateFile = new File(latestVersionFile);
				updateFile.delete();
			} catch (Exception e) {
				Console.printThrowable(e);
			}

			try {
				String patchFilePath = String.format("%s/%s/files/%s", pathPrefix, mContext.getPackageName(), "patch.bin");
				File patchFile = new File(patchFilePath);
				patchFile.delete();
			} catch (Exception e) {
				Console.printThrowable(e);
			}
		} catch (Exception e) {
			Console.printThrowable(e);
		}

		super.onPreExecute();
	}

	@Override
	protected Integer doInBackground(Object... params) {
		int result = 0;

		int updateType = 1;
		try {

			String updateUrlString = (String) params[0];
			String md5 = (String) params[1];
			updateType = (Integer) params[2];

			URL url = new URL(updateUrlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.connect();

			int totalLen = conn.getContentLength();
			InputStream in = conn.getInputStream();

			String downloadFilePath = null;
			if (0 == updateType || 1 == updateType) { // 增量升级文件
				downloadFilePath = String.format("%s/%s/files/%s", pathPrefix, mContext.getPackageName(), "patch.bin");
			} else { // 全量升级文件
				downloadFilePath = String.format("%s/%s/files/%s", pathPrefix, mContext.getPackageName(), "update.apk");
			}
			FileOutputStream out = new FileOutputStream(downloadFilePath);

			byte[] tmp = new byte[1024 * 8];
			int i = 0;
			int a = 0;

			while ((i = in.read(tmp)) != -1) {
				a += i;
				out.write(tmp, 0, i);
				publishProgress(totalLen, a);
			}

			if (0 == updateType || 1 == updateType) { // 增量升级
				File patchFile = new File(downloadFilePath);
				if (true == patchFile.exists()) {
					result = patch(downloadFilePath, md5);
				}
			}
		} catch (Exception e) {
			Console.printThrowable(e);

			result = -1;
		}

		if (0 < updateType && -1 == result) {
			result = -2;
		}

		return result;
	}

	@Override
	protected void onProgressUpdate(Object... values) {

		try {
			int total = (Integer) values[0];
			int current = (Integer) values[1];

			showUpdateProgress(total, current);

			if (current > 0) {
				// 设置的次数过多,效率不高
				setProgressDialogMessage(mContext.getString(R.string.data_loading));
			}
			if (total > 0 && total == current) {
				setProgressDialogMessage(mContext.getString(R.string.data_parsing));
			}
		} catch (Exception e) {
			Console.printThrowable(e);
		}
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(final Integer result) {
		try {
			dismissDialog();
			if (0 == result) {
				String updateFilePath = String.format("%s/%s/files/%s", pathPrefix, mContext.getPackageName(), "update.apk");
				softInstall(updateFilePath);
			} else {
				ViewUtils.showOkAlertDialog(mContext, mContext.getString(R.string.info_title), "升级失败", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						T.show(mContext, "更新失败", Toast.LENGTH_SHORT);
					}

				});
			}

		} catch (Exception e) {
			Console.printThrowable(e);
		}

		super.onPostExecute(result);
	}

	private int patch(final String patchFilePath, final String md5) {
		int result = 0;
		try {
			ApplicationInfo applicationInfo = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), 0);
			String currentVersionFile = applicationInfo.sourceDir;
			String latestVersionFile = String.format("%s/%s/files/%s", pathPrefix, mContext.getPackageName(), "update.apk");

			BspatchJNI t = new BspatchJNI();
			if (0 == (result = t.bspatch(currentVersionFile, latestVersionFile, patchFilePath))) {
				RandomAccessFile updateFile = new RandomAccessFile(latestVersionFile, "r");
				int length = (int) updateFile.length();
				byte[] data = new byte[length];
				updateFile.read(data);
				updateFile.close();

				String md5ValueString = md5(data);
				if (true != md5ValueString.equalsIgnoreCase(md5)) {
					result = -1;
				}
			}

			try {
				File patch = new File(patchFilePath);
				patch.delete();
			} catch (Exception e) {
				Console.printThrowable(e);
			}

		} catch (Exception e) {
			Console.printThrowable(e);

			result = -1;
		}

		return result;
	}

	private int softInstall(String path) {
		int result = 0;

		try {
			File file = new File(path);
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(android.content.Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
			mContext.startActivity(intent);
			((Activity) mContext).finish();
		} catch (Exception e) {
			Console.printThrowable(e);
		}

		return result;
	}

	/**
	 * 显示弹出框的进度
	 * 
	 * @param total
	 * @param received
	 */
	private void showUpdateProgress(int total, int received) {
		try {

			if (mDialog != null && total > 0) {
				// log("total::" + total + "::received::" + received);
				mDialog.setProgress(received);
				mDialog.setMax(total);
			}
		} catch (Exception e) {
			Console.printThrowable(e);
		}
	}

	/**
	 * 显示信息
	 * 
	 * @param message
	 */
	private void setProgressDialogMessage(String message) {
		if (mDialog != null) {
			mDialog.setMessage(message);
		}
	}

	private void dismissDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
		}
	}

	private static String md5(final byte[] data) {
		String md5ValueString = null;

		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(data);
			byte messageDigest[] = md5.digest();

			StringBuffer hexValue = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				int val = ((int) messageDigest[i]) & 0xff;
				if (val < 16) {
					hexValue.append("0");
				}
				hexValue.append(Integer.toHexString(val));
			}

			md5ValueString = hexValue.toString();
		} catch (Exception e) {
			Console.printThrowable(e);
		}

		return md5ValueString;
	}
}
