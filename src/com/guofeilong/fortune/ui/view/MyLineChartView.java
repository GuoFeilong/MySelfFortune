package com.guofeilong.fortune.ui.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.guofeilong.fortune.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

@SuppressLint("ViewConstructor")
public class MyLineChartView extends View {

	private static final String MOUTH_UNIT = "月";
	public static int CIRCLE_SIZE = 14;
	public static int CIRCLE_ADD = 0;
	public static int PAINT_WIDTH = 5;
	public static int PAINT_LINE_WIDTH = 5;
	public static int COLOR_SOLID = 1;
	private int mColor;
	private Point mSelectedPoint;

	public void setmColor(int mColor) {
		this.mColor = mColor;
	}

	// 枚举实现坐标桌面的样式风格
	public static enum Mstyle {
		Line, Curve
	}

	private Mstyle mstyle = Mstyle.Line;
	private Point[] mPoints = new Point[100];

	private Context context;
	private int bheight = 0;
	private HashMap<Double, Double> map;
	private ArrayList<Double> dlk;
	private double totalvalue = 30;
	private double pjvalue = 5;
	private String xstr, ystr = "";// 横纵坐标的属性
	private int margint = 15;
	private int marginb = 10;
	private int c_c = 0;
	private int resid = 0;
	private Boolean isylineshow;
	private int currentMouth;

	public int getCurrentMouth() {
		return currentMouth;
	}

	public void setCurrentMouth(int currentMouth) {
		this.currentMouth = currentMouth;
	}

	/**
	 * @param map
	 *            需要的数据，虽然key是double，但是只用于排序和显示，与横向距离无关
	 * @param totalvalue
	 *            Y轴的最大值
	 * @param pjvalue
	 *            Y平均值
	 * @param xstr
	 *            X轴的单位
	 * @param ystr
	 *            Y轴的单位
	 * @param isylineshow
	 *            是否显示纵向网格
	 * @return
	 */
	public void SetTuView(HashMap<Double, Double> map, double totalvalue, double pjvalue, String xstr, String ystr, Boolean isylineshow) {
		this.map = map;
		this.totalvalue = totalvalue;
		this.pjvalue = pjvalue;
		this.xstr = xstr;
		this.ystr = ystr;
		this.isylineshow = isylineshow;
		// 屏幕横向
		// act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

	public MyLineChartView(Context ct) {
		super(ct);
		this.context = ct;
	}

	public MyLineChartView(Context ct, AttributeSet attrs) {
		super(ct, attrs);
		this.context = ct;
	}

	public MyLineChartView(Context ct, AttributeSet attrs, int defStyle) {
		super(ct, attrs, defStyle);
		this.context = ct;
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (c_c != 0)
			this.setbg(c_c);
		if (resid != 0)
			this.setBackgroundResource(resid);
		dlk = getintfrommap(map);
		int height = getHeight();
		if (bheight == 0)
			bheight = height - marginb;

		int width = getWidth();
		int blwidh = dip2px(context, 50);// --y轴距离左边界的宽度
		double pjsize = totalvalue / pjvalue;// 界面布局的尺寸的比例
		// set up paint
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.GRAY);
		paint.setColor(Color.rgb(218, 218, 218));
		// 分割线的宽度
		paint.setStrokeWidth(1);
		paint.setStyle(Style.STROKE);
		// --改变顶点线的颜色
		for (int i = 0; i < pjsize + 1; i++) {
			if (i == pjsize)
				paint.setColor(Color.GRAY);
			paint.setColor(Color.rgb(218, 218, 218));
			canvas.drawLine(blwidh, (float) (bheight - (bheight / pjsize) * i) + margint, width, (float) (bheight - (bheight / pjsize) * i + margint), paint);// Y坐标
			Paint tempPaint = new Paint();
			tempPaint.setAlpha(0x0000ff);
			tempPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.traffic_day_use_detail_size));
			drawMyline(String.format("%.1f%s", pjvalue * i, ystr), blwidh / 2, bheight - (bheight / pjsize) * i + margint, canvas, tempPaint);
		}
		ArrayList<Integer> xlist = new ArrayList<Integer>();// 记录每个x的值
		// 画直线（纵向）
		paint.setColor(Color.rgb(245, 245, 245));
		if (dlk == null)
			return;
		for (int i = 0; i < dlk.size(); i++) {
			xlist.add(blwidh + (width - blwidh) / dlk.size() * i);
			if (isylineshow) {
				canvas.drawLine(blwidh + (width - blwidh) / dlk.size() * i, margint, blwidh + (width - blwidh) / dlk.size() * i, bheight + margint, paint);
			}
			// --条件控制绘制xzhou
			if (i == 0 || i == dlk.size() - 1) {
				Paint tempPaint = new Paint();
				tempPaint.setAlpha(0x0000ff);
				tempPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.traffic_day_use_detail_size));
				// --数值控制x轴的位置
				drawMyline(currentMouth + MOUTH_UNIT + String.format("%d%s", dlk.get(i).intValue(), xstr), blwidh + (width - blwidh) / dlk.size() * i, bheight + 75, canvas, tempPaint);// X坐标
			}
		}

		// 点的操作设置
		mPoints = getpoints(dlk, map, xlist, totalvalue, bheight);
		// --改变折线的颜色和宽度
		PAINT_LINE_WIDTH = getResources().getDimensionPixelSize(R.dimen.traffic_day_use_paintline_width);
		paint.setColor(Color.rgb(11, 211, 237));
		paint.setColor(mColor);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(PAINT_LINE_WIDTH);

		if (mstyle == Mstyle.Curve)
			drawscrollline(mPoints, canvas, paint);
		else
			drawline(mPoints, canvas, paint);

		for (int i = 0; i < mPoints.length; i++) {
			// 转折点的样式,返回的形状决定
			paint.setColor(mColor);
			// paint.setColor(Color.rgb(11, 211, 237));
			paint.setStyle(Style.FILL);
			CIRCLE_SIZE = getResources().getDimensionPixelSize(R.dimen.traffic_day_use_circle_size);
			canvas.drawCircle(mPoints[i].x, mPoints[i].y, CIRCLE_SIZE, paint);
			paint.setStrokeWidth(PAINT_WIDTH - COLOR_SOLID);
			paint.setColor(Color.rgb(245, 245, 245));
			paint.setStyle(Style.FILL);
			float dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.traffic_day_use_white_circle_width);
		}
	}

	private void drawscrollline(Point[] ps, Canvas canvas, Paint paint) {
		Point startp = new Point();
		Point endp = new Point();
		for (int i = 0; i < ps.length - 1; i++) {
			startp = ps[i];
			endp = ps[i + 1];
			int wt = (startp.x + endp.x) / 2;
			Point p3 = new Point();
			Point p4 = new Point();
			p3.y = startp.y;
			p3.x = wt;
			p4.y = endp.y;
			p4.x = wt;

			Path path = new Path();
			path.moveTo(startp.x, startp.y);
			path.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
			canvas.drawPath(path, paint);

		}
	}

	private void drawline(Point[] ps, Canvas canvas, Paint paint) {
		Point startp = new Point();
		Point endp = new Point();
		for (int i = 0; i < ps.length - 1; i++) {
			startp = ps[i];
			endp = ps[i + 1];
			int temp = CIRCLE_SIZE / 2;
			temp = 0;
			canvas.drawLine(startp.x + temp + CIRCLE_ADD, startp.y + temp + CIRCLE_ADD, endp.x - temp + CIRCLE_ADD, endp.y + temp + CIRCLE_ADD, paint);
		}
	}

	private Point[] getpoints(ArrayList<Double> dlk, HashMap<Double, Double> map, ArrayList<Integer> xlist, double max, int h) {
		Point[] points = new Point[dlk.size()];
		for (int i = 0; i < dlk.size(); i++) {
			int ph = h - (int) (h * (map.get(dlk.get(i)) / max));

			points[i] = new Point(xlist.get(i), ph + margint);
		}
		return points;
	}

	private void drawMyline(String text, int x, double y, Canvas canvas, Paint p) {
		p.setColor(Color.rgb(101, 101, 101));
		p.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(text, x, (float) y, p);
	}

	public int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	@SuppressWarnings("rawtypes")
	public ArrayList<Double> getintfrommap(HashMap<Double, Double> map) {
		ArrayList<Double> dlk = new ArrayList<Double>();
		int position = 0;
		if (map == null)
			return null;
		Set set = map.entrySet();
		Iterator iterator = set.iterator();

		while (iterator.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry mapentry = (Map.Entry) iterator.next();
			dlk.add((Double) mapentry.getKey());
		}
		for (int i = 0; i < dlk.size(); i++) {
			int j = i + 1;
			position = i;
			Double temp = dlk.get(i);
			for (; j < dlk.size(); j++) {
				if (dlk.get(j) < temp) {
					temp = dlk.get(j);
					position = j;
				}
			}

			dlk.set(position, dlk.get(i));
			dlk.set(i, temp);
		}
		return dlk;

	}

	public void setbg(int c) {
		this.setBackgroundColor(c);
	}

	public HashMap<Double, Double> getMap() {

		return map;
	}

	public void setMap(HashMap<Double, Double> map) {
		this.map = map;
	}

	public double getTotalvalue() {
		return totalvalue;
	}

	public void setTotalvalue(double yMaxValue) {
		this.totalvalue = yMaxValue;
	}

	public double getPjvalue() {
		return pjvalue;
	}

	public void setPjvalue(double yAVG) {
		this.pjvalue = yAVG;
	}

	public String getXstr() {
		return xstr;
	}

	public void setXstr(String xstr) {
		this.xstr = xstr;
	}

	public String getYstr() {
		return ystr;
	}

	public void setYstr(String ystr) {
		this.ystr = ystr;
	}

	public int getMargint() {
		return margint;
	}

	public void setMargint(int margint) {
		this.margint = margint;
	}

	public Boolean getIsylineshow() {
		return isylineshow;
	}

	public void setIsylineshow(Boolean isylineshow) {
		this.isylineshow = isylineshow;
	}

	public int getMarginb() {
		return marginb;
	}

	public void setMarginb(int marginb) {
		this.marginb = marginb;
	}

	public Mstyle getMstyle() {
		return mstyle;
	}

	public void setMstyle(Mstyle mstyle) {
		this.mstyle = mstyle;
	}

	public int getBheight() {
		return bheight;
	}

	public void setBheight(int bheight) {
		this.bheight = bheight;
	}

	public int getC() {
		return c_c;
	}

	public void setC(int c) {
		this.c_c = c;
	}

	public int getResid() {
		return resid;
	}

	public void setResid(int resid) {
		this.resid = resid;
	}

}
