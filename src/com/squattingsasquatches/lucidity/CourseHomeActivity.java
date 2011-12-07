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
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
	
	/* Misc */
	private Intent nextActivity;
	private ArrayList<Assignment> upcomingAssignments;
	private ArrayList<Assignment> pastAssignments;
	private int sectionId;
	
	/* GPS */
	private Location currentLocation = null;//= getLocation();
	private boolean checkedIn = false;
	
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
		
		upcomingAssignments = new ArrayList<Assignment>();
		upcomingListView = (ListView) findViewById(R.id.ListContainer);
		pastDueListView = (ListView) findViewById(R.id.ListContainer2);
        loading = new ProgressDialog(this);

        remoteDB = new RemoteDBAdapter(this);
        localDB = new LocalDBAdapter(this).open();
        sectionId = getIntent().getIntExtra("com.squattingsasquatches.sectionId", -1);

        TextView txtHeading = (TextView) findViewById(R.id.txtHeading);
        txtHeading.setText("Choose a Course");

        loading.setTitle("Please wait");
        loading.setMessage("Loading section info... ");
        loading.setCancelable(false);
        loading.show();

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
		
		int resultLength = data.length();
		
		for (int i = 0; i < resultLength; ++i) {
			try {
				JSONObject assignment = data.getJSONObject(i);
				
				Date dueDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).parse(assignment.getString("due_date")),
					 rightNow = new Date();
				
				Assignment a = new Assignment(
								assignment.getInt("id"),
								assignment.getString("name"),
								assignment.getInt("doc_id"),
								assignment.getString("description"),
								dueDate);
				
				if (rightNow.after(dueDate))
					pastAssignments.add(a);
				else
					upcomingAssignments.add(a);
				
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
	
	private Location getLocation(){
		Criteria criteria = new Criteria();
		
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setSpeedRequired(false);
		criteria.setCostAllowed(true);
		
		String serviceString = Context.LOCATION_SERVICE;
		LocationManager locationManager = (LocationManager)getSystemService(serviceString);
		String bestProvider = locationManager.getBestProvider(criteria, true);
		
		return locationManager.getLastKnownLocation(bestProvider);
	}
	
	private boolean locationCheckIn(){
		//do something together with php
		this.currentLocation = getLocation();
		//send
		//receive
		
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		String text;
		
		if(checkedIn){
			text = "Successfully Checked In!";
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		} else {
			text = "Checking in Failed!";
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
		
		return false;
	}
	
}
