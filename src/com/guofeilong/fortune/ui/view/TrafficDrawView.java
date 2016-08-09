package com.guofeilong.fortune.ui.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.guofeilong.fortune.R;
import com.guofeilong.fortune.utils.ViewUtils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

public class TrafficDrawView extends View {
	private static final String TAG = "TrafficDrawView";

	/**
	 * 总刻度
	 */
	private int mScaleCount;
	/**
	 * 一刻度长度
	 */
	private int mBaseScaleLength;
	private int mBaseScaleMark;
	private int mBaseBarWidth;
	/**
	 * 文字显示信息
	 */
	private int mTopText;
	private int mBottomText;

	/**
	 * 线形图小圆半径
	 */
	private int mCircleRadius;

	/**
	 * 最大高度
	 */
	private int mMaxHeight;

	/**
	 * 数据
	 */
	private Map<String, Map<String, TrafficData>> mTrafficData;
	/**
	 * 文字大小
	 */
	private int mTextSize;
	public static final int COLOR_LINE = Color.rgb(132, 139, 143);
	private int mBaseLineY;
	private Paint mBaseLinePaint;
	private Paint mBarPaint;
	private int mBaseLineBetweenDistance;
	private List<String> dateList;
	private List<TrafficData> sumInfos;
	private int[] colors;
	private int mViewHeight;
	private int mDrawWidth;

	public TrafficDrawView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		colors = getResources().getIntArray(R.array.water_colors);
		Resources resources = getContext().getResources();
		mBaseScaleMark = resources.getDimensionPixelSize(R.dimen.base_mark);
		mTopText = resources.getDimensionPixelSize(R.dimen.top_text);
		mBottomText = resources.getDimensionPixelSize(R.dimen.bottom_text);
		mCircleRadius = resources.getDimensionPixelSize(R.dimen.circle_radius);
		mTextSize = ViewUtils.sp2px((Activity) getContext(), 10);
		mBaseBarWidth = resources.getDimensionPixelSize(R.dimen.base_bar_width);
		mBaseLinePaint = new Paint();
		mBaseLinePaint.setTextSize(mTextSize);
		mBaseLinePaint.setFakeBoldText(true);
		mBaseLinePaint.setAntiAlias(true);
		mBaseLinePaint.setColor(COLOR_LINE);

		mBarPaint = new Paint();
		mBarPaint.setStrokeWidth(mBaseBarWidth);
		mBarPaint.setTextSize(mTextSize);
		mBarPaint.setFakeBoldText(true);
		mBarPaint.setAntiAlias(true);

		mScaleCount = 10;
		mBaseLineBetweenDistance = ViewUtils.dpToPx(getResources(), 5);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		mViewHeight = getHeight();
		mBaseLineY = mViewHeight - mTextSize - mBottomText;
		mMaxHeight = mBaseLineY - (mTextSize + mTopText + mCircleRadius);
		Log.d(TAG, "onLayout:height=" + getHeight() + "width=" + getWidth());
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int height = View.MeasureSpec.getSize(heightMeasureSpec);
		int width = View.MeasureSpec.getSize(widthMeasureSpec);
		Log.d(TAG, "onMeasure:height=" + height + "width=" + width);

		setMeasuredDimension(mDrawWidth, height); // 这里面是原始的大小，需要重新计算可以修改本行
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Log.d(TAG, "onDraw:height=" + mViewHeight);
		if (sumInfos == null) {
			return;
		}
		for (int i = 0; i < dateList.size(); i++) {
			int scaleX = i * mBaseScaleLength;
			// 绘制刻度
			mBaseLinePaint.setStrokeWidth(1);
			canvas.drawLine(scaleX, mBaseLineY, scaleX, mBaseLineY + mBaseScaleMark, mBaseLinePaint);
			// 绘制日期
//			String[] dateArr = dateList.get(i).split("-");
//			String date = dateArr[2] + "/" + dateArr[1];
			String date = dateList.get(i);
			int textX_date = getTextDateDrawX(mBarPaint, date, i * mBaseScaleLength);
			int textY_date = mBaseLineY + mBottomText;
			// mBaseLinePaint.setTypeface(BusinessHandler.getInstance().mDefendTrafficTypeface);
			canvas.drawText(date, textX_date, textY_date, mBaseLinePaint);
			// 绘制日期长线
			mBaseLinePaint.setStrokeWidth(1);
			canvas.drawLine(0, mBaseLineY, mDrawWidth, mBaseLineY, mBaseLinePaint);

			if (mTrafficData != null && sumInfos != null) {
				if (null != mTrafficData && 0 != mTrafficData.size()) {
					Map<String, TrafficData> oneDayTrafficData;
					oneDayTrafficData = mTrafficData.get(dateList.get(i));
					if (oneDayTrafficData == null) {
						oneDayTrafficData = new HashMap<String, TrafficData>();
					}
					int totalBarWith = sumInfos.size() * mBaseBarWidth + (sumInfos.size() - 1) * mBaseLineBetweenDistance;
					int startX = (mBaseScaleLength - totalBarWith) / 2 + mBaseBarWidth / 2;
					for (int j = 0; j < sumInfos.size(); j++) {
						TrafficData maxTrafficData = sumInfos.get(j);
						TrafficData trafficData;
						trafficData = oneDayTrafficData.get(maxTrafficData.getGroupId());
						if (trafficData == null) {
							trafficData = new TrafficData();
							trafficData.setGroupId(maxTrafficData.getGroupId());
							trafficData.setData(0);
						}
						changeBarPaintColor(j);

						trafficData.setMaxValue(maxTrafficData.getData());
						drawBarGraph(canvas, trafficData, i * mBaseScaleLength + startX + mBaseLineBetweenDistance * j + mBaseBarWidth * j);
					}
				}
			}
		}
	}

	public static int dpToPx(Resources res, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
	}

	private void drawBarGraph(Canvas canvas, TrafficData data, int barX) {
		String d = String.valueOf(data.getData());
		float barY = mBaseLineY - mathBarHeight(data);
		canvas.drawLine(barX, mBaseLineY, barX, barY, mBarPaint);
		int textX_data = getTextDataDrawX(mBaseLinePaint, d, barX);
		float textY_data = barY - mTopText;
		canvas.drawText(d, textX_data, textY_data, mBaseLinePaint);
	}

	private int getTextDateDrawX(Paint p, String data, int base) {
		return base + (mBaseScaleLength - (int) p.measureText(data)) / 2;
	}

	private int getTextDataDrawX(Paint p, String data, int base) {
		return base + (mBaseBarWidth - (int) p.measureText(data)) / 2 - mBaseBarWidth / 2;
	}

	/**
	 * 此时画笔颜色
	 */
	private void changeBarPaintColor(int index) {
		int color = colors[index % colors.length];
		mBarPaint.setColor(color);
	}

	/**
	 * 计算高度
	 * 
	 * @return
	 */
	private float mathBarHeight(TrafficData data) {
		float height = 0;
		if (data.getMaxValue() == 0) {
			return 0;
		} else {
			height = ((float) data.getData() * mMaxHeight) / (float) data.getMaxValue();
		}
		float speed = (height / 20);
		float currentheight = data.getCurrentHeight();
		if (currentheight < height) {
			currentheight = currentheight + speed;
			data.setCurrentHeight(currentheight);
			invalidate();
			System.out.println(currentheight);
			return currentheight;
		}
		return height;
	}

	/**
	 * 设置数据
	 * 
	 * @param listData
	 * @param day
	 *            当天
	 */
	public void setTrafficData(Map<String, Map<String, TrafficData>> map, List<TrafficData> sumInfo, List<String> date) {
		mTrafficData = map;
		sumInfos = sumInfo;
		dateList = date;
		mBaseScaleLength = mBaseBarWidth * (sumInfos.size() * 2 + 3);
		mDrawWidth = mScaleCount * mBaseScaleLength + 1;
		setMeasuredDimension(mDrawWidth, getMeasuredHeight()); // 这里面是原始的大小，需要重新计算可以修改本行
		setVisibility(View.GONE);
		setVisibility(View.VISIBLE);
		postInvalidate();
		Log.d(TAG, "setTrafficData mDrawWidth=" + mDrawWidth + "getMeasuredHeight()" + getMeasuredHeight());

	}

	public static class TrafficData implements Comparable<TrafficData> {
		public TrafficData(int data, String date) {
			this.setData(data);
			this.date = date;
		}

		public TrafficData() {
		}

		private String name;
		private long maxValue;
		private float currentHeight;
		private String groupId;
		private long data; // 数据
		private int measureId;

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		private String date; // 日期

		@Override
		public int compareTo(TrafficData another) {
			int x = Integer.valueOf(this.date.split("-")[1]);
			int y = Integer.valueOf(another.date.split("-")[1]);
			if (x > y) {
				return 1;
			} else {
				return -1;
			}
		}

		public float getCurrentHeight() {
			return currentHeight;
		}

		public void setCurrentHeight(float currentHeight) {
			this.currentHeight = currentHeight;
		}

		public int getMeasureId() {
			return measureId;
		}

		public void setMeasureId(int measureId) {
			this.measureId = measureId;
		}

		public String getGroupId() {
			return groupId;
		}

		public void setGroupId(String groupId) {
			this.groupId = groupId;
		}

		public long getData() {
			return data;
		}

		public void setData(long data) {
			this.data = data;
		}

		public long getMaxValue() {
			return maxValue;
		}

		public void setMaxValue(long maxValue) {
			this.maxValue = maxValue;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}
}
