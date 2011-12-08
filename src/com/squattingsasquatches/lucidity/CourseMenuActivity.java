package com.squattingsasquatches.lucidity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class CourseMenuActivity extends Activity {
	
	/* DBs */
	private RemoteDBAdapter remoteDB;
	private LocalDBAdapter localDB;
	
	/* UI */
	private ProgressDialog loading;
	private ListView coursesListView;
	
	/* Misc */
	private Intent nextActivity;
	private ArrayList<Section> userSections;
	private int userId;
	private boolean updateCourses;
	
	InternalReceiver getCourses;
	
	@Override
	public void onPause() {
		super.onPause();
		if (remoteDB != null)
			remoteDB.unregisterReceiver("user.courses.view");
		if (localDB != null)
			localDB.close();
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generic_list);
        
        updateCourses = getIntent().getBooleanExtra("com.squattingsasquatches.updateCourses", false);
        
        userSections = new ArrayList<Section>();
        coursesListView = (ListView) findViewById(R.id.ListContainer);
        loading = new ProgressDialog(this);
        localDB = new LocalDBAdapter(this).open();
        remoteDB = new RemoteDBAdapter(this);
        userId = getIntent().getIntExtra("com.squattingsasquatches.userId", -1);
        
        // Receivers
        getCourses = new InternalReceiver(){
			public void update( JSONArray data ){
				CourseMenuActivity.this.displayCourses( data );
			}
		};
		getCourses.addParam("user_id", userId);
		
		
		remoteDB.addReceiver("user.courses.view", getCourses);
		
        loading.setTitle("Please wait");
        loading.setMessage("Loading your saved courses... ");
        loading.setCancelable(false);
        loading.show();
        
        if (updateCourses) {
        	remoteDB.execute("user.courses.view");
        } else {
        	displayCourses(localDB.getSections());
        }
	}
	
	public void attachCourseOnClickListener() {
		userSections.add(new Section(-1, "Add a Course"));
		coursesListView.setAdapter(new ListAdapter<Section>(this, userSections));
		coursesListView.setOnItemClickListener(listViewHandler);
		loading.dismiss();
	}
	
	public void displayCourses( JSONArray data )
	{
		userSections.clear();
		
		int resultLength = data.length();
		
		for (int i = 0; i < resultLength; ++i) {
			try {
				JSONObject section = data.getJSONObject(i);
				
				userSections.add(new Section(
									section.getInt(LocalDBAdapter.KEY_SECTION_ID),
									section.getString(LocalDBAdapter.KEY_SECTION_NUMBER),
									new Course(section.getInt(LocalDBAdapter.KEY_COURSE_ID), section.getInt(LocalDBAdapter.KEY_COURSE_NUMBER),
											new Subject(section.getString(LocalDBAdapter.KEY_SUBJECT_PREFIX))),
									new User(section.getInt(LocalDBAdapter.KEY_PROFESSOR_ID), section.getString(LocalDBAdapter.KEY_PROFESSOR_NAME)),
									section.getString(LocalDBAdapter.KEY_DAYS),
									section.getString(LocalDBAdapter.KEY_LOCATION),
									section.getString(LocalDBAdapter.KEY_START_TIME),
									section.getString(LocalDBAdapter.KEY_END_TIME),
									section.getInt(LocalDBAdapter.KEY_VERIFIED)));
			} catch (JSONException e) {
				Log.d("getCoursesCallback", "JSON error");
			}
		}
		
		if (updateCourses && resultLength > 0)
			localDB.saveSectionInfo(userSections);
		
		attachCourseOnClickListener();
	}
	
	private final OnItemClickListener listViewHandler = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			Object o = coursesListView.getItemAtPosition(position);
			Section section = (Section) o;
			
			switch (section.getId()) {
				case -1:
					// Add a Course
					// Start SubjectsActivity
					nextActivity = new Intent(CourseMenuActivity.this, SelectSubjectActivity.class);
					nextActivity.putExtra("com.squattingsasquatches.userId", userId);
					startActivity(nextActivity);
					break;
				default:
					// load selected course and start CourseHome activity
					nextActivity = new Intent(CourseMenuActivity.this, CourseHomeActivity.class);
					nextActivity.putExtra("com.squattingsasquatches.sectionId", section.getId());
					startActivity(nextActivity);
					break;
			}
		}
	};
}
