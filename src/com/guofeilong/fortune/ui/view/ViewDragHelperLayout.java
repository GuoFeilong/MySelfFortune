package com.guofeilong.fortune.ui.view;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.widget.ViewDragHelper;
import android.support.v4.widget.ViewDragHelper.Callback;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class ViewDragHelperLayout extends RelativeLayout {
	private ViewDragHelper mDragger;

	public ViewDragHelperLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mDragger = ViewDragHelper.create(this, 1.0f, new Callback() {

			@Override
			public boolean tryCaptureView(View child, int arg1) {
				return true;
			}

			@Override
			public int clampViewPositionHorizontal(View child, int left, int dx) {
				final int leftBound = getPaddingLeft();
				final int rightBound = getWidth() - child.getWidth() - leftBound;

				final int newLeft = Math.min(Math.max(left, leftBound), rightBound);

				return newLeft;
			}

			@Override
			public int clampViewPositionVertical(View child, int top, int dy) {
				final int topBound = getPaddingTop();
				final int bottomBound = getHeight() - child.getHeight() - topBound;
				final int newTop = Math.min(Math.max(top, topBound), bottomBound);
				return newTop;

			}

		});

	}

	public ViewDragHelperLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ViewDragHelperLayout(Context context) {
		this(context, null);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		return mDragger.shouldInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mDragger.processTouchEvent(event);
		return true;
	}

}
