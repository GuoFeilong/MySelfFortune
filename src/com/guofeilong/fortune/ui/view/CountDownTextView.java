package com.guofeilong.fortune.ui.view;

import com.guofeilong.fortune.Console;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

public class CountDownTextView extends TextView implements Runnable {
	/**
	 * 正在倒计时
	 */
	private final int HANDLER_WHAT_COUNTING_BACKWARDS = 23;
	/**
	 * 倒计时结束
	 */
	private final int HANDLER_WHAT_COUNTING_BACKWARDS_OVER = 24;
	/**
	 * 倒计时总时间
	 */
	private int TIME_SECOND_TOTAL = 30;

	private boolean isStartBackWards = true;
	private IBackwardsOverListener mBackwardsOverListener;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLER_WHAT_COUNTING_BACKWARDS:
				setText(String.valueOf(msg.arg1));
				break;
			case HANDLER_WHAT_COUNTING_BACKWARDS_OVER:
				// setVisibility(View.INVISIBLE);
				if (mBackwardsOverListener != null) {
					mBackwardsOverListener.onBackwardsOver();
				}
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	public CountDownTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 开始倒计时
	 */
	public void startBackwardsThread() {
		if (mBackwardsOverListener != null) {
			mBackwardsOverListener.onBackwardsStart();
		}
		// setVisibility(View.VISIBLE);
		isStartBackWards = true;
		TIME_SECOND_TOTAL = 30;
		new Thread(this).start();
	}

	/**
	 * 停止倒计时
	 */
	public void stoppingBackwardsThread() {
		// setVisibility(View.INVISIBLE);
		isStartBackWards = false;
		TIME_SECOND_TOTAL = 30;
	}
	@Override
	public void run() {
		while (isStartBackWards) {
			Message msg = mHandler.obtainMessage();
			msg.arg1 = TIME_SECOND_TOTAL;
			if (TIME_SECOND_TOTAL >= 0) {
				msg.what = HANDLER_WHAT_COUNTING_BACKWARDS;
			} else {
				msg.what = HANDLER_WHAT_COUNTING_BACKWARDS_OVER;
			}
			mHandler.sendMessage(msg);

			// 倒数数
			TIME_SECOND_TOTAL--;
			if (TIME_SECOND_TOTAL < -1) {
				break;
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Console.printThrowable(e);
			}
		}

	}

	public IBackwardsOverListener getBackwardsOverListener() {
		return mBackwardsOverListener;
	}

	public void setBackwardsOverListener(IBackwardsOverListener mBackwardsOverListener) {
		this.mBackwardsOverListener = mBackwardsOverListener;
	}

	public interface IBackwardsOverListener {
		void onBackwardsOver();

		void onBackwardsStart();
	}
}
