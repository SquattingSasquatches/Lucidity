package com.squattingsasquatches;

import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LucidityActivity extends Activity {
	
	DBAdapter db;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        db = new DBAdapter(LucidityActivity.this);
        
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        
        // get device's unique ID
        
        /*
        if (deviceUUID.equals("")) {
        	// deal with problem of device having no UUID
        } else {
        	
        }*/
        
        View.OnClickListener handler = new View.OnClickListener() {
            public void onClick(View v) {
            	switch (v.getId()) {
            		case R.id.btnLogin:
            			HashMap<String, String> loginParams = new HashMap<String, String>();
            			String deviceUUID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                    	loginParams.put("action", "user.register");
                    	loginParams.put("name", ((TextView) findViewById(R.id.txtName)).getText().toString());
                    	loginParams.put("device_id", deviceUUID);
                    	db.query(loginParams);
                    	JSONObject result = db.getResult();
            			break;
            	}
            }
        };
        
        btnLogin.setOnClickListener(handler);
    }
}