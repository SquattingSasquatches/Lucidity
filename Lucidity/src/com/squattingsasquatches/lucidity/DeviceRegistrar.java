package com.squattingsasquatches.lucidity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class DeviceRegistrar {
	
	public static final String REMOTE_REGISTRATION_ACTION = "com.squattingsasquatches.lucidity.REMOTE_REGISTRATION";
	public static final String SENDER_ID = "lucidity.app@gmail.com";
	
	public static void startRegistration(Context ctx, BroadcastReceiver receiver) {
		Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
		registrationIntent.putExtra("app", PendingIntent.getBroadcast(ctx, 0, new Intent(), 0));
		registrationIntent.putExtra("sender", C2DMReceiver.SENDER_ID);
		
		ctx.registerReceiver(receiver, new IntentFilter(REMOTE_REGISTRATION_ACTION));
		ctx.startService(registrationIntent);
	}
	
	public static void unregisterReceiver(Context ctx, BroadcastReceiver receiver) {
		try {
			ctx.unregisterReceiver(receiver);
		} catch (IllegalArgumentException e) {
			Log.d("unregisterReceiver", "receiver never registered");
		}
	}
	
	public static void registerWithServer(Context ctx, String registrationId) {
		Log.d("onRegistered", "successfully registered with C2DM server; registrationId: " + registrationId);
		Intent notifySplash = new Intent(REMOTE_REGISTRATION_ACTION);
        notifySplash.putExtra(Codes.KEY_C2DM_ID, registrationId);
        notifySplash.putExtra(Codes.KEY_C2DM_RESULT, Codes.SUCCESS);
        ctx.sendBroadcast(notifySplash);
	}
	
	public static void unregisterWithServer(Context context, String registrationId) {
		Log.d("c2dm", "unregistered");
		//TODO: unregister with remote server
	}
}