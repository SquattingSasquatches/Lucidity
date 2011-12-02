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
		if (resultCode == Codes.REMOTE_QUERY_COMPLETE) {
			
			result = resultData.getString("result");
			
			if (result != null) {
				try {
					update( new JSONArray(result) );
				} catch (JSONException e) {

					try {
						update( new JSONArray("[" + result + "]") );
					} catch (JSONException e1) {
						Log.e("InternalReceiver.onReceiveResult", "Error converting result data to JSONArray");
					}
				}
			}
		} 
	}
	public void update( JSONArray data ){}
	
}
