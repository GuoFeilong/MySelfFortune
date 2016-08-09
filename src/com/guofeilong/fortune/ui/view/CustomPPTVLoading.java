package com.guofeilong.fortune.ui.view;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.guofeilong.fortune.R;

import java.util.ArrayList;
import java.util.List;

public class CustomPPTVLoading extends RelativeLayout implements OnGlobalLayoutListener {
	private static final float SCR_UNIT_SMALL = 0.5F;
	private static final float SCR_UNIT_BIG = 1F;

	private int mFristColor;
	private int mSencondColor;
	private RelativeLayout mBallConatainer;
	private int defalutWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
	private List<ImageView> mBalls;
	private ObjectAnimator oa;
	private ObjectAnimator oa1;

	public CustomPPTVLoading(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomPPTVLoading(Context context) {
		this(context, null);
	}

	public void setmFristColor(int mFristColor) {
		this.mFristColor = mFristColor;

		Bitmap bm = createdNewBM(mFristColor);

		mBalls.get(0).setImageBitmap(bm);

	}

	private Bitmap createdNewBM(int mFristColor) {
		Bitmap bm = Bitmap.createBitmap(defalutWidth, defalutWidth, Config.ARGB_8888);
		int w = bm.getWidth();
		int h = bm.getHeight();
		int cx = w / 2;

		Canvas canvas = new Canvas(bm);
		Paint paint = new Paint();
		paint.setColor(mFristColor);
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setStyle(Style.FILL);
		canvas.drawCircle(cx, cx, cx, paint);
		return bm;
	}

	public void setmSencondColor(int mSencondColor) {
		this.mSencondColor = mSencondColor;

		Bitmap bm = createdNewBM(mSencondColor);

		mBalls.get(1).setImageBitmap(bm);
	}

	public CustomPPTVLoading(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		getViewTreeObserver().addOnGlobalLayoutListener(this);
		mBalls = new ArrayList<ImageView>();
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.pptv_ball, defStyle, 0);
		int indexCount = a.getIndexCount();

		for (int i = 0; i < indexCount; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.pptv_ball_pptv_frist_color:
				mFristColor = a.getColor(attr, Color.BLACK);
				Log.e("TAG", "FIRST_COLOR" + mFristColor);
				break;
			case R.styleable.pptv_ball_pptv_second_color:
				mSencondColor = a.getColor(attr, Color.BLACK);
				Log.e("TAG", "SENCOND_COLOR" + mSencondColor);
				break;
			default:
				break;
			}
		}

		a.recycle();
		mBallConatainer = creatBallContainer();
		initView();
		addView(mBallConatainer);

		startLoading();
	}

	/**
	 * 开始loading
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void startLoading() {

		RelativeLayout.LayoutParams lp = (LayoutParams) mBalls.get(0).getLayoutParams();
		RelativeLayout.LayoutParams lp1 = (LayoutParams) mBalls.get(1).getLayoutParams();

		PropertyValuesHolder trans = PropertyValuesHolder.ofFloat("translationX", lp.leftMargin, lp1.leftMargin + 30, lp.leftMargin);
		// PropertyValuesHolder trans =
		// PropertyValuesHolder.ofFloat("translationX", lp.leftMargin,
		// lp1.leftMargin);
		PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", SCR_UNIT_BIG, SCR_UNIT_SMALL, SCR_UNIT_BIG);
		PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", SCR_UNIT_BIG, SCR_UNIT_SMALL, SCR_UNIT_BIG);

		oa = ObjectAnimator.ofPropertyValuesHolder(mBalls.get(0), trans, scaleX, scaleY);
		oa.setDuration(1000);
		oa.setRepeatCount(Animation.INFINITE);
		oa.setRepeatMode(Animation.REVERSE);
		oa.start();

		PropertyValuesHolder trans1 = PropertyValuesHolder.ofFloat("translationX", lp1.leftMargin, lp.leftMargin - 30, lp1.leftMargin);
		// PropertyValuesHolder trans1 =
		// PropertyValuesHolder.ofFloat("translationX", lp1.leftMargin,
		// lp.leftMargin);
		PropertyValuesHolder scaleX1 = PropertyValuesHolder.ofFloat("scaleX", SCR_UNIT_SMALL, SCR_UNIT_BIG, SCR_UNIT_SMALL);
		PropertyValuesHolder scaleY1 = PropertyValuesHolder.ofFloat("scaleY", SCR_UNIT_SMALL, SCR_UNIT_BIG, SCR_UNIT_SMALL);

		oa1 = ObjectAnimator.ofPropertyValuesHolder(mBalls.get(1), trans1, scaleX1, scaleY1);
		oa1.setDuration(1000);
		oa1.setRepeatCount(Animation.INFINITE);
		oa1.setRepeatMode(Animation.REVERSE);
		oa1.start();

	}

	/**
	 * 停止loading
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void stopLoading() {
		oa.cancel();
		oa1.cancel();
	}

	/**
	 * 初始化控件view
	 */
	private void initView() {
		createdBall(mBallConatainer, mFristColor, 1);
		createdBall(mBallConatainer, mSencondColor, 1.6);

	}

	/**
	 * 动态的生成一个小球
	 * 
	 * @param ballConatainer
	 * 
	 * @param mFristColor2
	 */
	private void createdBall(RelativeLayout ballConatainer, int mFristColor2, double d) {
		Bitmap bm = Bitmap.createBitmap(defalutWidth, defalutWidth, Config.ARGB_8888);
		int w = bm.getWidth();
		int h = bm.getHeight();
		int cx = w / 2;

		Canvas canvas = new Canvas(bm);
		Paint paint = new Paint();
		paint.setColor(mFristColor);
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setStyle(Style.FILL);
		canvas.drawCircle(cx, cx, cx, paint);
		Log.e("TAG", "w==" + w + "h==" + h);

		ImageView ballView = new ImageView(getContext());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w, h);
		ballView.setImageBitmap(bm);
		ballView.setLayoutParams(params);
		params.leftMargin = (int) (cx * d + w * d);
		params.rightMargin = (int) (cx * d + w * d);
		ballView.setLayoutParams(params);

		ballConatainer.addView(ballView);
		mBalls.add(ballView);
	}

	/**
	 * 创建放小球的容器
	 * 
	 * @return
	 */
	private RelativeLayout creatBallContainer() {
		// 创建一个 布局装两个小球

		RelativeLayout layout = new RelativeLayout(getContext());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		layout.setLayoutParams(params);
		return layout;
	}

	@Override
	public void onGlobalLayout() {

	}

}
