package com.squattingsasquatches.lucidity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class C2DMReceiver extends BroadcastReceiver {
	
	public static final String SENDER_ID = "lucidity.app@gmail.com";
	
	private Context ctx;
	private String c2dmRegistrationID;
	
	@Override
	public void onReceive(Context ctx, Intent intent) {
		c2dmRegistrationID = intent.getStringExtra("registration_id");
		this.ctx = ctx;
		if (intent.getAction().equals("com.google.android.c2dm.intent.REGISTRATION")) {
			handleRegistration(intent);
		} else if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
			handleMessage(intent);
		}
	}
	
	private void handleRegistration(Intent intent) {
		Bundle extras = intent.getExtras();
		
		if (extras != null) {
			if (extras.getString("error") != null) {
		        // Registration failed, should try again later.
			    String error = extras.getString("error");
			    Log.d("c2dm error", error);
		    } else if (extras.getString("unregistered") != null) {
		    	String storedRegistrationId = ""; //TODO: load registrationId from local DB
		    	DeviceRegistrar.unregisterWithServer(ctx, storedRegistrationId);
		    } else if (c2dmRegistrationID != null) {
		    	DeviceRegistrar.registerWithServer(ctx, c2dmRegistrationID);
		    }
		}
	}
	
    public void handleMessage(Intent intent) {
       Bundle extras = intent.getExtras();
       
       if (extras != null) {
           Toast.makeText(ctx,  "Message Received: " + extras.getString("msg"), Toast.LENGTH_LONG).show();
       }
   }
}