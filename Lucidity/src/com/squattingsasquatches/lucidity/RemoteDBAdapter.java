package com.squattingsasquatches.lucidity;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class RemoteDBAdapter {
	
	private Context ctx;
	private Intent dbService;
	private RemoteResultReceiver receiver;
	private HashMap<String, String> params;
	
	public RemoteDBAdapter(Context ctx) {
		this.ctx = ctx;
		
		// initialize our db service
        dbService = new Intent(ctx, PHPService.class);
        params = new HashMap<String, String>();
        receiver = new RemoteResultReceiver(new Handler());
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
	
	public void setReceiver(RemoteResultReceiver.Receiver receiver) {
		this.receiver.setReceiver(receiver);
	}
	
	public void unregisterReceiver() {
		try {
			receiver.setReceiver(null);
		} catch(IllegalArgumentException e) {
			Log.i("unregisterReceiver", "receiver not registered");
		}
	}
	
	public void execute() {
		dbService.putExtra("params", params);
		dbService.putExtra("receiver", receiver);
		ctx.startService(dbService);
	}
	
	public void execute(int callback) {
		dbService.putExtra(Codes.KEY_CALLBACK, callback);
		execute();
	}
	
	public void execute(String action, HashMap<String, String> params, int callback) {
		setAction(action);
		this.params = params;
		execute(callback);
	}
	
	public void execute(String action, String paramKeys, String paramValues, int callback) {
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
}
