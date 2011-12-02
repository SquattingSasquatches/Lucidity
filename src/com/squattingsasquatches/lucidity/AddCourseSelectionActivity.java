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
import android.widget.TextView;

public class AddCourseSelectionActivity extends Activity {
	
	/* DBs */
	private RemoteDBAdapter remoteDB;
	private LocalDBAdapter localDB;
	
	/* UI */
	private ProgressDialog loading;
	private ListView coursesListView;
	
	/* Misc */
	private Intent nextActivity;
	private ArrayList<Course> subjectCourses;
	private int subjectId;
	private String subjectPrefix;
	private int uniId;
	
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

		setContentView(R.layout.generic_list);
		
		subjectCourses = new ArrayList<Course>();
		coursesListView = (ListView) findViewById(R.id.ListContainer);
        loading = new ProgressDialog(this);

        remoteDB = new RemoteDBAdapter(this);
        localDB = new LocalDBAdapter(this).open();
        subjectId = getIntent().getIntExtra("com.squattingsasquatches.subjectId", -1);
        subjectPrefix = getIntent().getStringExtra("com.squattingsasquatches.subjectPrefix");
        uniId = localDB.getUserUniId();

        TextView txtHeading = (TextView) findViewById(R.id.txtHeading);
        txtHeading.setText("Choose a Course");

        loading.setTitle("Please wait");
        loading.setMessage("Loading available courses... ");
        loading.setCancelable(false);
        loading.show();

        InternalReceiver subjectsCoursesView = new InternalReceiver(){
			public void update( JSONArray data ){
				AddCourseSelectionActivity.this.displayCourses( data );
			}
		};
		subjectsCoursesView.addParam("uni_id", uniId);
		subjectsCoursesView.addParam("subject_id", subjectId);
		
		Log.i("sel", uniId+" | "+subjectId);
        
        remoteDB.addReceiver("uni.courses.view", subjectsCoursesView);
        remoteDB.execute("uni.courses.view");
	}
	
	public void displayCourses(JSONArray data) {
		subjectCourses.clear();
		
		int resultLength = data.length();
		
		for (int i = 0; i < resultLength; ++i) {
			try {
				JSONObject course = data.getJSONObject(i);
				subjectCourses.add(new Course(
											course.getInt("course_id"),
											course.getInt("course_number"),
											course.getString("course_name"),
											new Subject(subjectId, subjectPrefix),
											new University(uniId)
										));
			} catch (JSONException e) {
				Log.d("getCoursesCallback", "JSON error");
			}
		}
		
		coursesListView.setAdapter(new ListAdapter<Course>(this, subjectCourses));
		coursesListView.setOnItemClickListener(listViewHandler);
		
		loading.dismiss();
	}
	
	
	private final OnItemClickListener listViewHandler = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			Object o = coursesListView.getItemAtPosition(position);
			Course course = (Course) o;
			
			nextActivity = new Intent(AddCourseSelectionActivity.this, CourseDescriptionActivity.class);
			nextActivity.putExtra("com.squattingsasquatches.courseId", course.getId());
			startActivity(nextActivity);
		}
	};
}
