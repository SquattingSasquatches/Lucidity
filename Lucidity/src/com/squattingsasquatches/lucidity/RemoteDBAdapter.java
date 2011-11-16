package com.squattingsasquatches.lucidity;

import java.util.HashMap;

import org.json.JSONArray;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class RemoteDBAdapter {
	
	public static final String ACTION_RESP = "com.squattingsasquatches.intent.action.MESSAGE_PROCESSED";
	
	private Activity caller;
	private String callback;
	private Context ctx;
	private Intent dbService;
	private ResponseReceiver receiver;
	private HashMap<String, String> params;
	
	public RemoteDBAdapter(Context ctx, Activity caller) {
		this.ctx = ctx;
		this.caller = caller;
		
		// initialize our db service
        dbService = new Intent(ctx, PHPService.class);
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
		dbService.putExtra(PHPService.PARAM_IN_MSG, params);
		ctx.startService(dbService);
	}
	
	public void execute(String action, HashMap<String, String> params, String callback) {
		setAction(action);
		this.params = params;
		execute(callback);
	}
	
	public void execute(String action, String paramKeys, String paramValues, String callback) {
		setAction(action);
		String[] keys = paramKeys.split(","), vals = paramValues.split(",");
		
		for (int i = 0; i < keys.length; ++i) {
			addParam(keys[i].trim(), vals[i].trim());
		}
		
		execute(callback);
	}
    
	public boolean stopService() {
		return ctx.stopService(dbService);
	}
	
    public class ResponseReceiver extends BroadcastReceiver {
    	
		@Override
		public void onReceive(Context context, Intent intent) {
			String result = intent.getStringExtra(PHPService.PARAM_OUT_MSG);
			JSONArray resultJSON = new JSONArray();
									
			try {
				
				if (!result.equals("false"))
					resultJSON = new JSONArray(result);

				// Reflection... callback method is determined at runtime
				caller.getClass().getMethod(callback, JSONArray.class).invoke(caller, resultJSON);
				
			} catch (Exception e) {
				Log.e("BroadcastReceiver Execption", e.getMessage());
			}
		}
    }
}
