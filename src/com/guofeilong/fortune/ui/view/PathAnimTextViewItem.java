package com.guofeilong.fortune.ui.view;

import com.guofeilong.fortune.R;
import com.guofeilong.fortune.ui.view.PathAnimMenu.MyPoint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

/**
 * @ClassName: PathAnimItem
 * @Description:旋转菜单 条目
 * @author: Android_Tian
 */
public class PathAnimTextViewItem extends TextView {
	private MyPoint startPoint;// 开始的点
	private MyPoint endPoint;// 结束的点
	private MyPoint nearPoint;// 近的点
	private MyPoint farPoint;// 远的点
	private String description;
	private int viewHeight;// 背景色的高度

	public PathAnimTextViewItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		setGravity(Gravity.CENTER);
		setTextColor(getResources().getColor(R.color.conmmd_textcolor));
		setTextSize(11);
		setSingleLine();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	@Override
	public void setBackgroundResource(int resid) {
		super.setBackgroundResource(resid);
		Drawable drawable = getBackground();
		viewHeight = drawable.getIntrinsicHeight();
		Log.i("PathAnimItem", "viewHeight=" + viewHeight);
	}

	public MyPoint getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(MyPoint startPoint) {
		this.startPoint = startPoint;
	}

	public MyPoint getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(MyPoint endPoint) {
		this.endPoint = endPoint;
	}

	public MyPoint getNearPoint() {
		return nearPoint;
	}

	public void setNearPoint(MyPoint nearPoint) {
		this.nearPoint = nearPoint;
	}

	public MyPoint getFarPoint() {
		return farPoint;
	}

	public void setFarPoint(MyPoint farPoint) {
		this.farPoint = farPoint;
	}

	public int getViewHeight() {
		return viewHeight;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
