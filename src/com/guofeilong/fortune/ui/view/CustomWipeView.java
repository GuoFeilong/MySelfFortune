package com.guofeilong.fortune.ui.view;

import com.guofeilong.fortune.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("DrawAllocation")
public class CustomWipeView extends View {
	/**
	 * 记录用户绘制的Path
	 */
	private Path mPath = new Path();
	/**
	 * 传入的内容附着在第一擦除图层
	 */
	private String mTextContent;
	/**
	 * 字体大小
	 */
	private int mTextSize;
	/**
	 * 字体颜色
	 */
	private int mTextColor;
	/**
	 * 前面要擦除的背景
	 */
	private Bitmap mBefor;
	/**
	 * 擦除后显示的背景
	 */
	private Bitmap mAfter;
	/**
	 * 背景画笔
	 */
	private Paint mBeforPaint;
	/**
	 * 前景画笔
	 */
	private Paint mAfterPaint;
	/**
	 * 擦除画笔
	 */
	private Paint mWipePaint;
	/**
	 * 画布
	 */
	private Canvas mCanvas;
	/**
	 * 控件宽
	 */
	private int mWidth;
	/**
	 * 控件高
	 */
	private int mHeight;
	/**
	 * 文字长度
	 */
	private float measureText;
	private int mLastX;
	private int mLastY;
	private boolean isComplete;
	private Bitmap tempBitmap;

	public CustomWipeView(Context context) {
		this(context, null);
	}

	public CustomWipeView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * 初始化自定义的数据类型
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 */
	public CustomWipeView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.WipeViewStyle, defStyleAttr, 0);
		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.WipeViewStyle_titleText:
				mTextContent = a.getString(i);
				break;
			case R.styleable.WipeViewStyle_titleTextColor:
				mTextColor = a.getColor(i, Color.BLACK);
				break;
			case R.styleable.WipeViewStyle_titleTextSize:
				mTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
				break;
			case R.styleable.WipeViewStyle_image_befor:
				mBefor = BitmapFactory.decodeResource(getResources(), a.getResourceId(attr, 0));
				Log.e("", "");
				break;
			case R.styleable.WipeViewStyle_image_after:
				mAfter = BitmapFactory.decodeResource(getResources(), a.getResourceId(attr, 0));
				break;
			}
		}
		a.recycle();
		// 获取自定义属性后,初始化画笔等工具
		init();
	}

	/**
	 * 初始化方法,画笔,画布等
	 */
	private void init() {
		mBeforPaint = new Paint();
		mBeforPaint.setStyle(Style.FILL);
		mBeforPaint.setTextSize(mTextSize);
		mBeforPaint.setColor(mTextColor);
		mBeforPaint.setAntiAlias(true);

		measureText = mBeforPaint.measureText(mTextContent);

		mAfterPaint = new Paint();
		mAfterPaint.setStyle(Style.FILL);
		mBeforPaint.setAntiAlias(true);

		mWipePaint = new Paint();
		mWipePaint.setStyle(Paint.Style.STROKE);
		mWipePaint.setStrokeWidth(80);
		mWipePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
		mWipePaint.setAntiAlias(true);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		/**
		 * 设置宽度
		 */
		int specMode = MeasureSpec.getMode(widthMeasureSpec);
		int specSize = MeasureSpec.getSize(widthMeasureSpec);

		if (specMode == MeasureSpec.EXACTLY)// match_parent , accurate
		{
			mWidth = specSize;
		} else {
			// 由图片决定的宽
			int desireByImg = mAfter.getWidth();
			int desireByTitle = mAfter.getWidth();

			if (specMode == MeasureSpec.AT_MOST)// wrap_content
			{
				int desire = Math.max(desireByImg, desireByTitle);
				mWidth = Math.min(desire, specSize);
			}
		}

		/***
		 * 设置高度
		 */

		specMode = MeasureSpec.getMode(heightMeasureSpec);
		specSize = MeasureSpec.getSize(heightMeasureSpec);
		if (specMode == MeasureSpec.EXACTLY)// match_parent , accurate
		{
			mHeight = specSize;
		} else {
			int desire = mAfter.getHeight();
			if (specMode == MeasureSpec.AT_MOST)// wrap_content
			{
				mHeight = Math.min(desire, specSize);
			}
		}
		setMeasuredDimension(mWidth, mHeight);

		tempBitmap = Bitmap.createBitmap(mWidth, mHeight, Config.ARGB_8888);
		mCanvas = new Canvas(tempBitmap);

		// 创建定宽高的before bitmap
		Bitmap createBefor = Bitmap.createScaledBitmap(mBefor, mWidth, mHeight, true);
		// BitmapShader bitmapShader = new BitmapShader(createBefor,
		// TileMode.CLAMP, TileMode.CLAMP);
		// mBeforPaint.setShader(bitmapShader);
		// 画前景
		mCanvas.drawBitmap(createBefor, new Matrix(), mBeforPaint);
		// mCanvas.drawCircle(mHeight / 2, mHeight / 2, mHeight / 2,
		// mBeforPaint);

		FontMetrics fm = mBeforPaint.getFontMetrics();
		int a = (int) (Math.ceil(fm.descent - fm.top) + 2);
		mCanvas.drawText(mTextContent, mWidth / 2 - measureText / 2, mHeight / 2 + a / 2, mBeforPaint);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// 创建定宽高的after bitmap
		Bitmap createAfter = Bitmap.createScaledBitmap(mAfter, mWidth, mHeight, true);
		// 画背景
		// canvas.drawBitmap(createAfter, new Matrix(), mAfterPaint);
		BitmapShader bitmapShader = new BitmapShader(createAfter, TileMode.CLAMP, TileMode.CLAMP);
		mAfterPaint.setShader(bitmapShader);
		canvas.drawCircle(mHeight / 2, mHeight / 2, mHeight / 2, mAfterPaint);
		if (!isComplete) {
			drawPath();
			// 把内存中时时改变的bitmap绘制到系统的canvas中
			canvas.drawBitmap(tempBitmap, 0, 0, null);
		}
	}

	/**
	 * 绘制线条
	 */
	private void drawPath() {
		mWipePaint.setStyle(Paint.Style.STROKE);
		mWipePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
		mCanvas.drawPath(mPath, mWipePaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		int x = (int) event.getX();
		int y = (int) event.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mLastX = x;
			mLastY = y;
			mPath.moveTo(mLastX, mLastY);
			break;
		case MotionEvent.ACTION_MOVE:

			int dx = Math.abs(x - mLastX);
			int dy = Math.abs(y - mLastY);

			if (dx > 3 || dy > 3)
				mPath.lineTo(x, y);

			mLastX = x;
			mLastY = y;
			break;
		case MotionEvent.ACTION_UP:
			new Thread(mRunnable).start();
			break;
		}

		invalidate();
		return true;
	}

	/**
	 * 统计擦除区域任务
	 */
	private Runnable mRunnable = new Runnable() {
		private int[] mPixels;

		@Override
		public void run() {

			int w = getWidth();
			int h = getHeight();

			float wipeArea = 0;
			float totalArea = w * h;

			Bitmap bitmap = tempBitmap;

			mPixels = new int[w * h];

			/**
			 * 拿到所有的像素信息
			 */
			bitmap.getPixels(mPixels, 0, w, 0, 0, w, h);

			/**
			 * 遍历统计擦除的区域
			 */
			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					int index = i + j * w;
					if (mPixels[index] == 0) {
						wipeArea++;
					}
				}
			}

			/**
			 * 根据所占百分比，进行一些操作
			 */
			if (wipeArea > 0 && totalArea > 0) {
				int percent = (int) (wipeArea * 100 / totalArea);
				Log.e("TAG", percent + "");
				if (percent > 70) {
					isComplete = true;
					postInvalidate();
				}
			}
		}

	};

}
