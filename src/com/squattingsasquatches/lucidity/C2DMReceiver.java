package com.squattingsasquatches.lucidity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class C2DMReceiver extends BroadcastReceiver {

	public static final String SENDER_ID = "lucidity.app@gmail.com";

	private String c2dmRegistrationID;
	private Context ctx;

	public void handleMessage(Intent intent) {
		final Bundle extras = intent.getExtras();

		if (extras != null) {
			final String activity = extras.getString("activity"), action = extras
					.getString("action");

			Log.i("C2DM", "Message received");
			if (activity != null)
				try {
					final Intent newActivity = new Intent(this.ctx,
							Class.forName("com.squattingsasquatches.lucidity."
									+ activity));
					newActivity.putExtras(extras);
					newActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					this.ctx.startActivity(newActivity);
				} catch (final ClassNotFoundException e) {
					Log.e("C2DM Message Handler", "Invalid class supplied");
				}
			else if (action != null) {
				final Intent i = new Intent(
						"com.squattingsasquatches.lucidity." + action);
				i.putExtras(extras);
				this.ctx.sendBroadcast(i);
			}
		}
	}

	private void handleRegistration(Intent intent) {
		final Bundle extras = intent.getExtras();

		if (extras != null)
			if (extras.getString("error") != null) {
				// Registration failed, should try again later.
				final String error = extras.getString("error");
				Log.d("c2dm error", error);
			} else if (extras.getString("unregistered") != null) {
				final String storedRegistrationId = ""; // TODO: load
														// registrationId
				// from local DB
				DeviceRegistrar.unregisterWithServer(this.ctx,
						storedRegistrationId);
			} else if (this.c2dmRegistrationID != null)
				DeviceRegistrar.registerWithServer(this.ctx,
						this.c2dmRegistrationID);
	}

	@Override
	public void onReceive(Context ctx, Intent intent) {
		this.c2dmRegistrationID = intent.getStringExtra("registration_id");
		this.ctx = ctx;
		if (intent.getAction().equals(
				"com.google.android.c2dm.intent.REGISTRATION"))
			this.handleRegistration(intent);
		else if (intent.getAction().equals(
				"com.google.android.c2dm.intent.RECEIVE"))
			this.handleMessage(intent);
	}
}