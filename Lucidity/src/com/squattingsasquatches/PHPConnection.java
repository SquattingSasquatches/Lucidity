package com.squattingsasquatches;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class PHPConnection extends AsyncTask<ArrayList<NameValuePair>, Void, Void> {

	private String action;
	private String result = "";
	private Object mutex = new Object();
	
	public PHPConnection(String action, Object mutex) {
		this.action = action;
		this.mutex = mutex;
	}
	
	public String getResult() {
		return result;
	}
	
	@Override
	protected Void doInBackground(ArrayList<NameValuePair>... params) {
		InputStream is;
		String line;
		
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
				
			} catch (UnsupportedEncodingException e) {
				Log.e("doInBackground", "Error getting http result " + e.getMessage());
			}
			
		} catch (Exception e) {
			Log.e("doInBackground", "Error in http connection " + e.getMessage());
		}
		
		synchronized (mutex) {
			mutex.notify();
		}
		
		return null;
	}
}
