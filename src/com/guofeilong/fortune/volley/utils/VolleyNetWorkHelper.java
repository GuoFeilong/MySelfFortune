package com.guofeilong.fortune.volley.utils;

import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

public abstract class VolleyNetWorkHelper<T> implements Response.Listener<JSONObject>, ErrorListener {
	private Context context;

	public VolleyNetWorkHelper(Context context) {
		this.context = context;
	}

	protected Context getContext() {
		return context;
	}

	protected VolleyNetWorkRequest getRequestForGet(String url, List<NameValuePair> params) {
		if (params == null) {
			return new VolleyNetWorkRequest(url, this, this);
		} else {
			return new VolleyNetWorkRequest(url, params, this, this);
		}

	}

	protected VolleyNetWorkRequest getRequestForPost(String url, Map<String, String> params) {
		return new VolleyNetWorkRequest(Method.POST, url, params, this, this);
	}

	public void sendGETRequest(String url, List<NameValuePair> params) {
		Volley.newRequestQueue(getContext()).add(getRequestForGet(url, params));
	}

	public void sendPostRequest(String url, Map<String, String> params) {
		Volley.newRequestQueue(getContext()).add(getRequestForPost(url, params));
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		Log.d("Amuro", error.getMessage());
		disposeVolleyError(error);
	}

	protected abstract void disposeVolleyError(VolleyError error);

	@Override
	public void onResponse(JSONObject response) {
		Log.d("Amuro", response.toString());
		disposeResponse(response);
	}

	protected abstract void disposeResponse(JSONObject response);

	private VolleyUIDataListener<T> uiDataListener;

	public void setUiDataListener(VolleyUIDataListener<T> uiDataListener) {
		this.uiDataListener = uiDataListener;
	}

	protected void notifyDataChanged(T data) {
		if (uiDataListener != null) {
			uiDataListener.onDataChanged(data);
		}
	}

	protected void notifyErrorHappened(String errorCode, String errorMessage) {
		if (uiDataListener != null) {
			uiDataListener.onErrorHappened(errorCode, errorMessage);
		}
	}

}