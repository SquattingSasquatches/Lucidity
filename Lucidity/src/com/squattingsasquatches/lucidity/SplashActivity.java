package com.squattingsasquatches.lucidity;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ViewFlipper;

public class SplashActivity extends Activity implements RemoteResultReceiver.Receiver {
	
	public static final String REMOTE_REGISTRATION_ACTION = "com.squattingsasquatches.lucidity.REMOTE_REGISTRATION";
	
	private RemoteDBAdapter remoteDB;
	private String deviceID;
	private ViewFlipper layoutFlipper;
	private Button btnRegister;
	private Intent nextActivity;
	private Intent registrationIntent;
	private ProgressDialog loading;
	private String usersName;
	
	@Override
	public void onPause() {
		super.onPause();
		remoteDB.unregisterReceiver();
		unregisterReceiver(remoteRegistration);
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        
        btnRegister = (Button) findViewById(R.id.btnRegister);    
        layoutFlipper = (ViewFlipper) findViewById(R.id.ViewFlipper);
        remoteDB = new RemoteDBAdapter(this);
        loading = new ProgressDialog(this);
        registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
        
        Animation animationFlipIn  = AnimationUtils.loadAnimation(this, R.anim.fadein);
        Animation animationFlipOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        layoutFlipper.setInAnimation(animationFlipIn);
        layoutFlipper.setOutAnimation(animationFlipOut);
      
        remoteDB.setReceiver(this);
        
        loading.setTitle("Please wait");
        loading.setMessage("Registering with Lucidity server... ");
        
		registrationIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0));
		registrationIntent.putExtra("sender", C2DMReceiver.SENDER_ID);
        
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID); //get device's unique ID
        Log.i("deviceID", deviceID);
        
        if (deviceID.equals("")) {
        	/* 
        	 * deal with problem of device having no UID, maybe generate our own 16-bit hexadecimal number and check it against the remoteDB
        	 * but what if someone else happens to have that device id?
        	 */
        } else {
        	remoteDB.setAction("user.login");
        	remoteDB.addParam("device_id", deviceID);
        	remoteDB.execute(Codes.LOGIN);
        }
        
        btnRegister.setOnClickListener(registerBtnHandler);
        registerReceiver(remoteRegistration, new IntentFilter(REMOTE_REGISTRATION_ACTION));
    }
    
    public void goToCourseList() {
    	nextActivity = new Intent(this, CourseMenuStudent.class);
		nextActivity.putExtra("com.squattingsasquatches.deviceID", deviceID);
		this.startActivity(nextActivity);
		finish(); //just this one time
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
    	
    	switch (resultCode) {
			case Codes.SUCCESS:
				// TODO: edit user.register.php to return user_id, name, and c2dm_id; use those values for below
		    	// TODO: remember that all registration is done. (set bit in local DB) 
				String deviceRegistrationID = "";
	        	LocalDBAdapter localDB = new LocalDBAdapter(this);
	        	localDB.open();
	        	localDB.saveUserData(0, usersName, deviceRegistrationID); // Actually need to save newly generated user_id from remote database instead of 0.
	        	localDB.close();
				// start course menu activity
				Log.i("Register", "SUCCESS");
				goToCourseList();
				break;
			default:
				Log.i("Register", "Error while regsitering. Error code: " + resultCode);
		}
    	
    	loading.dismiss();
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
			if (result != null && !result.equals("")) {
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
	
	private final BroadcastReceiver remoteRegistration = new BroadcastReceiver() {
		@Override
        public void onReceive(Context context, Intent intent) {
			Log.i("remoteRegistration", "here");
			int result = intent.getIntExtra(Codes.KEY_C2DM_RESULT, Codes.ERROR);
			if (result == Codes.SUCCESS) {
				remoteDB.setAction("user.register");
		    	remoteDB.addParam("name", usersName);
		    	remoteDB.addParam("device_id", deviceID);
		    	remoteDB.addParam("c2dm_id", intent.getStringExtra(Codes.KEY_C2DM_ID));
		    	remoteDB.execute(Codes.REGISTER);
			} else {
				Log.i("C2DMResultHandler", "wtf");
			}
        }
	};
	
	private final View.OnClickListener registerBtnHandler = new View.OnClickListener() {
        public void onClick(View v) {
        	switch (v.getId()) {
        		case R.id.btnRegister:
        	        loading.show();
        			// C2DM registration
        			// Registration with our server is now handled by remoteRegistration BroadcastReceiver
        	        usersName = ((EditText) findViewById(R.id.txtName)).getText().toString();
        			startService(registrationIntent);
        			break;
        		default:
        	}
        }
    };
}