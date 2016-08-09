package com.guofeilong.fortune.ui.view;

import java.text.DecimalFormat;
import java.text.NumberFormat;



import com.guofeilong.fortune.R;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface.OnCancelListener;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 
 * @Description: 提示框
 * @author jingwen
 * @date 2011-8-9 下午2:50:22
 */
public class MyProgressDialog {

	private AlertDialog.Builder alertDialogBuilder;

	private AlertDialog alertDialog;

	public static final int STYLE_SPINNER = ProgressDialog.STYLE_SPINNER;

	public static final int STYLE_HORIZONTAL = ProgressDialog.STYLE_HORIZONTAL;

	private ProgressBar mProgress;
	/**
	 * 信息
	 */
	private TextView mMessageView;

	private int mProgressStyle;
	/**
	 * 提示框内容
	 */
	private String mMessage;
	/**
	 * 提示框标题
	 */
	private String mTitle;
	/**
	 * 
	 */
	private OnCancelListener onCancelListener;
	/**
	 * 是否能按撤销键取消提示框
	 */
	private Boolean cancelable;

	/**
	 * %s/%s
	 */
	private TextView mProgressNumber;
	/**
	 * 百分比
	 */
	private TextView mProgressPercent;
	private String mProgressNumberFormat;
	private NumberFormat mProgressPercentFormat;
	private DecimalFormat formatter;
	private int mMax;
	private int mProgressVal;

	public Handler mViewUpdateHandler;
	Context context;

	public MyProgressDialog(Context context, String title, String message, int progressStyle, Boolean cancelable) {
		this(context, title, message, progressStyle, cancelable, null);
	}

	public MyProgressDialog(Context context, String title, String message, int progressStyle, Boolean cancelable, OnCancelListener onCancelListener) {
		this(context);
		// 记住这些初始参数
		// if (progressStyle == STYLE_HORIZONTAL)
		mProgressStyle = progressStyle;
		if (title != null)
			mTitle = title;
		if (message != null)
			mMessage = message;
		if (onCancelListener != null)
			this.onCancelListener = onCancelListener;
		this.cancelable = cancelable;
		init(title, message, mProgressStyle, cancelable, onCancelListener);
	}

	/**
	 * 
	 * @Description: 初始化提示框
	 * @author jingwen
	 * @date 2011-8-9 下午2:53:31
	 * @param title
	 * @param message
	 * @param progressStyle
	 * @param cancelable
	 * @param onCancelListener
	 */
	private void init(String title, String message, int progressStyle, Boolean cancelable, OnCancelListener onCancelListener) {
		alertDialog = alertDialogBuilder.create();
		setProgressStyle(mProgressStyle);
		if (title != null) {
			setTitle(title);
		}
		if (message != null) {
			setMessage(message);
		}
		if (onCancelListener != null) {
			setOnCancelListener(onCancelListener);
		}
		setCancelable(cancelable);
	}

	public MyProgressDialog(Context context) {
		alertDialog = null;
		this.context = context;
		alertDialogBuilder = new AlertDialog.Builder(context);
		LayoutInflater inflater = LayoutInflater.from(context);
		mProgressStyle = STYLE_SPINNER;
		View view = inflater.inflate(R.layout.view_progress_dialog, null);
		mProgress = (ProgressBar) view.findViewById(R.id.progress);
		mMessageView = (TextView) view.findViewById(R.id.message);
		alertDialogBuilder.setView(view);
		// View view = inflater.inflate(R.layout.alert_dialog_progress, null);
		// mMessageView = (TextView) view.findViewById(R.id.message);
		// mProgress = (ProgressBar) view.findViewById(R.id.progress);
		// mProgressNumber = (TextView) view.findViewById(R.id.progress_number);
		// mProgressPercent = (TextView)
		// view.findViewById(R.id.progress_percent);
		// builder.setView(view);

		mProgressNumberFormat = "%s/%s";
		mProgressPercentFormat = NumberFormat.getPercentInstance();
		mProgressPercentFormat.setMaximumFractionDigits(0);
		formatter = new DecimalFormat();
		mViewUpdateHandler = new Handler() {// 可以考虑拿掉，把更新方法直接放在相应的set方法
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				upDateProgress();
			}
		};
	}

	private void upDateProgress() {
		int progress = mProgress.getProgress();
		int max = mProgress.getMax();
		double percent = (double) progress / (double) max;
		String format = mProgressNumberFormat;
		String progress2 = filesize(progress);
		String max2 = filesize(max);
		mProgressNumber.setText(String.format(format, progress2, max2));
		// mProgressNumber.setText(String.format(format, progress,
		// max));
		SpannableString tmp = new SpannableString(mProgressPercentFormat.format(percent));
		tmp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, tmp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mProgressPercent.setText(tmp);
	}

	private void onProgressChanged() {
		// if (mProgressStyle == STYLE_HORIZONTAL) {
		// mViewUpdateHandler.sendEmptyMessage(0);
		// }
		upDateProgress();
	}

	public void setProgress(int value) {
		if (mProgressStyle == STYLE_HORIZONTAL) {
			mProgress.setProgress(value);
			onProgressChanged();
		}

	}

	public void setMax(int max) {
		if (mProgress != null) {
			mProgress.setMax(max);
			onProgressChanged();
		} else {
			mMax = max;
		}
	}

	private String filesize(long sizeSrc) {
		long size = sizeSrc;
		String str = "";
		if (size > 1024) {
			str = "KB";
			size /= 1024;
			if (size >= 1024) {
				str = "M";
				size /= 1024;
			}
		} else {
			str = "B";
		}
		formatter.setGroupingSize(3);
		// log("fomat:::::" + formatter.format(size) + str);
		return formatter.format(size) + str;
	}

	public int getProgress() {
		if (mProgress != null) {
			return mProgress.getProgress();
		}
		return mProgressVal;
	}

	public int getMax() {
		if (mProgress != null) {
			return mProgress.getMax();
		}
		return mMax;
	}

	public void show() {
		if (alertDialog == null) {
			alertDialog = alertDialogBuilder.create();
			WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
			lp.alpha = 0.8f;
			alertDialog.getWindow().setAttributes(lp);
		}
		alertDialog.show();
	}

	/**
	 * 用初始状态重新显示
	 */
	public void reShow() {
		dismiss();
		init(mTitle, mMessage, mProgressStyle, cancelable, onCancelListener);
		show();
	}

	/**
	 * 
	 * @Description: 设置提示标题
	 * @author jingwen
	 * @date 2011-8-9 下午2:47:05
	 * @param str
	 */
	public void setTitle(String str) {
		if (alertDialog == null) {
			alertDialogBuilder.setTitle(str);
		} else {
			alertDialog.setTitle(str);
		}
	}

	/**
	 * 
	 * @Description: 是否能取消提示框
	 * @author jingwen
	 * @date 2011-8-9 下午2:47:48
	 * @param cancelable
	 */
	public void setCancelable(Boolean cancelable) {
		if (alertDialog == null) {
			alertDialogBuilder.setCancelable(cancelable);
		} else {
			alertDialog.setCancelable(cancelable);
		}
	}

	/**
	 * 
	 * @Description: 更改提示框的文字
	 * @author jingwen
	 * @date 2011-8-9 下午2:45:32
	 * @param message
	 */
	public void setMessage(CharSequence message) {
		if (mMessageView != null) {
			mMessageView.setText(message);
		}
	}

	/**
	 * 
	 * @Description: 提示框的风格
	 * @author jingwen
	 * @date 2011-8-9 下午2:46:07
	 * @param style
	 */
	public void setProgressStyle(int style) {
		if (mProgressStyle == style) {
			return;
		}
		mProgressStyle = style;
		LayoutInflater inflater = LayoutInflater.from(context);
		if (mProgressStyle == STYLE_HORIZONTAL) {
			View view = inflater.inflate(R.layout.view_alert_dialog_progress, null);
			mMessageView = (TextView) view.findViewById(R.id.message);
			mProgress = (ProgressBar) view.findViewById(R.id.progress);
			mProgress.setIndeterminate(false);
			mProgressNumber = (TextView) view.findViewById(R.id.progress_number);
			mProgressPercent = (TextView) view.findViewById(R.id.progress_percent);
			alertDialogBuilder.setView(view);
		} else {
			View view = inflater.inflate(R.layout.view_progress_dialog, null);
			mProgress = (ProgressBar) view.findViewById(R.id.progress);
			mMessageView = (TextView) view.findViewById(R.id.message);
			alertDialogBuilder.setView(view);
		}
	}

	/**
	 * 
	 * @Description: 不显示提示框
	 * @author jingwen
	 * @date 2011-8-9 下午2:46:33
	 */
	public void dismiss() {
		if (alertDialog != null)
			alertDialog.dismiss();

	}

	public void setOnCancelListener(OnCancelListener onCancelListener) {
		if (alertDialog == null) {
			alertDialogBuilder.setOnCancelListener(onCancelListener);
		} else {
			alertDialog.setOnCancelListener(onCancelListener);
		}

	}

}
