package com.guofeilong.fortune.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class FinishBroadcastReceiver extends BroadcastReceiver {
	private FinishAppListener mFinishAppListener;

	public interface FinishAppListener {
		public void onFinish();

	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (mFinishAppListener != null) {
			mFinishAppListener.onFinish();
		}
	}

	public FinishAppListener getFinishAppListener() {
		return mFinishAppListener;
	}

	public void setFinishAppListener(FinishAppListener mFinishAppListener) {
		this.mFinishAppListener = mFinishAppListener;
	}

}