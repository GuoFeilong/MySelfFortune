package com.guofeilong.fortune.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CustomProgressBar extends View {

	private int mMaxProgress = 100;// 最大进度
	private int mCurrentProgress = 0;// 当前进度不能大于最大进度
	private int mReachedBarColor;// 当前进度的颜色
	private int mUnreachedBarColor;// 所有区域的颜色
	private float mReachedBarHeight;// 当前进度的高度
	private float mUnreachedBarHeight;// 所有区域的高度

	private Paint mReachedBarPaint;// 进度条画笔
	private Paint mUnreachedBarPaint;// 所有区域的画笔
	private Paint mTextPaint;
	private RectF mReachedBarRectF = new RectF(0, 0, 0, 0);// 进度条矩形
	private RectF mUnreachedBaRectF = new RectF(0, 0, 0, 0);// 所有区域矩形

	private int mWidth = 0;
	private int mHeight;

	private float mMaxData;
	private float mCurrentData;

	private boolean hideOrShowPercent;// 默认不显示

	public CustomProgressBar(Context context) {
		super(context);
		initPaint();
	}

	public CustomProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPaint();
	}

	public CustomProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initPaint();
	}

	public void setHideOrShowPercent(boolean hideOrShowPercent) {
		this.hideOrShowPercent = hideOrShowPercent;
	}

	/**
	 * 初始化画笔
	 */
	private void initPaint() {
		mReachedBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
		mUnreachedBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
		mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
		mReachedBarPaint.setColor(Color.GREEN);// 默认绿色画笔
		mUnreachedBarPaint.setColor(Color.WHITE);// 默认白色画笔
		mTextPaint.setColor(Color.RED);// 默认白色画笔
	}

	/**
	 * 设置进度条的背景色和进度色
	 * 
	 * @param progressColor
	 * @param backColor
	 */
	public void setProBarColor(int progressColor, int backColor) {
		mReachedBarPaint.setColor(progressColor);
		mUnreachedBarPaint.setColor(backColor);
		invalidate();
	}

	/**
	 * 设置当前数据在进度条上显示的进度
	 * 
	 * @param maxData
	 *            最大数据
	 * @param cureentData
	 *            当前数据
	 */
	public void setDataProgress(float maxData, float cureentData) {
		// mReachedBarRectF = new RectF(0, 0, mWidth * (cureentData/maxData),
		// mHeight);
		// Log.i("MyProgressBar", "setDataProgress:" + mWidth);
		// Log.i("MyProgressBar", "setDataProgress:" + mWidth *
		// (cureentData/maxData));

		mMaxData = maxData;
		mCurrentData = cureentData;

		invalidate();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mWidth = w;
		mHeight = h;

		Log.i("MyProgressBar", "onSizeChanged宽度是" + mWidth);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int radius = mHeight / 2;// 进度条圆弧的半径
		mUnreachedBaRectF = new RectF(0, 0, mWidth, mHeight);
		float rightWidht = mWidth * ((mCurrentData > mMaxData ? mMaxData : mCurrentData) / mMaxData);
		canvas.drawRoundRect(mUnreachedBaRectF, radius, radius, mUnreachedBarPaint);
		mReachedBarRectF = new RectF(0, 0, rightWidht, mHeight);
		canvas.drawRoundRect(mReachedBarRectF, radius, radius, mReachedBarPaint);

		if (hideOrShowPercent) {
			mTextPaint.setTextSize(25);
			FontMetrics fm = mTextPaint.getFontMetrics();
			float y = (float) (Math.ceil(fm.descent - fm.ascent));// 字体的高度
			float text = ((mCurrentData / mMaxData) * 100);
			String valueString = String.format("%.1f", text);
			canvas.drawText(valueString + "%", mWidth / 2, (mHeight + y) / 2 - 4, mTextPaint);
		}
	}

}
