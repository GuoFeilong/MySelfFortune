package com.guofeilong.fortune.ui.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.guofeilong.fortune.R;
import com.guofeilong.fortune.utils.T;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

public class CanvasView extends View {
	private List<MyPoint> myPoints;
	/**
	 * 大圆的半径
	 */
	private float BIG_CICLE_SIZE = 1.5F / 8F;
	/**
	 * 小圆的半径
	 */
	private float SMALL_CICLE_SIZE = 1F / 25F;
	/**
	 * 小(大)圆的半径
	 */
	private float MIDDLE_CICLE_SIZE = 1.5F / 25F;
	/**
	 * 短线的长度
	 */
	private float SHORT_LINE_SIZE = 1F / 4F;
	/**
	 * 长线的长度
	 */
	private float LONG_LINE_SIZE = 1.3F / 4F;
	/**
	 * 控件的大小
	 */
	private int mSize;
	/**
	 * 大圈的颜色
	 */
	private int mBigCircleColor;
	/**
	 * 小(大)圈的颜色
	 */
	private int mMiddleCircleColor;
	/**
	 * 小圈的颜色
	 */
	private int mSmallCircleColor;
	/**
	 * 大圈字色颜色
	 */
	private int mMaintTextColor;
	/**
	 * 普通字的颜色
	 */
	private int mCommonTextColor;
	/**
	 * 主字的大小
	 */
	private int mMainTextSize;
	/**
	 * 普通字的大小
	 */
	private int mCommonTextSize;
	/**
	 * 画字的笔
	 */
	private Paint mTextPaint;
	/**
	 * 画圆的笔
	 */
	private Paint mCiclePaint;
	/**
	 * 中心点
	 */
	private int mCenter;
	/**
	 * 文字偏移
	 */
	private float textOffsetY;

	/**
	 * 初始化参数
	 */
	private void init() {
		myPoints = new ArrayList<CanvasView.MyPoint>();

		mTextPaint = new Paint();
		mTextPaint.setTextSize(mCommonTextSize);
		mTextPaint.setColor(mCommonTextColor);

		textOffsetY = (mTextPaint.descent() + mTextPaint.ascent()) / 2;

		mCiclePaint = new Paint();
		mCiclePaint.setAntiAlias(true);
		mCiclePaint.setColor(mBigCircleColor);
	}

	public CanvasView(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CanvasDemo, 0, 0);
		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.CanvasDemo_big_circle_color:
				mBigCircleColor = a.getColor(attr, R.color.demo_title2);
				break;
			case R.styleable.CanvasDemo_middle_circle_color:
				mMiddleCircleColor = a.getColor(attr, R.color.demo_title3);
				break;
			case R.styleable.CanvasDemo_small_circle_color:
				mSmallCircleColor = a.getColor(attr, R.color.demo_title6);
				break;
			case R.styleable.CanvasDemo_common_text_color:
				mCommonTextColor = a.getColor(attr, R.color.conmmd_background);
				break;
			case R.styleable.CanvasDemo_main_text_color:
				mMaintTextColor = a.getColor(attr, Color.WHITE);
				break;
			case R.styleable.CanvasDemo_main_text_size:
				mMainTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, context.getResources().getDisplayMetrics()));
				break;
			case R.styleable.CanvasDemo_common_text_size:
				mCommonTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 11, context.getResources().getDisplayMetrics()));

				break;

			default:
				break;
			}
		}

		a.recycle();

		init();

	}

	public CanvasView(Context context) {
		this(context, null);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mSize = Math.min(getMeasuredWidth(), getMeasuredHeight());
		mCenter = mSize / 2;
		setMeasuredDimension(mSize, mSize);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		drawARC(canvas);
		drawBG(canvas);
		drawText(canvas);

		// for (int i = 0; i <20; i++) {
		// int currentColor = i % 2 == 0 ? R.color.demo_title6 :
		// R.color.demo_title5;
		// drawLine(canvas, (new Random().nextInt(11) + 1) * 30f, mSize *
		// SHORT_LINE_SIZE * (1 + new Random().nextInt(3)), currentColor);
		// }

		drawLineAndCircle(canvas, 10f, mSize * SHORT_LINE_SIZE * 1.1f, R.color.demo_title1);
		drawLineAndCircle(canvas, 30f, mSize * SHORT_LINE_SIZE * 1.2f, R.color.demo_title2);
		drawLineAndCircle(canvas, 50f, mSize * SHORT_LINE_SIZE * 1.3f, R.color.demo_title3);
		drawLineAndCircle(canvas, 66f, mSize * SHORT_LINE_SIZE * 1.4f, R.color.demo_title4);
		drawLineAndCircle(canvas, 77f, mSize * SHORT_LINE_SIZE * 1.5f, R.color.demo_title5);
		drawLineAndCircle(canvas, 30f, mSize * SHORT_LINE_SIZE * 1.2f, R.color.demo_title2);
		drawLineAndCircle(canvas, 90f, mSize * SHORT_LINE_SIZE * 1.6f, R.color.demo_title6);
		drawLineAndCircle(canvas, 100f, mSize * SHORT_LINE_SIZE * 1.7f, R.color.demo_title7);
		drawLineAndCircle(canvas, 110f, mSize * SHORT_LINE_SIZE * 1.8f, R.color.demo_title1);
		drawLineAndCircle(canvas, 130f, mSize * SHORT_LINE_SIZE * 1.9f, R.color.demo_title2);
		drawLineAndCircle(canvas, 140f, mSize * SHORT_LINE_SIZE * 1.65f, R.color.demo_title3);
		drawLineAndCircle(canvas, 155f, mSize * SHORT_LINE_SIZE * 1.11f, R.color.demo_title4);
		drawLineAndCircle(canvas, 187f, mSize * SHORT_LINE_SIZE * 2.1f, R.color.demo_title5);
		drawLineAndCircle(canvas, 210f, mSize * SHORT_LINE_SIZE * 1.77f, R.color.demo_title6);
		drawLineAndCircle(canvas, 245f, mSize * SHORT_LINE_SIZE * 1.5f, R.color.demo_title7);
		drawLineAndCircle(canvas, 260f, mSize * SHORT_LINE_SIZE * 2.0f, R.color.demo_title2);
		drawLineAndCircle(canvas, 298f, mSize * SHORT_LINE_SIZE * 1.1f, R.color.demo_title1);
		drawLineAndCircle(canvas, 330f, mSize * SHORT_LINE_SIZE * 1.44f, R.color.demo_title6);
		drawLineAndCircle(canvas, 354f, mSize * SHORT_LINE_SIZE * 1.56f, R.color.demo_title2);
	}
 
	/**
	 * 
	 * @param canvas
	 * @param degree
	 * @param longLine
	 */
	private void drawLineAndCircle(Canvas canvas, float degree, float longLine, int lineColor) {
		// 画布旋转画小球,锁定画布
		canvas.save();
		// 平移旋转
		canvas.translate(mCenter, mCenter);
		canvas.rotate(degree);
		mCiclePaint.setColor(getResources().getColor(lineColor));
		mCiclePaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()));
		mCiclePaint.setStyle(Style.FILL);

		float radio = BIG_CICLE_SIZE * mSize;
		float x = (float) (radio * Math.cos(Math.PI / 180 * degree));
		float y = (float) (radio * Math.sin(Math.PI / 180 * degree));

		// float longLine = mSize * LONG_LINE_SIZE;
		float x1 = (float) (longLine * Math.cos(Math.PI / 180 * degree));
		float y1 = (float) (longLine * Math.sin(Math.PI / 180 * degree));

		canvas.drawLine(x, y, x1, y1, mCiclePaint);

		mCiclePaint.setColor(getResources().getColor(lineColor));
		canvas.drawCircle(x1, y1, mSize * SMALL_CICLE_SIZE, mCiclePaint);

		// TODO drawText 描述手机详情
		mTextPaint.setColor(mCommonTextColor);
		float measureText = mTextPaint.measureText("价格太贵");

		MyPoint temp = new MyPoint(x1, y1, "价格太贵" + new Random().nextInt(100), mSize * SMALL_CICLE_SIZE);
		myPoints.add(temp);
		if (degree <= 180.0f) {
			canvas.drawText("价格太贵", x1 - measureText / 2, y1 + 1.7f * mSize * SMALL_CICLE_SIZE, mTextPaint);
		} else {
			canvas.drawText("运行流畅", x1 - measureText / 2, y1 - 1.7f * mSize * SMALL_CICLE_SIZE, mTextPaint);
		}

		/**
		 * 释放画布,旋转过后的画布 在全部画完毕后释放
		 */
		canvas.restore();

	}

	/**
	 * 画文字
	 * 
	 * @param canvas
	 */
	private void drawText(Canvas canvas) {
		mTextPaint.setColor(mMaintTextColor);
		// 画文字居中
		float textWith = mTextPaint.measureText("iPhone 6");
		canvas.drawText("iPhone 6", mCenter - textWith / 2, mCenter - textOffsetY, mTextPaint);
		textWith = mTextPaint.measureText("详情");
		canvas.drawText("详情", mCenter - textWith / 2, mCenter - 9 * textOffsetY, mTextPaint);
	}

	/**
	 * 画扇形
	 * 
	 * @param canvas
	 */
	private void drawARC(Canvas canvas) {
		mCiclePaint.setColor(mMiddleCircleColor);
		mCiclePaint.setStyle(Style.FILL);
		RectF oval = new RectF(mSize / 2 - BIG_CICLE_SIZE * mSize, mSize / 2 - BIG_CICLE_SIZE * mSize, mSize / 2 + BIG_CICLE_SIZE * mSize, mSize / 2 + BIG_CICLE_SIZE * mSize);
		canvas.drawArc(oval, 0f, 280f, true, mCiclePaint);

		mCiclePaint.setColor(mSmallCircleColor);
		canvas.drawArc(oval, 280f, 50f, true, mCiclePaint);

		mCiclePaint.setColor(mCommonTextColor);
		canvas.drawArc(oval, 330f, 40f, true, mCiclePaint);

	}

	/**
	 * 画背景
	 * 
	 * @param canvas
	 */
	private void drawBG(Canvas canvas) {
		mCiclePaint.setColor(Color.WHITE);
		canvas.drawCircle(mCenter, mCenter, BIG_CICLE_SIZE * mSize / 6 * 5, mCiclePaint);

		mCiclePaint.setColor(mBigCircleColor);
		canvas.drawCircle(mCenter, mCenter, BIG_CICLE_SIZE * mSize / 6 * 5 - 5, mCiclePaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 手指按下的坐标
			float x2 = event.getX();
			float y2 = event.getY();

			isClickOncircle(x2, y2);

			break;
		case MotionEvent.ACTION_UP:

			break;
		case MotionEvent.ACTION_MOVE:

			break;

		default:
			break;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 判断点击在哪个点上
	 * 
	 * @param x2
	 * @param y2
	 */
	private void isClickOncircle(float x2, float y2) {
		for (int i = 0; i < myPoints.size(); i++) {
			MyPoint myPoint = myPoints.get(i);
			float circleRadio = myPoint.getCircleRadio();

			float x3 = myPoint.getX();
			float y3 = myPoint.getY();

			double sqrt = Math.sqrt((x3 - x2) * (x3 - x2) + (y3 - y2) * (y3 - y2));

			if (sqrt > circleRadio) {
				// 说明没在圆范围内
				
				T.show(getContext(),"没点中.....", 0);
			} else {
				// 在圆范围内
				T.show(getContext(), myPoint.getDesc(), 0);
			}

		}
	}

	/**
	 * 圆形坐标
	 * 
	 * @author guofl
	 * 
	 */
	class MyPoint {
		float x;
		float y;
		String desc;
		float circleRadio;

		public MyPoint(float x, float y, String desc, float circleRadio) {
			super();
			this.x = x;
			this.y = y;
			this.desc = desc;
			this.circleRadio = circleRadio;
		}

		public synchronized float getX() {
			return x;
		}

		public synchronized void setX(float x) {
			this.x = x;
		}

		public synchronized float getY() {
			return y;
		}

		public synchronized void setY(float y) {
			this.y = y;
		}

		public synchronized String getDesc() {
			return desc;
		}

		public synchronized void setDesc(String desc) {
			this.desc = desc;
		}

		public synchronized float getCircleRadio() {
			return circleRadio;
		}

		public synchronized void setCircleRadio(float circleRadio) {
			this.circleRadio = circleRadio;
		}

	}

}
