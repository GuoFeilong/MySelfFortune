package com.guofeilong.fortune.ui;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.ui.view.PhotoWallAdapter;
import com.guofeilong.fortune.ui.view.WaterWaveLoadingView;
import com.guofeilong.fortune.utils.ImageDownloader;

@SuppressLint("NewApi")
public class AsyTaskImageActivity extends BaseActivity implements OnClickListener {
	private int mColor;
	private String mTitle;
	private GridView mDragGridView;
	private AsyAdapter mAsyAdapter;
	private List<String> mUrls;
	private List<MyAsyTask> mAllTask;
	private List<Bitmap> mABitmaps;
	private ImageDownloader mdDownloader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_asytask_image);
		mTitle = getMyTitleAndColor();
		initNavigation();
		initData();
		initView();
		initEvent();
	}

	/**
	 * 初始化事件
	 */
	private void initEvent() {
		if (mAsyAdapter != null && mUrls != null) {
			mAsyAdapter.setImageUrls(mUrls);
			mDragGridView.setAdapter(mAsyAdapter);
		}

	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mDragGridView = (GridView) findViewById(R.id.gv_asy_image);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mdDownloader = new ImageDownloader(null);
		mABitmaps = new ArrayList<Bitmap>();
		mAllTask = new ArrayList<AsyTaskImageActivity.MyAsyTask>();
		mUrls = new ArrayList<String>();
		mUrls.add("http://store.storeimages.cdn-apple.com/8348/as-images.apple.com/is/image/AppleInc/aos/published/images/w/42/w42ss/sbwh/w42ss-sbwh-detail?wid=424&hei=424&fmt=jpeg&qlt=95&op_sharpen=0&resMode=bicub&op_usm=0.5,0.5,0,0&iccEmbed=0&layer=comp&.v=1428077873837");
		mUrls.add("http://store.storeimages.cdn-apple.com/8348/as-images.apple.com/is/image/AppleInc/aos/published/images/s/38/s38si/sbbl/s38si-sbbl-detail?wid=424&hei=424&fmt=jpeg&qlt=95&op_sharpen=0&resMode=bicub&op_usm=0.5,0.5,0,0&iccEmbed=0&layer=comp&.v=1428077732002");
		mUrls.add("http://store.storeimages.cdn-apple.com/8348/as-images.apple.com/is/image/AppleInc/aos/published/images/s/38/s38si/sbgr/s38si-sbgr-detail?wid=424&hei=424&fmt=jpeg&qlt=95&op_sharpen=0&resMode=bicub&op_usm=0.5,0.5,0,0&iccEmbed=0&layer=comp&.v=1428077884255");
		mUrls.add("http://store.storeimages.cdn-apple.com/8348/as-images.apple.com/is/image/AppleInc/aos/published/images/s/38/s38si/sbpi/s38si-sbpi-detail?wid=424&hei=424&fmt=jpeg&qlt=95&op_sharpen=0&resMode=bicub&op_usm=0.5,0.5,0,0&iccEmbed=0&layer=comp&.v=1428077753406");
		mUrls.add("http://store.storeimages.cdn-apple.com/8348/as-images.apple.com/is/image/AppleInc/aos/published/images/e/38/e38yg/sbbk/e38yg-sbbk-detail?wid=424&hei=424&fmt=jpeg&qlt=95&op_sharpen=0&resMode=bicub&op_usm=0.5,0.5,0,0&iccEmbed=0&layer=comp&.v=1428077625659");
		mUrls.add("http://store.storeimages.cdn-apple.com/8348/as-images.apple.com/is/image/AppleInc/aos/published/images/s/38/s38si/sbgr/s38si-sbgr-detail?wid=424&hei=424&fmt=jpeg&qlt=95&op_sharpen=0&resMode=bicub&op_usm=0.5,0.5,0,0&iccEmbed=0&layer=comp&.v=1428077884255");
		mUrls.add("http://store.storeimages.cdn-apple.com/8348/as-images.apple.com/is/image/AppleInc/aos/published/images/i/ph/iphone6/plus/iphone6-plus-box-silver-2014_GEO_CN?wid=478&hei=595&fmt=jpeg&qlt=95&op_sharpen=0&resMode=bicub&op_usm=0.5,0.5,0,0&iccEmbed=0&layer=comp&.v=1410265616537");
		mUrls.add("http://store.storeimages.cdn-apple.com/8348/as-images.apple.com/is/image/AppleInc/aos/published/images/m/ac/macbook/gold/macbook-gold-home-bb-201504?wid=766&hei=274&fmt=png-alpha&qlt=95&.v=1429047563353");
		mUrls.add("http://store.storeimages.cdn-apple.com/8348/as-images.apple.com/is/image/AppleInc/aos/published/images/m/ac/macbook/select/macbook-select-gold-201501_GEO_CN?wid=470&hei=556&fmt=png-alpha&qlt=95&.v=1425911827170");
		mUrls.add("http://store.storeimages.cdn-apple.com/8348/as-images.apple.com/is/image/AppleInc/aos/published/images/i/ma/imac/retina/imac-retina-step1-hero-2014?wid=550&hei=331&fmt=jpeg&qlt=95&op_sharpen=0&resMode=bicub&op_usm=0.5,0.5,0,0&iccEmbed=0&layer=comp&.v=1413462243503");
		mAsyAdapter = new AsyAdapter();

	}

	/**
	 * 获取标题
	 * 
	 * @return
	 */
	private String getMyTitleAndColor() {
		String title = "";

		Intent intent = getIntent();
		if (intent != null) {
			Bundle extras = intent.getExtras();
			if (extras != null) {
				title = extras.getString(AppConstants.INTENT_AMOUNT);
				mColor = extras.getInt(AppConstants.INTENT_NAME_FROM);
			}
		}
		return title;
	}

	/**
	 * 初始化通用标题栏
	 */
	private void initNavigation() {
		TextView nameTextView = (TextView) findViewById(R.id.commond_textview_title_name);
		nameTextView.setText(mTitle);
		nameTextView.setTextColor(mColor);
		RelativeLayout leftButton = (RelativeLayout) findViewById(R.id.commond_imagebutton_title_leftbutton);
		leftButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.commond_imagebutton_title_leftbutton:
			finish();
			break;
		default:
			break;
		}

	}

	class AsyHolder {
		ImageView rlImage;
		WaterWaveLoadingView asyLoading;
	}

	/**
	 * 异步加载的适配器
	 * 
	 * @author guofl
	 * 
	 */
	class AsyAdapter extends BaseAdapter {
		private List<String> imageUrls;

		public void setImageUrls(List<String> imageUrls) {
			this.imageUrls = imageUrls;
		}

		@Override
		public int getCount() {
			return imageUrls.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			AsyHolder asyHolder = null;
			if (convertView == null) {
				asyHolder = new AsyHolder();
				convertView = getLayoutInflater().inflate(R.layout.item_asy_image, null);
				asyHolder.rlImage = (ImageView) convertView.findViewById(R.id.rl_asy_image);
				asyHolder.asyLoading = (WaterWaveLoadingView) convertView.findViewById(R.id.waterloading_asy_image);
				convertView.setTag(asyHolder);
			} else {
				asyHolder = (AsyHolder) convertView.getTag();
			}
			setData2Postion(asyHolder, imageUrls, position);
			return convertView;
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		for (int i = 0; i < mAllTask.size(); i++) {
			MyAsyTask myAsyTask = mAllTask.get(i);
			if (myAsyTask != null && myAsyTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
				myAsyTask.cancel(true);
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mDragGridView = null;
		mAsyAdapter = null;
		for (int i = 0; i < mABitmaps.size(); i++) {
			mABitmaps.get(i).recycle();
		}
		mABitmaps.clear();
		mABitmaps = null;
		System.gc();

	}

	/**
	 * 设置数据到item中
	 * 
	 * @param asyHolder
	 * @param imageUrls
	 * @param position
	 */
	public void setData2Postion(AsyHolder asyHolder, List<String> imageUrls, int position) {
		String url = imageUrls.get(position);
		asyHolder.rlImage.setTag(url);
		// mdDownloader.download(url, asyHolder.rlImage);

		MyAsyTask asyTask = new MyAsyTask();
		asyTask.execute(asyHolder);
		mAllTask.add(asyTask);
	}

	/**
	 * 异步任务
	 * 
	 * @author guofl
	 * 
	 */
	class MyAsyTask extends AsyncTask<AsyHolder, Void, Bitmap> {

		private Bitmap decodeStream;
		private WeakReference<ImageView> imageViewReference;
		private ImageView bg;

		private AsyHolder holder;

		@Override
		protected Bitmap doInBackground(AsyHolder... params) {
			try {
				holder = params[0];
				bg = holder.rlImage;
				imageViewReference = new WeakReference<ImageView>(bg);
				URL url2 = new URL(bg.getTag().toString());
				URLConnection openConnection = url2.openConnection();
				InputStream inputStream = openConnection.getInputStream();
				BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
				decodeStream = BitmapFactory.decodeStream(bufferedInputStream);
				// decodeStream = ViewUtils.zoomImage(decodeStream,
				// decodeStream.getWidth() / 2, decodeStream.getHeight() / 2);
				Thread.sleep(300);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return decodeStream;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);

			if (isCancelled()) {
				result = null;
			}

			if (imageViewReference != null) {
				ImageView imageView = imageViewReference.get();
				if (imageView == null) {
					return;
				}
				imageView.setImageBitmap(result);
				holder.asyLoading.setReflesh(false);
				this.bg = null;
				holder.asyLoading.setVisibility(View.INVISIBLE);

			}

		}

	}

}
