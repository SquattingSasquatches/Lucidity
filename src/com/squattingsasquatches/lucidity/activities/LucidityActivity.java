/**
 * 
 */
package com.squattingsasquatches.lucidity.activities;

/**
 * @author Asa
 *
 */



import com.squattingsasquatches.lucidity.LocalDBAdapter;
import com.squattingsasquatches.lucidity.RemoteDBAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public abstract class LucidityActivity extends Activity {
	
	// Local SQLite storage.
	protected LocalDBAdapter localDB;
	
	// Remote data connection to university server.
	protected RemoteDBAdapter remoteDB;
	
	// Intent used to start the next Activity.
	protected Intent nextActivity;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		remoteDB = new RemoteDBAdapter(this);
        localDB = new LocalDBAdapter(this).open();
	}
//	protected void onStart() {
//		
//	}
//	 
//	protected void onRestart() {
//		 
//	}
//	
//	protected void onResume() {
//		 
//	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (remoteDB != null)
			remoteDB.unregisterAllReceivers();
		if (localDB != null)
			localDB.close();
	}
//	
//	protected void onStop() {
//	 
//	}
//	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		remoteDB.unregisterAllReceivers();
	}
	
}
