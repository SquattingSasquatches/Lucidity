package com.squattingsasquatches.lucidity;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

public class InternalReceiver extends ResultReceiver {

	protected HashMap<String, String> params;

	public InternalReceiver() {
		super(new Handler());
		this.params = new HashMap<String, String>();
	}

	public void addParam(String key, int value) {
		this.params.put(key, String.valueOf(value));
	}

	public void addParam(String key, String value) {
		this.params.put(key, value);
	}

	public HashMap<String, String> getParams() {
		return this.params;
	}

	public void onConnectionError(String errorMessage) {
	}

	public void onHttpError(int statusCode) {
	}

	public void onInvalidMessage(String result) {
		Log.e("InternalReceiver", "Query did not return valid result. Result: "
				+ result);
	}

	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		if (resultCode == Codes.REMOTE_QUERY_COMPLETE) {
			final String result = resultData.getString("result");
			Log.i("OnReceiveResult()", result);
			try {
				if (result.startsWith("["))
					this.update(new JSONArray(result));
				else
					this.update(new JSONArray("[" + result + "]"));
			} catch (final JSONException e) {

				this.onInvalidMessage(result);
			}
		} else if (resultCode == Codes.REMOTE_CONNECTION_ERROR)
			this.onConnectionError(resultData
					.getString("connection_error_code"));
		else
			this.onHttpError(resultData.getInt("httpstatuscode"));
	}

	public void setAction(String action) {
		this.params.put("action", action);
	}

	public void update(JSONArray data) {
	}

	public boolean validate() {
		return true;
	}

}
