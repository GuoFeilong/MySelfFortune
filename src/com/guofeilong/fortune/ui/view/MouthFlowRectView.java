package com.guofeilong.fortune.ui.view;

import com.guofeilong.fortune.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 柱状图方块
 * 
 * @ClassName: MouthFlowRectView
 * @Description:TODO
 * @author: Android_Tian
 * @date: 2014-12-17 下午7:09:27
 */
public class MouthFlowRectView extends LinearLayout {

	private View rectLayout;
	private ImageView rectColor;
	private TextView rectName;

	public MouthFlowRectView(Context context, AttributeSet attrs) {
		super(context, attrs);
		rectLayout = LayoutInflater.from(context).inflate(R.layout.item_traffic_mouth_flow, this, true);
		rectColor = (ImageView) rectLayout.findViewById(R.id.iv_rect_normal);
		rectName = (TextView) rectLayout.findViewById(R.id.tv_rect_text);
	}

	public MouthFlowRectView(Context context) {
		super(context);
		rectLayout = LayoutInflater.from(context).inflate(R.layout.item_traffic_mouth_flow, this, true);
		rectColor = (ImageView) rectLayout.findViewById(R.id.iv_rect_normal);
		rectName = (TextView) rectLayout.findViewById(R.id.tv_rect_text);
	}

	public void setRextColorAndName(int color, String name) {
		rectColor.setBackgroundColor(color);
		if (name != null) {
			rectName.setText(name);
		}
	}

}
