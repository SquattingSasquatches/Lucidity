package com.squattingsasquatches;

import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LucidityActivity extends Activity {
	
	private DBAdapter db;
	private HashMap<String, String> params;
	private String deviceID;
	private int result;
	private Button btnRegister;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        
        db = new DBAdapter();
        
        // get device's unique ID
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        
        View.OnClickListener handler = new View.OnClickListener() {
            public void onClick(View v) {
            	switch (v.getId()) {
            		case R.id.btnRegister:
            			params = new HashMap<String, String>();
            			params.put("action", "user.register");
            			params.put("name", ((TextView) findViewById(R.id.txtName)).getText().toString());
            			params.put("device_id", deviceID);
                    	db.query(params);
                    	result = db.getResult();
                    	
                    	switch (result) {
                    		case RETURN_CODE.DATABASE_ERROR:
                    			Log.i("Register", "DATABASE_ERROR");
                    			break;
                    		case RETURN_CODE.USER_ALREADY_REGISTERED:
                    			// not sure how this happened
                    			break;
                    		case RETURN_CODE.SUCCESS:
                    			// change to main activity
                    			Log.i("Register", "SUCCESS");
                    			break;
                    		default:
                    			Log.i("Register", "something impossible happened.");
                    	}
            			break;
            	}
            }
        };
        
        if (deviceID.equals("")) {
        	// deal with problem of device having no UUID, maybe generate our own 16-bit hexadecimal number and check it against the db
        	// but what if someone else happens to have that device id?
        } else {
        	params = new HashMap<String, String>();
        	params.put("action", "user.login");
        	params.put("device_id", deviceID);
        	db.query(params);
        	result = db.getResult();
        	
        	switch (result) {
        		case RETURN_CODE.DATABASE_ERROR:
        			// problem communicating with database
        			Log.i("Login", "DATABASE_ERROR");
        			break;
        		case RETURN_CODE.USER_NOT_REGISTERED:
        			// Device not registered
        			setContentView(R.layout.register);
        			btnRegister = (Button) findViewById(R.id.btnRegister);
        			btnRegister.setOnClickListener(handler);
        			break;
        		case RETURN_CODE.SUCCESS:
        			// user logged in. possible idea: save user_id so we don't have to always pass device_id to our php calls. could pass it between bundles.
        			// change to main activity
        			Log.i("Login", "SUCCESS");
        			break;
        		default:
        			Log.i("Login", "something impossible happened.");
        	}
        }
    }
}