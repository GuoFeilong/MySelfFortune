package com.guofeilong.fortune.ui.view;

import com.guofeilong.fortune.Console;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 
 * @ClassName: NumberBeatTextView
 * @Description:数字跳动
 * @date: 2014-10-30 下午5:46:02
 */
public class NumberBeatTextView extends TextView {

	private static final long BEAT_TIME = 800;
	private float mBeatNumber;
	private BeatRunnable mRunnable = new BeatRunnable();

	public NumberBeatTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 设置滚动数字
	 * 
	 * @param num
	 */
	public void beatNumber(float num) {
		mBeatNumber = num;
		if (mBeatNumber < 1) {
			setText(String.valueOf(mBeatNumber));
			return;
		}
		mRunnable.restart();
		new Thread(mRunnable).start();
	}

	/**
	 * 数字跳动
	 * 
	 */
	private class BeatRunnable implements Runnable {
		boolean running = true;

		@Override
		public void run() {
			float part = mBeatNumber / 50;
			float num = 0;
			while (running) {
				num = num + part;
				if (num > mBeatNumber) {
					num = mBeatNumber;
					running = false;
				}
				refreshNum(num);
				try {
					Thread.sleep(BEAT_TIME / 50);
				} catch (InterruptedException e) {
					Console.printThrowable(e);
				}
			}
		}

		/**
		 * 重新运行
		 */
		public void restart() {
			if (mRunnable.running) {
				mRunnable.running = false;
			}
			mRunnable.running = true;
		}
	}

	private static final int HANDLER_BEATNUM = 1;
	private Handler mBeatHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLER_BEATNUM:
				setText(String.valueOf(getFloatFormate(Float.parseFloat(msg.obj.toString()), 1)));
				break;

			default:
				break;
			}
		}
	};

	/**
	 * 保留小数点后几位
	 * 
	 * @param a
	 * @param num
	 * @return
	 */
	public static float getFloatFormate(float a, int num) {
		double c = Math.pow(10, num);
		float b = (float) (Math.round(a * c)) / (int) c;// (这里的100就是2位小数点,如果要其它位,如4位,这里两个100改成10000)
		return b;
	}

	public void refreshNum(float num) {
		Message message = mBeatHandler.obtainMessage();
		message.what = HANDLER_BEATNUM;
		message.obj = num;
		mBeatHandler.sendMessage(message);
	}
}
