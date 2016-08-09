package com.guofeilong.fortune.ui.view;

import java.util.ArrayList;
import java.util.List;

import com.guofeilong.fortune.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * 扇形图
 * 
 * @author AndroidDr.Lee
 */
public class PieChatView extends View {

	public interface OnChatChangeListener {
		public void onChatChange(Object object);
	}

	public interface OnChatItemClickListener {
		public void onChatItemClick(Object object);
	}

	private static final String TAG = "ChatSurfaceView";

	public static final int[] CHAT_COLOR = { 0xFF00b5e8, 0xFFb89ef8, 0xFFc8d339, 0xFFedb72b, 0xFFe89fbd, 0xFFffe51e };

	private DrawingRunnalbe mDrawingRunnable = new DrawingRunnalbe();
	private ChatViewController mController;
	private ArrayList<ChatItem> mChatItemList = new ArrayList<ChatItem>();
	private float mViewWidth;
	private float mViewHeight;
	private float mSelectOffset;
	private Paint mPaint;

	public Paint getmPaint() {
		return mPaint;
	}

	public void setmPaint(Paint mPaint) {
		this.mPaint = mPaint;
	}

	public ChatViewController getmController() {
		return mController;
	}

	private OnChatChangeListener mOnChatChangeListener;
	private OnChatItemClickListener mOnChatItemClickListener;

	public PieChatView(Context context) {
		super(context);
		init();
	}

	public PieChatView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		mPaint = new Paint();
		mPaint.setStyle(Style.FILL);
		mPaint.setAntiAlias(true);

		mController = new ChatViewController();
		setOnTouchListener(mController);

		Resources resources = getContext().getResources();
		mViewWidth = resources.getDimension(R.dimen.chatview_width);
		mViewHeight = resources.getDimension(R.dimen.chatview_heigh);
		mSelectOffset = resources.getDimension(R.dimen.chatview_select_offset);

		mController.mRectF.set(mSelectOffset, mSelectOffset, mViewWidth - mSelectOffset, mViewHeight - mSelectOffset);
		mController.mSelectRectF.set(mSelectOffset, mSelectOffset * 2, mViewWidth - mSelectOffset, mViewHeight);
		mController.mCenterX = mViewWidth / 2;
		mController.mCenterY = mViewHeight / 2;
	}

	public void setOnChatChangeListener(OnChatChangeListener listener) {
		mOnChatChangeListener = listener;
	}

	/**
	 * 设置数据
	 * 
	 * @param array
	 */
	public void setData(long[] value, List<?> items) {
		synchronized (mChatItemList) {
			try {
				mChatItemList.clear();
				int sum = 0;
				// 封装数据
				for (int i = 0, size = value.length; i < size; i++) {
					sum = sum + (int) value[i];
				}

				if (sum > 0) {
					float start_sweep = 0;
					for (int i = 0, size = value.length; i < size; i++) {
						int key = (int) value[i];
						ChatItem chatItem = getChatItem(i, start_sweep, (float) (key / (float) sum), items.get(i));
						mChatItemList.add(chatItem);
						start_sweep = start_sweep + chatItem.value_sweep;
					}
				} else {
					// TODO
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		mController.autoFind();
	}

	/**
	 * 
	 * 获取一个扇形结构
	 * 
	 * @param index
	 * @param start_sweep
	 * @param per
	 * @param item
	 * @return
	 */
	private ChatItem getChatItem(int index, float start_sweep, float per, Object item) {
		ChatItem chat = new ChatItem();
		chat.color = getColor(index);
		chat.start_sweep = start_sweep;
		chat.value_sweep = 360f * per;
		chat.item = item;
		return chat;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		mController.drawCirle(canvas, mPaint);
	}

	class DrawingRunnalbe implements Runnable {
		boolean mRunning = true;

		@Override
		public void run() {
			if (mRunning && !(mController.mState == State.FINISH)) {
				mController.handleActionState();
				invalidate();
				postDelayed(mDrawingRunnable, 16);
			}
		}

		public void stop() {
			mRunning = false;
			removeCallbacks(this);
		}

		public void start() {
			mRunning = true;
		}
	}

	private class ChatItem {
		Object item;
		int color;
		float start_sweep;
		float value_sweep;
	}

	private int getColor(int index) {
		return CHAT_COLOR[index];
	}

	public void setOnChatItemClickListener(OnChatItemClickListener mOnChatItemClickListener) {
		this.mOnChatItemClickListener = mOnChatItemClickListener;
	}

	public enum State {
		/**
		 * 触屏
		 */
		ONTOUCH,
		/**
		 * 自动寻址
		 */
		AUTOFIND,
		/**
		 * 
		 */
		INERTIA,
		/**
		 * 结束
		 */
		FINISH
	}

	public class ChatViewController implements OnTouchListener {
		/**
		 * 指针位置
		 */
		private static final float POINT_ITEM_ANGLE = 90f;
		private RectF mRectF = new RectF();
		private RectF mSelectRectF = new RectF();
		float mCurrentAngle = 0f;
		// 中心点
		float mCenterX;
		float mCenterY;
		// 最后触屏坐标
		private float mLastX = 0;
		private float mLastY = 0;

		private State mState;
		// 最终角度
		private float mFinshAngle = 0;
		// 选择
		private int mSelectIndex = -1;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			ViewGroup group = (ViewGroup) getParent();
			if (group != null) {
				group.requestDisallowInterceptTouchEvent(true);
			}
			// 获取当前坐标
			float currentX = event.getX();
			float currentY = event.getY();
			// 获取view偏移量
			float left = 0;
			float top = 0;
			// 相对位置
			float relativeX = currentX - left;
			float relativeY = currentY - top;

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mAutoPer = 0;
				mDrawingRunnable.stop();
				mLastX = relativeX;
				mLastY = relativeY;
				mState = State.ONTOUCH;
				mActionDownTime = System.currentTimeMillis();
				break;
			case MotionEvent.ACTION_MOVE:
				float c = (float) Math.sqrt((Math.pow(Math.abs((mLastX - relativeX)), 2) + Math.pow(Math.abs((mLastY - relativeY)), 2)));
				float a = (float) Math.sqrt((Math.pow(Math.abs(mLastX - mCenterX), 2) + Math.pow(Math.abs((mLastY - mCenterY)), 2)));
				float b = (float) Math.sqrt((Math.pow(Math.abs(relativeX - mCenterX), 2) + Math.pow(Math.abs((relativeY - mCenterY)), 2)));
				float cosc = (float) (Math.pow(a, 2) + Math.pow(b, 2) - Math.pow(c, 2)) / (2 * a * b);

				// 确定方向
				if (rotateDirection(relativeX, relativeY)) {
					mCurrentAngle = (float) (mCurrentAngle + Math.acos(cosc) * 180 / Math.PI);
				} else {
					mCurrentAngle = (float) (mCurrentAngle - Math.acos(cosc) * 180 / Math.PI);
				}
				// 重置
				mLastX = relativeX;
				mLastY = relativeY;

				invalidate();
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				long currentTimeMillis = System.currentTimeMillis();

				if (currentTimeMillis - mActionDownTime < 100) {
					if (mOnChatItemClickListener != null) {
						mOnChatItemClickListener.onChatItemClick(mChatItemList.get(mSelectIndex).item);
					}
				}
				autoFind();
				break;
			default:
				break;
			}
			return true;
		}

		void autoFind() {
			mCurrentAngle = formatAngle(mCurrentAngle);
			mState = State.AUTOFIND;
			// 计算最终角度
			mFinshAngle = getAutoFinishAngle();
			mDrawingRunnable.start();
			postDelayed(mDrawingRunnable, 16);
		}

		/**
		 * 
		 * 计算自动寻址最终角度
		 * 
		 * @return
		 */
		private float getAutoFinishAngle() {
			float pointBaseValue = POINT_ITEM_ANGLE;
			if (mCurrentAngle % 360 > POINT_ITEM_ANGLE) {
				pointBaseValue = POINT_ITEM_ANGLE + 360;
			}
			for (int i = 0, size = mChatItemList.size(); i < size; i++) {
				ChatItem chat = mChatItemList.get(i);
				float angle = mCurrentAngle + chat.start_sweep;
				// 查找当前item
				if (pointBaseValue >= angle && pointBaseValue < angle + chat.value_sweep) {
					mSelectIndex = i;
					float middle = angle + chat.value_sweep / 2;
					// 判断方向
					if (middle >= pointBaseValue) {
						return mCurrentAngle - (middle - pointBaseValue);
					} else {
						return mCurrentAngle + (pointBaseValue - middle);
					}
				}
			}
			return 0;
		}

		public void moveToNext() {
			ChatItem chatItem1 = mChatItemList.get(mSelectIndex);
			ChatItem chatItem2;
			if (mSelectIndex == mChatItemList.size() - 1) {
				chatItem2 = mChatItemList.get(0);
				mSelectIndex = 0;
			} else {
				chatItem2 = mChatItemList.get(mSelectIndex + 1);
				mSelectIndex = mSelectIndex + 1;

			}
			mCurrentAngle = formatAngle(mCurrentAngle);
			mState = State.AUTOFIND;
			// 计算最终角度
			mFinshAngle = mCurrentAngle + chatItem1.value_sweep / 2 + chatItem2.value_sweep / 2;
			mDrawingRunnable.start();
			postDelayed(mDrawingRunnable, 16);

		}

		/**
		 * 格式化角度
		 * 
		 * @param angle
		 * @return
		 */
		private float formatAngle(float angle) {
			if (angle < 0) {
				int n = (int) Math.abs(angle / 360f) + 1;
				return angle + n * 360;
			} else {
				int n = (int) Math.abs(angle / 360);
				return angle - n * 360;
			}
		}

		/**
		 * 判断方向
		 * 
		 * @param x
		 * @param y
		 * @return
		 */
		private boolean rotateDirection(float x, float y) {
			float first_angle = getAngle((mLastX - mCenterX), (mLastY - mCenterY));
			float second_angle = getAngle((x - mCenterX), (y - mCenterY));
			if ((90 > first_angle) && (second_angle > 270)) {
				first_angle = first_angle + (float) 360;
			}
			if ((90 > second_angle) && (first_angle > 270)) {
				second_angle = second_angle + (float) 360;
			}

			return second_angle > first_angle;
		}

		private float getAngle(float x, float y) {
			if ((x == 0) && (y == 0)) {
				return 0;
			}
			float angle = (float) (Math.atan(y / x) * 180 / Math.PI);
			if (x == 0) {
				if (y > 0) {
					return 90;
				} else {
					return 270;
				}
			}
			if (x > 0) {
				if (y < 0) {
					return (float) 360 + angle;
				}
			}
			if (x < 0) {
				if (y > 0) {
					return (float) 180 + angle;
				} else if (y == 0) {
					return 180;
				} else if (y < 0) {
					return (float) 180 + angle;
				}
			}
			return angle;
		}

		void drawCirle(Canvas canvas, Paint paint) {
			for (int i = 0, size = mChatItemList.size(); i < size; i++) {
				RectF rect = mRectF;
				ChatItem chatItem = mChatItemList.get(i);
				paint.setColor(chatItem.color);
				if (size > 1 && i == mSelectIndex && mState == State.FINISH && chatItem.value_sweep < 180) {
					rect = mSelectRectF;
				}
				canvas.drawArc(rect, mController.mCurrentAngle + chatItem.start_sweep, chatItem.value_sweep, true, paint);
				// float r = mViewWidth / 2;
				// float z = (float) (2 * Math.PI * r);
				// float sweep = (chatItem.start_sweep - chatItem.value_sweep) /
				// 2;
				// float x = (float) (Math.sin(sweep) * r + r);
				// float y = (float) (Math.cos(sweep) * r + mSelectOffset);
				// Paint p = new Paint();
				// Path path = new Path();
				// path.addArc(rect, mController.mCurrentAngle +
				// chatItem.start_sweep + chatItem.value_sweep / 2,
				// chatItem.value_sweep);
				// canvas.drawTextOnPath("盛寅鹏", path, 0, 10, p);

			}
		}

		float mAutoPer = 0;
		private long mActionDownTime;

		void handleActionState() {
			switch (mState) {
			case FINISH:
				break;
			case AUTOFIND:
				if (mAutoPer == 0) {
					mAutoPer = Math.abs(Math.abs(mCurrentAngle) - Math.abs(mFinshAngle)) / 8;
				}
				// 方向
				boolean direction = mFinshAngle > mCurrentAngle;
				if (direction) {
					mCurrentAngle = mCurrentAngle + mAutoPer;
				} else {
					mCurrentAngle = mCurrentAngle - mAutoPer;
				}

				if (Math.abs(Math.abs(mCurrentAngle) - Math.abs(mFinshAngle)) <= mAutoPer) {
					mCurrentAngle = mFinshAngle;
					if (null != mOnChatChangeListener && null != mChatItemList && 0 < mChatItemList.size()) {
						mOnChatChangeListener.onChatChange(mChatItemList.get(mSelectIndex).item);
					}
					mState = State.FINISH;
					mAutoPer = 0;
					invalidate();
				}
				break;
			default:
				break;
			}
		}
	}

}
