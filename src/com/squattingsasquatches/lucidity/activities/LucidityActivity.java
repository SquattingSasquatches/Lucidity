/**
 * 
 */
package com.squattingsasquatches.lucidity.activities;

/**
 * @author Asa
 *
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.squattingsasquatches.lucidity.LucidityDatabase;
import com.squattingsasquatches.lucidity.RemoteDBAdapter;
import com.squattingsasquatches.lucidity.objects.User;

public abstract class LucidityActivity extends Activity {

	public static LucidityDatabase db;

	// Remote data connection to university server.
	protected RemoteDBAdapter remoteDB;

	// Intent used to start the next Activity.
	protected Intent nextActivity;

	protected User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);

			db = new LucidityDatabase(this);
			remoteDB = new RemoteDBAdapter(this);

			user = new User();

			if (User.exists()) {
				user.load();
				remoteDB.setServerAddress(user.getUniversity()
						.getServerAddress());
				remoteDB.setServerPort(user.getUniversity().getServerPort());
			} else {
				Log.i("OnCreate()", "User does not exist.");
			}
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			Log.i("SplashActivity.onCreate()", "Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	// protected void onStart() {
	//
	// }
	//
	// protected void onRestart() {
	//
	// }
	//
	// protected void onResume() {
	//
	// }

	@Override
	protected void onPause() {
		super.onPause();
		if (remoteDB != null)
			remoteDB.unregisterAllReceivers();
		// if (db != null)
		// db.close();
	}

	//
	// protected void onStop() {
	//
	// }
	//
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// db.close();
		remoteDB.unregisterAllReceivers();
	}

}
