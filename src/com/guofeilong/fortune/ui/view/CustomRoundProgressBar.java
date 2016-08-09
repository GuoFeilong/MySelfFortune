package com.guofeilong.fortune.ui.view;

import com.guofeilong.fortune.R;
import com.guofeilong.fortune.utils.ViewUtils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.Paint.FontMetrics;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

/**
 * 
 * @ClassName: RoundProgressBar
 * @Description:TODO
 * @author: Android_Tian
 * @date: 2014-10-30 上午11:35:53
 */
public class CustomRoundProgressBar extends View implements Runnable {
	private float STARTSANGLE = 270; // --开始角度
	private float endAngle;// --画的扇形的角度
	private float sectorWidth; // --扇形的宽度
	private int SPEED = 5;// --动画速度
	private Boolean openAnimation = true;// --是否开启动画
	private Paint paint = new Paint();
	private float currentSweep = 0;
	private int processColor;
	private float value;

	public CustomRoundProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		sectorWidth = ViewUtils.dpToPx(getResources(), 8);
		processColor = getResources().getColor(R.color.conmmd_blue);
		if (attrs != null) {
			TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomRoundProgressBar, 0, 0);
			sectorWidth = a.getDimension(R.styleable.CustomRoundProgressBar_circleWidth, ViewUtils.dpToPx(getResources(), 2));
			a.recycle();
		}
	}

	public void startThreadAnimation() {
		new Thread(this).start();
	}

	public CustomRoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		sectorWidth = ViewUtils.dpToPx(getResources(), 2);
		processColor = getResources().getColor(R.color.conmmd_blue);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
		canvas.drawColor(Color.TRANSPARENT);
		paint.reset();
		paint.setAntiAlias(true);

		float midX, midY, radius, innerRadius;
		midX = getWidth() / 2;
		midY = getHeight() / 2;

		radius = midX;
		innerRadius = radius - sectorWidth;
		paint.setColor(processColor);

		canvas.drawCircle(midX, midY, radius - 1, paint);

		paint.setColor(Color.parseColor("#eeeeee"));
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL);
		RectF rectF = new RectF(midX - radius, midY - radius, midX + radius, midY + radius);
		canvas.drawArc(rectF, STARTSANGLE, currentSweep, true, paint);

		// 画背景大圆，线
		paint.setColor(getResources().getColor(R.color.white));
		canvas.drawCircle(midX, midY, innerRadius + 1, paint);
		// 画上方大半圆
		paint.setColor(getResources().getColor(R.color.white));
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL);
		RectF k1 = new RectF(midX - (innerRadius + 1), midY - (innerRadius + 1), midX + (innerRadius + 1), midY + (innerRadius + 1));
		canvas.drawArc(k1, 160, 220, false, paint);
		// 画下方小半圆
		RectF k2 = new RectF(midX - (innerRadius + 1), midY - (innerRadius + 1), midX + (innerRadius + 1), midY + (innerRadius + 1));
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(getResources().getColor(R.color.white));
		canvas.drawArc(k2, 20, 140, false, paint);

		paint.setTextSize(30);
		paint.setColor(Color.RED);
		
		FontMetrics fm = paint.getFontMetrics();
		float newV = value * 100;
		String format = String.format("%.1f", newV);
		canvas.drawText( format+ "%", midX/3*2, midY+5 , paint);
	}

	public void setCurrentAngle(float currentAngle) {
		this.STARTSANGLE = currentAngle;
	}

	public void setDataValue(float value) {
		this.value = value;
		this.endAngle = value * 360;
		invalidate();
	}

	public void setSectorWidth(float sectorWidth) {
		this.sectorWidth = sectorWidth;
	}

	public void setProcessColor(int processColor) {
		this.processColor = processColor;
	}

	public void openAnimation(Boolean openAnimation) {
		this.openAnimation = openAnimation;
	}

	public float getCurrentSweep() {
		return currentSweep;
	}

	public void setCurrentSweep(float currentSweep) {
		this.currentSweep = currentSweep;
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			invalidate();
		};
	};

	@Override
	public void run() {
		if (openAnimation) {

			while (currentSweep <= endAngle) {

				currentSweep = currentSweep + SPEED;
				if (currentSweep >= endAngle) {
					currentSweep = endAngle;
					mHandler.sendEmptyMessage(1);
					break;
				} else {
					mHandler.sendEmptyMessage(1);
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			mHandler.sendEmptyMessage(1);
		}

	}

}
