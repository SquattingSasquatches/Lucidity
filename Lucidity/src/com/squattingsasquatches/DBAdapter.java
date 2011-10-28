package com.squattingsasquatches;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class DBAdapter {
	
	private PHPConnection connection;
	private String action = "";
	private String result = "";
	private Object mutex = new Object();
	
	@SuppressWarnings("unchecked")
	public void query(HashMap<String, String> params) {
		ArrayList<NameValuePair> nvParams = new ArrayList<NameValuePair>();
		
		action = params.get("action");
		
		if (action == null) {
			Log.e("query()", "No action specified");
			return;
		}
		
		for (HashMap.Entry<String, String> p : params.entrySet()) {
			if (p.getKey() != action) {
				nvParams.add(new BasicNameValuePair(p.getKey(), p.getValue()));
			}
		}
		
		synchronized (mutex) {

			connection = new PHPConnection(action, mutex);
			connection.execute(nvParams);
			
			try {
				mutex.wait();
			} catch (InterruptedException e) {
				Log.e("DBAdapter", e.getMessage());
			}
			
			result = connection.getResult();
		
		}
	}
	
	public int getResult() {
		return Integer.parseInt(result);
	}
	
	public JSONObject getJSONResult() {
		try {
			return new JSONObject(result);
		} catch (JSONException e) {
			Log.e("getJSONResult() error", e.getMessage());
		}
		
		return null;
	}
}
