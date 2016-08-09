package com.guofeilong.fortune.ui.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.guofeilong.fortune.R;
import com.guofeilong.fortune.utils.ViewUtils;

public class AngularGaugeView extends RelativeLayout implements OnGlobalLayoutListener {
	private ImageView mNeedle;
	private float mStartAngle;;
	private int mWidth;
	private int mHeight;
	/**
	 * 背景图片
	 */
	private Bitmap mBackGroundBitMap;
	/**
	 * 文字半径
	 */
	private int mRadio;
	private Paint mLinePaint;
	private double mDegreey;
	/**
	 * 刻度集合
	 */
	private List<Scale> mScales;
	/**
	 * 偏移角度
	 */
	private double mDeflectionAnglePI;
	private double mDeflectionAngle;
	private int mScaleLength;
	private int mScaleMargin;
	private float mTextHeight;
	private float mScaleAngle;
	private Paint mTextPaint;

	public AngularGaugeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		getViewTreeObserver().addOnGlobalLayoutListener(this);
		mDeflectionAnglePI = Math.PI / 4;
		mDeflectionAngle = (Math.PI / 4) * (180 / Math.PI);
		mScaleLength = ViewUtils.dpToPx(getResources(), 8);
		mScaleMargin = ViewUtils.dpToPx(getResources(), 24);
		mScales = new ArrayList<AngularGaugeView.Scale>();
		mLinePaint = new Paint();
		mLinePaint.setStrokeWidth(ViewUtils.dpToPx(getResources(), 1));
		mLinePaint.setColor(getResources().getColor(R.color.conmmd_textcolor_s));
		mLinePaint.setTextSize(ViewUtils.sp2px(getContext(), 12));
		mLinePaint.setAntiAlias(true);
		mTextPaint = new Paint();
		mTextPaint.setColor(getResources().getColor(R.color.conmmd_textcolor));
		mTextPaint.setTextSize(ViewUtils.sp2px(getContext(), 11));
		mTextPaint.setAntiAlias(true);
		mTextHeight = mLinePaint.measureText("  ");
		ArrayList<Scale> arrayList = new ArrayList<Scale>();
		setScale(arrayList);

	}

	@Override
	protected void onFinishInflate() {

		super.onFinishInflate();
	}

	private boolean isAni = true;

	@Override
	public void onGlobalLayout() {
		mNeedle = (ImageView) findViewWithTag("needle");
		if (isAni) {
			AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
			alphaAnimation.setFillAfter(true);
			alphaAnimation.setDuration(0);
			RotateAnimation rotateAnimation = new RotateAnimation(0, -(float) mDeflectionAngle, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			rotateAnimation.setDuration(0);
			rotateAnimation.setFillAfter(true);
			rotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
			if (mNeedle != null) {
				mNeedle.startAnimation(rotateAnimation);
			}
			AnimationSet set = new AnimationSet(false);
			set.addAnimation(alphaAnimation);
			set.addAnimation(rotateAnimation);
			set.setFillAfter(true);
			mNeedle.startAnimation(set);
			this.mStartAngle = -(float) mDeflectionAngle;
			isAni = false;
		}
		mWidth = getWidth();
		mHeight = getHeight();
		mRadio = mWidth / 2 - ViewUtils.dpToPx(getResources(), 43);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_bg_circle_speed);
		mBackGroundBitMap = Bitmap.createScaledBitmap(bitmap, mWidth, mHeight, true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		mNeedle.invalidate();
		// mNeedle.setVisibility(View.VISIBLE);
		canvas.drawBitmap(mBackGroundBitMap, 0, 0, mLinePaint);
		if (mScales != null && mScales.size() > 0) {
			canvas.rotate((int) mDeflectionAngle, mWidth / 2, mWidth / 2); // 旋转画纸
			for (int i = 0; i < (mScales.size() - 1); i++) {
				canvas.drawLine(mWidth / 2, mWidth - mScaleMargin, mWidth / 2, mWidth - mScaleMargin - mScaleLength, mLinePaint);
				canvas.rotate(mScaleAngle, mWidth / 2, mWidth / 2); // 旋转画纸
				canvas.drawLine(mWidth / 2, mWidth - mScaleMargin, mWidth / 2, mWidth - mScaleMargin - mScaleLength, mLinePaint);
			}
			canvas.rotate((int) mDeflectionAngle, mWidth / 2, mWidth / 2); // 旋转画纸
			for (int i = 0; i < mScales.size(); i++) {
				Scale scale = mScales.get(i);
				mDegreey = i * ((2 * Math.PI - Math.PI / 2) / (mScales.size() - 1)) + mDeflectionAnglePI;
				int x = (int) (mWidth / 2 - mRadio * Math.sin(mDegreey));
				int y = (int) (mWidth / 2 + mRadio * Math.cos(mDegreey));
				String s = scale.unit;
				canvas.drawText(s, x - (mLinePaint.measureText(s) / 2), y + mTextHeight / 2, mTextPaint);
			}
		}
		super.onDraw(canvas);
	}

	public void moveTo(int position) {
		startAnimation(mStartAngle, -(float) mDeflectionAngle + position * mScaleAngle, 2000);
	}

	protected void startAnimation(float startAngle, float endAngle, int duration) {
		RotateAnimation rotateAnimation = new RotateAnimation(startAngle, endAngle, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotateAnimation.setDuration(duration);
		rotateAnimation.setFillAfter(true);
		rotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
		if (mNeedle != null) {
			mNeedle.startAnimation(rotateAnimation);

		}
		this.mStartAngle = endAngle;
	}

	public void reset() {
		mNeedle.clearAnimation();
		startAnimation(0, -(float) mDeflectionAngle, 0);

	}

	/**
	 * 设置单位
	 * 
	 * @param scales
	 */
	public void setScale(List<Scale> scales) {
		for (int i = 0; i < 5; i++) {
			Scale scale = new Scale();
			if (i == 0) {
				scale.setId(i);
				scale.setUnit("Low");
			} else if (i == 4) {
				scale.setId(i);
				scale.setUnit("High");
			} else {
				scale.setUnit(i + "M");

			}
			scales.add(scale);

		}
		this.mScales = scales;
		mScaleAngle = (float) (360 - mDeflectionAngle * 2) / (scales.size() - 1);
		postInvalidate();

	}


	private static class Scale {
		private String unit;
		private int id;
		private int value;

		public String getUnit() {
			return unit;
		}

		public void setUnit(String unit) {
			this.unit = unit;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

	}

}
