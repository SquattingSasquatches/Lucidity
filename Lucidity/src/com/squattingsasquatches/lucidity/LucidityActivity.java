package com.squattingsasquatches.lucidity;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ViewFlipper;

import com.squattingsasquatches.R;

public class LucidityActivity extends Activity implements RemoteResultReceiver.Receiver {
	
	private final String C2DM_EMAIL = "wbaaron@crimson.ua.edu"; // will eventually change to app specific email, i.e. lucidity-app@gmail.com
	private RemoteDBAdapter db;
	private String deviceID;
	private int c2dm_id;
	private ViewFlipper layoutFlipper;
	private Button btnRegister;
	private View.OnClickListener handler;
	private Intent nextActivity;
	private Intent registrationIntent;
	private ProgressDialog loading;
	
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
        
        
        db = new RemoteDBAdapter(this);
        db.setReceiver(this);
        
        loading = new ProgressDialog(this);
        loading.setTitle("Please wait");
        loading.setMessage("Registering with Lucidity server... ");
        
        registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
		registrationIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0));
		registrationIntent.putExtra("sender", C2DM_EMAIL);
        
        // get device's unique ID
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.i("deviceID", deviceID);
        
        handler = new View.OnClickListener() {
            public void onClick(View v) {
            	switch (v.getId()) {
            		case R.id.btnRegister:
            	        loading.show();
            			// C2DM registration
            			// Registration with our server is now handled by the receiver of registrationIntent
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
        	db.execute(Codes.LOGIN);
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
			return Codes.NOT_A_JSON_ARRAY;
		}
    }
    
    public void loginCallback(JSONArray result) {
    	int resultCode = getResultCode(result);
		
    	switch (resultCode) {
			case Codes.NO_USER_ID_FOUND:
				// Device not registered
				btnRegister.setOnClickListener(handler);
				layoutFlipper.showNext();    			
				break;
			case Codes.SUCCESS:
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
		
    	loading.dismiss();
    	
    	switch (resultCode) {
			case Codes.SUCCESS:
				// change to main activity
				Log.i("Register", "SUCCESS");
				goToCourseList();
				break;
			default:
				Log.i("Register", "Error while regsitering. Error code: " + resultCode);
		}
    }
    
    public void doCallback(int callbackCode, JSONArray result) {
    	if (callbackCode == Codes.LOGIN)
    		loginCallback(result);
    	else if (callbackCode == Codes.REGISTER)
    		registerCallback(result);
    	else
    		Log.i("WTF", "How'd you get here?");
    }

	public void onReceiveResult(int resultCode, Bundle resultData) {
		// result from remote PHP query
		Log.i("onReceiveResult", String.valueOf(resultCode));
		
		if (resultCode == Codes.REMOTE_QUERY_COMPLETE) {
			String result = resultData.getString("result");
			int callbackCode = resultData.getInt("callback");
			if (result != null) {
				try {
					doCallback(callbackCode, new JSONArray(result));
				} catch (JSONException e) {
					Log.e("onReceiveResult", "error with JSONArray");
				}
			}
		} else {
			// bads!
		}
	}
	
	public void onReceive(Context context, Intent intent) {
		// result from C2DM Intent
		if (intent.getAction().equals("com.google.android.c2dm.intent.REGISTRATION")) {
			handleRegistration(context, intent);
	    } else if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
	    	//handleMessage(context, intent);
	    }
	}
	
	private void handleRegistration(Context context, Intent intent) {
	    String registration = intent.getStringExtra("registration_id");
	    Log.i("C2DM Registration", "Here.");
	    if (intent.getStringExtra("error") != null) {
	        // Registration failed, should try again later.
	    	Log.i("C2DM Registration", "C2DM Error");
	    } else if (intent.getStringExtra("unregistered") != null) {
	        // unregistration done, new messages from the authorized sender will be rejected
	    } else if (registration != null) {
	    	Log.i("C2DM Registration ID", registration);
	    	c2dm_id = Integer.valueOf(registration);
	    	// Send the registration ID to the 3rd party site that is sending the messages.
	    	// This should be done in a separate thread.
	    	// When done, remember that all registration is done. 
	    	String usersName = ((EditText) findViewById(R.id.txtName)).getText().toString();
	    	db.setAction("user.register");
	    	db.addParam("name", usersName);
        	db.addParam("device_id", deviceID);
        	db.addParam("c2dm_id", registration);
        	db.execute(Codes.REGISTER);
        	
        	/*LocalDBAdapter localDB = new LocalDBAdapter(this);
        	localDB.open();
        	localDB.saveUserData(Integer.valueOf(deviceID), usersName, c2dm_id);
        	/*
        	 * Actually need to save newly generated user_id from remote database instead of device_id.
        	 * May need to move this funcationality to registerCallback. Would also need to edit what user.register.php return to include user_id and name;
        	 */
	    }
	}
}