package com.squattingsasquatches.lucidity;

import java.util.ArrayList;

import org.json.JSONArray;

import com.squattingsasquatches.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class CourseMenuStudent extends Activity {
	
	private ListView coursesListView;
	private RemoteDBAdapter db;
	private String deviceID;
	private ArrayList<Course> courses;
	private ProgressDialog loading;
	
	@Override
	public void onPause() {
		super.onPause();
		db.unregisterReceiver();
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_menu_student);
        
        courses = new ArrayList<Course>();
        
        loading = new ProgressDialog(this);
        
        loading.setTitle("Please wait");
        loading.setMessage("Loading your courses... ");
        loading.setOnCancelListener(dialogCancelListener);
        loading.show();
        
        coursesListView = (ListView) findViewById(R.id.coursesListView);
        
        deviceID = getIntent().getStringExtra("com.squattingsasquatches.deviceID");
        db = new RemoteDBAdapter(getApplicationContext(), this);
        
        db.setAction("user.courses.view");
		db.addParam("device_id", deviceID);
		db.execute("getCoursesCallback");
		
		/*
		 * I think we should save a user's courses locally, maybe in a SQLite DB.
		 * That way we only need to send the query to get them after they add a course instead of everytime they visit this activity.
		 * For now though, the query is run everytime the activity is created.
		 * 
		 * Query should be run after first registration, after new device registration, and after everytime a course is added
		 */
	}
	
	public void getCoursesCallback(JSONArray result) {
		// populate coursesListView with courses
        // "Add a Course" as last view
		loading.dismiss();
		
		courses.clear();
		
		initializeCourseOnClickListener();
	}
	
	public void initializeCourseOnClickListener() {
		courses.add(new Course(0, "Add a Course"));
		coursesListView.setAdapter(new CourseListAdapter(this, courses));
		coursesListView.setOnItemClickListener(listViewHandler);
	}
	
	OnItemClickListener listViewHandler = new OnItemClickListener() {
		
		public void onItemClick(AdapterView<?> a, View v, int position, long id) {
			
			Object o = coursesListView.getItemAtPosition(position);
			Course course = (Course) o;
			
			Toast.makeText(CourseMenuStudent.this, "You have chosen: " + " " + course.getName(), Toast.LENGTH_LONG).show();
			
			switch (course.getId()) {
				case -1:
					// reload courses
					loading.show();
					courses.clear();
					db.execute("getCoursesCallback");
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
	
	OnCancelListener dialogCancelListener = new OnCancelListener() {

		public void onCancel(DialogInterface dialog) {
			if (db.stopService()) {
				courses.clear();
				courses.add(new Course(-1, "Load Courses"));
				initializeCourseOnClickListener();
			}
		}
		
	};
}
