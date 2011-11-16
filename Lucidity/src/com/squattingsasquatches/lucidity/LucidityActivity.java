package com.squattingsasquatches.lucidity;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ViewFlipper;

import com.squattingsasquatches.R;

public class LucidityActivity extends Activity {
	
	private final String C2DM_EMAIL = "wbaaron@crimson.ua.edu"; // will eventually change to app specific email, i.e. lucidity-app@gmail.com
	private RemoteDBAdapter db;
	private String deviceID;
	private ViewFlipper layoutFlipper;
	private Button btnRegister;
	private View.OnClickListener handler;
	private Intent nextActivity;
	
	@Override
	public void onPause() {
		super.onPause();
		db.unregisterReceiver();
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        
        btnRegister = (Button) findViewById(R.id.btnRegister);       
        
        layoutFlipper = (ViewFlipper) findViewById(R.id.ViewFlipper);
        Animation animationFlipIn  = AnimationUtils.loadAnimation(this, R.anim.fadein);
        Animation animationFlipOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        layoutFlipper.setInAnimation(animationFlipIn);
        layoutFlipper.setOutAnimation(animationFlipOut);
        
        
        db = new RemoteDBAdapter(getApplicationContext(), this);
        
        // get device's unique ID
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.i("deviceID", deviceID);
        
        handler = new View.OnClickListener() {
            public void onClick(View v) {
            	switch (v.getId()) {
            		case R.id.btnRegister:
            			// C2DM registration
            			// Registration with our server is now handled by the receiver of registrationIntent
            			Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
            			registrationIntent.putExtra("app", PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(), 0));
            			registrationIntent.putExtra("sender", C2DM_EMAIL);
            			startService(registrationIntent);
            			break;
            		default:
            	}
            }
        };
        
        if (deviceID.equals("")) {
        	// deal with problem of device having no UUID, maybe generate our own 16-bit hexadecimal number and check it against the db
        	// but what if someone else happens to have that device id?
        } else {
        	db.setAction("user.login");
        	db.addParam("device_id", deviceID);
        	db.execute("loginCallback");
        }
    }
    
    public void loginCallback(JSONArray result) {
    	int resultCode = getResultCode(result);
		
    	switch (resultCode) {
			case CONSTANTS.NO_USER_ID_FOUND:
				// Device not registered
				btnRegister.setOnClickListener(handler);
				layoutFlipper.showNext();    			
				break;
			case CONSTANTS.SUCCESS:
				// change to main activity
				Log.i("Login", "SUCCESS");
				goToCourseList();
				break;
			default:
				Log.i("Register", "Error while logging in. Error code: " + resultCode);
		}
    }
    
    public void registerCallback(JSONArray result) {
    	int resultCode = getResultCode(result);
		
    	switch (resultCode) {
			case CONSTANTS.SUCCESS:
				// change to main activity
				Log.i("Register", "SUCCESS");
				goToCourseList();
				break;
			default:
				Log.i("Register", "Error while regsitering. Error code: " + resultCode);
		}
    }
    
    public void goToCourseList() {
    	nextActivity = new Intent(this, CourseMenuStudent.class);
		nextActivity.putExtra("com.squattingsasquatches.deviceID", deviceID);
		this.startActivity(nextActivity);
		finish(); // just this one time
    }
    
    public int getResultCode(JSONArray result) {
    	try {
			return result.getJSONObject(0).getInt("id");
		} catch (JSONException e) {
			Log.e("getResultCode", e.getMessage());
			return CONSTANTS.NOT_A_JSON_ARRAY;
		}
    }
}