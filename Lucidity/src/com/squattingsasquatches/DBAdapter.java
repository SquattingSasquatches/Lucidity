package com.squattingsasquatches;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class DBAdapter extends AsyncTask<JSONObject, Void, JSONObject>{
	
	private JSONObject result;
	private Activity caller;
	@SuppressWarnings("unused")
	private ProgressDialog dialog;
	
	public DBAdapter(Activity caller) {
		this.caller = caller;
	}
	
	public void query(HashMap<String, String> params) {
		JSONObject jsonParams = new JSONObject();
		for (HashMap.Entry<String, String> p : params.entrySet()) {
			try {
				jsonParams.accumulate(p.getKey(), p.getValue());
			} catch (JSONException e) {
				Log.e("query() > JSONException", e.getMessage());
			}
		}
		this.execute(jsonParams);
	}
	
	public JSONObject getResult() {
		return result;
	}

	@Override
	protected JSONObject doInBackground(JSONObject... params) {
		
		return null;
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
