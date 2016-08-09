package com.guofeilong.fortune.ui.view;

import com.guofeilong.fortune.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义带描边的圆形icon
 * 
 * @author guofl
 * 
 */
@SuppressLint("NewApi")
public class CustomCircleIcon extends View {
	/**
	 * 描边的颜色
	 */
	private int mStrokeColor;
	/**
	 * 填充的颜色
	 */
	private int mSolidColor;
	/**
	 * 中间的icon
	 */
	private Bitmap mIcon;
	private int mHeight;
	private int mWidth;
	private Paint mPaint;

	public CustomCircleIcon(Context context) {
		this(context, null);
	}

	public CustomCircleIcon(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomCircleIcon(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleIconStyle, defStyleAttr, 0);
		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.CircleIconStyle_circle_icon:
				mIcon = BitmapFactory.decodeResource(getResources(), a.getResourceId(attr, 0));
				break;
			case R.styleable.CircleIconStyle_circle_icon_solid_color:
				mSolidColor = a.getColor(attr, Color.WHITE);
				break;
			case R.styleable.CircleIconStyle_circle_icon_stroke_color:
				mStrokeColor = a.getColor(attr, Color.GRAY);
				break;
			}
		}
		a.recycle();
		// 获取自定义属性后,初始化画笔等工具
		init();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mHeight = h;
		mWidth = w;
	}

	/**
	 * 初始化画笔
	 */
	private void init() {
		mPaint = new Paint();
		mPaint.setStyle(Style.FILL);
		mPaint.setAntiAlias(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		mPaint.setColor(mStrokeColor);
		float cx = mWidth / 2;
		float cy = mWidth / 2;
		canvas.drawCircle(cx, cy, cx, mPaint);

		mPaint.setColor(mSolidColor);
		canvas.drawCircle(cx, cy, cx - 1, mPaint);
		Bitmap zoomImage = zoomImage(mIcon, (mWidth / 3) * 2, (mHeight / 3) * 2);
		canvas.drawBitmap(zoomImage, cx - zoomImage.getWidth() / 2, cx - zoomImage.getHeight() / 2, mPaint);
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

}
