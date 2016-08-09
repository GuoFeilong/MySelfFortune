package com.guofeilong.fortune.utils;

import java.util.LinkedList;
import java.util.List;

import com.guofeilong.fortune.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;


public class BitmapUtil {

	/**
	 * 设置九宫格头像数量 标记每个头像的具体位置以及头像的大小
	 * 
	 * @param count
	 * @param context
	 * @return
	 */
	public static List<MyBitmapEntity> getBitmapEntitys(int count, Context context) {
		List<MyBitmapEntity> mList = new LinkedList<MyBitmapEntity>();
		String value = PropertiesUtil.readData(context, String.valueOf(count), R.raw.data);
		LogUtil.d("value=>" + value);
		String[] arr1 = value.split(";");
		int length = arr1.length;
		for (int i = 0; i < length; i++) {
			String content = arr1[i];
			String[] arr2 = content.split(",");
			MyBitmapEntity entity = null;
			for (int j = 0; j < arr2.length; j++) {
				entity = new MyBitmapEntity();
				entity.x = Float.valueOf(arr2[0]);
				entity.y = Float.valueOf(arr2[1]);
				entity.width = Float.valueOf(arr2[2]);
				entity.height = Float.valueOf(arr2[3]);
			}
			mList.add(entity);
		}
		return mList;
	}

	public static Bitmap getCombineBitmaps(List<MyBitmapEntity> mEntityList, Bitmap... bitmaps) {
		LogUtil.d("count=>" + mEntityList.size());
		Bitmap newBitmap = Bitmap.createBitmap(200, 200, Config.ARGB_8888);
		LogUtil.d("newBitmap=>" + newBitmap.getWidth() + "," + newBitmap.getHeight());
		for (int i = 0; i < mEntityList.size(); i++) {
			LogUtil.d("i==>" + i);
			// 产生的bitmap要根据存储的大小来生成合适的bitmap
			Bitmap second = resizeImage(bitmaps[i], (int) (mEntityList.get(i).width), (int) (mEntityList.get(i).height));
			newBitmap = mixtureBitmap(newBitmap, second, new PointF(mEntityList.get(i).x, mEntityList.get(i).y));
		}
		return newBitmap;
	}

	// 使用Bitmap加Matrix来缩放
	public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
		Bitmap BitmapOrg = bitmap;
		int width = BitmapOrg.getWidth();
		int height = BitmapOrg.getHeight();
		int newWidth = w;
		int newHeight = h;
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width, height, matrix, true);
		return resizedBitmap;
	}

	/**
	 * Mix two Bitmap as one.
	 * 
	 * @param bitmapOne
	 * @param bitmapTwo
	 * @param point
	 *            where the second bitmap is painted.
	 * @return
	 */
	public static Bitmap mixtureBitmap(Bitmap first, Bitmap second, PointF fromPoint) {
		if (first == null || second == null || fromPoint == null) {
			return null;
		}
		Bitmap newBitmap = Bitmap.createBitmap(first.getWidth(), first.getHeight(), Config.ARGB_8888);
		Canvas cv = new Canvas(newBitmap);
		cv.drawBitmap(first, 0, 0, null);
		cv.drawBitmap(second, fromPoint.x, fromPoint.y, null);
		cv.save(Canvas.ALL_SAVE_FLAG);
		cv.restore();
		return newBitmap;
	}

	public static class MyBitmapEntity {
		float x;
		float y;
		float width;
		float height;
		static int devide = 1;
		int index = -1;

		@Override
		public String toString() {
			return "MyBitmap [x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + ", devide=" + devide + ", index=" + index + "]";
		}

	}

}
