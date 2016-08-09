package com.guofeilong.fortune.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



import com.guofeilong.fortune.ui.view.PatternSettingView;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

public class PatternPasswordUtils {

	// private static final String TAG = "LockPatternUtils";
	private final static String KEY_LOCK_PWD = "lock_pwd";

	private static Context mContext;

	private static SharedPreferences preference;

	// private final ContentResolver mContentResolver;

	public PatternPasswordUtils(Context context) {
		mContext = context;
		preference = mContext.getSharedPreferences(KEY_LOCK_PWD, Context.MODE_PRIVATE);
		// mContentResolver = context.getContentResolver();
	}

	/**
	 * Deserialize a pattern.
	 * 
	 * @param string
	 *            The pattern serialized with {@link #patternToString}
	 * @return The pattern.
	 */
	public static List<PatternSettingView.Cell> stringToPattern(String string) {
		List<PatternSettingView.Cell> result = new ArrayList<PatternSettingView.Cell>();

		final byte[] bytes = string.getBytes();
		for (int i = 0; i < bytes.length; i++) {
			byte b = bytes[i];
			result.add(PatternSettingView.Cell.of(b / 3, b % 3));
		}
		return result;
	}

	/**
	 * Serialize a pattern.
	 * 
	 * @param pattern
	 *            The pattern.
	 * @return The pattern in string form.
	 */
	public static String patternToString(List<PatternSettingView.Cell> pattern) {
		if (pattern == null) {
			return "";
		}
		final int patternSize = pattern.size();

		byte[] res = new byte[patternSize];
		for (int i = 0; i < patternSize; i++) {
			PatternSettingView.Cell cell = pattern.get(i);
			res[i] = (byte) (cell.getRow() * 3 + cell.getColumn());
		}
		return Arrays.toString(res);
	}

	public void saveLockPattern(List<PatternSettingView.Cell> pattern) {
		Editor editor = preference.edit();
		editor.putString(KEY_LOCK_PWD, patternToString(pattern));
		editor.commit();
	}

	public String getLockPaternString() {
		return preference.getString(KEY_LOCK_PWD, "");
	}

	public int checkPattern(List<PatternSettingView.Cell> pattern) {
		String stored = getLockPaternString();
		if (!TextUtils.isEmpty(stored)) {
			return stored.equals(patternToString(pattern)) ? 1 : 0;
		}
		return -1;
	}

	public void clearLock() {
		saveLockPattern(null);
	}

}
