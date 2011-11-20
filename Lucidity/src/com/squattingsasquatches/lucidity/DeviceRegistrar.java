package com.squattingsasquatches.lucidity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DeviceRegistrar {
	
	public static final String SENDER_ID = "lucidity.app@gmail.com";
	
	public static void registerWithServer(Context ctx, String registrationId) {
		Log.d("onRegistered", "successfully registered with C2DM server; registrationId: " + registrationId);
		Intent notifySplash = new Intent(SplashActivity.REMOTE_REGISTRATION_ACTION);
        notifySplash.putExtra(Codes.KEY_C2DM_ID, registrationId);
        notifySplash.putExtra(Codes.KEY_C2DM_RESULT, Codes.SUCCESS);
        ctx.sendBroadcast(notifySplash);
	}
	
	public static void unregisterWithServer(Context context, String registrationId) {
		Log.d("c2dm", "unregistered");
		//TODO: unregister with remote server
	}
}