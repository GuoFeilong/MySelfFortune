package com.guofeilong.fortune.ui;

import com.guofeilong.fortune.Console;
import com.guofeilong.fortune.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RechargeFragment extends Fragment {
	public final static int SET_NEWSLIST = 77;
	private String mTestData;
	private int mColor;
	private Activity mActivity;
	private TextView mTestDaView;
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SET_NEWSLIST:
				mTestDaView.setTextColor(mColor);
				mTestDaView.setText(mTestData);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	public void setmTestData(String mTestData,int color) {
		this.mTestData = mTestData;
		this.mColor=color;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		this.mActivity = activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_recharge, container, false);
		mTestDaView = (TextView) view.findViewById(R.id.tv_test_data);
		Log.e("fsfsfsf", mTestData);
		return view;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {

		if (isVisibleToUser) {
			// fragment可见的时候加载数据
			if (mTestData != null) {
				Log.e("fsfsfsf", mTestData);
				mHandler.obtainMessage(SET_NEWSLIST).sendToTarget();
			} else {
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Thread.sleep(0);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						mHandler.obtainMessage(SET_NEWSLIST).sendToTarget();
					}
				}).start();
			}
		} else {

		}
		super.setUserVisibleHint(isVisibleToUser);

	}

}
