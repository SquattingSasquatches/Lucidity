package com.squattingsasquatches;

import org.json.JSONArray;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class CourseMenuStudent extends Activity {
	
	private ListView coursesListView;
	private PHPConnection db;
	private String deviceID;
	
	@Override
	public void onPause() {
		super.onPause();
		db.unregisterReceiver();
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_menu_student);
        
        coursesListView = (ListView) findViewById(R.id.coursesListView);
        
        deviceID = getIntent().getStringExtra("com.squattingsasquatches.deviceID");
        db = new PHPConnection(getApplicationContext(), this);
        
        db.setAction("user.courses.view");
		db.addParam("device_id", deviceID);
		db.execute("getCoursesCallback");
		
		/*
		 * I think we should save a user's courses locally, maybe in a SQLite DB.
		 * That way we only need to send the query to get them after they add a course instead of everytime they visit this activity.
		 * For now though, the query is run everytime the activity is created.
		 */
	
	}
	
	public void getCoursesCallback(JSONArray result) {
		// populate coursesListView with courses
        // "Add a Course" as last view
		
		// uses CourseListView to construct
	}
}
