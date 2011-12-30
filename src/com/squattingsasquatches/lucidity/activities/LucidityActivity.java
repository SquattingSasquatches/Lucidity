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

import com.squattingsasquatches.lucidity.LucidityDatabase;
import com.squattingsasquatches.lucidity.RemoteDBAdapter;

public abstract class LucidityActivity extends Activity {

	public static LucidityDatabase db;

	// Remote data connection to university server.
	protected RemoteDBAdapter remoteDB;

	// Intent used to start the next Activity.
	protected Intent nextActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		db = new LucidityDatabase(this);
		remoteDB = new RemoteDBAdapter(this);
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
		if (db != null)
			db.close();
	}

	//
	// protected void onStop() {
	//
	// }
	//
	@Override
	protected void onDestroy() {
		super.onDestroy();
		db.close();
		remoteDB.unregisterAllReceivers();
	}

}
