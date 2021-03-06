package com.squattingsasquatches.lucidity;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

public class InternalReceiver extends ResultReceiver {
	
	private HashMap<String, String> params;
	//private Receiver mReceiver;
	
	public InternalReceiver()
	{
		super(new Handler());
		params = new HashMap<String, String>();
	}
	
	public void addParam(String key, String value) {
		params.put(key, value);
	}
	
	public void addParam(String key, int value) {
		params.put(key, String.valueOf(value));
	}
	
	public HashMap<String, String> getParams() {
		return params;
	}
	
	public void setAction(String action) {
		params.put("action", action);
	}


//    public void setReceiver(Receiver receiver) {
//        //mReceiver = receiver;
//    }
	public void onReceiveResult(int resultCode, Bundle resultData) {
		// result from remote PHP query		
		
		
		// getStringArrayList caused null pointer exception
		if (resultCode == Codes.REMOTE_QUERY_COMPLETE) {
			String result = resultData.getString("result");
			try {
				if (result.startsWith("["))
					update( new JSONArray(result) );
				else
					update( new JSONArray("[" + result + "]") );
			} catch (JSONException e) {
				Log.e("onReceiveResult", "Query did not return valid result");
			}
		}
		else if (resultCode == Codes.REMOTE_CONNECTION_ERROR) {	
			onConnectionError( resultData.getString("connection_error_code") );
		} 
		else
		{
			onHttpError( resultData.getInt("httpstatuscode") );
		}
	}
	public void update( JSONArray data ){}
	public void onHttpError( int statusCode ){}
	public void onConnectionError( String errorMessage ){}
	
}
