package com.guofeilong.fortune.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.guofeilong.fortune.R;

public class CustomIconAndTextView extends View {
	private String mText = "RTSS";
	private int mColor;
	private int mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 12, getResources().getDisplayMetrics());
	private Bitmap mIcon;

	private Paint mPaint;
	private Paint mTextPaint;
	private Canvas mCanvas;
	private Bitmap mTempBitmap;

	private Rect mTextRect;
	private Rect mIconRect;

	private float mAfp=0.1f;

	public CustomIconAndTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomIconAndTextView(Context context) {
		this(context, null);
	}

	public CustomIconAndTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// 获取自定义属性
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ChangeColorIconWithText);
		int indexCount = a.getIndexCount();
		for (int i = 0; i < indexCount; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.ChangeColorIconWithText_color:
				mColor = a.getColor(attr, Color.GRAY);
				break;
			case R.styleable.ChangeColorIconWithText_text:
				mText = a.getString(attr);
				break;
			case R.styleable.ChangeColorIconWithText_text_size:
				mTextSize = (int) a.getDimension(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, context.getResources().getDisplayMetrics()));
				break;
			case R.styleable.ChangeColorIconWithText_icon:
				BitmapDrawable drawable = (BitmapDrawable) a.getDrawable(attr);
				mIcon = drawable.getBitmap();
				break;

			default:
				break;
			}

		}
		a.recycle();
		init();
	}

	public float getmAfp() {
		return mAfp;
	}

	public void setmAfp(float mAfp) {
		this.mAfp = mAfp;
	}

	/**
	 * 初始化一些数据
	 */
	private void init() {
		mTextRect = new Rect();
		mTextPaint = new Paint();
		mTextPaint.setColor(Color.GRAY);
		mTextPaint.setTextSize(mTextSize);
		mTextPaint.getTextBounds(mText, 0, mText.length(), mTextRect);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int iconWidth = Math.min(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - mTextRect.height());
		int left = getMeasuredWidth() / 2 - iconWidth / 2;
		int top = getMeasuredHeight() / 2 - (mTextRect.height() + iconWidth) / 2;
		mIconRect = new Rect(left, top, left + iconWidth, top + iconWidth);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(mIcon, null, mIconRect, null);
		drawTargetIcon(mAfp);
		drawSourceText(canvas, mAfp);
		drawTargetText(canvas, mAfp);
		canvas.drawBitmap(mTempBitmap, 0, 0, null);
	}

	private void drawTargetText(Canvas canvas, float mAfp2) {
		int x = getMeasuredWidth() / 2 - mTextRect.width() / 2;
		int y = mIconRect.bottom + mTextRect.height();
		mTextPaint.setColor(mColor);
		mTextPaint.setAlpha(255 - (int) Math.ceil(255 * mAfp2));
		canvas.drawText(mText, x, y, mTextPaint);
	}

	/**
	 * 
	 * @param canvas
	 * @param mAfp2
	 */
	private void drawSourceText(Canvas canvas, float mAfp2) {
		int x = getMeasuredWidth() / 2 - mTextRect.width() / 2;
		int y = mIconRect.bottom + mTextRect.height();
		mTextPaint.setAlpha((int) Math.ceil(255 * mAfp2));
		canvas.drawText(mText, x, y, mTextPaint);
	}

	/**
	 * 绘制icon
	 * 
	 * @param canvas
	 * @param mAfp2
	 */
	private void drawTargetIcon(float mAfp2) {
		mTempBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Config.ARGB_8888);
		mCanvas = new Canvas(mTempBitmap);
		mPaint = new Paint();
		mPaint.setAlpha((int) Math.ceil(255 * mAfp2));
		mPaint.setDither(true);
		mPaint.setAntiAlias(true);
		mPaint.setColor(mColor);

		mCanvas.drawRect(mIconRect, mPaint);
		mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		mPaint.setAlpha(255);
		mCanvas.drawBitmap(mIcon, null, mIconRect, mPaint);

	}
}
