package com.guofeilong.fortune.ui.view;

import com.guofeilong.fortune.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

public class TopDrawableTextView extends TextView {
	private Bitmap mTopIcon;
	private int mIconSize;
	private Paint mPaint;

	public TopDrawableTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DrawableTopEditText);
		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.DrawableTopEditText_top_icon:
				mTopIcon = BitmapFactory.decodeResource(getResources(), a.getResourceId(attr, 0));
				break;

			case R.styleable.DrawableTopEditText_top_icon_size:
				mIconSize = (int) a.getDimension(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, context.getResources().getDisplayMetrics()));
				mTopIcon = changeBitMapSize(mIconSize);
				break;

			default:
				break;
			}
		}

		a.recycle();

		init();
	}

	public TopDrawableTextView(Context context) {
		super(context);

	}

	public TopDrawableTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	/**
	 * 返回bitmap
	 * 
	 * @param mIconSize2
	 * @return
	 */
	private Bitmap changeBitMapSize(int mIconSize2) {
		Bitmap bitmap = null;
		if (mIconSize2 == 0) {
			bitmap = mTopIcon;
		} else {
			bitmap = zoomImage(mTopIcon, mIconSize2, mIconSize2);
		}
		return bitmap;
	}

	/***
	 * 图片的缩放方法
	 * 
	 * @param bgimage
	 *            ：源图片资源
	 * @param newWidth
	 *            ：缩放后宽度
	 * @param newHeight
	 *            ：缩放后高度
	 * @return
	 */
	public static Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
		// 获取这个图片的宽和高
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
		return bitmap;
	}

	/**
	 * 初始化方式,画笔等
	 */
	private void init() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(Color.BLACK);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.drawBitmap(mTopIcon, 0, 0, mPaint);

	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		// TODO Auto-generated method stub
		text = "    "+text.toString();
		super.setText(text, type);
	}

}
