package com.guofeilong.fortune.utils;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.ui.view.MyProgressDialog;

/**
 * 
 * 视图相关的工具类
 * 
 * 
 */
public class ViewUtils {
	private ViewUtils() {
	}

	public static int dpToPx(Resources res, float f) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, f, res.getDisplayMetrics());
	}

	public static int Px2Dp(Resources res, float px) {
		final float scale = res.getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	/**
	 * drawable转换bitmap
	 * 
	 * @return bitmap
	 */
	public static Bitmap drawable2Bitmap(Context mContext, int resourceID) {
		Drawable drawable = mContext.getResources().getDrawable(resourceID);
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 切换界面跳转
	 * 
	 * @param <T>
	 */
	public static <T> void changeActivity(Context context, Class<T> clazz) {
		Intent intent = new Intent(context, clazz);
		context.startActivity(intent);
	}

	public static <T> void changeActivity(Context context, Class<T> clazz, String title) {
		Intent intent = new Intent(context, clazz);
		intent.putExtra(AppConstants.INTENT_AMOUNT, title);
		context.startActivity(intent);
	}

	public static <T> void changeActivity(Context context, Class<T> clazz, String title, int color) {
		Intent intent = new Intent(context, clazz);
		intent.putExtra(AppConstants.INTENT_AMOUNT, title);
		intent.putExtra(AppConstants.INTENT_NAME_FROM, color);
		context.startActivity(intent);
	}

	/**
	 * sp --> px
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * 自动弹出软键盘
	 */
	public static void showSoftInput(Context context, View view) {
		InputMethodManager imm = ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE));
		imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
		if (imm.isActive())
			view.requestFocus();
	}

	/**
	 * 自动隐藏软键盘
	 */
	public static void hideSolftInput(Context context) {
		InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (((Activity) context).getCurrentFocus() != null && ((Activity) context).getCurrentFocus().getWindowToken() != null) {
			manager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 自定义可编辑对话框
	 * 
	 * @param dialogTitle
	 * @param dialogContent
	 * @param onYesClickListener
	 * @param onNoClickListener
	 */
	public static void showEditTextDialog(final Context context, final OnSettingFlowDialogOnClickListener onYesClickListener, String hint) {
		final Dialog mDialog = new Dialog(context, R.style.myDialogTheme);
		mDialog.setContentView(R.layout.dialog_edittext);
		RelativeLayout mCancel = (RelativeLayout) mDialog.findViewById(R.id.rl_cancle);
		final EditText editText = (EditText) mDialog.findViewById(R.id.et_per_gift);
		RelativeLayout mConfirm = (RelativeLayout) mDialog.findViewById(R.id.rl_confirm);
		editText.setHint(hint);
		mConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String input = editText.getText().toString().trim();
				if (onYesClickListener != null) {
					boolean onClick = onYesClickListener.OnClick(input);
					if (onClick) {
						mDialog.dismiss();
					}
				}
			}
		});
		mCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});
		mDialog.show();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				showSoftInput(context, editText);
			}
		}, 200);
	}

	public static void showEditTextDialog(final Context context, final OnSettingFlowDialogOnClickListener onYesClickListener, String hint, boolean isShowMoneySign) {
		final Dialog mDialog = new Dialog(context, R.style.myDialogTheme);
		mDialog.setContentView(R.layout.dialog_edittext);
		TextView tvSign = (TextView) mDialog.findViewById(R.id.tv_india_currency_sigh);
		RelativeLayout mCancel = (RelativeLayout) mDialog.findViewById(R.id.rl_cancle);
		final EditText editText = (EditText) mDialog.findViewById(R.id.et_per_gift);
		RelativeLayout mConfirm = (RelativeLayout) mDialog.findViewById(R.id.rl_confirm);
		editText.setHint(hint);
		if (!isShowMoneySign) {
			tvSign.setVisibility(View.INVISIBLE);
		}
		mConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String input = editText.getText().toString().trim();
				if (onYesClickListener != null) {
					boolean onClick = onYesClickListener.OnClick(input);
					if (onClick) {
						mDialog.dismiss();
					}
				}
			}
		});
		mCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});
		mDialog.show();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				showSoftInput(context, editText);
			}
		}, 200);
	}

	/**
	 * 可编辑dialog传入参数可修改键盘风格 keyBoardStyle=0 表示数字 keyBoardStyle=1 表示text键盘
	 * 
	 * @param context
	 * @param onYesClickListener
	 * @param hint
	 * @param keyBoardStyle
	 */
	public static void showEditTextDialog(final Context context, final OnSettingFlowDialogOnClickListener onYesClickListener, String hint, int keyBoardStyle, String title, String unit) {
		final Dialog mDialog = new Dialog(context, R.style.myDialogTheme);
		mDialog.setContentView(R.layout.dialog_edittext);
		RelativeLayout mCancel = (RelativeLayout) mDialog.findViewById(R.id.rl_cancle);
		final TextView sigh = (TextView) mDialog.findViewById(R.id.tv_india_currency_sigh);
		sigh.setText(unit);
		final EditText editText = (EditText) mDialog.findViewById(R.id.et_per_gift);
		TextView editTitle = (TextView) mDialog.findViewById(R.id.tv_edit_title);
		editTitle.setText(title);
		RelativeLayout mConfirm = (RelativeLayout) mDialog.findViewById(R.id.rl_confirm);
		if (0 == keyBoardStyle) {
			editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
			editText.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if (TextUtils.isEmpty(s)) {
						sigh.setTextColor(context.getResources().getColor(R.color.conmmd_textcolor_s));
					} else {
						sigh.setTextColor(context.getResources().getColor(R.color.conmmd_black));
					}
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub

				}
			});
		} else if (1 == keyBoardStyle) {
			sigh.setVisibility(View.INVISIBLE);
			editText.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
		}
		editText.setHint(hint);
		mConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String input = editText.getText().toString().trim();
				if (onYesClickListener != null) {
					boolean onClick = onYesClickListener.OnClick(input);
					if (onClick) {
						mDialog.dismiss();
					}
				}
			}
		});
		mCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});
		mDialog.show();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				showSoftInput(context, editText);
			}
		}, 200);
	}

	public static interface OnSettingFlowDialogOnClickListener {
		boolean OnClick(String value);
	}

	/**
	 * 自定义特色的对话框
	 * 
	 * @param context
	 * @param message
	 * @param okLabel
	 * @param cancelLabel
	 * @param onYesClickListener
	 * @param onNoClickListener
	 * @return
	 */
	public static Dialog showYesNoDialog(Context context, String message, String okLabel, String cancelLabel, OnClickListener onYesClickListener, OnClickListener onNoClickListener) {
		Dialog dialog = null;
		if (dialog == null) {
			dialog = new Dialog(context, R.style.myDialogTheme);
		}
		dialog.setContentView(R.layout.dialog_yes_and_no);
		TextView dialogContent = (TextView) dialog.findViewById(R.id.tv_dialog_content);
		TextView cancelTextView = (TextView) dialog.findViewById(R.id.tv_cancle);
		TextView confirmTextView = (TextView) dialog.findViewById(R.id.tv_confirm);
		RelativeLayout cancel = (RelativeLayout) dialog.findViewById(R.id.rl_cancle);
		RelativeLayout confirm = (RelativeLayout) dialog.findViewById(R.id.rl_confirm);
		cancelTextView.setText(cancelLabel);
		confirmTextView.setText(okLabel);
		dialogContent.setText(message);
		confirm.setOnClickListener(onYesClickListener);
		cancel.setOnClickListener(onNoClickListener);
		dialog.show();
		return dialog;
	}

	/**
	 * 增加一个控制点击外部窗口是否取消的dialog的方法
	 * 
	 * @param context
	 * @param message
	 * @param isCancelOutSide
	 * @param okLabel
	 * @param cancelLabel
	 * @param onYesClickListener
	 * @param onNoClickListener
	 * @return
	 */
	public static Dialog showYesNoDialog(Context context, String message, boolean isCancelOutSide, String okLabel, String cancelLabel, OnClickListener onYesClickListener, OnClickListener onNoClickListener) {
		Dialog dialog = null;
		if (dialog == null) {
			dialog = new Dialog(context, R.style.myDialogTheme);
		}
		dialog.setContentView(R.layout.dialog_yes_and_no);
		TextView dialogContent = (TextView) dialog.findViewById(R.id.tv_dialog_content);
		TextView cancelTextView = (TextView) dialog.findViewById(R.id.tv_cancle);
		TextView confirmTextView = (TextView) dialog.findViewById(R.id.tv_confirm);
		RelativeLayout cancel = (RelativeLayout) dialog.findViewById(R.id.rl_cancle);
		RelativeLayout confirm = (RelativeLayout) dialog.findViewById(R.id.rl_confirm);
		cancelTextView.setText(cancelLabel);
		confirmTextView.setText(okLabel);
		dialogContent.setText(message);
		dialog.setCanceledOnTouchOutside(isCancelOutSide);
		confirm.setOnClickListener(onYesClickListener);
		cancel.setOnClickListener(onNoClickListener);
		dialog.show();
		return dialog;
	}

	/**
	 * 自定义特色的对话框
	 * 
	 * @param context
	 * @param message
	 * @param okLabel
	 * @param cancelLabel
	 * @param onYesClickListener
	 * @param onNoClickListener
	 * @return
	 */
	public static Dialog showYesNoDialogAutoDismiss(Context context, boolean isCancelOutSide, String message, String okLabel, String cancelLabel, final AutoDismissOnClickListener mAutoDismissOnClickListener) {
		final Dialog dialog = new Dialog(context, R.style.myDialogTheme);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.dialog_yes_and_no);
		TextView dialogContent = (TextView) dialog.findViewById(R.id.tv_dialog_content);
		TextView cancelTextView = (TextView) dialog.findViewById(R.id.tv_cancle);
		TextView confirmTextView = (TextView) dialog.findViewById(R.id.tv_confirm);
		RelativeLayout cancel = (RelativeLayout) dialog.findViewById(R.id.rl_cancle);
		RelativeLayout confirm = (RelativeLayout) dialog.findViewById(R.id.rl_confirm);
		cancelTextView.setText(cancelLabel);
		confirmTextView.setText(okLabel);
		dialogContent.setText(message);
		dialog.setCanceledOnTouchOutside(isCancelOutSide);
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mAutoDismissOnClickListener != null) {
					mAutoDismissOnClickListener.onYesClick();
				}
				dialog.dismiss();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mAutoDismissOnClickListener != null) {
					mAutoDismissOnClickListener.onNoClick();
				}
				dialog.dismiss();
			}
		});
		dialog.show();
		return dialog;
	}

	/**
	 * 自定义特色的对话框
	 * 
	 * @param context
	 * @param message
	 * @param okLabel
	 * @param cancelLabel
	 * @param onYesClickListener
	 * @param onNoClickListener
	 * @return
	 */
	public static Dialog showYesNoDialogAutoDismiss(Context context, String message, String okLabel, String cancelLabel, final AutoDismissOnClickListener mAutoDismissOnClickListener) {
		final Dialog dialog = new Dialog(context, R.style.myDialogTheme);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.dialog_yes_and_no);
		TextView dialogContent = (TextView) dialog.findViewById(R.id.tv_dialog_content);
		TextView cancelTextView = (TextView) dialog.findViewById(R.id.tv_cancle);
		TextView confirmTextView = (TextView) dialog.findViewById(R.id.tv_confirm);
		RelativeLayout cancel = (RelativeLayout) dialog.findViewById(R.id.rl_cancle);
		RelativeLayout confirm = (RelativeLayout) dialog.findViewById(R.id.rl_confirm);
		cancelTextView.setText(cancelLabel);
		confirmTextView.setText(okLabel);
		dialogContent.setText(message);
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mAutoDismissOnClickListener != null) {
					mAutoDismissOnClickListener.onYesClick();
				}
				dialog.dismiss();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mAutoDismissOnClickListener != null) {
					mAutoDismissOnClickListener.onNoClick();
				}
				dialog.dismiss();
			}
		});
		dialog.show();
		return dialog;
	}

	/**
	 * 用户确认对话框
	 * 
	 */
	public static Dialog showYesDialogAutoDismiss(Context context, String message, String okLabel, final AutoDismissOnClickListener mAutoDismissOnClickListener) {
		final Dialog dialog = new Dialog(context, R.style.myDialogTheme);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.dialog_yes);
		TextView dialogContent = (TextView) dialog.findViewById(R.id.tv_dialog_content);
		TextView cancelTextView = (TextView) dialog.findViewById(R.id.tv_cancle);
		RelativeLayout cancel = (RelativeLayout) dialog.findViewById(R.id.rl_cancle);
		cancelTextView.setText(okLabel);
		dialogContent.setText(message);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mAutoDismissOnClickListener != null) {
					mAutoDismissOnClickListener.onNoClick();
				}
				dialog.dismiss();
			}
		});
		dialog.show();
		return dialog;
	}

	/**
	 * 信息显示对话框
	 * 
	 * @param context
	 * @param message
	 * @param okLabel
	 * @param mAutoDismissOnClickListener
	 * @return
	 */
	public static Dialog showInfoDialogAutoDismiss(Context context, String title, String message, String okLabel, final AutoDismissOnClickListener mAutoDismissOnClickListener) {
		final Dialog dialog = new Dialog(context, R.style.myDialogTheme);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.dialog_info);

		TextView dialogTitle = (TextView) dialog.findViewById(R.id.tv_dialog_title);
		TextView dialogContent = (TextView) dialog.findViewById(R.id.tv_dialog_content);
		TextView cancelTextView = (TextView) dialog.findViewById(R.id.tv_cancle);
		RelativeLayout cancel = (RelativeLayout) dialog.findViewById(R.id.rl_cancle);

		cancelTextView.setText(okLabel);
		dialogTitle.setText(title);
		dialogContent.setText(message);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mAutoDismissOnClickListener != null) {
					mAutoDismissOnClickListener.onNoClick();
				}
				dialog.dismiss();
			}
		});
		dialog.show();
		return dialog;
	}

	public interface AutoDismissOnClickListener {
		void onYesClick();

		void onNoClick();
	}

	public static class SelectMenuPopupWindow extends PopupWindow {
		private LinearLayout btn_take_photo, btn_tack_text, btn_cancel, btn_tack_delete;
		public View mMenuView;

		public void setDeleteVisibility(int visibility) {
			btn_tack_delete.setVisibility(visibility);
		}

		public SelectMenuPopupWindow(Activity context, OnClickListener itemsOnClick) {
			super(context);
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mMenuView = inflater.inflate(R.layout.find_act_menu_dialog, null);
			btn_take_photo = (LinearLayout) mMenuView.findViewById(R.id.ll_take_photo);
			btn_tack_text = (LinearLayout) mMenuView.findViewById(R.id.ll_photo_album);
			btn_tack_delete = (LinearLayout) mMenuView.findViewById(R.id.ll_photo_delete);
			btn_cancel = (LinearLayout) mMenuView.findViewById(R.id.ll_find_comment_cancel);
			btn_cancel.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					dismiss();
				}
			});
			// 设置按钮监听
			btn_tack_text.setOnClickListener(itemsOnClick);
			btn_take_photo.setOnClickListener(itemsOnClick);
			btn_tack_delete.setOnClickListener(itemsOnClick);
			this.setContentView(mMenuView);
			this.setWidth(LayoutParams.MATCH_PARENT);
			this.setHeight(LayoutParams.WRAP_CONTENT);
			this.setFocusable(true);
			this.setAnimationStyle(R.style.AnimBottom);
			ColorDrawable dw = new ColorDrawable(0xb0000000);
			this.setBackgroundDrawable(dw);
			mMenuView.setOnTouchListener(new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {

					int height = mMenuView.findViewById(R.id.pop_layout).getTop();
					int y = (int) event.getY();
					if (event.getAction() == MotionEvent.ACTION_UP) {
						if (y < height) {
							dismiss();
						}
					}
					return true;
				}
			});

		}
	}

	// Login Help
	public static class LoginHelpPopupWindow extends PopupWindow {
		private LinearLayout ll_forgetpassword, ll_first_time, btn_cancel;
		public View mMenuView;

		public LoginHelpPopupWindow(Activity context, OnClickListener itemsOnClick) {
			super(context);
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mMenuView = inflater.inflate(R.layout.login_help_dialog, null);
			ll_forgetpassword = (LinearLayout) mMenuView.findViewById(R.id.ll_forgetpassword);
			ll_first_time = (LinearLayout) mMenuView.findViewById(R.id.ll_first_time);
			btn_cancel = (LinearLayout) mMenuView.findViewById(R.id.ll_find_comment_cancel);
			btn_cancel.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					dismiss();
				}
			});
			// 设置按钮监听
			ll_forgetpassword.setOnClickListener(itemsOnClick);
			ll_first_time.setOnClickListener(itemsOnClick);
			this.setContentView(mMenuView);
			this.setWidth(LayoutParams.MATCH_PARENT);
			this.setHeight(LayoutParams.WRAP_CONTENT);
			this.setFocusable(true);
			this.setAnimationStyle(R.style.AnimBottom);
			ColorDrawable dw = new ColorDrawable(0xb0000000);
			this.setBackgroundDrawable(dw);
			mMenuView.setOnTouchListener(new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {

					int height = mMenuView.findViewById(R.id.pop_layout).getTop();
					int y = (int) event.getY();
					if (event.getAction() == MotionEvent.ACTION_UP) {
						if (y < height) {
							dismiss();
						}
					}
					return true;
				}
			});
		}
	}

	// findpassword
	public static class FindPassWordPopupWindow extends PopupWindow {
		private LinearLayout ll_id, ll_pass_port, btn_cancel;
		public View mMenuView;

		public FindPassWordPopupWindow(Activity context, OnClickListener itemsOnClick) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mMenuView = inflater.inflate(R.layout.find_psw_dialog, null);
			ll_id = (LinearLayout) mMenuView.findViewById(R.id.ll_id);
			ll_pass_port = (LinearLayout) mMenuView.findViewById(R.id.ll_pass_port);
			btn_cancel = (LinearLayout) mMenuView.findViewById(R.id.ll_find_comment_cancel);
			btn_cancel.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					dismiss();
				}
			});
			// 设置按钮监听
			ll_id.setOnClickListener(itemsOnClick);
			ll_pass_port.setOnClickListener(itemsOnClick);
			this.setContentView(mMenuView);
			this.setWidth(LayoutParams.MATCH_PARENT);
			this.setHeight(LayoutParams.WRAP_CONTENT);
			this.setFocusable(true);
			this.setAnimationStyle(R.style.AnimBottom);
			ColorDrawable dw = new ColorDrawable(0xb0000000);
			this.setBackgroundDrawable(dw);
			mMenuView.setOnTouchListener(new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {

					int height = mMenuView.findViewById(R.id.pop_layout).getTop();
					int y = (int) event.getY();
					if (event.getAction() == MotionEvent.ACTION_UP) {
						if (y < height) {
							dismiss();
						}
					}
					return true;
				}
			});
		}
	}

	// ServiceTopUp
	public static class ServiceTopUpPopupWindow extends PopupWindow {
		private LinearLayout ll_vvm_data, ll_vvm_volte, btn_cancel;
		public View mMenuView;

		public ServiceTopUpPopupWindow(Activity context, OnClickListener itemsOnClick) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mMenuView = inflater.inflate(R.layout.service_topup_dialog, null);
			ll_vvm_data = (LinearLayout) mMenuView.findViewById(R.id.ll_vvm_data);
			ll_vvm_volte = (LinearLayout) mMenuView.findViewById(R.id.ll_vvm_volte);
			btn_cancel = (LinearLayout) mMenuView.findViewById(R.id.ll_find_comment_cancel);
			btn_cancel.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					dismiss();
				}
			});
			// 设置按钮监听
			ll_vvm_volte.setOnClickListener(itemsOnClick);
			ll_vvm_data.setOnClickListener(itemsOnClick);
			this.setContentView(mMenuView);
			this.setWidth(LayoutParams.MATCH_PARENT);
			this.setHeight(LayoutParams.WRAP_CONTENT);
			this.setFocusable(true);
			this.setAnimationStyle(R.style.AnimBottom);
			ColorDrawable dw = new ColorDrawable(0xb0000000);
			this.setBackgroundDrawable(dw);
			mMenuView.setOnTouchListener(new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {

					int height = mMenuView.findViewById(R.id.pop_layout).getTop();
					int y = (int) event.getY();
					if (event.getAction() == MotionEvent.ACTION_UP) {
						if (y < height) {
							dismiss();
						}
					}
					return true;
				}
			});
		}
	}

	/**
	 * 得到圆形图片
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

	/**
	 * 图片缩放
	 */
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		// 计算inSampleSize值
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// 源图片的高度和宽度
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// 计算出实际宽高和目标宽高的比率
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			// 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
			// 一定都会大于等于目标的宽和高。
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	/**
	 * 建立一个自定义progress
	 * 
	 * @param context
	 * @param title
	 * @param message
	 * @param style
	 * @param cancelable
	 * @param cancelListener
	 * @return
	 */
	public static MyProgressDialog builderProgressDialog(Context context, String title, String message, int style, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
		MyProgressDialog progressDialog = new MyProgressDialog(context);
		progressDialog.setProgressStyle(style);
		if (title != null) {
			progressDialog.setTitle(title);
		}
		if (message != null) {
			progressDialog.setMessage(message);
		}
		progressDialog.setCancelable(cancelable);
		if (cancelListener != null)
			progressDialog.setOnCancelListener(cancelListener);
		return progressDialog;
	}

	/**
	 * 显示”确定“ 对话框
	 * 
	 * @param title
	 * @param onOkClickListener
	 */
	public static AlertDialog showOkAlertDialog(Context context, String title, String message, DialogInterface.OnClickListener onOkClickListener) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle(title);
		alertDialogBuilder.setMessage(message);
		alertDialogBuilder.setCancelable(false);
		alertDialogBuilder.setPositiveButton("确定", onOkClickListener);
		alertDialogBuilder.create();
		return alertDialogBuilder.show();
	}

	/***
	 * 图片的缩放方法
	 * 
	 * @param bgimage
	 *            ：源图片资源
	 * @param newWidth
	 *            ：缩放后宽度
	 * @param newHeight
	 *            ：缩放后高度
	 * @return
	 */
	public static Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
		// 获取这个图片的宽和高
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
		return bitmap;
	}

	public static void setTextChangedButtonEnable(final List<EditText> list, final View button) {
		for (int i = 0; i < list.size(); i++) {
			EditText editText = list.get(i);
			editText.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					for (int i = 0; i < list.size(); i++) {
						EditText editText = list.get(i);
						if (editText.getText().toString().length() == 0) {
							button.setEnabled(false);
							break;
						} else {
							button.setEnabled(true);
						}
					}
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub

				}
			});
		}
	}
}
