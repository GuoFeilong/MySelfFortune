package com.guofeilong.fortune.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class ChangeColorIconView extends View {
	private PorterDuffXfermode porterDuffXfermode;// Xfermode
	private Paint paint;// 画笔
	private Bitmap bitmap;// 源图片

	private int width, height;// 控件宽高
	private Path path;// 画贝塞尔曲线需要用到
	private Canvas mCanvas;// 在该画布上绘制目标图片
	private Bitmap bg;// 目标图片
	private int mResource;
	private float waveY;// 上升的高度

	private boolean isReflesh = false;// 是否刷新并产生填充效果，默认为true
	private int color;// 传入要更改的颜色

	public void setColor(int color) {
		this.color = color;
	}

	public ChangeColorIconView(Context context, int mResource) {
		super(context);
		this.mResource = mResource;
	}

	public void setmResource(int mResource) {
		this.mResource = mResource;

	}

	/**
	 * @return 是否刷新
	 */
	public boolean isReflesh() {
		return isReflesh;
	}

	/**
	 * 提供接口设置刷新
	 * 
	 * @param isReflesh
	 */
	public void setReflesh(boolean isReflesh) {
		this.isReflesh = isReflesh;
		// 重绘
		postInvalidate();
	}

	/**
	 * @param context
	 */
	public ChangeColorIconView(Context context) {
		this(context, null);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public ChangeColorIconView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public ChangeColorIconView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 初始化变量
	 */
	private void init() {
		// 初始化画笔
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(color);
		// 获得资源文件
		this.setBackgroundResource(mResource);
		Drawable background = this.getBackground();
		bitmap = drawable2Bitmap(background);

		// 设置宽高为图片的宽高
		width = bitmap.getWidth();
		height = bitmap.getHeight();

		waveY = 0;

		// 初始化Xfermode
		porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
		// 初始化path
		path = new Path();
		// 初始化画布
		mCanvas = new Canvas();
		// 创建bitmap
		bg = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		// 将新建的bitmap注入画布
		mCanvas.setBitmap(bg);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		init();
		// 画目标图，存在bg上
		drawTargetBitmap();
		// 将目标图绘制在当前画布上，起点为左边距，上边距的交点
		canvas.drawBitmap(bg, getPaddingLeft(), getPaddingTop(), null);
		if (isReflesh) {
			// 重绘，使用boolean变量isReflesh进行控制，并对外提供访问的接口,默认为true且刷新
			invalidate();
		}
	}

	private void drawTargetBitmap() {
		// 重置path
		path.reset();
		// 擦除像素
		bg.eraseColor(Color.parseColor("#00ffffff"));

		path.moveTo(0, waveY);
		path.lineTo(width, 0);
		// 与下下边界闭合
		path.lineTo(width, height);
		path.lineTo(0, height);
		// 进行闭合
		path.close();

		mCanvas.drawBitmap(bitmap, 0, 0, paint);// 画logo
		paint.setXfermode(porterDuffXfermode);// 设置Xfermode
		mCanvas.drawPath(path, paint);
		paint.setXfermode(null);// 重置Xfermode
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 获得宽高测量模式和大小
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		// 保存测量结果
		int width, height;

		if (widthMode == MeasureSpec.EXACTLY) {
			// 宽度加左右内边距
			width = widthSize + getPaddingLeft() + getPaddingRight();
		} else {
			// 宽度加左右内边距
			width = this.width + getPaddingLeft() + getPaddingRight();
			if (widthMode == MeasureSpec.AT_MOST) {
				// 取小的那个
				width = Math.min(width, widthSize);
			}

		}

		if (heightMode == MeasureSpec.EXACTLY) {
			// 高度加左右内边距
			height = heightSize + getPaddingTop() + getPaddingBottom();
		} else {
			// 高度加左右内边距
			height = this.height + getPaddingTop() + getPaddingBottom();

			if (heightMode == MeasureSpec.AT_MOST) {
				// 取小的那个
				height = Math.min(height, heightSize);
			}

		}
		// 设置高度宽度为logo宽度和高度,实际开发中应该判断MeasureSpec的模式，进行对应的逻辑处理,这里做了简单的判断测量
		setMeasuredDimension(width, height);

	}

	/**
	 * drawable转换bitmap
	 * 
	 * @return bitmap
	 */
	private Bitmap drawable2Bitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}
}
