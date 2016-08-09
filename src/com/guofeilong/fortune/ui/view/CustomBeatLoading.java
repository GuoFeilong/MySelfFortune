package com.guofeilong.fortune.ui.view;

import java.util.ArrayList;
import java.util.List;

import com.guofeilong.fortune.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class CustomBeatLoading extends RelativeLayout implements OnGlobalLayoutListener {
	private Bitmap mFirst;
	private Bitmap mSecond;
	private Bitmap mThird;
	private int mChildViewSize;
	private List<ImageView> mViews;

	public CustomBeatLoading(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomBeatLoading(Context context) {
		this(context, null);
	}

	public CustomBeatLoading(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		getViewTreeObserver().addOnGlobalLayoutListener(this);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BeatLoadingStyle, defStyleAttr, 0);
		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.BeatLoadingStyle_image_first:
				mFirst = BitmapFactory.decodeResource(getResources(), a.getResourceId(attr, 0));
				break;
			case R.styleable.BeatLoadingStyle_image_second:
				mSecond = BitmapFactory.decodeResource(getResources(), a.getResourceId(attr, 0));
				break;
			case R.styleable.BeatLoadingStyle_image_third:
				mThird = BitmapFactory.decodeResource(getResources(), a.getResourceId(attr, 0));
				break;
			case R.styleable.BeatLoadingStyle_image_size:
				mChildViewSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, getResources().getDisplayMetrics()));
				break;
			default:
				break;
			}
		}

		a.recycle();
		initView(context);
	}

	/**
	 * 初始化控件
	 * 
	 * @param context
	 */
	private void initView(Context context) {
		mViews = new ArrayList<ImageView>();
		createView(mViews, context, mFirst);
		createView(mViews, context, mSecond);
		createView(mViews, context, mThird);
		Log.e("孩子个数", getChildCount() + "-------");
//		startLoading();
	}

	private void createView(List<ImageView> mViews2, Context context, Bitmap mFirst2) {
		ImageView ivFirst = new ImageView(context);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mChildViewSize, mChildViewSize);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		ivFirst.setLayoutParams(params);
		ivFirst.setImageBitmap(mFirst2);
		addView(ivFirst);
		mViews2.add(ivFirst);
	}

	@Override
	public void onGlobalLayout() {
	}

	private void setVisiable(List<ImageView> mViews2, int id) {
		for (int i = 0; i < mViews2.size(); i++) {

			mViews2.get(i).setVisibility(View.INVISIBLE);
		}
		mViews2.get(id).setVisibility(View.VISIBLE);
	}

	/**
	 * 开始loading
	 */
	public void startLoading() {
		final TranslateAnimation inAnimation;
		inAnimation = (TranslateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.in_from_top2);
		// inAnimation.setRepeatCount(1);

		setVisiable(mViews, 0);
		mViews.get(0).startAnimation(inAnimation);
		inAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				TranslateAnimation inAnimation00;
				inAnimation00 = (TranslateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.out_from_top2);
				inAnimation00.setFillAfter(false);
				mViews.get(0).startAnimation(inAnimation00);
				inAnimation00.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						TranslateAnimation inAnimation1;
						inAnimation1 = (TranslateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.in_from_top2);
						// inAnimation1.setRepeatCount(1);
						setVisiable(mViews, 1);
						mViews.get(1).startAnimation(inAnimation1);
						inAnimation1.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationStart(Animation animation) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onAnimationRepeat(Animation animation) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onAnimationEnd(Animation animation) {
								TranslateAnimation inAnimation11;
								inAnimation11 = (TranslateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.out_from_top2);
								inAnimation11.setFillAfter(false);
								mViews.get(1).startAnimation(inAnimation11);
								inAnimation11.setAnimationListener(new AnimationListener() {

									@Override
									public void onAnimationStart(Animation animation) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onAnimationRepeat(Animation animation) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onAnimationEnd(Animation animation) {
										TranslateAnimation inAnimation2;
										inAnimation2 = (TranslateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.in_from_top2);
										// inAnimation2.setRepeatCount(1);
										setVisiable(mViews, 2);
										mViews.get(2).startAnimation(inAnimation2);
										inAnimation2.setAnimationListener(new AnimationListener() {

											@Override
											public void onAnimationStart(Animation animation) {
												// TODO Auto-generated method
												// stub

											}

											@Override
											public void onAnimationRepeat(Animation animation) {
												// TODO Auto-generated method
												// stub

											}

											@Override
											public void onAnimationEnd(Animation animation) {
												TranslateAnimation inAnimation22;
												inAnimation22 = (TranslateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.out_from_top2);
												inAnimation22.setFillAfter(false);
												mViews.get(2).startAnimation(inAnimation22);
												inAnimation22.setAnimationListener(new AnimationListener() {

													@Override
													public void onAnimationStart(Animation animation) {
														// TODO Auto-generated
														// method stub

													}

													@Override
													public void onAnimationRepeat(Animation animation) {
														// TODO Auto-generated
														// method stub

													}

													@Override
													public void onAnimationEnd(Animation animation) {
														// TODO Auto-generated
														// method stub
														setVisiable(mViews, 0);
														mViews.get(0).startAnimation(inAnimation);
													}
												});

											}
										});
									}
								});

							}
						});

					}
				});

			}
		});

	}

	public void stopAnimation() {
		for (int i = 0; i < mViews.size(); i++) {
			mViews.get(i).clearAnimation();
			mViews.get(i).setVisibility(View.GONE);
		}
	}

}
