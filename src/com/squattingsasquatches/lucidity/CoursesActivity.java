package com.squattingsasquatches.lucidity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class CoursesActivity extends Activity {

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
	private Context ctx;
	
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
		
		ctx = this;
		subjectCourses = new ArrayList<Course>();
		coursesListView = (ListView) findViewById(R.id.ListContainer);
        loading = new ProgressDialog(this);
        localDB = new LocalDBAdapter(this).open();
        remoteDB = new RemoteDBAdapter(this);
        subjectId = getIntent().getIntExtra("com.squattingsasquatches.subjectId", -1);
        
        TextView txtHeading = (TextView) findViewById(R.id.txtHeading);
        txtHeading.setText("Choose a Subject");
        
        loading.setTitle("Please wait");
        loading.setMessage("Loading subjects... ");
        loading.setCancelable(false);
        loading.show();
        
        InternalReceiver uniSubjectsView = new InternalReceiver(){
			public void update( JSONArray data ){
				CoursesActivity.this.loadSubjectsCallback( data );
			}
		};
		uniSubjectsView.params.put("uni_id", Integer.toString(localDB.getUserUniId()));
        
        remoteDB.addReceiver("uni.subjects.view", uniSubjectsView);
        remoteDB.execute("uni.subjects.view");
	}
	
	public void loadSubjectsCallback(JSONArray result) {
		subjectCourses.clear();
		
		int resultLength = result.length();
		
		for (int i = 0; i < resultLength; ++i) {
			try {
				JSONObject subject = result.getJSONObject(i);
				subjectCourses.add(new Course(
										subject.getInt("course_id"),
										subject.getString("course_name")
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
			Subject subject = (Subject) o;
			
			int selected = subject.getId();
			
			Log.d("uni click", selected+"");
			// load courses with subject
		}
	};

}
