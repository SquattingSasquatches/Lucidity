package com.squattingsasquatches.lucidity;

import org.json.JSONArray;
import org.json.JSONException;


import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import java.util.HashMap;
import android.os.ResultReceiver;

public class InternalReceiver extends ResultReceiver {
	
	private String result;
	public HashMap<String, String> params;
	//private Receiver mReceiver;
	
	public InternalReceiver()
	{
		super(new Handler());
	}
	public void addParam(String key, String value) {
		params.put(key, value);
	}
	
	public void addParam(String key, int value) {
		params.put(key, String.valueOf(value));
	}
	
	public void setAction(String action) {
		params.put("action", action);
	}


//    public void setReceiver(Receiver receiver) {
//        //mReceiver = receiver;
//    }
	public void onReceiveResult(int resultCode, Bundle resultData) {
		// result from remote PHP query
		Log.i("InternalReceiver.onReceiveResult", String.valueOf(resultCode));
		
		if (resultCode == Codes.REMOTE_QUERY_COMPLETE) {
			
			result = resultData.getString("result");
			
			if (result != null) {
				try {
					update( new JSONArray(result) );
				} catch (JSONException e) {
					Log.e("InternalReceiver.onReceiveResult", "error with JSONArray");
				}
			}
		} 
	}
	public void update( JSONArray data ){}
	
}
