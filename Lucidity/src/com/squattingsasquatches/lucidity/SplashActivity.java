package com.squattingsasquatches.lucidity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class SplashActivity extends Activity implements RemoteResultReceiver.Receiver {
	
	private RemoteDBAdapter remoteDB;
	private ViewFlipper layoutFlipper;
	private Button btnRegister;
	private Intent nextActivity;
	private ProgressDialog loading;
	private AutoCompleteTextView txtUni;
	private TextView txtLoading;
	private User user;
	
	@Override
	public void onPause() {
		super.onPause();
		remoteDB.unregisterReceiver();
		DeviceRegistrar.unregisterReceiver(this, remoteRegistration);
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        
        user = new User();
        btnRegister = (Button) findViewById(R.id.btnRegister);    
        layoutFlipper = (ViewFlipper) findViewById(R.id.ViewFlipper);
        txtUni = (AutoCompleteTextView) findViewById(R.id.acUni);
        txtLoading = (TextView) findViewById(R.id.txtLoading);
        remoteDB = new RemoteDBAdapter(this);
        loading = new ProgressDialog(this);
        
        Animation animationFadeIn  = AnimationUtils.loadAnimation(this, R.anim.fadein);
        Animation animationFadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        layoutFlipper.setInAnimation(animationFadeIn);
        layoutFlipper.setOutAnimation(animationFadeOut);
      
        remoteDB.setReceiver(this);
        
        loading.setTitle("Please wait");
        loading.setMessage("Registering with Lucidity server... ");
        
        user.setDeviceId(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)); //get device's unique ID
        Log.i("deviceID", user.getDeviceId());
        
        if (user.getDeviceId().equals("")) {
        	/* 
        	 * deal with problem of device having no deviceID, maybe generate our own 64-bit number and check it against the remoteDB
        	 * but what if someone else happens to have that device id?
        	 */
        } else {
        	remoteDB.setAction("user.login");
        	remoteDB.addParam("device_id", user.getDeviceId());
        	remoteDB.execute(Codes.LOGIN);
        }
        
        btnRegister.setOnClickListener(registerBtnHandler);
    }
    
    /* switch to CourseMenu Activity */
    public void goToCourseList() {
    	goToCourseList(false);
    }
    
    /* switch to CourseMenu Activity and grab courses from remote DB */
    public void goToCourseList(boolean updateCourses) {
    	nextActivity = new Intent(this, CourseMenuStudent.class);
		nextActivity.putExtra("com.squattingsasquatches.userId", user.getId());
		nextActivity.putExtra("updateCourses", updateCourses);
		this.startActivity(nextActivity);
		finish(); //just this one time
    }
    
    /* get result code from a call to PHPService */
    public int getResultCode(JSONArray result) {
    	try {
			return result.getJSONObject(0).getInt("id");
		} catch (JSONException e) {
			Log.e("getResultCode", e.getMessage());
			return Codes.NOT_A_JSON_ARRAY;
		}
    }
    
    public void loadUniversities() {
    	txtLoading.setText(R.string.loading_unis);
    	remoteDB.setAction("unis.view");
    	remoteDB.execute(Codes.LOAD_UNIVERSITIES);
    }
    
    /* shows registration fields or switches to CourseMenu Activity */
    public void loginCallback(JSONArray result) {
    	int resultCode = getResultCode(result);
		
    	switch (resultCode) {
			case Codes.NO_USER_ID_FOUND:
				// Device not registered
				// load universities for registration
				loadUniversities();
				break;
			case Codes.SUCCESS:
				// change to main activity
				Log.d("Login", "SUCCESS");
				goToCourseList();
				break;
			default:
				Log.d("Register", "Error while logging in. Error code: " + resultCode);
		}
    }
    
    /* saves users data to localDB and moves to CourseMenu Activity */
    public void registerCallback(JSONArray result) {
    	int resultCode = getResultCode(result);
    	
    	switch (resultCode) {
			case Codes.SUCCESS:
	        	LocalDBAdapter localDB = new LocalDBAdapter(this);
	        	localDB.open().saveUserData(user); // Actually need to save newly generated user_id from remote database instead of 0.
	        	localDB.close();
				// start course menu activity
				Log.d("Register", "SUCCESS");
				goToCourseList(true);
				break;
			default:
				Log.d("Register", "Error while registering. Error code: " + resultCode);
		}
    	
    	loading.dismiss();
    }
    
    public void loadUniversitiesCallback(JSONArray result) {
    	ArrayAdapter<String> adapter;
    	ArrayList<String> unis = new ArrayList<String>();
    	int resultLength = result.length();
    	
    	for (int i = 0; i < resultLength; ++i) {
    		try {
				unis.add(result.getJSONObject(i).getString("name"));
			} catch (JSONException e) {
				Log.d("loadUniversities", "error loading univerisites");
			}
    	}
    	
    	adapter = new ArrayAdapter<String>(this, R.layout.ac_list_item, unis.toArray(new String[unis.size()]));
    	txtUni.setAdapter(adapter);
    	loading.dismiss();
    	layoutFlipper.showNext();
    }
    
    /* calls the designated callback */
    public void doCallback(int callbackCode, JSONArray result) {
    	if (callbackCode == Codes.LOGIN)
    		loginCallback(result);
    	else if (callbackCode == Codes.REGISTER)
    		registerCallback(result);
    	else if (callbackCode == Codes.LOAD_UNIVERSITIES)
    		loadUniversitiesCallback(result);
    	else
    		Log.d("WTF", "How'd you get here?");
    }

    /* receives result from PHPService */
	public void onReceiveResult(int resultCode, Bundle resultData) {
		Log.d("onReceiveResult", String.valueOf(resultCode));
		
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
		} else if (resultCode == Codes.REMOTE_QUERY_ERROR) {
			//TODO: actually handle this situation instead of just throwing up a toast
			Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT);
		}
	}
	
	/* sends user and c2dm data to remote server */
	private final BroadcastReceiver remoteRegistration = new BroadcastReceiver() {
		@Override
        public void onReceive(Context context, Intent intent) {
			Log.i("remoteRegistration", "here");
			int result = intent.getIntExtra(Codes.KEY_C2DM_RESULT, Codes.ERROR);
			user.setC2dmRegistrationId(intent.getStringExtra(Codes.KEY_C2DM_ID));
			if (result == Codes.SUCCESS) {
				remoteDB.setAction("user.register");
		    	remoteDB.addParam("name", user.getName());
		    	remoteDB.addParam("device_id", user.getDeviceId());
		    	remoteDB.addParam("c2dm_id", user.getC2dmRegistrationId());
		    	remoteDB.addParam("uni_id", user.getUniId());
		    	remoteDB.execute(Codes.REGISTER);
			} else {
				Log.i("C2DMResultHandler", "wtf");
			}
        }
	};
	
	
	/* starts registration */
	private final View.OnClickListener registerBtnHandler = new View.OnClickListener() {
        public void onClick(View v) {
        	switch (v.getId()) {
        		case R.id.btnRegister:
        	        loading.show();
        			// C2DM registration
        			// Registration with our server is now handled by remoteRegistration BroadcastReceiver
        	        user.setName(((EditText) findViewById(R.id.txtName)).getText().toString());
        	        user.setUniId(1870); //TODO: Fix this. In adapter, use actual University objects as opposed to just Strings. Need to write custom AutoCompleteTextView
        	        Log.d("sel uni", String.valueOf(user.getUniId()));
        			DeviceRegistrar.startRegistration(getApplicationContext(), remoteRegistration);
        			break;
        		default:
        	}
        }
    };
}