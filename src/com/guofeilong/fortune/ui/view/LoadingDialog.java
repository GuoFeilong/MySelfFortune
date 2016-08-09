package com.guofeilong.fortune.ui.view;

import com.guofeilong.fortune.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;


public class LoadingDialog extends Dialog {

	private TextView mTextView;
	private String mLoadingText;

	public LoadingDialog(Context context) {
		super(context, R.style.Translucent_NoTitle);
	}

	public LoadingDialog(Context context, boolean cancelable) {
		super(context, R.style.Translucent_NoTitle);
		this.setCancelable(cancelable);
	}

	public LoadingDialog(Context context, boolean cancelable, String loadingText) {
		super(context, R.style.Translucent_NoTitle);
		this.setCancelable(cancelable);
		this.mLoadingText = loadingText;

	}

	protected LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_loading);
		mTextView = (TextView) findViewById(R.id.dialog_txt);
		if (TextUtils.isEmpty(mLoadingText)) {
			mLoadingText = "loading...";
		}
		mTextView.setText(mLoadingText);
	}

	@Override
	public void show() {
		super.show();
	}

	@Override
	public void cancel() {
		super.cancel();
	}
}
