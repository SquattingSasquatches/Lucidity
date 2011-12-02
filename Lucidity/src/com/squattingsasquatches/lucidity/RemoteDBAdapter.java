package com.squattingsasquatches.lucidity;

import java.util.HashMap;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class RemoteDBAdapter {
	
	private Context ctx;
	private Intent dbService;
	private HashMap<String, InternalReceiver> receivers;
	
	public RemoteDBAdapter(Context ctx) {
		this.ctx = ctx;
		
		// initialize our db service
        dbService = new Intent(ctx, PHPService.class);
        receivers = new HashMap<String, InternalReceiver>();
	}
	
	
	public void addReceiver(String name, InternalReceiver receiver) {
		receivers.put( name, receiver );
	}
	
	public void unregisterReceiver(String name) {
		try {
			receivers.remove(null);
		} catch(IllegalArgumentException e) {
			Log.i("unregisterReceiver", "receiver not registered");
		}
	}
	
	public void execute(String name) {
		dbService.putExtra("params", receivers.get(name).params);
		dbService.putExtra("receiver", receivers.get(name));
		ctx.startService(dbService);
	}
	
    
	public boolean stopService() {
		return ctx.stopService(dbService);
	}
}
