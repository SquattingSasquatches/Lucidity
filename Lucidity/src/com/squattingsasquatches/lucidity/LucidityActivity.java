package com.squattingsasquatches.lucidity;

import org.json.JSONArray;
import org.json.JSONException;

import com.squattingsasquatches.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class LucidityActivity extends Activity {
	
	private PHPConnection db;
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
        
        
        db = new PHPConnection(getApplicationContext(), this);
        
        // get device's unique ID
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.i("deviceID", deviceID);
        
        handler = new View.OnClickListener() {
            public void onClick(View v) {
            	switch (v.getId()) {
            		case R.id.btnRegister:
            			db.setAction("user.register");
            			db.addParam("name", ((TextView) findViewById(R.id.txtName)).getText().toString());
            			db.addParam("device_id", deviceID);
            			db.execute("registerCallback");
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
    	int resultCode;
    	
		try {
			resultCode = result.getJSONObject(0).getInt("id");
		} catch (JSONException e) {
			Log.e("loginCallback", e.getMessage());
			resultCode = RETURN_CODE.NOT_A_JSON_ARRAY;
		}
		
    	switch (resultCode) {
	    	case RETURN_CODE.NO_DEVICE_ID_SUPPLIED:
				Log.i("Login", "NO_DEVICE_ID_SUPPLIED");
				break;
			case RETURN_CODE.DATABASE_ERROR:
				Log.i("Login", "DATABASE_ERROR");
				break;
			case RETURN_CODE.NO_USER_ID_FOUND:
				// Device not registered
				btnRegister.setOnClickListener(handler);
				layoutFlipper.showNext();    			
				break;
			case RETURN_CODE.SUCCESS:
				// change to main activity
				Log.i("Login", "SUCCESS");
				goToCourseList();
				break;
			case RETURN_CODE.NOT_A_JSON_ARRAY:
				Log.i("Login", "something bad happened.");
				break;
			default:
				Log.i("Login", "something impossible happened.");
		}
    }
    
    public void registerCallback(JSONArray result) {
    	int resultCode;
    	
		try {
			resultCode = result.getJSONObject(0).getInt("id");
		} catch (JSONException e) {
			Log.e("registerCallback", e.getMessage());
			resultCode = RETURN_CODE.NOT_A_JSON_ARRAY;
		}
		
    	switch (resultCode) {
			case RETURN_CODE.DATABASE_ERROR:
				Log.i("Register", "DATABASE_ERROR");
				break;
			case RETURN_CODE.NO_STUDENT_ID_SUPPLIED:
				Log.i("Register", "NO_STUDENT_ID_SUPPLIED");
				break;
			case RETURN_CODE.NO_DEVICE_ID_SUPPLIED:
				Log.i("Register", "NO_DEVICE_ID_SUPPLIED");
				break;
			case RETURN_CODE.STUDENT_ALREADY_EXISTS:
				// not sure how this happened
				Log.i("Register", "STUDENT_ALREADY_EXISTS");
				break;
			case RETURN_CODE.DEVICE_ID_ALREADY_EXISTS:
				Log.i("Register", "DEVICE_ID_ALREADY_EXISTS");
				break;
			case RETURN_CODE.SUCCESS:
				// change to main activity
				Log.i("Register", "SUCCESS");
				goToCourseList();
				break;
			case RETURN_CODE.NOT_A_JSON_ARRAY:
				Log.i("Login", "something bad happened.");
				break;
			default:
				Log.i("Register", "something impossible happened.");
		}
    }
    
    public void goToCourseList() {
    	nextActivity = new Intent(this, CourseMenuStudent.class);
		nextActivity.putExtra("com.squattingsasquatches.deviceID", deviceID);
		this.startActivity(nextActivity);
		finish(); // just this one time
    }
}