package com.squattingsasquatches.lucidity.activities;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;

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

import com.squattingsasquatches.lucidity.AutoCompleteArrayAdapter;
import com.squattingsasquatches.lucidity.Codes;
import com.squattingsasquatches.lucidity.Config;
import com.squattingsasquatches.lucidity.DeviceRegistrar;
import com.squattingsasquatches.lucidity.InternalReceiver;
import com.squattingsasquatches.lucidity.R;
import com.squattingsasquatches.lucidity.objects.University;
import com.squattingsasquatches.lucidity.objects.User;

public class SplashActivity extends LucidityActivity {

	private static final int MENU_ITEM = Menu.FIRST;
	private MenuItem menuActivate, menuSet, menuSettings;
	private ViewFlipper layoutFlipper;
	private Button btnRegister, btnSelectRegister, btnSelectLogin, btnLogin;
	private ProgressDialog loading;
	private TextView txtName;
	private AutoCompleteTextView txtUni;
	private TextView txtLoading;
	private University selectedUni;
	private ArrayList<University> unis;
	private boolean firstRun = false;

	// private ArrayList<University> unisToSave;

	@Override
	public void onDestroy() {
		super.onDestroy();
		// LucidityDatabase.close();
		DeviceRegistrar.unregisterReceiver(this, remoteRegistration);
	}

	/** Called when the activity is first created. */

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.splash);

		// Load default universities if the University table is empty.

		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnSelectRegister = (Button) findViewById(R.id.btnSelectRegister);
		btnSelectLogin = (Button) findViewById(R.id.btnSelectLogin);
		btnLogin = (Button) findViewById(R.id.btnLogin);

		layoutFlipper = (ViewFlipper) findViewById(R.id.ViewFlipper);

		txtName = (EditText) findViewById(R.id.txtName);
		txtLoading = (TextView) findViewById(R.id.txtLoading);

		loading = new ProgressDialog(this);

		selectedUni = new University();

		// If the list of universities is empty, or if the user killed the
		// app while inserting universities into the database, load default.
		if (University.isEmpty()) {

			loading.setTitle("Please wait");
			loading.setMessage("Loading default universities... ");
			loading.setCancelable(false);
			loading.show();

			try {
				unis = University.loadDefault(this);
			} catch (Exception e) {
				e.printStackTrace();
			}

			loading.dismiss();
		} else {
			unis = University.getAll();

		}

		if (user.getDeviceId().equals("")) {
			firstRun = true;

		}

		// get device's unique ID
		user.setDeviceId(Settings.Secure.getString(getContentResolver(),
				Settings.Secure.ANDROID_ID));
		if (firstRun || user.getUniversity().getServerAddress().equals("")) {
			// Show Login/Register option.
			showSelectionScreen();
		} else {
			login();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		int groupId = 0;
		int menuItemId = MENU_ITEM;
		int menuItemOrder = Menu.NONE;

		menuActivate = menu.add(groupId, menuItemId, menuItemOrder,
				"Activate New Device");
		menuSet = menu.add(groupId, menuItemId, menuItemOrder, "Set Server IP");
		menuSettings = menu.add(groupId, menuItemId, menuItemOrder, "Settings");

		menuSettings.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem _menuItem) {
				Intent settingsActivity = new Intent(getBaseContext(),
						SettingsScreen.class);
				startActivity(settingsActivity);
				return false;
			}
		});

		menuActivate.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem _menuItem) {
				Toast.makeText(SplashActivity.this, "Not yet implemented...",
						Toast.LENGTH_LONG).show();
				return true;
			}
		});

		final AlertDialog.Builder alert = new AlertDialog.Builder(this);
		final EditText input = new EditText(this);
		input.setText(Config.SERVER_ADDRESS);

		alert.setView(input)
				.setPositiveButton("Set",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								String value = input.getText().toString()
										.trim();
								Config.setServerAddress(value);
								Intent intent = getIntent();
								startActivity(intent);
								finish();
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.cancel();
							}
						});

		menuSet.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem _menuItem) {
				alert.show();
				return true;
			}
		});

		return true;
	}

	public void onConnectionError(int statusCode) {
		txtLoading.setText(R.string.connection_error);

	}

	public void onHttpError(String errorMessage) {
	}

	/* switch to CourseMenu Activity */
	public void goToCourseList() {
		goToCourseList(false);
	}

	/* switch to CourseMenu Activity and grab courses from remote DB */
	public void goToCourseList(boolean updateCourses) {
		nextActivity = new Intent(this, CourseMenuActivity.class);
		nextActivity.putExtra("userId", User.getStoredId());
		nextActivity.putExtra("updateCourses", updateCourses);
		startActivity(nextActivity);
		finish(); // just this one time
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

	public void showRegistration() {

		layoutFlipper.showNext();

	}

	public void showSelectionScreen() {
		btnSelectRegister.setOnClickListener(selectRegisterBtnHandler);
		btnSelectLogin.setOnClickListener(selectLoginBtnHandler);
		layoutFlipper.showNext();
	}

	public void login() {

		loading.setTitle("Please wait");
		loading.setMessage("Logging in... ");
		loading.setCancelable(false);
		loading.show();

		userLogin.addParam("device_id", user.getDeviceId());

		remoteDB.addReceiver("user.login", userLogin);
		remoteDB.execute("user.login");
	}

	/* shows registration fields or switches to CourseMenu Activity */
	public void loginCallback(JSONArray result) {
		int resultCode = getResultCode(result);

		loading.dismiss();

		switch (resultCode) {
		case Codes.NO_USER_ID_FOUND:
			// Device not registered
			// Show registration form.

			showRegistration();
			break;
		case Codes.SUCCESS:
			// change to main activity
			Log.d("Login", "SUCCESS");
			User.save(user);
			goToCourseList();
			break;
		default:
			Log.d("Register", "Error while logging in. Error code: "
					+ resultCode);
		}
	}

	/* saves users data to localDB and moves to CourseMenu Activity */
	public void registerCallback(JSONArray result) {

		loading.dismiss();

		int resultCode = getResultCode(result);

		switch (resultCode) {
		case Codes.SUCCESS:
			try {
				user.setId(result.getJSONObject(0).getInt("user_id"));
				// Log.e("userId", result.getJSONObject(0).getInt("user_id") +
				// "");
				if (user.getDeviceId().equals(""))
					user.setDeviceId(result.getJSONObject(0).getString(
							"device_id"));
			} catch (JSONException e) {
				// Log.e("Register", "device_id not returned by remote server");
			}
			User.save(user);

			// start course menu activity
			Log.d("Register", "SUCCESS");
			goToCourseList(true);
			break;
		case Codes.DEVICE_ID_ALREADY_EXISTS:
			Toast.makeText(SplashActivity.this, "Device already registered.",
					Toast.LENGTH_LONG).show();
			break;
		case Codes.NO_DEVICE_ID_SUPPLIED:
			Toast.makeText(SplashActivity.this, "No device id supplied.",
					Toast.LENGTH_LONG).show();
			break;
		case Codes.NO_NAME_SUPPLIED:
			Toast.makeText(SplashActivity.this, "No name supplied.",
					Toast.LENGTH_LONG).show();
			break;
		default:
			Log.d("Register", "Error while registering. Error code: "
					+ resultCode);
		}

	}

	// public void loadUniversities(JSONArray result) {
	// unis = new ArrayList<University>();
	// int resultLength = result.length();
	//
	// for (int i = 0; i < resultLength; ++i) {
	// try {
	// unis.add(new University(result.getJSONObject(i).getInt("id"),
	// result.getJSONObject(i).getString("name")));
	// } catch (JSONException e) {
	// Log.d("loadUniversities", "error loading univerisites");
	// }
	// }
	// unisToSave = (ArrayList<University>) unis.clone();
	// AutoCompleteArrayAdapter<University> adapter;
	// adapter = new AutoCompleteArrayAdapter<University>(this,
	// R.layout.ac_list_item, unis);
	// txtUni.setAdapter(adapter);
	// txtUni.setOnItemClickListener(uniClickListener);
	// txtUni.setValidator(new ACValidator());
	// txtUni.setOnFocusChangeListener(new ValidateStarter());
	// loading.dismiss();
	// layoutFlipper.showNext();
	// }

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
				userRegister.addParam("uni_id",
						Integer.toString(user.getUniversity().getId()));

				remoteDB.addReceiver("user.register", userRegister);
				remoteDB.execute("user.register");
			} else {
				Log.i("C2DMResultHandler", "wtf");
			}
		}
	};

	/* starts registration */
	private final View.OnClickListener registerBtnHandler = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			txtUni.performValidation();

			if (txtUni.getText().toString().equals("")
					|| txtName.getText().toString().equals("")) {

				if (txtName.getText().toString().equals("")) {
					Toast.makeText(SplashActivity.this,
							"Please enter your name.", Toast.LENGTH_LONG)
							.show();

					return;
				}

				if (txtUni.getText().toString().equals(""))
					Toast.makeText(SplashActivity.this,
							"Please enter a university.", Toast.LENGTH_LONG)
							.show();

				return;

			}

			loading.setTitle("Please wait");
			loading.setMessage("Registering... ");
			loading.setCancelable(false);

			loading.show();
			user.setName(txtName.getText().toString());
			user.setUniversity(selectedUni);
			Log.i("onClick", "uni id: " + selectedUni.getId());

			remoteDB.setServerPort(selectedUni.getServerPort());
			remoteDB.setServerAddress(selectedUni.getServerAddress());

			// C2DM registration
			// Registration with our server is now handled by
			// remoteRegistration BroadcastReceiver
			DeviceRegistrar.startRegistration(getApplicationContext(),
					remoteRegistration);

		}
	};
	private final View.OnClickListener selectLoginBtnHandler = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			AutoCompleteArrayAdapter<University> adapter;
			adapter = new AutoCompleteArrayAdapter<University>(
					SplashActivity.this, R.layout.ac_list_item, unis);

			txtUni = (AutoCompleteTextView) findViewById(R.id.autocompleteUniversities);
			txtUni.setAdapter(adapter);
			txtUni.setOnItemClickListener(uniClickListener);
			txtUni.setValidator(new ACValidator());
			txtUni.setOnFocusChangeListener(new ValidateStarter());

			btnLogin.setOnClickListener(loginBtnHandler);
			layoutFlipper.showNext();

		}
	};
	private View.OnClickListener selectRegisterBtnHandler = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			AutoCompleteArrayAdapter<University> adapter;
			adapter = new AutoCompleteArrayAdapter<University>(
					SplashActivity.this, R.layout.ac_list_item, unis);

			txtUni = (AutoCompleteTextView) findViewById(R.id.acUni);
			txtUni.setAdapter(adapter);
			txtUni.setOnItemClickListener(uniClickListener);
			txtUni.setValidator(new ACValidator());
			txtUni.setOnFocusChangeListener(new ValidateStarter());

			btnRegister.setOnClickListener(registerBtnHandler);
			layoutFlipper.showNext();
			layoutFlipper.showNext();
		}
	};

	private final View.OnClickListener loginBtnHandler = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			loading.setTitle("Please wait");
			loading.setMessage("Signing in... ");
			loading.setCancelable(false);

			loading.show();
			user.setUniversity(selectedUni);
			remoteDB.setServerPort(selectedUni.getServerPort());
			remoteDB.setServerAddress(selectedUni.getServerAddress());
			login();
		}
	};
	private OnItemClickListener uniClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parentView, View selectedView,
				int position, long id) {
			Log.i("uniClickListener", "" + position);
			selectedUni = (University) parentView.getAdapter()
					.getItem(position);
		}

	};

	// Receivers
	private InternalReceiver userRegister = new InternalReceiver() {
		@Override
		public void update(JSONArray data) {
			SplashActivity.this.registerCallback(data);
		}

		@Override
		public void onHttpError(int statusCode) {
			loading.dismiss();
			Toast.makeText(SplashActivity.this, "HTTP error: " + statusCode,
					Toast.LENGTH_LONG).show();
			SplashActivity.this.onConnectionError(statusCode);
		}

		@Override
		public void onConnectionError(String errorMessage) {
			loading.dismiss();
			Toast.makeText(SplashActivity.this, "Unable to connect.",
					Toast.LENGTH_LONG).show();
			SplashActivity.this.onHttpError(errorMessage);
		}
	};

	private InternalReceiver userLogin = new InternalReceiver() {
		// @Override
		// public boolean validate()
		// {
		// if( this.params.get("device_id").equals("") ) return false;
		// if( User.)
		// return true;
		// }
		@Override
		public void update(JSONArray data) {
			SplashActivity.this.loginCallback(data);
		}

		@Override
		public void onHttpError(int statusCode) {
			SplashActivity.this.onConnectionError(statusCode);
		}

		@Override
		public void onConnectionError(String errorMessage) {
			SplashActivity.this.onHttpError(errorMessage);
		}
	};

	/*
	 * private InternalReceiver universitiesView = new InternalReceiver() {
	 * 
	 * @Override public void update(JSONArray data) { //
	 * SplashActivity.this.loadUniversities(data); }
	 * 
	 * @Override public void onHttpError(int statusCode) {
	 * SplashActivity.this.onConnectionError(statusCode); }
	 * 
	 * @Override public void onConnectionError(String errorMessage) {
	 * SplashActivity.this.onHttpError(errorMessage); } };
	 */

	class ValidateStarter implements OnFocusChangeListener {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (!hasFocus) {
				((AutoCompleteTextView) v).performValidation();
			}
		}

	};

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	//
	// super.onActivityResult(requestCode, resultCode, data);
	//
	// if (requestCode == 1) {
	// Bundle extras = data.getExtras();
	// selectedUni = new University(extras.getInt("id"),
	// extras.getString("name"),
	// extras.getString("serveraddress"), extras.getInt("port"),
	// extras.getInt("manualFlag"));
	// }
	// }

	class ACValidator implements AutoCompleteTextView.Validator {

		@Override
		public CharSequence fixText(CharSequence invalidText) {
			Toast.makeText(SplashActivity.this,
					"Invalid University entered. Please try again.",
					Toast.LENGTH_LONG).show();
			return "";
		}

		@Override
		public boolean isValid(CharSequence text) {
			String universityName = text.toString();
			Iterator<University> it = unis.iterator();

			// Check entered text against valid universities
			while (it.hasNext()) {
				if ((it.next().getName()).equals(universityName))
					return true;
			}

			return false; // University isn't valid
		}

	};
}