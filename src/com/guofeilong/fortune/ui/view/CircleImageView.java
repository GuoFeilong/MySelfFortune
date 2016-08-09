package com.guofeilong.fortune.ui.view;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 
 * @ClassName: CircleImageView
 * @Description:灰边圆形
 * @author: Android_Tian
 * @date: 2014-11-4 下午5:01:50
 */

public class CircleImageView extends ImageView {
	private static final Xfermode MASK_XFERMODE = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
	private Bitmap mMask;
	private Paint mPaint;
	private Bitmap localBitmap;

	public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public CircleImageView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public CircleImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	protected void onDraw(Canvas paramCanvas) {
		Drawable localDrawable = getDrawable();
		if (localDrawable == null) {
			return;
		}
		if (this.mPaint == null) {
			mPaint = new Paint();
			mPaint.setXfermode(MASK_XFERMODE);
		}

		float f1 = getWidth();
		float f2 = getHeight();
		int i = paramCanvas.saveLayer(0.0F, 0.0F, f1, f2, null, 31);
		int j = getWidth();
		int k = getHeight();
		localDrawable.setBounds(0, 0, j, k);
		localDrawable.draw(paramCanvas);
		if ((this.mMask == null) || (this.mMask.isRecycled())) {
			Bitmap.Config localConfig = Bitmap.Config.ARGB_8888;
			localBitmap = Bitmap.createBitmap(j, k, localConfig);
			Canvas localCanvas = new Canvas(localBitmap);
			Paint localPaint = new Paint(1);
			localPaint.setColor(-16777216);
			RectF localRectF = new RectF(0.0F, 0.0F, f1, f2);
			localCanvas.drawOval(localRectF, localPaint);
			this.mMask = localBitmap;
		}

		paramCanvas.drawBitmap(mMask, 0.0F, 0.0F, mPaint);

		// Paint p = new Paint();
		// p.setColor(getResources().getColor(R.color.conmmd_darkgray));
		// int strokeWith = ViewUtils.dpToPx(getResources(), 2);
		// p.setStrokeWidth(strokeWith);
		// p.setStyle(Style.STROKE);
		// p.setAntiAlias(true);
		// paramCanvas.drawCircle(j / 2, k / 2, j / 2 - strokeWith / 2, p);
		paramCanvas.restoreToCount(i);
		localBitmap.recycle();
		mMask.recycle();
		return;
	}
}
