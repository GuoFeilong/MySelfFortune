//package com.guofeilong.fortune.ui.view;
//
//import com.guofeilong.fortune.R;
//import com.guofeilong.fortune.ui.view.AudioManager.AudioStateListener;
//
//import android.R.interpolator;
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.os.Environment;
//import android.os.Handler;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Switch;
//
//@SuppressLint("NewApi")
//public class AudioButton extends Button implements AudioStateListener {
//	private static final int DISTANCE_Y_CANCEL = 50;
//	private static final int STATE_NORMAL = 1;
//	private static final int STATE_RECORD = 2;
//	private static final int STATE_WANT_CANCEL = 3;
//	private int mCurrentState = STATE_NORMAL;
//	private boolean isRecord;
//	// 是否出发onclick
//	private boolean isReady;
//	private DialogManager mDialogManager;
//	private AudioManager mAudioManager;
//
//	/**
//	 * 录音结束的listener回调
//	 * 
//	 * @author guofl
//	 * 
//	 */
//	public interface AudioFinishRecordListener {
//		void onFinish(float secone, String filePath);
//
//	}
//
//	private AudioFinishRecordListener mListener;
//
//	public void setAudioFinishRecordListener(AudioFinishRecordListener listener) {
//		mListener = listener;
//	}
//
//	// when init view will call two parameter construct method
//	public AudioButton(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		mDialogManager = new DialogManager(context);
//		String dir = Environment.getExternalStorageDirectory() + "/guofl_audios";
//		mAudioManager = AudioManager.getInstance(dir);
//		mAudioManager.setOnAudioStateListener(this);
//		setOnLongClickListener(new OnLongClickListener() {
//
//			@Override
//			public boolean onLongClick(View v) {
//				isReady = true;
//				mAudioManager.prepareAudio();
//				return false;
//			}
//		});
//	}
//
//	private static final int MSG_AUDIO_PREPARED = 0;
//	private static final int MSG_VOICE_CHANGE = 1;
//	private static final int MSG_DIALOG_DISMISS = 2;
//	protected static final int MAX_AUDIO_LEVEL = 7;
//	private float mTime;
//	private Runnable mGetVoiceLevel = new Runnable() {
//
//		@Override
//		public void run() {
//			while (isRecord) {
//				try {
//					Thread.sleep(100);
//					mTime += 0.1;
//					mHandler.sendEmptyMessage(MSG_VOICE_CHANGE);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//			}
//		}
//	};
//	private Handler mHandler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			switch (msg.what) {
//			case MSG_AUDIO_PREPARED:
//				mDialogManager.showDialog();
//				isRecord = true;
//				new Thread(mGetVoiceLevel).start();
//
//				break;
//			case MSG_VOICE_CHANGE:
//				mDialogManager.updateVoice(mAudioManager.getVoiceLevel(MAX_AUDIO_LEVEL));
//				break;
//			case MSG_DIALOG_DISMISS:
//				mDialogManager.dissMissDialog();
//				break;
//
//			default:
//				break;
//			}
//		};
//	};
//
//	@Override
//	public void wellPrepared() {
//		mHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
//	}
//
//	public AudioButton(Context context) {
//		this(context, null);
//	}
//
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		int x = (int) event.getX();
//		int y = (int) event.getY();
//		switch (event.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			// test
//			changeButtonState(STATE_RECORD);
//			break;
//		case MotionEvent.ACTION_MOVE:
//			if (isRecord) {
//				if (isWantCancel(x, y)) {
//					changeButtonState(STATE_WANT_CANCEL);
//				} else {
//					changeButtonState(STATE_RECORD);
//				}
//			}
//			break;
//		case MotionEvent.ACTION_UP:
//			if (!isReady) {
//				reset();
//				return super.onTouchEvent(event);
//			}
//			if (!isRecord || mTime < 0.6) {
//				mDialogManager.tooShort();
//				mAudioManager.cancel();
//				mHandler.sendEmptyMessageDelayed(MSG_DIALOG_DISMISS, 1300);
//			} else if (mCurrentState == STATE_RECORD) {
//				// 正常录音结束的传入 callback 保存录音
//				mDialogManager.dissMissDialog();
//				mAudioManager.release();
//				if (mListener != null) {
//					mListener.onFinish(mTime, mAudioManager.getCurrentFilePath());
//				}
//
//			} else if (mCurrentState == STATE_WANT_CANCEL) {
//				// reset
//				mDialogManager.dissMissDialog();
//				mAudioManager.cancel();
//			}
//			reset();
//			break;
//
//		default:
//			break;
//		}
//		return super.onTouchEvent(event);
//	}
//
//	private void reset() {
//		isReady = false;
//		isRecord = false;
//		mTime = 0;
//		changeButtonState(STATE_NORMAL);
//		setTextColor(getResources().getColor(R.color.conmmd_textcolor));
//	}
//
//	/**
//	 * judge is want to cancel
//	 * 
//	 * @param x
//	 * @param y
//	 */
//	private boolean isWantCancel(int x, int y) {
//		if (x < 0 || x > getWidth()) {
//			return true;
//		}
//		if (y < -DISTANCE_Y_CANCEL || y > DISTANCE_Y_CANCEL + getHeight()) {
//			return true;
//		}
//		return false;
//	}
//
//	/**
//	 * change the background of the button base on different state
//	 * 
//	 * @param stateRecord
//	 */
//	private void changeButtonState(int stateRecord) {
//		if (mCurrentState != stateRecord) {
//			mCurrentState = stateRecord;
//			switch (stateRecord) {
//			case STATE_NORMAL:
//				setBackground(getResources().getDrawable(R.drawable.bg_audio_button_normal));
//				setText(getResources().getString(R.string.state_normal));
//				break;
//			case STATE_RECORD:
//				setBackground(getResources().getDrawable(R.drawable.bg_audio_button_cancel));
//				setText(getResources().getString(R.string.state_press));
//				setTextColor(getResources().getColor(R.color.demo_title1));
//				if (isRecord) {
//					mDialogManager.recording();
//				}
//				break;
//			case STATE_WANT_CANCEL:
//				setBackground(getResources().getDrawable(R.drawable.bg_audio_button_cancel));
//				setTextColor(getResources().getColor(R.color.demo_title3));
//				setText(getResources().getString(R.string.state_cancel));
//				mDialogManager.wantToCancel();
//				break;
//
//			default:
//				break;
//			}
//		}
//	}
//
//}
