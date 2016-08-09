package com.guofeilong.fortune;

import com.guofeilong.fortune.utils.T;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Console {
	private static final String TAG = "RTSS";

	/**
	 * 日志输出
	 * 
	 */

	private Console() {
	}

	public static boolean isPrint = true;
	public static boolean isPrintStackTrace = true;

	/**
	 * print message
	 * 
	 * @param msg
	 */
	public static void print(String msg) {
		if (isPrint) {
			System.out.print(msg);
		}
	}

	/**
	 * print message
	 * 
	 * @param msg
	 */
	public static void toastMessage(Context context, String msg) {
		if (isPrint) {
			T.showShort(context, msg);
		}
	}

	/**
	 * print debug message
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void debug(String tag, String msg) {
		if (isPrint) {
			Log.d(tag, msg);
		}
	}

	/**
	 * print error message
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void error(String tag, String msg) {
		if (isPrint) {
			Log.e(tag, msg);
		}
	}

	/**
	 * print info message
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void info(String tag, String msg) {
		if (isPrint) {
			Log.i(tag, msg);
		}
	}

	/**
	 * print warn message
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void warn(String tag, String msg) {
		if (isPrint) {
			Log.w(tag, msg);
		}
	}

	/**
	 * print verbose message
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void verbose(String tag, String msg) {
		if (isPrint) {
			Log.v(tag, msg);
		}
	}

	/**
	 * printStackTrace exception message
	 * 
	 * @param t
	 */
	public static void printThrowable(Throwable e) {
		if (isPrintStackTrace) {
			e.printStackTrace();
		}
	}

	public static void debug(String string) {
		if (isPrint) {
			Log.d(TAG, string);
		}
	}
}
