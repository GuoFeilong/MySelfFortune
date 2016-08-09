package com.guofeilong.fortune.ui.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.guofeilong.fortune.R;
import com.guofeilong.fortune.ui.ChangeColorIconView;
import com.guofeilong.fortune.utils.Rotate3dAnimation;
import com.guofeilong.fortune.utils.Settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GridMenuDragAdapter extends BaseAdapter implements GridMenuDragBaseAdapter {

	private int type;
	private String[] gvTitles;
	private static ArrayList<RelativeLayout> mGvList = new ArrayList<RelativeLayout>();
	/**
	 * 存储拖拽的标记
	 */
	private static final String IS_HAS_DRAGED = "is_has_draged";
	private static final String IS_HAS_DRAGED_KEY = "is_has_draged_key";
	/**
	 * 背景的标记
	 */
	private static final String GRID_MENU_BG = "grid_menu_bg";
	/**
	 * 标题的标记
	 */
	private static final String GRID_MENU_TITLE = "grid_menu_title";
	/**
	 * 这是调整后的九宫格菜单的标记
	 */
	private static final String NEW_GRIDVIEW_MENU = "new_gridview_menu";
	private Context mContext;
	private List<HashMap<String, Object>> list;
	private LayoutInflater mInflater;
	private int mHidePosition = -1;

	public GridMenuDragAdapter(Context context, List<HashMap<String, Object>> list, int type, String[] gvTitles) {
		this.list = list;
		mInflater = LayoutInflater.from(context);
		mContext = context;
		this.type = type;
		this.gvTitles = gvTitles;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		convertView = mInflater.inflate(R.layout.item_gridmenu_item, null);
		RelativeLayout grivdBG = (RelativeLayout) convertView.findViewById(R.id.rl_gv_item);
		mGvList.add(grivdBG);
		TextView tv_title = (TextView) convertView.findViewById(R.id.tv_gridview_title);
		ImageView bottomLine = (ImageView) convertView.findViewById(R.id.iv_bottom_line);
		ImageView rightLine = (ImageView) convertView.findViewById(R.id.iv_right_line);

		HashMap<String, Object> currentData = list.get(position);
		ChangeColorIconView iconView = (ChangeColorIconView) convertView.findViewById(R.id.cci_gridmenu_icon);
		iconView.setmResource((Integer) currentData.get(GRID_MENU_BG));
		if (position % 2 == 0) {

			iconView.setColor(mContext.getResources().getColor(R.color.conmmd_yellow));
		} else {
			iconView.setColor(mContext.getResources().getColor(R.color.conmmd_blue));

		}
		iconView.setColor(mContext.getResources().getColor(R.color.conmmd_blue));
		Bitmap drawable2Bitmap = drawable2Bitmap((Integer) currentData.get(GRID_MENU_BG));
		RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(drawable2Bitmap.getWidth(), drawable2Bitmap.getHeight());
		rp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		iconView.setLayoutParams(rp);
		tv_title.setText((String) currentData.get(GRID_MENU_TITLE));

		if (position % 2 != 0) {
			rightLine.setVisibility(View.GONE);
		}
		if ((position == list.size() - 1) || (position == list.size() - 2)) {
			bottomLine.setVisibility(View.GONE);
		}
		goToRightActivity(convertView, grivdBG, (String) currentData.get(GRID_MENU_TITLE));
		return convertView;

	}

	/**
	 * 根据变化后的条目跳转
	 * 
	 * @param grivdBG
	 * @param title
	 */
	private void goToRightActivity(final View view, RelativeLayout grivdBG, final String title) {
		if (!TextUtils.isEmpty(title)) {
			grivdBG.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					applyRotation(view, -180, -360, title);
				}
			});
		}
	}

	/**
	 * 反转效果
	 * 
	 * @param container
	 * @param start
	 * @param end
	 * @param title
	 */
	public void applyRotation(View container, float start, float end, final String title) {
		final float centerX = container.getWidth() / 2.0f;
		final float centerY = container.getHeight() / 2.0f;
		final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end, centerX, centerY, 0f, false);
		rotation.setDuration(500);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new AccelerateInterpolator());
		rotation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				changeClickFalg(false);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				changeClickFalg(true);
				// TODO 读取配置的菜单顺序跳转对应的activity
			}

		});
		container.startAnimation(rotation);

	}

	protected void changeClickFalg(boolean b) {
		for (int i = 0; i < mGvList.size(); i++) {
			mGvList.get(i).setClickable(b);
		}
	}

	@Override
	public void reorderItems(int oldPosition, int newPosition) {
		HashMap<String, Object> temp = null;
		try {
			temp = list.get(oldPosition);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (oldPosition < newPosition) {
			for (int i = oldPosition; i < newPosition; i++) {
				Collections.swap(list, i, i + 1);
			}
		} else if (oldPosition > newPosition) {
			for (int i = oldPosition; i > newPosition; i--) {
				Collections.swap(list, i, i - 1);
			}
		}

		list.set(newPosition, temp);
		// 存储调整后的list菜单内容工程目录
		Settings.getSettings(mContext).saveDargedMenu(NEW_GRIDVIEW_MENU + type, list);
		SharedPreferences sp = mContext.getSharedPreferences(IS_HAS_DRAGED + type, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putBoolean(IS_HAS_DRAGED_KEY + type, true);
		edit.commit();
	}

	@Override
	public void setHideItem(int hidePosition) {
		this.mHidePosition = hidePosition;
		notifyDataSetChanged();
	}

	/**
	 * 切换界面跳转
	 * 
	 * @param <T>
	 */
	private <T> void changeActivity(Class<T> clazz) {
		Intent intent = new Intent(mContext, clazz);
		mContext.startActivity(intent);
	}

	/**
	 * drawable转换bitmap
	 * 
	 * @return bitmap
	 */
	private Bitmap drawable2Bitmap(int resourceID) {
		Drawable drawable = mContext.getResources().getDrawable(resourceID);
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

}
