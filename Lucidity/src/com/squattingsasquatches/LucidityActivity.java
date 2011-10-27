package com.squattingsasquatches;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;

public class LucidityActivity extends Activity {
	
	DBAdapter db;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        db = new DBAdapter(LucidityActivity.this);
        
        // get device's unique ID
        String deviceUUID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        
        if (deviceUUID.equals("")) {
        	// deal with problem of device having no UUID
        } else {
        	// check database for UUID, create new user if not found
        }
    }
}