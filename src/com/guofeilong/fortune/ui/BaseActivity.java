package com.guofeilong.fortune.ui;

import java.io.Serializable;
import java.util.Locale;

import cn.jpush.android.api.InstrumentedActivity;

import com.guofeilong.fortune.UserConfig;
import com.guofeilong.fortune.broadcastreceiver.FinishBroadcastReceiver;
import com.guofeilong.fortune.broadcastreceiver.FinishBroadcastReceiver.FinishAppListener;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;

public class BaseActivity extends InstrumentedActivity implements FinishAppListener {
	private FinishBroadcastReceiver finishBroadcastReceiver;
	public static final String BROADCAST_FINISH = "BROADCAST_FINISH";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 国际化更改语言
		String language = UserConfig.getInstance(this).readCurrentLanguage();
		Configuration config = getResources().getConfiguration();
		if (language.equals(Locale.CHINA.getCountry())) {
			config.locale = Locale.CHINA;
		} else if (language.equals(Locale.US.getCountry())) {
			config.locale = Locale.US;

		}
		getResources().updateConfiguration(config, getResources().getDisplayMetrics());
		super.onCreate(savedInstanceState);
		finishBroadcastReceiver = new FinishBroadcastReceiver();
		finishBroadcastReceiver.setFinishAppListener(this);
		registerReceiver(finishBroadcastReceiver, new IntentFilter(BROADCAST_FINISH));
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(finishBroadcastReceiver);
		super.onDestroy();
	}

	@Override
	public void onFinish() {
		finish();
	}

}
