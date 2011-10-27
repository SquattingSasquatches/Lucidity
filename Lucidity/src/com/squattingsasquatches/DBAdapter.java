package com.squattingsasquatches;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class DBAdapter extends AsyncTask<ArrayList<NameValuePair>, Void, JSONObject>{
	
	private JSONObject result = null;
	private Activity caller = null;
	private String action = "";
	
	@SuppressWarnings("unused")
	private ProgressDialog dialog;
	
	public DBAdapter(Activity caller) {
		this.caller = caller;
	}
	
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
		
		this.execute(nvParams);
	}
	
	public JSONObject getResult() {
		return result;
	}

	@Override
	protected JSONObject doInBackground(ArrayList<NameValuePair>... params) {
		InputStream is;
		String line, result = "";
		JSONObject jsonResult = new JSONObject();
		
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("https://www.thesouthernshirtco.com/lucidity/" + action + ".php");
			httppost.setEntity(new UrlEncodedFormEntity(params[0]));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
				
				while ((line = reader.readLine()) != null) {
					result += line;
				}
				 
				jsonResult = new JSONObject(result);
				
			} catch (UnsupportedEncodingException e) {
				Log.e("doInBackground", "Error getting http result " + e.getMessage());
			}
			
		} catch (Exception e) {
			Log.e("doInBackground", "Error in http connection " + e.getMessage());
		}
		
		return jsonResult;
	}
	
	@Override
	protected void onPostExecute(JSONObject result) {
		dialog = null;
		this.result = result;
	}
	 
	@Override
	protected void onPreExecute() {
		dialog = ProgressDialog.show(caller, "", "Loading. Please wait...", true);
	}
}
