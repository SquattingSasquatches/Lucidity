package com.squattingsasquatches;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class PHPConnection {
	
	public static final String ACTION_RESP = "com.squattingsasquatches.intent.action.MESSAGE_PROCESSED";
	
	private Activity caller;
	private String callback;
	private Context ctx;
	private Intent dbService;
	private ResponseReceiver receiver;
	private HashMap<String, String> params;
	
	public PHPConnection(Context ctx, Activity caller) {
		
		this.ctx = ctx;
		this.caller = caller;
		
		// initialize our db service
        dbService = new Intent(ctx, DBService.class);
        params = new HashMap<String, String>();
        
        IntentFilter filter = new IntentFilter(ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ResponseReceiver();
        ctx.registerReceiver(receiver, filter);
	}
	
	public void addParam(String key, String value) {
		params.put(key, value);
	}
	
	public void setAction(String action) {
		params.put("action", action);
	}
	
	public void unregisterReceiver() {
		try {
			ctx.unregisterReceiver(receiver);
		} catch(IllegalArgumentException e) {
			Log.i("unregisterReceiver", "receiver not registered");
		}
	}
	
	public void execute(String callback) {
		this.callback = callback;
		dbService.putExtra(DBService.PARAM_IN_MSG, params);
		ctx.startService(dbService);
	}
    
    public class ResponseReceiver extends BroadcastReceiver {
    	
		@Override
		public void onReceive(Context context, Intent intent) {
			String result = intent.getStringExtra(DBService.PARAM_OUT_MSG);
			
			
			if (result.equals("false")) {
				// query returned empty
			} else {
				// query returned error code or success
				try {
					JSONArray resultJSON = new JSONArray(result);
					
					try {
						// Reflection... callback method is determined at runtime
						if (resultJSON.isNull(1)) {
							// result has only 1 element, return error code id
							// - might be a problem if query returns result set with only 1 element and it's not an error...not sure what to do
							caller.getClass().getMethod(callback, int.class).invoke(caller, resultJSON.getJSONObject(0).getInt("id"));
						} else {
							// return entire result set
							caller.getClass().getMethod(callback, JSONArray.class).invoke(caller, resultJSON);
						}
						
					} catch (IllegalArgumentException e) {
						Log.e("IllegalArgumentException", e.getMessage());
					} catch (SecurityException e) {
						Log.e("SecurityException", e.getMessage());
					} catch (IllegalAccessException e) {
						Log.e("IllegalAccessException", e.getMessage());
					} catch (InvocationTargetException e) {
						Log.e("InvocationTargetException", e.getMessage());
					} catch (NoSuchMethodException e) {
						Log.e("NoSuchMethodException", e.getMessage());
					}
						
				} catch (JSONException e) {
					Log.e("BroadcastReceiver JSONExecption", e.getMessage());
					
				}
			}			
		}
    }
}
