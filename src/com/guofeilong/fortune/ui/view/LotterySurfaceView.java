package com.guofeilong.fortune.ui.view;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.guofeilong.fortune.R;
import com.guofeilong.fortune.utils.T;

public class LotterySurfaceView extends SurfaceView implements Callback, Runnable {
	private int mLotteryIndex;
	private String mCurrentDesc;
	/**
	 * 奖项个数
	 */
	private static final int ITEM_COUNT = 6;
	// TODO 转盘的转动
	private double mSpeed = 10;
	private SurfaceHolder mHolder;
	private Canvas mCanvas;
	/**
	 * 线程的控制开关
	 */
	private boolean isRunning;
	/**
	 * 用于绘制的子线程
	 */
	private Thread mThread;

	/**
	 * 半径
	 */
	private int mRadius;
	/**
	 * 背景padding
	 */
	private int mPadding;
	/**
	 * 用于绘制文本
	 */
	private Paint mTextPaint;
	/**
	 * 用于绘制图片
	 */
	private Paint mDrawPaint;
	/**
	 * 用于绘制背景
	 */
	private Paint mBgPaint;
	/**
	 * 字体大小
	 */
	private float mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics());
	/**
	 * 模块的颜色
	 */
	private int[] mColors;
	/**
	 * 奖项描述
	 */
	private String[] mDesc = { "iPhone6", "ipad", "谢谢回顾", "MacBook", "100元", "很遗憾" };
	private int[] mPics = { R.drawable.lottery_iphone, R.drawable.lottery_ipad, R.drawable.lottery_sorry, R.drawable.lottery_macbook, R.drawable.lottery_money, R.drawable.lottery_thanks };
	private RectF oval;
	private float startAngle;
	private float sweepAngle;
	private int mCenter;
	private boolean isShouldEnd;

	public interface OnMyItemClickListener {
		void showMyToast();
	}

	private OnMyItemClickListener mClickListener;

	public synchronized void setmClickListener(OnMyItemClickListener mClickListener) {
		this.mClickListener = mClickListener;
	}

	public LotterySurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 获取surfaceview的holder
		mHolder = getHolder();
		// 添加回调
		mHolder.addCallback(this);
		// 设置获取焦点
		setFocusable(true);
		setFocusableInTouchMode(true);
		setKeepScreenOn(true);

		mColors = context.getResources().getIntArray(R.array.water_colors);
		initPaint();
	}

	/**
	 * 初始化画笔
	 */
	private void initPaint() {
		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setColor(Color.WHITE);
		mTextPaint.setTextSize(mTextSize);

		mDrawPaint = new Paint();
		mDrawPaint.setAntiAlias(true);
		mDrawPaint.setColor(Color.BLACK);

		mBgPaint = new Paint();
		mBgPaint.setAntiAlias(true);
		mBgPaint.setColor(getContext().getResources().getColor(R.color.demo_title7));
	}

	public LotterySurfaceView(Context context) {
		this(context, null);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		isRunning = true;
		mThread = new Thread(this);
		mThread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mThread.interrupt();
	}

	@Override
	public void run() {
		// test
		while (isRunning) {
			long start = System.currentTimeMillis();
			draw();
			long end = System.currentTimeMillis();
			try {
				if (end - start < 50) {
					Thread.sleep(50 - (end - start));
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
		mPadding = getPaddingLeft();
		mRadius = (width - 2 * mPadding) / 2;
		mCenter = width / 2;
		setMeasuredDimension(width, width);
	}

	private void draw() {
		try {
			mCanvas = mHolder.lockCanvas();
			if (mCanvas != null) {
				drawBG();
				drawMokuai();

			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (mCanvas != null) {
				mHolder.unlockCanvasAndPost(mCanvas);
			}
		}
	}

	/**
	 * 画模块
	 */
	private void drawMokuai() {
		/**
		 * 绘制每个块块，每个块块上的文本，每个块块上的图片
		 */
		float tmpAngle = startAngle;
		float sweepAngle = (float) (360 / ITEM_COUNT);

		oval = new RectF(mPadding / 2, mPadding / 2, getMeasuredWidth() - mPadding / 2, getMeasuredHeight() - mPadding / 2);
		for (int i = 0; i < ITEM_COUNT; i++) {
			mDrawPaint.setColor(mColors[i]);
			mCanvas.drawArc(oval, tmpAngle, sweepAngle, true, mDrawPaint);
			drawText(tmpAngle, sweepAngle, mDesc[i]);
			drawIcon(tmpAngle, BitmapFactory.decodeResource(getResources(), mPics[i]));
			tmpAngle += sweepAngle;
		}

		// 如果mSpeed不等于0，则相当于在滚动
		startAngle += mSpeed;

		// 点击停止时，设置mSpeed为递减，为0值转盘停止
		if (isShouldEnd) {
			mSpeed -= 1;
		}
		if (mSpeed <= 0) {
			mSpeed = 0;
			isShouldEnd = false;

			if (mClickListener != null) {
				setmClickListener(mClickListener);
			}
			mCurrentDesc = mDesc[mLotteryIndex];
		}
		// 根据当前旋转的mStartAngle计算当前滚动到的区域
		calInExactArea(startAngle);

	}

	
	
	public synchronized String getmDesc() {
		return mCurrentDesc;
	}

	/**
	 * 根据当前旋转的mStartAngle计算当前滚动到的区域
	 * 
	 * @param startAngle
	 */
	public void calInExactArea(float startAngle) {
		// 让指针从水平向右开始计算
		float rotate = startAngle + 90;
		rotate %= 360.0;
		for (int i = 0; i < ITEM_COUNT; i++) {
			// 每个的中奖范围
			float from = 360 - (i + 1) * (360 / ITEM_COUNT);
			float to = from + 360 - (i) * (360 / ITEM_COUNT);

			if ((rotate > from) && (rotate < to)) {
				return;
			}
		}
	}

	/**
	 * 绘制图片
	 * 
	 * @param startAngle
	 * @param decodeResource
	 */
	private void drawIcon(float startAngle, Bitmap decodeResource) {
		// 设置图片的大小
		int imgW = mRadius / 4;
		// 角度
		float angle = (float) ((startAngle + 360 / ITEM_COUNT / 2) * Math.PI / 180);

		int x = (int) (mCenter + mRadius / 2 * Math.cos(angle));
		int y = (int) (mCenter + mRadius / 2 * Math.sin(angle));

		Rect rect = new Rect(x - imgW / 2, y - imgW / 2, x + imgW / 2, y + imgW / 2);
		mCanvas.drawBitmap(decodeResource, null, rect, null);
	}

	/**
	 * 绘制文本
	 * 
	 * @param startAngle
	 * @param sweepAngle
	 * @param string
	 */
	private void drawText(float startAngle, float sweepAngle, String string) {
		oval = new RectF(mPadding / 2, mPadding / 2, getMeasuredWidth() - mPadding / 2, getMeasuredHeight() - mPadding / 2);
		Path path = new Path();
		path.addArc(oval, startAngle, sweepAngle);

		/**
		 * 在制定path上绘制文字.算出水平偏移量和垂直偏移量
		 * 
		 * <pre>
		 * 计算出文字的长度和弧的长度 中心重合就是居中位置
		 * </pre>
		 */
		float textWith = mTextPaint.measureText(string);
		float hOffset = (float) (2 * mRadius * Math.PI / ITEM_COUNT / 2 - textWith / 2);
		// mCanvas.drawTextOnPath(string, path, 0, 0, mTextPaint);
		mCanvas.drawTextOnPath(string, path, hOffset, mRadius / 5, mTextPaint);
	}

	/**
	 * 绘制抽奖背景
	 */
	private void drawBG() {
		/**
		 * 改变surfaceview的背景色
		 */
		mCanvas.drawColor(getContext().getResources().getColor(R.color.conmmd_background));
		mCanvas.drawCircle(mRadius + mPadding, mRadius + mPadding, mRadius, mBgPaint);

	}

	/**
	 * 点击开始旋转
	 * 
	 * @param luckyIndex
	 */
	public void luckyStart(int luckyIndex) {

		mLotteryIndex = luckyIndex;
		// 每项角度大小
		float angle = (float) (360 / ITEM_COUNT);
		// 中奖角度范围（因为指针向上，所以水平第一项旋转到指针指向，需要旋转210-270；）
		float from = 270 - (luckyIndex + 1) * angle;
		float to = from + angle;
		// 停下来时旋转的距离
		float targetFrom = 4 * 360 + from;
		/**
		 * <pre>
		 *  (v1 + 0) * (v1+1) / 2 = target ;
		 *  v1*v1 + v1 - 2target = 0 ;
		 *  v1=-1+(1*1 + 8 *1 * target)/2;
		 * </pre>
		 */
		float v1 = (float) (Math.sqrt(1 * 1 + 8 * 1 * targetFrom) - 1) / 2;
		float targetTo = 4 * 360 + to;
		float v2 = (float) (Math.sqrt(1 * 1 + 8 * 1 * targetTo) - 1) / 2;

		mSpeed = (float) (v1 + Math.random() * (v2 - v1));
		isShouldEnd = false;
	}

	public void luckyEnd() {
		startAngle = 0;
		isShouldEnd = true;
	}

	public boolean isStart() {
		return mSpeed != 0;
	}

	public boolean isShouldEnd() {
		return isShouldEnd;
	}

}
