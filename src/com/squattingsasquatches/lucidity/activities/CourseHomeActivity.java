package com.squattingsasquatches.lucidity.activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
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

import com.squattingsasquatches.lucidity.CheckInManager;
import com.squattingsasquatches.lucidity.Codes;
import com.squattingsasquatches.lucidity.ExtendedListAdapter;
import com.squattingsasquatches.lucidity.InternalReceiver;
import com.squattingsasquatches.lucidity.ListAdapter;
import com.squattingsasquatches.lucidity.R;
import com.squattingsasquatches.lucidity.objects.Assignment;
import com.squattingsasquatches.lucidity.objects.Section;
import com.squattingsasquatches.lucidity.objects.User;

public class CourseHomeActivity extends LucidityActivity {

	/* UI */
	private ProgressDialog loading;
	private ListView upcomingListView;
	private ListView pastDueListView;
	private Button btnCheckIn;

	/* Misc */
	private ArrayList<Assignment> upcomingAssignments;
	private ArrayList<Assignment> pastAssignments;
	private int sectionId, courseNumber;
	private String coursePrefix;
	private User user;
	private SimpleDateFormat df;
	private InternalReceiver checkInView;
	private String deviceId;

	@Override
	public void onPause() {
		super.onPause();

		try {
			unregisterReceiver(c2dmCheckOut);
			unregisterReceiver(gpsReceiver);
		} catch (IllegalArgumentException e) {
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		IntentFilter intentFilter = new IntentFilter(
				"com.squattingsasquatches.lucidity.LOCATION_UPDATED");
		intentFilter.setPriority(1); // throughout the ship
		registerReceiver(gpsReceiver, intentFilter);
		updateCheckInBtn();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_home);

		user = new User();
		user.setDeviceId(Settings.Secure.getString(getContentResolver(),
				Settings.Secure.ANDROID_ID)); // get device's unique ID

		upcomingAssignments = new ArrayList<Assignment>();
		pastAssignments = new ArrayList<Assignment>();

		upcomingListView = (ListView) findViewById(R.id.ListContainer);
		pastDueListView = (ListView) findViewById(R.id.ListContainer2);
		btnCheckIn = (Button) findViewById(R.id.btnCheckIn);

		loading = new ProgressDialog(this);
		sectionId = getIntent().getIntExtra("sectionId", -1);
		coursePrefix = Section.get(sectionId).getCourse().getSubject()
				.getPrefix();
		courseNumber = Section.get(sectionId).getCourse().getCourseNum();
		df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		deviceId = Settings.Secure.getString(getContentResolver(),
				Settings.Secure.ANDROID_ID);

		TextView txtHeading = (TextView) findViewById(R.id.txtHeading);
		txtHeading.setText(coursePrefix + " " + courseNumber);

		checkInView = new InternalReceiver() {
			@Override
			public void update(JSONArray data) {
				CourseHomeActivity.this.gpsCheckInCallback(data);
			}
		};

		loading.setTitle("Please wait");
		loading.setMessage("Loading section info... ");
		loading.setCancelable(false);
		loading.show();

		btnCheckIn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				locationCheckIn();
			}
		});

		InternalReceiver assignmentsView = new InternalReceiver() {
			@Override
			public void update(JSONArray data) {
				CourseHomeActivity.this.displayAssignments(data);
			}
		};
		assignmentsView.addParam("section_id", sectionId);
		assignmentsView.addParam("device_id", deviceId);

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

				Date dueDate = df.parse(assignment.getString("due_date")), rightNow = new Date();

				Assignment a = new Assignment(assignment.getInt("id"),
						assignment.getString("name"),
						assignment.getInt("doc_id"),
						assignment.getString("descrip"), dueDate);
				
				

				if (dueDate.after(rightNow))
					upcomingAssignments.add(a);
				else
					pastAssignments.add(a);

			} catch (JSONException e) {
				Log.d("displayAssignments", "JSON error");
			} catch (ParseException e) {
				Log.d("displayAssignments", "Invalid due_date returned");
			}
		}

		if (pastAssignments.size() < 1) {
			pastAssignments.add(new Assignment(-2, "No Past Assignments"));
			pastDueListView.setClickable(false);
			pastDueListView.setAdapter(new ListAdapter<Assignment>(this,
					pastAssignments));
		} else {
			pastDueListView.setAdapter(new ExtendedListAdapter<Assignment>(
					this, pastAssignments));
			pastDueListView.setOnItemClickListener(listViewHandler);
		}

		if (upcomingAssignments.size() < 1) {
			upcomingAssignments.add(new Assignment(-2,
					"No Upcoming Assignments"));
			upcomingListView.setClickable(false);
			upcomingListView.setAdapter(new ListAdapter<Assignment>(this,
					upcomingAssignments));
		} else {
			upcomingListView.setAdapter(new ExtendedListAdapter<Assignment>(
					this, upcomingAssignments));
			upcomingListView.setOnItemClickListener(listViewHandler);
		}

		loading.dismiss();
	}

	private final OnItemClickListener listViewHandler = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			Object o = ((ListView) parent).getItemAtPosition(position);
			Assignment assignment = (Assignment) o;
			
			nextActivity = new Intent(CourseHomeActivity.this,
					ViewAssignmentActivity.class);
			nextActivity.putExtra("assignmentId", assignment.getId());
			nextActivity.putExtra("assignmentName", assignment.getName());
			nextActivity.putExtra("assignmentDocId", assignment.getDocId());
			nextActivity.putExtra("assignmentDescription",
					assignment.getDescription());
			nextActivity.putExtra("assignmentDueDate",
					df.format(assignment.getDueDate()));
			startActivity(nextActivity);
		}
	};

	private void gpsCheckInCallback(JSONArray data) {
		int resultCode = getResultCode(data);

		Context context = CourseHomeActivity.this;
		int duration = Toast.LENGTH_SHORT;
		String text;

		if (resultCode == Codes.SUCCESS) {
			text = "Successfully checked in!";
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
			CheckInManager.saveCheckedIn(true, sectionId);
			updateCheckInBtn();
		} else if (resultCode == Codes.USER_NOT_WITHIN_RADIUS) {
			text = "You must be in the classroom to check in.";
			Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
			toast.show();
			CheckInManager.saveCheckedIn(false, sectionId);
		} else { // ERROR
			text = "An error was encountered while attempting to check in.";
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
			CheckInManager.saveCheckedIn(false, sectionId);
		}

		loading.dismiss();
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

	public void updateCheckInBtn() {
		if (CheckInManager.isCheckedIn()) {
			btnCheckIn.setEnabled(false);
			btnCheckIn.setTextColor(Color.WHITE);
			btnCheckIn.setText("Checked In");
		} else {
			btnCheckIn.setEnabled(true);
			btnCheckIn.setTextColor(Color.parseColor("#FF666666"));
			btnCheckIn.setText("Check In");
		}
	}

	private void locationCheckIn() {
		loading.setTitle("Please wait");
		loading.setMessage("Checking your location... ");
		loading.show();

		CheckInManager.startGPS(this, sectionId);
	}

	private BroadcastReceiver c2dmCheckOut = new BroadcastReceiver() {

		@Override
		public void onReceive(Context ctx, Intent intent) {
			CheckInManager.saveCheckedIn(false, sectionId);
			CheckInManager.stopUpdating();
			btnCheckIn.setVisibility(View.INVISIBLE);
		}

	};

	private BroadcastReceiver gpsReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context ctx, Intent intent) {
			checkInView.addParam("section_id", sectionId);
			checkInView.addParam("device_id", user.getDeviceId());
			checkInView.addParam("gps_lat", "" + CheckInManager.getLatitude());
			checkInView
					.addParam("gps_long", "" + CheckInManager.getLongitude());

			remoteDB.addReceiver("user.checkin", checkInView);
			remoteDB.execute("user.checkin");
		}

	};

}