package com.squattingsasquatches.lucidity;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CourseHomeActivity extends Activity {

	/* DBs */
	private RemoteDBAdapter remoteDB;
	private LocalDBAdapter localDB;

	/* UI */
	private ProgressDialog loading;
	private ListView upcomingListView;
	private ListView pastDueListView;
	private Button btnCheckIn;

	/* Misc */
	private Intent nextActivity;
	private ArrayList<Assignment> upcomingAssignments;
	private ArrayList<Assignment> pastAssignments;
	private int sectionId, courseNumber;
	private String coursePrefix;
	private User user;
    
	/* GPS */
	private Location currentLocation = null;//= getLocation();
	private boolean checkedIn = false;
	private LocationManager locationManager;
	private String bestProvider;

	@Override
	public void onPause() {
		super.onPause();
		if (remoteDB != null)
			remoteDB.unregisterAllReceivers();
		if (localDB != null)
			localDB.close();
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_home);

		user = new User();
		user.setDeviceId(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)); //get device's unique ID

		upcomingAssignments = new ArrayList<Assignment>();
		pastAssignments = new ArrayList<Assignment>();
		upcomingListView = (ListView) findViewById(R.id.ListContainer);
		pastDueListView = (ListView) findViewById(R.id.ListContainer2);
		btnCheckIn = (Button) findViewById(R.id.btnCheckIn);
        loading = new ProgressDialog(this);

        remoteDB = new RemoteDBAdapter(this);
        localDB = new LocalDBAdapter(this).open();
        sectionId = getIntent().getIntExtra("com.squattingsasquatches.sectionId", -1);
        coursePrefix = getIntent().getStringExtra("com.squattingsasquatches.coursePrefix");
        courseNumber = getIntent().getIntExtra("com.squattingsasquatches.courseNumber", -1);

        TextView txtHeading = (TextView) findViewById(R.id.txtHeading);
        txtHeading.setText(coursePrefix + " " + courseNumber);

        loading.setTitle("Please wait");
        loading.setMessage("Loading section info... ");
        loading.setCancelable(false);
        loading.show();

        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                locationCheckIn();
            }
        });
        
        InternalReceiver assignmentsView = new InternalReceiver(){
			public void update( JSONArray data ){
				CourseHomeActivity.this.displayAssignments( data );
			}
		};
		assignmentsView.addParam("section_id", sectionId);
        
        remoteDB.addReceiver("section.assignments.view", assignmentsView);
        remoteDB.execute("section.assignments.view");
	}

	public void displayAssignments(JSONArray data) {
		upcomingAssignments.clear();
		pastAssignments.clear();

		int resultLength = data.length();

		for (int i = 0; i < resultLength; ++i) {
			try {
				JSONObject assignment = data.getJSONObject(i);

				Date dueDate = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM).parse(assignment.getString("due_date")),
					 rightNow = new Date();

				Assignment a = new Assignment(
								assignment.getInt("id"),
								assignment.getString("name"),
								assignment.getInt("doc_id"),
								assignment.getString("descrip"),
								dueDate);

				if (rightNow.after(dueDate))
					pastAssignments.add(a);
				else
					upcomingAssignments.add(a);
				
				if (pastAssignments.size() < 1) {
					pastAssignments.add(new Assignment(-2, "No Past Assignments"));
					pastDueListView.setClickable(false);
				}
				
				if (upcomingAssignments.size() < 1) {
					upcomingAssignments.add(new Assignment(-2, "No Upcoming Assignments"));
					upcomingListView.setClickable(false);
				}

			} catch (JSONException e) {
				Log.d("displayAssignments", "JSON error");
			} catch (ParseException e) {
				Log.d("displayAssignments", "Invalid due_date returned");
			}
		}

		upcomingListView.setAdapter(new ListAdapter<Assignment>(this, upcomingAssignments));
		pastDueListView.setAdapter(new ListAdapter<Assignment>(this, pastAssignments));

		upcomingListView.setOnItemClickListener(listViewHandler);
		pastDueListView.setOnItemClickListener(listViewHandler);

		loading.dismiss();
	}

	private final OnItemClickListener listViewHandler = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			Object o = upcomingListView.getItemAtPosition(position);
			Assignment assignment = (Assignment) o;
			DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
			
			nextActivity = new Intent(CourseHomeActivity.this, ViewAssignmentActivity.class);
			nextActivity.putExtra("com.squattingsasquatches.assignmentId", assignment.getId());
			nextActivity.putExtra("com.squattingsasquatches.assignmentName", assignment.getName());
			nextActivity.putExtra("com.squattingsasquatches.assignmentDocId", assignment.getDocId());
			nextActivity.putExtra("com.squattingsasquatches.assignmentDescription", assignment.getDescription());
			nextActivity.putExtra("com.squattingsasquatches.assignmentDueDate", df.format(assignment.getDueDate()));
			startActivity(nextActivity);
		}
	};

	private void gpsCheckInCallback(JSONArray data){
		int resultCode = getResultCode(data);

		Context context = CourseHomeActivity.this;
		int duration = Toast.LENGTH_SHORT;
		String text;

		if(resultCode == Codes.SUCCESS){
			text = "Successfully Checked In!";
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
			checkedIn = true;
			btnCheckIn.setEnabled(false);
			btnCheckIn.setText("Checked In");
		} else if(resultCode == Codes.USER_NOT_WITHIN_RADIUS){
			text = "Not Close Enough!";
			
			this.currentLocation = getLocation();
			double dist = distFrom(currentLocation.getLatitude(), currentLocation.getLongitude(), 33.2143, -87.5445);
			Toast toast = Toast.makeText(context, dist+"", Toast.LENGTH_LONG);
			toast.show();
			checkedIn = false;
		} else { //ERROR
			text = "Unspecified Error!";
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
			checkedIn = false;
		}			
		
		loading.dismiss();

	}
	
	public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
	    double earthRadius = 6370990.56;
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double dist = earthRadius * c;

	    return dist;
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

	private void startGPS(){
		Criteria criteria = new Criteria();

		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setPowerRequirement(Criteria.POWER_HIGH);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setSpeedRequired(false);
		criteria.setCostAllowed(true);

		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		bestProvider = locationManager.getBestProvider(criteria, true);
		
		locationManager.requestLocationUpdates(bestProvider, 0, 0, gpsListener);
	}
	
	private Location getLocation() {
		return locationManager.getLastKnownLocation(bestProvider);
	}

	private boolean locationCheckIn(){
		loading.setTitle("Please wait");
        loading.setMessage("Loading section info... ");
        loading.show();
		//do something together with php
		currentLocation = getLocation();

		InternalReceiver checkInView = new InternalReceiver(){
			public void update( JSONArray data ){
				CourseHomeActivity.this.gpsCheckInCallback( data );
			}
		};
		Log.i("lat", currentLocation.getLatitude()+"");
		Log.i("long", currentLocation.getLongitude()+"");
		checkInView.addParam("section_id", sectionId);
		checkInView.addParam("device_id", user.getDeviceId());
		checkInView.addParam("gps_lat", "" + currentLocation.getLatitude());
		checkInView.addParam("gps_long", "" + currentLocation.getLongitude());
    
		remoteDB.addReceiver("user.checkin", checkInView);
		remoteDB.execute("user.checkin");

		return false;
	}
	
	private LocationListener gpsListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location loc) {
			currentLocation = loc;
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
	};

}