package com.guofeilong.fortune.ui.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

public class DragRelativeLayout extends RelativeLayout implements OnGlobalLayoutListener {
	private int lastX, lastY;
	private int screenWidth;
	private int screenHeight;
	private int startLeft;
	private int startTop;
	private int startRight;
	private int startBottom;
	private RelativeLayout targetImageView;
	private Rect rect;
	private MyPoint moveCenterPoint;
	private MyPoint targetCenterPoint;
	private MyPoint centerPoint;
	private float tension = 0;
	private long dowmTime;
	private IDragImageViewOnclickListener mOnclickListener;
	private float distanceToRealPoint;

	public DragRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		getViewTreeObserver().addOnGlobalLayoutListener(this);

	}

	public void onResume() {
		moveToStart();
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {

		int ea = event.getAction();
		Log.i("TAG", "Touch:" + ea);
		switch (ea) {
		case MotionEvent.ACTION_DOWN:
			dowmTime = System.currentTimeMillis();
			lastX = (int) event.getRawX();
			lastY = (int) event.getRawY();
			break;

		case MotionEvent.ACTION_MOVE:
			long moveTime = System.currentTimeMillis();
			float moveActionTime = ((float) (moveTime - dowmTime));
			if (moveActionTime < 100) {
				break;
			}
			int dx = (int) event.getRawX() - lastX;
			int dy = (int) event.getRawY() - lastY;
			int left = getLeft() + dx;
			int top = getTop() + dy;
			int right = getRight() + dx;
			int bottom = getBottom() + dy;
			int centerX = (left + right) / 2;
			int centerY = (top + bottom) / 2;

			moveCenterPoint = new MyPoint(centerX, centerY);
			if (left < 0) {
				left = 0;
				right = left + getWidth();
			}

			if (right > screenWidth) {
				right = screenWidth;
				left = right - getWidth();
			}

			if (top < 0) {
				top = 0;
				bottom = top + getHeight();
			}

			if (bottom > screenHeight) {
				bottom = screenHeight;
				top = bottom - getHeight();
			}

			layout(left, top, right, bottom);

			lastX = (int) event.getRawX();
			lastY = (int) event.getRawY();
			break;
		case MotionEvent.ACTION_UP:
			long upTime = System.currentTimeMillis();
			float actionTime = ((float) (upTime - dowmTime));
			if (actionTime < 100) {
				if (mOnclickListener != null) {
					mOnclickListener.onDragImageViewOnclickListener(this);
				}
				break;
			}
			float speed = distanceToRealPoint / actionTime;
			if (speed > 1) {
				tension = speed * 2;
			} else {
				tension = 0;
			}
			long distance = 0;
			if (moveCenterPoint != null && targetCenterPoint != null) {
				distance = getDistance(moveCenterPoint, targetCenterPoint);
			}
			if (distance < (distanceToRealPoint / 3 * 2)) {
				animToTarget(moveCenterPoint, distance);
			} else {
				Animation animTranslate = animTranslate(moveCenterPoint, centerPoint, 1000, new AnticipateOvershootInterpolator(tension));
				animTranslate.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						DragRelativeLayout.this.clearAnimation();
						moveToStart();
					}
				});
				startAnimation(animTranslate);
			}
			break;
		}
		return super.onTouchEvent(event);
	}

	private int getDistance(MyPoint p1, MyPoint p2) {
		return (int) Math.sqrt(Math.pow((p2.x - p1.x), 2) + Math.pow(p2.y - p1.y, 2));
	}

	public void moveToStart() {
		layout(startLeft, startTop, startRight, startBottom);
	}

	public void animToTarget() {
		animToTarget(centerPoint, (long) distanceToRealPoint);
	}

	private void animToTarget(MyPoint p, long distance) {
		if (p == null) {
			return;
		}
		Animation animTranslate = animTranslate(p, targetCenterPoint, distance, null);
		animTranslate.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				DragRelativeLayout.this.clearAnimation();
				moveToTarget();
				if (mOnclickListener != null) {
					mOnclickListener.onDragImageViewMoveToTarget(DragRelativeLayout.this);
				}

			}
		});
		startAnimation(animTranslate);
	}

	private void moveToTarget() {
		int x = ((targetImageView.getRight() - targetImageView.getLeft()) - (this.getRight() - this.getLeft())) / 2;
		layout(targetImageView.getLeft() + x, targetImageView.getTop() + x, targetImageView.getRight() - x, targetImageView.getBottom() - x);
	}

	public void setTargetView(RelativeLayout targetImageView) {
		this.targetImageView = targetImageView;
	}

	/**
	 * 移动的动画
	 */
	protected Animation animTranslate(MyPoint fromPoint, MyPoint toPoint, long durationMillis, Interpolator interpolator) {
		TranslateAnimation anTransformation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, toPoint.x - fromPoint.x, Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, toPoint.y - fromPoint.y);
		anTransformation.setDuration(durationMillis);
		if (interpolator != null) {
			anTransformation.setInterpolator(interpolator);
		}

		return anTransformation;
	}

	public class MyPoint {
		public int x;
		public int y;

		public MyPoint(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}

		public MyPoint() {
			super();
		}

		@Override
		public String toString() {
			return "MyPoint [x=" + x + ", y=" + y + "]";
		}

	}

	@Override
	public void onGlobalLayout() {
		startLeft = getLeft();
		startTop = getTop();
		startRight = getRight();
		startBottom = getBottom();
		int centerX = (startLeft + startRight) / 2;
		int centerY = (startTop + startBottom) / 2;
		centerPoint = new MyPoint(centerX, centerY);
		screenWidth = ((RelativeLayout) getParent()).getWidth();
		screenHeight = ((RelativeLayout) getParent()).getHeight();
		rect = new Rect(targetImageView.getLeft(), targetImageView.getTop(), targetImageView.getRight(), targetImageView.getBottom());
		int targetCenterX = rect.centerX();
		int targetCenterY = rect.centerY();
		targetCenterPoint = new MyPoint(targetCenterX, targetCenterY);
		distanceToRealPoint = getDistance(centerPoint, targetCenterPoint);

	}

	public void setIOnclickListener(IDragImageViewOnclickListener mOnclickListener) {
		this.mOnclickListener = mOnclickListener;
	}

	public static interface IDragImageViewOnclickListener {
		public void onDragImageViewOnclickListener(View v);

		public void onDragImageViewMoveToTarget(View v);

	}

}
