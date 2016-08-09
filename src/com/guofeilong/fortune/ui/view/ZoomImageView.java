package com.guofeilong.fortune.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;

public class ZoomImageView extends ImageView implements OnGlobalLayoutListener, OnScaleGestureListener, OnTouchListener {
	private boolean mOnce;
	private float mInitScale;
	private float mMidScale;
	private float mMaxScale;
	private Matrix matrix;
	private ScaleGestureDetector mDetector;

	public ZoomImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		matrix = new Matrix();
		setScaleType(ScaleType.MATRIX);
		setOnTouchListener(this);
		mDetector = new ScaleGestureDetector(context, this);
	}

	public ZoomImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ZoomImageView(Context context) {
		this(context, null);
	}

	@Override
	public void onGlobalLayout() {
		// 获取imageview加载完成的图片
		if (!mOnce) {
			// 获取控件的宽高
			int hight = getHeight();
			int width = getWidth();

			// 获取图片的宽高
			Drawable drawable = getDrawable();
			int dh = 0;
			int dw = 0;
			// 对比
			float sacle = 1.0f;
			if (drawable == null) {
				return;
			} else {
				// 图片的宽高
				dh = drawable.getIntrinsicHeight();
				dw = drawable.getIntrinsicWidth();

				if ((dw > width && dh < hight) || (dw < width && dh > hight)) {
					sacle = width * 1.0f / dw;
				}
				if ((dw > width && dh > hight) || (dw < width && dh < hight)) {
					sacle = Math.min(width * 1.0f / dw, hight * 1.0f / dh);
				}

			}
			mInitScale = sacle;
			mMaxScale = mInitScale * 4;
			mMidScale = mInitScale * 2;
			// 移动图片到控件的中心
			int dx = getWidth() / 2 - dw / 2;
			int dy = getHeight() / 2 - dh / 2;

			matrix.postTranslate(dx, dy);
			matrix.postScale(mInitScale, mInitScale, getWidth() / 2, getHeight() / 2);
			setImageMatrix(matrix);

			mOnce = true;
		}

	}

	@Override
	protected void onAttachedToWindow() {
		/**
		 * 当viewattache执行
		 */
		super.onAttachedToWindow();
		this.getViewTreeObserver().addOnGlobalLayoutListener(this);

	}

	@SuppressLint("NewApi")
	@Override
	protected void onDetachedFromWindow() {
		/**
		 * 当view移除时候执行
		 */
		super.onDetachedFromWindow();
		this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
	}

	public float getSacle() {
		float[] values = new float[9];
		matrix.getValues(values);
		return values[Matrix.MSCALE_X];
	}

	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		// TODO Auto-generated method stub
		float sacle = getSacle();
		float scaleFactor = detector.getScaleFactor();

		if (getDrawable() == null) {
			return true;
		}
		// 在合理的缩放范围之内
		if ((sacle < mMaxScale && scaleFactor > 1.0f) || (sacle > mInitScale && scaleFactor < 1.0f)) {
			if (sacle * scaleFactor < mInitScale) {
				scaleFactor = mInitScale / sacle;
			}
			if (sacle * scaleFactor > mMaxScale) {
				scaleFactor = mMaxScale / sacle;
			}
		}
		matrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
		setImageMatrix(matrix);
		return true;
	}

	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) {
		// 开始缩放的时候返回true

		return true;
	}

	@Override
	public void onScaleEnd(ScaleGestureDetector detector) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		mDetector.onTouchEvent(event);
		return false;
	}

}
