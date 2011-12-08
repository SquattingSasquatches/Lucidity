package com.squattingsasquatches.lucidity;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class SplashActivity extends Activity {
	
	private static final int MENU_ITEM = Menu.FIRST;
	private MenuItem menuRefresh, menuActivate, menuSet, menuClose, menuCourseList;
	private LocalDBAdapter localDB;
	private RemoteDBAdapter remoteDB;
	private ViewFlipper layoutFlipper;
	private Button btnRegister;
	private Intent nextActivity;
	private ProgressDialog loading;
	private TextView txtName;
	private AutoCompleteTextView txtUni;
	private TextView txtLoading;
	private User user;
	private University selectedUni;
	private ArrayList<University> unis;
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		remoteDB.unregisterAllReceivers();
		DeviceRegistrar.unregisterReceiver(this, remoteRegistration);
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        
        user = new User();
        selectedUni = new University();
        btnRegister = (Button) findViewById(R.id.btnRegister);    
        layoutFlipper = (ViewFlipper) findViewById(R.id.ViewFlipper);
        txtName = (EditText) findViewById(R.id.txtName);
        txtUni = (AutoCompleteTextView) findViewById(R.id.acUni);
        txtLoading = (TextView) findViewById(R.id.txtLoading);
        localDB = new LocalDBAdapter(this).open();
        remoteDB = new RemoteDBAdapter(this);
        loading = new ProgressDialog(this);
        user.setDeviceId(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)); //get device's unique ID
        Log.i("deviceID", user.getDeviceId());
        
        userLogin.addParam("device_id", user.getDeviceId());
		universitiesView.addParam("device_id", user.getDeviceId());
		
        btnRegister.setOnClickListener(registerBtnHandler);
        
        remoteDB.addReceiver("user.login", userLogin);
        remoteDB.addReceiver("unis.view", universitiesView);
        
        loading.setTitle("Please wait");
        loading.setMessage("Loading your saved courses... ");
        loading.setCancelable(false);
        
        remoteDB.execute("user.login");
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		
		int groupId = 0;
		int menuItemId = MENU_ITEM;
		int menuItemOrder = Menu.NONE;
		
		menuRefresh = menu.add(groupId, menuItemId, menuItemOrder, "Refresh");
		menuActivate = menu.add(groupId, menuItemId, menuItemOrder, "Activate New");
		menuSet = menu.add(groupId, menuItemId, menuItemOrder, "Set Server IP");
		menuCourseList = menu.add(groupId, menuItemId, menuItemOrder, "Courses");
		menuClose = menu.add(groupId, menuItemId, menuItemOrder, "Close App");
		
		final SplashActivity thisone = this;
		
		menuCourseList.setOnMenuItemClickListener(new OnMenuItemClickListener(){
			public boolean onMenuItemClick(MenuItem _menuItem){
				goToCourseList();
				return true;
			}
		});
		
		menuRefresh.setOnMenuItemClickListener(new OnMenuItemClickListener(){
			public boolean onMenuItemClick(MenuItem _menuItem){
				Intent intent = getIntent();
				finish();
				startActivity(intent);
				return true;
			}
		});
		
		menuActivate.setOnMenuItemClickListener(new OnMenuItemClickListener(){
			public boolean onMenuItemClick(MenuItem _menuItem){
				Context context = getApplicationContext();
				CharSequence text = "Not Yet Implemented!";
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
				return true;
			}
		});
		
		final AlertDialog.Builder alert = new AlertDialog.Builder(this);
		final EditText input = new EditText(this);
		input.setText(Config.SERVER_ADDRESS);
		
		alert.setView(input);
		alert.setPositiveButton("Set", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString().trim();
				Config.setServerAddress(value);
				Intent intent = getIntent();
				finish();
				startActivity(intent);
			}
		});

		alert.setNegativeButton("Cancel",
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.cancel();
				}
		});
		
		menuSet.setOnMenuItemClickListener(new OnMenuItemClickListener(){
			public boolean onMenuItemClick(MenuItem _menuItem){
				alert.show();
				return true;
			}
		});
		
		menuClose.setOnMenuItemClickListener(new OnMenuItemClickListener(){
			public boolean onMenuItemClick(MenuItem _menuItem){
				thisone.finish();
				return true;
			}
		});
		
		return true;
	}
    
    public void onConnectionError( int statusCode )
    {
    	txtLoading.setText(R.string.connection_error);
    }
    public void onHttpError( String errorMessage )
    {
    	txtLoading.setText(R.string.connection_error);
    }
    
    /* switch to CourseMenu Activity */
    public void goToCourseList() {
    	goToCourseList(false);
    }
    
    /* switch to CourseMenu Activity and grab courses from remote DB */
    public void goToCourseList(boolean updateCourses) {
    	nextActivity = new Intent(this, CourseMenuActivity.class);
		nextActivity.putExtra("com.squattingsasquatches.userId", localDB.getUserId());
		nextActivity.putExtra("com.squattingsasquatches.updateCourses", updateCourses);
		localDB.close();
		startActivity(nextActivity);
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
    	remoteDB.execute("unis.view");
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
				try {
					user.setId(result.getJSONObject(0).getInt("user_id"));
					Log.e("userId", result.getJSONObject(0).getInt("user_id")+"");
					if (user.getDeviceId().equals(""))
						user.setDeviceId(result.getJSONObject(0).getString("device_id"));
				} catch (JSONException e) {
					Log.e("Register", "device_id not returned by remote server");
				}
				
	        	Log.e("saveUserData", ""+localDB.saveUserData(user));
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
    	AutoCompleteArrayAdapter<University> adapter;
    	unis = new ArrayList<University>();
    	int resultLength = result.length();
    	
    	for (int i = 0; i < resultLength; ++i) {
    		try {
				unis.add(new University(result.getJSONObject(i).getInt("id"), result.getJSONObject(i).getString("name")));
			} catch (JSONException e) {
				Log.d("loadUniversities", "error loading univerisites");
			}
    	}
    	
    	adapter = new AutoCompleteArrayAdapter<University>(this, R.layout.ac_list_item, unis);
    	txtUni.setAdapter(adapter);
    	txtUni.setOnItemClickListener(uniClickListener);
    	txtUni.setValidator(new ACValidator());
    	txtUni.setOnFocusChangeListener(new ValidateStarter());
    	loading.dismiss();
    	layoutFlipper.showNext();
    }
	
	/* sends user and c2dm data to remote server */
	private final BroadcastReceiver remoteRegistration = new BroadcastReceiver() {
		@Override
        public void onReceive(Context context, Intent intent) {
			int result = intent.getIntExtra(Codes.KEY_C2DM_RESULT, Codes.ERROR);
			user.setC2dmRegistrationId(intent.getStringExtra(Codes.KEY_C2DM_ID));
			if (result == Codes.SUCCESS) {
				userRegister.addParam("name", user.getName());
				userRegister.addParam("device_id", user.getDeviceId());
				userRegister.addParam("c2dm_id", user.getC2dmRegistrationId());
				userRegister.addParam("uni_id", Integer.toString(user.getUniversity().getId()));
				
				remoteDB.addReceiver("user.register", userRegister);
		    	remoteDB.execute("user.register");
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
        			txtUni.performValidation();
        			
        			if (txtUni.getText().toString().equals("") || txtName.getText().toString().equals("")) {
        				
        				if (txtName.getText().toString().equals(""))
        					Toast.makeText(SplashActivity.this, "Please enter your name.", Toast.LENGTH_LONG).show();
        				
        				break;
        			}
        			
        	        loading.show();
        			// C2DM registration
        			// Registration with our server is now handled by remoteRegistration BroadcastReceiver
        	        user.setName(txtName.getText().toString());
        	        user.getUniversity().setId(selectedUni.getId());
        			DeviceRegistrar.startRegistration(getApplicationContext(), remoteRegistration);
        			break;
        		default:
        	}
        }
    };
    
    private OnItemClickListener uniClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parentView, View selectedView, int position, long id) {
			selectedUni = (University) parentView.getAdapter().getItem(position);
		}
    	
    };
    
	// Receivers
    private InternalReceiver userRegister = new InternalReceiver(){
		public void update( JSONArray data ){
			SplashActivity.this.registerCallback( data );
		}
		public void onHttpError( int statusCode ){
			SplashActivity.this.onConnectionError( statusCode );
		}
		public void onConnectionError( String errorMessage ){
			SplashActivity.this.onHttpError( errorMessage );
		}
	};	
	
	private InternalReceiver userLogin = new InternalReceiver(){
		public void update( JSONArray data ){
			SplashActivity.this.loginCallback( data );
		}
		public void onHttpError( int statusCode ){
			SplashActivity.this.onConnectionError( statusCode );
		}
		public void onConnectionError( String errorMessage ){
			SplashActivity.this.onHttpError( errorMessage );
		}
	};
	
	private InternalReceiver universitiesView = new InternalReceiver(){
		public void update( JSONArray data ){
			SplashActivity.this.loadUniversitiesCallback( data );
		}
		public void onHttpError( int statusCode ){
			SplashActivity.this.onConnectionError( statusCode );
		}
		public void onConnectionError( String errorMessage ){
			SplashActivity.this.onHttpError( errorMessage );
		}
	};
	
	class ValidateStarter implements OnFocusChangeListener {
		
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                ((AutoCompleteTextView) v).performValidation();
            }
        }
        
    };
	
	class ACValidator implements AutoCompleteTextView.Validator {

		public CharSequence fixText(CharSequence invalidText) {
			Toast.makeText(SplashActivity.this, "Invalid University entered. Please try again.", Toast.LENGTH_LONG).show();
			return "";
		}

		public boolean isValid(CharSequence text) {
			String universityName = text.toString();
			Iterator<University> it = unis.iterator();
			
			// Check entered text against valid universities
			while (it.hasNext()) {
				if ((((University) it.next()).getName()).equals(universityName))
					return true;
			}
			
			return false; // University isn't valid
		}
		
	};
}