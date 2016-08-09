package com.guofeilong.fortune.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.guofeilong.fortune.Console;
import com.guofeilong.fortune.AppConstants;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.widget.ImageView;

/**
 * This helper class download images from the Internet and binds those with the
 * provided ImageView.
 * 
 * <br>
 * 从网路下载图片并填充给指定的ImageView
 * 
 * <p>
 * It requires the INTERNET permission, which should be added to your
 * application's manifest file. <br>
 * 使用该类需要在manifest配置文件中添加 INTERNET 权限。
 * <p>
 * 
 * A local cache of downloaded images is maintained internally to improve
 * performance. <br>
 * 会维护一份本地的cache以改善性能。
 */
public class ImageDownloader {
	private static final String LOG_TAG = "ImageDownloader";

	public static enum Mode {
		NO_ASYNC_TASK, NO_DOWNLOADED_DRAWABLE, CORRECT
	}

	private Mode mode = Mode.NO_DOWNLOADED_DRAWABLE;

	// 图片缓存在本地的文件路径
	private String mFilePath;

	public ImageDownloader(String filePath) {
		if (null != filePath) {
			this.mFilePath = filePath;
		} else {
			this.mFilePath = AppConstants.cacheTempFilePath;
		}
	}

	/**
	 * Download the specified image from the Internet and binds it to the
	 * provided ImageView. The binding is immediate if the image is found in the
	 * cache and will be done asynchronously otherwise. A null bitmap will be
	 * associated to the ImageView if an error occurs.
	 * 
	 * @param url
	 *            The URL of the image to download.
	 * @param imageView
	 *            The ImageView to bind the downloaded image to.
	 */
	public void download(String url, ImageView imageView) {
		try {
			if (null == imageView || url == null) {
				return;
			}

			resetPurgeTimer();

			Bitmap bitmap = getBitmapFromCache(url);

			if (bitmap == null) {
				forceDownload(url, imageView);

			} else {
				cancelPotentialDownload(url, imageView);
				imageView.setImageBitmap(bitmap);

			}
		} catch (Exception e) {
			Console.printThrowable(e);
		}
	}

	/*
	 * Same as download but the image is always downloaded and the cache is not
	 * used. Kept private at the moment as its interest is not clear. private
	 * void forceDownload(String url, ImageView view) { forceDownload(url, view,
	 * null); }
	 */

	/**
	 * Same as download but the image is always downloaded and the cache is not
	 * used. Kept private at the moment as its interest is not clear.
	 */
	private void forceDownload(String url, ImageView imageView) {
		// State sanity: url is guaranteed to never be null in
		// DownloadedDrawable and cache keys.
		if (url == null) {
			// imageView.setImageDrawable(null);
			return;
		}

		if (cancelPotentialDownload(url, imageView)) {
			switch (mode) {
			case NO_ASYNC_TASK:
				Bitmap bitmap = downloadBitmap(url);
				addBitmapToCache(url, bitmap);
				imageView.setImageBitmap(bitmap);
				break;

			case NO_DOWNLOADED_DRAWABLE:
				BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
				task.executeOnExecutor(Executors.newFixedThreadPool(2), url);
				break;

			case CORRECT:
				task = new BitmapDownloaderTask(imageView);
				DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
				imageView.setImageDrawable(downloadedDrawable);
				// imageView.setMaxHeight(72);
				// imageView.setMaxWidth(72);
				task.executeOnExecutor(Executors.newFixedThreadPool(2), url);
				break;
			}
		}
	}

	/**
	 * Returns true if the current download has been canceled or if there was no
	 * download in progress on this image view. Returns false if the download in
	 * progress deals with the same url. The download is not stopped in that
	 * case.
	 */
	private boolean cancelPotentialDownload(String url, ImageView imageView) {
		BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);

		if (bitmapDownloaderTask != null) {
			String bitmapUrl = bitmapDownloaderTask.url;
			if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
				bitmapDownloaderTask.cancel(true);
			} else {
				// The same URL is already being downloaded.
				return false;
			}
		}
		return true;
	}

	/**
	 * @param imageView
	 *            Any imageView
	 * @return Retrieve the currently active download task (if any) associated
	 *         with this imageView. null if there is no such task.
	 */
	private BitmapDownloaderTask getBitmapDownloaderTask(ImageView imageView) {
		if (imageView != null) {
			Drawable drawable = imageView.getDrawable();
			if (drawable instanceof DownloadedDrawable) {
				DownloadedDrawable downloadedDrawable = (DownloadedDrawable) drawable;
				return downloadedDrawable.getBitmapDownloaderTask();
			}
		}
		return null;
	}

	private Bitmap downloadBitmap(String url) {
		// final int IO_BUFFER_SIZE = 4 * 1024;
		// AndroidHttpClient is not allowed to be used from the main thread
		try {
			final HttpClient client = (mode == Mode.NO_ASYNC_TASK) ? new DefaultHttpClient() : AndroidHttpClient.newInstance("Android");
			final HttpGet getRequest = new HttpGet(url);
			try {
				HttpResponse response = client.execute(getRequest);
				final int statusCode = response.getStatusLine().getStatusCode();
				// String v =
				// response.getFirstHeader("Content-Type").getValue();//image/png

				if (statusCode != HttpStatus.SC_OK) {
					Console.debug(LOG_TAG, "Error " + statusCode + " while retrieving bitmap from " + url);
					return null;
				}

				final HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream inputStream = null;
					try {
						inputStream = entity.getContent();
						// return BitmapFactory.decodeStream(inputStream);
						// Bug on slow connections, fixed in future release.
						return BitmapFactory.decodeStream(new FlushedInputStream(inputStream));
					} finally {
						if (inputStream != null) {
							inputStream.close();
						}
						entity.consumeContent();
					}
				}
			} catch (IOException e) {
				getRequest.abort();
				Console.debug(LOG_TAG, "I/O error while retrieving bitmap from " + url);
				Console.printThrowable(e);
			} catch (IllegalStateException e) {
				getRequest.abort();
				Console.debug(LOG_TAG, "Incorrect URL: " + url);
			} catch (Exception e) {
				getRequest.abort();
				Console.debug(LOG_TAG, "Error while retrieving bitmap from " + url);
				Console.printThrowable(e);
			} finally {
				if ((client instanceof AndroidHttpClient)) {
					((AndroidHttpClient) client).close();
				}
			}
		} catch (Exception e) {
			Console.printThrowable(e);
		}

		return null;
	}

	/*
	 * An InputStream that skips the exact number of bytes provided, unless it
	 * reaches EOF.
	 */
	class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					int b = read();
					if (b < 0) {
						break; // we reached EOF
					} else {
						bytesSkipped = 1; // we read one byte
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}

	/**
	 * The actual AsyncTask that will asynchronously download the image.
	 */
	class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
		private String url;
		private final WeakReference<ImageView> imageViewReference;

		public BitmapDownloaderTask(ImageView imageView) {
			imageViewReference = new WeakReference<ImageView>(imageView);
		}

		/**
		 * Actual download method.
		 */
		@Override
		protected Bitmap doInBackground(String... params) {
			url = params[0];
			// read sdcard image file
			String fileName = url.replaceAll("\\W", "_");
			Bitmap bm = readBitmapBySDCard(fileName);

			if (null != bm) {
				return bm;
			}
			// sdcard image file is not exists, open download
			return downloadBitmap(url);
		}

		/**
		 * Once the image is downloaded, associates it to the imageView
		 */
		@Override
		protected void onPostExecute(Bitmap bitmap) {

			if (isCancelled()) {
				bitmap = null;
			}
			addBitmapToCache(url, bitmap);

			if (imageViewReference != null) {
				ImageView imageView = imageViewReference.get();
				if (imageView == null) {
					return;
				}
				BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
				// Change bitmap only if this process is still associated
				// with it
				// Or if we don't use any bitmap to task association
				// (NO_DOWNLOADED_DRAWABLE mode)
				if ((this == bitmapDownloaderTask) || (mode != Mode.CORRECT)) {
					imageView.setImageBitmap(bitmap);

				}
			}

			if (null != bitmap) {
				String fileName = url.replaceAll("\\W", "_");
				if (!isExistsBitmapBySDCard(fileName)) {
					byte[] imageBytes = bitmapToBytes(bitmap, url);
					writeImageToSDCard(fileName, imageBytes);
				}

			}
		}
	}

	/**
	 * 
	 * @param url
	 * @param bitmap
	 */
	public void addBitmapToCacheAndSD(String url, Bitmap bitmap) {
		if (null != bitmap) {
			addBitmapToCache(url, bitmap);
			String fileName = url.replaceAll("\\W", "_");
			if (isExistsBitmapBySDCard(fileName)) {
				File file = new File(this.mFilePath, fileName);
				file.delete();
			}
			byte[] imageBytes = bitmapToBytes(bitmap, url);
			writeImageToSDCard(fileName, imageBytes);
		}
	}

	/**
	 * A fake Drawable that will be attached to the imageView while the download
	 * is in progress.
	 * 
	 * <p>
	 * Contains a reference to the actual download task, so that a download task
	 * can be stopped if a new binding is required, and makes sure that only the
	 * last started download process can bind its result, independently of the
	 * download finish order.
	 * </p>
	 */
	class DownloadedDrawable extends ColorDrawable {
		private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;

		public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask) {
			super(Color.TRANSPARENT);
			bitmapDownloaderTaskReference = new WeakReference<BitmapDownloaderTask>(bitmapDownloaderTask);
		}

		public BitmapDownloaderTask getBitmapDownloaderTask() {
			return bitmapDownloaderTaskReference.get();
		}
	}

	public void setMode(Mode mode) {
		this.mode = mode;
		clearCache();
	}

	/*
	 * Cache-related fields and methods.
	 * 
	 * We use a hard and a soft cache. A soft reference cache is too
	 * aggressively cleared by the Garbage Collector.
	 */

	private static final int HARD_CACHE_CAPACITY = 10;
	private static final int DELAY_BEFORE_PURGE = 10 * 1000; // in
																// milliseconds

	// Hard cache, with a fixed maximum capacity and a life duration
	private final HashMap<String, Bitmap> sHardBitmapCache = new LinkedHashMap<String, Bitmap>(HARD_CACHE_CAPACITY / 2, 0.75f, true) {
		@Override
		protected boolean removeEldestEntry(LinkedHashMap.Entry<String, Bitmap> eldest) {
			if (size() > HARD_CACHE_CAPACITY) {
				// Entries push-out of hard reference cache are transferred
				// to soft reference cache
				sSoftBitmapCache.put(eldest.getKey(), new SoftReference<Bitmap>(eldest.getValue()));
				return true;
			} else
				return false;
		}
	};

	// Soft cache for bitmaps kicked out of hard cache
	private final ConcurrentHashMap<String, SoftReference<Bitmap>> sSoftBitmapCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>(HARD_CACHE_CAPACITY / 2);

	private final Handler purgeHandler = new Handler();

	private final Runnable purger = new Runnable() {
		public void run() {
			clearCache();
		}
	};

	/**
	 * Adds this bitmap to the cache.
	 * 
	 * @param bitmap
	 *            The newly downloaded bitmap.
	 */
	private void addBitmapToCache(String url, Bitmap bitmap) {
		if (bitmap != null) {
			synchronized (sHardBitmapCache) {
				sHardBitmapCache.put(url, bitmap);
			}
		}
	}

	/**
	 * @param url
	 *            The URL of the image that will be retrieved from the cache.
	 * @return The cached bitmap or null if it was not found.
	 */
	private Bitmap getBitmapFromCache(String url) {
		// First try the hard reference cache
		synchronized (sHardBitmapCache) {
			final Bitmap bitmap = sHardBitmapCache.get(url);
			if (bitmap != null) {
				// Bitmap found in hard cache
				// Move element to first position, so that it is removed
				// last
				sHardBitmapCache.remove(url);
				sHardBitmapCache.put(url, bitmap);
				return bitmap;
			}
		}

		// Then try the soft reference cache
		SoftReference<Bitmap> bitmapReference = sSoftBitmapCache.get(url);
		if (bitmapReference != null) {
			final Bitmap bitmap = bitmapReference.get();
			if (bitmap != null) {
				// Bitmap found in soft cache
				return bitmap;
			} else {
				// Soft reference has been Garbage Collected
				sSoftBitmapCache.remove(url);
			}
		}

		return null;
	}

	/**
	 * Clears the image cache used internally to improve performance. Note that
	 * for memory efficiency reasons, the cache will automatically be cleared
	 * after a certain inactivity delay.
	 */
	public void clearCache() {
		sHardBitmapCache.clear();
		sSoftBitmapCache.clear();
	}

	/**
	 * Allow a new delay before the automatic cache clear is done.
	 */
	private void resetPurgeTimer() {
		purgeHandler.removeCallbacks(purger);
		purgeHandler.postDelayed(purger, DELAY_BEFORE_PURGE);
	}

	/**
	 * read / write image file (sdcard is exists and used)
	 */
	private boolean sdCardIsUsed() {
		try {
			return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private Bitmap readBitmapBySDCard(String fileName) {
		try {
			if (sdCardIsUsed()) {
				File file = new File(this.mFilePath, fileName);
				if (!file.exists()) {
					return null;
				}
				Bitmap bmBitmap = BitmapFactory.decodeFile(this.mFilePath + File.separator + fileName);
				return bmBitmap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @Title: isExistsBitmapBySDCard.
	 * @Description: 判断文件是否存在
	 */
	private boolean isExistsBitmapBySDCard(String fileName) {
		try {
			if (sdCardIsUsed()) {
				File file = new File(this.mFilePath, fileName);
				if (file.exists()) {
					return true;
				} else {
					return false;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param fileName
	 *            "test.jpg"
	 */
	private boolean writeImageToSDCard(String fileName, byte[] data) {
		try {
			if (null == data) {
				return false;
			}
			if (sdCardIsUsed()) {
				File fileDir = new File(this.mFilePath);
				if (!fileDir.exists()) {
					fileDir.mkdirs();
				}
				File file = new File(this.mFilePath, fileName);
				if (file.exists()) {
					return false;
				}

				FileOutputStream fs = new FileOutputStream(file);
				fs.write(data);
				fs.flush();
				fs.close();
				fs = null;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * bytes to bitmap OR bitmap to bytes (by file's extension)
	 */
	/**
	 * @param fileName
	 *            ex:"../test.png" or "test.jpg"
	 */
	private byte[] bitmapToBytes(Bitmap bitmap, String fileName) {
		if (null != bitmap && null != fileName) {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
			if ("jpg".equals(extension)) {
				bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
			} else if ("png".equals(extension)) {
				bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
			} else if ("jpeg".equals(extension)) {
				bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
			} else {
				bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
			}
			return outputStream.toByteArray();
		}
		return null;
	}

	/**
	 * @param bytes
	 *            bytes to bitmap
	 */
	private Bitmap bytesToBitmap(byte[] bytes) {
		if (bytes.length != 0) {
			return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		}
		return null;
	}
}
