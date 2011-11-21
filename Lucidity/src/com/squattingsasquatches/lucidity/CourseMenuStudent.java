package com.squattingsasquatches.lucidity;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class CourseMenuStudent extends Activity implements RemoteResultReceiver.Receiver {
	
	private ListView coursesListView;
	private RemoteDBAdapter remoteDB;
	private LocalDBAdapter localDB;
	private String deviceID;
	private ArrayList<Course> courses;
	private ProgressDialog loading;
	
	@Override
	public void onPause() {
		super.onPause();
		if (remoteDB != null)
			remoteDB.unregisterReceiver();
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_menu_student);
        
        boolean updateCourses = getIntent().getExtras().getBoolean("updateCourses", false);
        
        courses = new ArrayList<Course>();
        coursesListView = (ListView) findViewById(R.id.coursesListView);
        loading = new ProgressDialog(this);
        localDB = new LocalDBAdapter(this);
        deviceID = getIntent().getStringExtra("com.squattingsasquatches.deviceID");
        
        loading.setTitle("Please wait");
        loading.setMessage("Loading your courses... ");
        loading.setOnCancelListener(dialogCancelListener);
        loading.show();
        
        if (updateCourses) {
        	remoteDB = new RemoteDBAdapter(this);
	        remoteDB.setReceiver(this);
	        remoteDB.setAction("user.courses.view");
	        remoteDB.addParam("device_id", deviceID);
	        remoteDB.execute();
        } else {
        	getCoursesCallback(localDB.open().getCourses());
        }
	}
	
	public void getCoursesCallback(JSONArray result) {
		// populate coursesListView with courses
        // append "Add a Course" option
		loading.dismiss();
		courses.clear();
		
		int resultLength = result.length();
		
		for (int i = 0; i < resultLength; ++i) {
			try {
				JSONObject course = result.getJSONObject(i);
				courses.add(new Course(course.getInt("id"), course.getInt("professors_id"), course.getString("name"), new Date(course.getString("start_date")), new Date(course.getString("end_date"))));
			} catch (JSONException e) {
				Log.d("getCoursesCallback", "JSON error");
			}
		}
		
		initializeCourseOnClickListener();
	}
	
	public void initializeCourseOnClickListener() {
		courses.add(new Course(0, "Add a Course"));
		coursesListView.setAdapter(new CourseListAdapter(this, courses));
		coursesListView.setOnItemClickListener(listViewHandler);
	}
	
	public void onReceiveResult(int resultCode, Bundle resultData) {
		// result from remote PHP query
		Log.i("onReceiveResult", String.valueOf(resultCode));
		
		if (resultCode == Codes.REMOTE_QUERY_COMPLETE) {
			String result = resultData.getString("result");
			if (result != null) {
				try {
					getCoursesCallback(new JSONArray(result));
				} catch (JSONException e) {
					Log.e("onReceiveResult", "error with JSONArray");
				}
			}
		} else {
			// bads!
		}
	}
	
	private final OnItemClickListener listViewHandler = new OnItemClickListener() {
		
		public void onItemClick(AdapterView<?> a, View v, int position, long id) {
			
			Object o = coursesListView.getItemAtPosition(position);
			Course course = (Course) o;
			
			Toast.makeText(CourseMenuStudent.this, "You have chosen: " + " " + course.getName(), Toast.LENGTH_LONG).show();
			
			switch (course.getId()) {
				case -1:
					// reload courses
					loading.show();
					courses.clear();
					remoteDB.execute();
					break;
				case 0:
					// Add a Course
					// start AddCourse activity
					break;
				default:
					// load selected course and start Course activity
					break;
			}
		}
		
	};
	
	private final OnCancelListener dialogCancelListener = new OnCancelListener() {

		public void onCancel(DialogInterface dialog) {
			if (remoteDB != null && remoteDB.stopService()) {
				courses.clear();
				courses.add(new Course(-1, "Load Courses"));
				initializeCourseOnClickListener();
			}
		}
		
	};
}
