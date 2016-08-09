package com.guofeilong.fortune.ui;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.business.ChartMessage;
import com.guofeilong.fortune.business.Result;
import com.guofeilong.fortune.business.ChartMessage.MyType;
import com.guofeilong.fortune.okhttp.OkHttpClientManager;
import com.guofeilong.fortune.utils.T;
import com.guofeilong.fortune.utils.TuLingNeT;
import com.guofeilong.fortune.volley.request.WeatherNetWorkHelper;
import com.guofeilong.fortune.volley.utils.VolleyNetWorkHelper;
import com.guofeilong.fortune.volley.utils.VolleyUIDataListener;

public class VolleyTestActivity extends BaseActivity implements OnClickListener, VolleyUIDataListener<com.guofeilong.fortune.volley.bean.Weather> {
	private static final int _ID = 1000;
	private int index;
	/**
	 * 测试网络请求
	 */
	private static final String URL = "http://www.baidu.com";
	private static final String URL_JSON = "http://m.weather.com.cn/data/101010100.html";
	private static final String URL_IMAGE = "http://img5.duitang.com/uploads/item/201403/08/20140308022025_5AFuT.thumb.700_0.jpeg";
	private static final String URL_XML = "http://flash.weather.com.cn/wmaps/xml/china.xml";
	private static final String URL_GSON = "http://www.weather.com.cn/data/sk/101010100.html";
	private int mColor;
	private String mTitle;
	private List<Button> mButtons;
	private TextView mVolleyReponse;
	private ImageView mVolleyImage;
	private LinearLayout mButtonContainer;
	private List<Integer> mIDs;
	/**
	 * volley的消息列表,要把用的消息add到消息队列中
	 */
	private RequestQueue volleyRequestQueue;

	private VolleyNetWorkHelper<com.guofeilong.fortune.volley.bean.Weather> mNetWorkHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_volley_test);
		mTitle = getMyTitleAndColor();
		initNavigation();
		initData();
		initVolley();
		initView();
		initEvent();

		/**
		 * volley 二次封装使用
		 */
		mNetWorkHelper = new WeatherNetWorkHelper(this);
		mNetWorkHelper.setUiDataListener(this);
	}

	/**
	 * 初始化volley
	 */
	private void initVolley() {
		volleyRequestQueue = Volley.newRequestQueue(this);

	}

	/**
	 * 初始化事件
	 */
	private void initEvent() {
		addOneButton("StringRequest");
		addOneButton("JsonRequest");
		addOneButton("ImagRequest");
		addOneButton("XML_Request");
		addOneButton("Gson_Request");
		addOneButton("Volley_tuling");
		addOneButton("Volley_封装");
		addOneButton("OkHttpTest");

		setButtonColor(mButtons);
		for (int i = 0; i < mButtons.size(); i++) {
			mButtons.get(i).setOnClickListener(this);
		}
	}

	/**
	 * 设置按钮颜色
	 * 
	 * @param mButtons2
	 */
	private void setButtonColor(List<Button> mButtons2) {
		for (int i = 0; i < mButtons2.size(); i++) {
			mButtons2.get(i).setTextColor(mColor);
		}
	}

	/**
	 * 初始化控件https://github.com/GuoFeilong/GuoFeiLongSelf.git
	 */
	private void initView() {
		mVolleyReponse = (TextView) findViewById(R.id.tv_volley_response);
		mVolleyImage = (ImageView) findViewById(R.id.iv_volley_image);
		mButtonContainer = (LinearLayout) findViewById(R.id.ll_button_container);

	}

	/**
	 * 增加一个button
	 */
	private void addOneButton(String text) {
		Button button = new Button(this);
		button.setText(text);
		button.setTextColor(mColor);
		button.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 3, getResources().getDisplayMetrics()));
		button.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_audio_button_normal));
		button.setGravity(Gravity.CENTER);
		button.setId(_ID + index);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(getScreenWith() / 4, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, getResources().getDisplayMetrics()));
		lp.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
		lp.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
		lp.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
		lp.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
		button.setLayoutParams(lp);

		mIDs.add(_ID + index);
		mButtonContainer.addView(button);
		mButtons.add(button);

		index++;
	}

	/**
	 * 获取屏幕宽度
	 * 
	 * @return
	 */
	private int getScreenWith() {
		WindowManager windowManager = getWindowManager();
		Display defaultDisplay = windowManager.getDefaultDisplay();
		return defaultDisplay.getWidth();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mButtons = new ArrayList<Button>();
		mIDs = new ArrayList<Integer>();
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
		if (v.getId() == mIDs.get(0)) {
			chanButtonClickState(v);
			addStringRequest(URL);
		} else if (v.getId() == mIDs.get(1)) {
			chanButtonClickState(v);
			addJsonResquest(URL_JSON);
		} else if (v.getId() == mIDs.get(2)) {
			chanButtonClickState(v);
			mVolleyImage.setVisibility(View.VISIBLE);
			addImageResquest(URL_IMAGE);
		} else if (v.getId() == mIDs.get(3)) {
			chanButtonClickState(v);
			addXMLResquest(URL_XML);
		} else if (v.getId() == mIDs.get(4)) {
			chanButtonClickState(v);
			addGsonResquest(URL_GSON);
		} else if (v.getId() == mIDs.get(5)) {
			chanButtonClickState(v);
			String doGet = TuLingNeT.doGet("讲个笑话");
			addStringRequest(doGet);
		} else if (v.getId() == mIDs.get(6)) {
			chanButtonClickState(v);

			// 发送请求
			mNetWorkHelper.sendGETRequest(URL_GSON, null);
		}else if(v.getId()==mIDs.get(7)){
			chanButtonClickState(v);
//			OkHttpClientManager.getAsyn(URL, new OkHttpClientManager.ResultCallback() {
//				@Override
//				public void onError(com.squareup.okhttp.Request request, Exception e) {
//					T.show(VolleyTestActivity.this,"error",0);
//				}
//
//				@Override
//				public void onResponse(Object response) {
//
//					T.show(VolleyTestActivity.this,"success",0);
//				}
//			});
			T.show(this,"okhttptest",0);
		}

		switch (v.getId()) {
		case R.id.commond_imagebutton_title_leftbutton:
			finish();
			break;
		default:
			break;
		}

	}

	/**
	 * Volley 结合Gson 解析json
	 * 
	 * @param urlGson
	 */
	private void addGsonResquest(String urlGson) {
		GsonRequest<Weather> gsonRequest = new GsonRequest<Weather>(urlGson, Weather.class, new Response.Listener<Weather>() {

			@Override
			public void onResponse(Weather response) {
				mVolleyReponse.setText(response.toString());
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				T.show(VolleyTestActivity.this, "返回错误....", 0);
				mVolleyReponse.setText(error.toString());
			}
		});

		volleyRequestQueue.add(gsonRequest);
	}

	/**
	 * Volley XML Requst
	 * 
	 * @param urlXml
	 */
	private void addXMLResquest(String urlXml) {
		final StringBuffer buffer = new StringBuffer();
		XMLRequest xmlRequest = new XMLRequest(urlXml, new Response.Listener<XmlPullParser>() {

			@Override
			public void onResponse(XmlPullParser response) {
				try {
					int eventType = response.getEventType();
					while (eventType != XmlPullParser.END_DOCUMENT) {
						switch (eventType) {
						case XmlPullParser.START_TAG:
							String name = response.getName();
							if (name.equals("city")) {
								String city = response.getAttributeValue(0);
								String weather = response.getAttributeValue(6);
								buffer.append("city==").append(city).append("==weather==").append(weather);
							}
							break;

						default:
							break;
						}
						eventType = response.next();
					}

					mVolleyReponse.setText(buffer.toString());
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				T.show(VolleyTestActivity.this, "返回错误....", 0);
				mVolleyReponse.setText(error.toString());
			}
		});

		volleyRequestQueue.add(xmlRequest);
	}

	/**
	 * Volley Image Requst imagr loader 更高效内部原理也是imagerequest
	 * 
	 * @param urlImage
	 */
	private void addImageResquest(String urlImage) {
		mVolleyReponse.setText("");
		ImageLoader imageLoader = new ImageLoader(volleyRequestQueue, new BitmapCache());
		ImageListener imageListener = imageLoader.getImageListener(mVolleyImage, R.drawable.bg_transfer, R.drawable.bg_transfer);
		imageLoader.get(URL_IMAGE, imageListener);
	}

	/**
	 * 图片缓存
	 * 
	 * @author guofl
	 * 
	 */
	public class BitmapCache implements ImageCache {

		private LruCache<String, Bitmap> mCache;

		public BitmapCache() {
			int maxSize = 10 * 1024 * 1024;
			mCache = new LruCache<String, Bitmap>(maxSize) {
				@Override
				protected int sizeOf(String key, Bitmap bitmap) {
					return bitmap.getRowBytes() * bitmap.getHeight();
				}
			};
		}

		@Override
		public Bitmap getBitmap(String url) {
			return mCache.get(url);
		}

		@Override
		public void putBitmap(String url, Bitmap bitmap) {
			mCache.put(url, bitmap);
		}

	}

	/**
	 * Volley Json Request
	 * 
	 * @param urlJson
	 */
	private void addJsonResquest(String urlJson) {
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL_JSON, null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {

				mVolleyReponse.setText(response.toString());
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				T.show(VolleyTestActivity.this, "返回错误....", 0);
				mVolleyReponse.setText(error.toString());

			}
		});

		volleyRequestQueue.add(jsonObjectRequest);
	}

	/**
	 * 改变按钮选中状态
	 * 
	 * @param v
	 */
	private void chanButtonClickState(View v) {
		for (int i = 0; i < mButtons.size(); i++) {
			mButtons.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_audio_button_normal));
			mButtons.get(i).setTextColor(mColor);
		}
		v.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_audio_button_cancel));
		Button vv = (Button) v;
		vv.setTextColor(getResources().getColor(R.color.demo_title1));
		mVolleyImage.setVisibility(View.INVISIBLE);
	}

	/**
	 * volley的stringquest请求
	 */
	private void addStringRequest(String url) {
		StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {

				// ChartMessage chartMessage = new ChartMessage();
				// try {
				// Result result = new Result();
				// Gson gson = new Gson();
				//
				// result = gson.fromJson(response, Result.class);
				// chartMessage.setDate(new Date());
				// chartMessage.setMsg(result.getText());
				// chartMessage.setType(MyType.INCOMING);
				// } catch (Exception e) {
				// // TODO Auto-generated catch block
				// chartMessage.setMsg("服务器忙,请重试....");
				// e.printStackTrace();
				// }
				//

				mVolleyReponse.setText(response);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				T.show(VolleyTestActivity.this, "response error", 0);
			}
		});

		volleyRequestQueue.add(stringRequest);
	}

	/**
	 * 自定义xml解析请求
	 * 
	 * @author guofl
	 * 
	 */
	public class XMLRequest extends Request<XmlPullParser> {

		private final Listener<XmlPullParser> mListener;

		public XMLRequest(int method, String url, Listener<XmlPullParser> listener, ErrorListener errorListener) {
			super(method, url, errorListener);
			mListener = listener;
		}

		public XMLRequest(String url, Listener<XmlPullParser> listener, ErrorListener errorListener) {
			this(Method.GET, url, listener, errorListener);
		}

		@Override
		protected Response<XmlPullParser> parseNetworkResponse(NetworkResponse response) {
			try {
				String xmlString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				XmlPullParser xmlPullParser = factory.newPullParser();
				xmlPullParser.setInput(new StringReader(xmlString));
				return Response.success(xmlPullParser, HttpHeaderParser.parseCacheHeaders(response));
			} catch (UnsupportedEncodingException e) {
				return Response.error(new ParseError(e));
			} catch (XmlPullParserException e) {
				return Response.error(new ParseError(e));
			}
		}

		@Override
		protected void deliverResponse(XmlPullParser response) {
			mListener.onResponse(response);
		}

	}

	/**
	 * 自定义Gson解析请求
	 * 
	 * @author guofl
	 * 
	 * @param <T>
	 */
	public class GsonRequest<T> extends Request<T> {

		private final Listener<T> mListener;

		private Gson mGson;

		private Class<T> mClass;

		public GsonRequest(int method, String url, Class<T> clazz, Listener<T> listener, ErrorListener errorListener) {
			super(method, url, errorListener);
			mGson = new Gson();
			mClass = clazz;
			mListener = listener;
		}

		public GsonRequest(String url, Class<T> clazz, Listener<T> listener, ErrorListener errorListener) {
			this(Method.GET, url, clazz, listener, errorListener);
		}

		@Override
		protected Response<T> parseNetworkResponse(NetworkResponse response) {
			try {
				String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
				return Response.success(mGson.fromJson(jsonString, mClass), HttpHeaderParser.parseCacheHeaders(response));
			} catch (UnsupportedEncodingException e) {
				return Response.error(new ParseError(e));
			}
		}

		@Override
		protected void deliverResponse(T response) {
			mListener.onResponse(response);
		}

	}

	public class Weather {

		private Weatherinfo weatherinfo;

		public Weatherinfo getWeatherinfo() {
			return weatherinfo;
		}

		public void setWeatherinfo(Weatherinfo weatherinfo) {
			this.weatherinfo = weatherinfo;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((weatherinfo == null) ? 0 : weatherinfo.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Weather other = (Weather) obj;
			if (weatherinfo == null) {
				if (other.weatherinfo != null)
					return false;
			} else if (!weatherinfo.equals(other.weatherinfo))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "Weather [weatherinfo=" + weatherinfo + "]";
		}

	}

	/**
	 * 天气对象
	 * 
	 * @author guofl
	 * 
	 */
	class Weatherinfo {
		String city;
		String cityid;
		String temp;
		String WD;
		String time;

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getCityID() {
			return cityid;
		}

		public void setCityID(String cityID) {
			this.cityid = cityID;
		}

		public String getTemp() {
			return temp;
		}

		public void setTemp(String temp) {
			this.temp = temp;
		}

		public String getWind() {
			return WD;
		}

		public void setWind(String wind) {
			this.WD = wind;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((city == null) ? 0 : city.hashCode());
			result = prime * result + ((cityid == null) ? 0 : cityid.hashCode());
			result = prime * result + ((temp == null) ? 0 : temp.hashCode());
			result = prime * result + ((time == null) ? 0 : time.hashCode());
			result = prime * result + ((WD == null) ? 0 : WD.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Weatherinfo other = (Weatherinfo) obj;
			if (city == null) {
				if (other.city != null)
					return false;
			} else if (!city.equals(other.city))
				return false;
			if (cityid == null) {
				if (other.cityid != null)
					return false;
			} else if (!cityid.equals(other.cityid))
				return false;
			if (temp == null) {
				if (other.temp != null)
					return false;
			} else if (!temp.equals(other.temp))
				return false;
			if (time == null) {
				if (other.time != null)
					return false;
			} else if (!time.equals(other.time))
				return false;
			if (WD == null) {
				if (other.WD != null)
					return false;
			} else if (!WD.equals(other.WD))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "Weatherinfo [city=" + city + ", cityID=" + cityid + ", temp=" + temp + ", wind=" + WD + ", time=" + time + "]";
		}

	}

	@Override
	public void onDataChanged(com.guofeilong.fortune.volley.bean.Weather data) {
		T.show(this, data.toString(), 0);
		mVolleyReponse.setText(data.toString());

	}

	@Override
	public void onErrorHappened(String errorCode, String errorMessage) {
		T.show(this, errorCode + "----" + errorMessage, 0);
	}
}
