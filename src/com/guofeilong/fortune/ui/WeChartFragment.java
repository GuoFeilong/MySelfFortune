package com.guofeilong.fortune.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class WeChartFragment extends Fragment {
	private String mCurrentTtitl;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	public WeChartFragment(String mCurrentTtitl) {
		super();
		this.mCurrentTtitl = mCurrentTtitl;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		TextView textView = new TextView(getActivity());
		textView.setText(mCurrentTtitl);
		textView.setTextSize(15);
		return textView;
	}
}
