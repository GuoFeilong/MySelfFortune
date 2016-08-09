package com.guofeilong.fortune.utils;

import java.net.URLEncoder;
import java.util.Date;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.business.ChartMessage;
import com.guofeilong.fortune.business.Result;
import com.guofeilong.fortune.business.ChartMessage.MyType;

public class TuLingNeT {
	public static String doGet(String msg) {
		String getURL = "";
		try {
			String APIKEY = AppConstants.TULING_APP_KEY;
			String INFO = URLEncoder.encode(msg, AppConstants.TULING_ENCODE);
			getURL = AppConstants.TULING_API_ADDRESS + APIKEY + "&info=" + INFO;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getURL;
	}

	/**
	 * 用户输入msg 返回chartmessage对象
	 * 
	 * @param msg
	 * @return
	 */

	public static ChartMessage sendMessage(String msg, Context context) {

		String url = doGet(msg);
		final ChartMessage chartMessage = new ChartMessage();
		// 初始化volley
		initVolley(context);
		StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				Log.e("TULING_TAG_2", response);
				try {
					Result result = new Result();
					Gson gson = new Gson();

					result = gson.fromJson(response, Result.class);
					chartMessage.setDate(new Date());
					chartMessage.setMsg(result.getText());
					chartMessage.setType(MyType.INCOMING);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					chartMessage.setMsg("服务器忙,请重试....");
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {

			}
		});

		volleyRequestQueue.add(stringRequest);
		Log.e("TULING_TAG", chartMessage.toString());
		return chartMessage;

	}

	private static RequestQueue volleyRequestQueue;

	/**
	 * 初始化volley
	 */
	public static void initVolley(Context context) {
		volleyRequestQueue = Volley.newRequestQueue(context);
	}

}
