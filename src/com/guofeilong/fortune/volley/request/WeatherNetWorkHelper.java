package com.guofeilong.fortune.volley.request;

import org.json.JSONObject;

import android.content.Context;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.guofeilong.fortune.ui.GolenCoinActivity;
import com.guofeilong.fortune.volley.bean.Weather;
import com.guofeilong.fortune.volley.utils.VolleySystemCode;
import com.guofeilong.fortune.volley.utils.VolleyNetWorkHelper;

public class WeatherNetWorkHelper extends VolleyNetWorkHelper<Weather> {

	public WeatherNetWorkHelper(Context context) {
		super(context);
	}

	@Override
	protected void disposeVolleyError(VolleyError error) {
		notifyErrorHappened(VolleySystemCode.SUCCESS_CODE, error == null ? "NULL" : error.getMessage());
	}

	@Override
	protected void disposeResponse(JSONObject response) {

		Weather weather = null;

		if (response != null) {

			try {
				// String errorCode = response.getString("errorCode");
				// String errorMessage = response.getString("errorMessage");
				// String respMsg = response.getString("respMsg");
				// String success = response.getString("success");

				// if (VolleySystemCode.SUCCESS_CODE.equals(errorCode)) {
				// weather = new Weather();
				// weather.setErrorCode(errorCode);
				// weather.setErrorMessage(errorMessage);
				// weather.setRespMsg(respMsg);
				// weather.setSuccess(success);

				// notifyDataChanged(weather);
				// } else {
				// notifyErrorHappened(errorCode, errorMessage);
				// }

				Gson gson = new Gson();
				weather = gson.fromJson(response.toString(), Weather.class);
				notifyDataChanged(weather);

			} catch (Exception e) {
				notifyErrorHappened(VolleySystemCode.RESPONSE_FORMAT_ERROR, "Response format error");
			}
		} else {
			notifyErrorHappened(VolleySystemCode.RESPONSE_IS_NULL, "Response is null!");
		}

	}

}
