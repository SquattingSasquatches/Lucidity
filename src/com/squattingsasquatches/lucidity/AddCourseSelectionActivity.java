package com.squattingsasquatches.lucidity;

import java.util.ArrayList;
import java.util.Date;

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
        subjectId = getIntent().getIntExtra("com.squattingsasquatches.subjectId", -1);

        TextView txtHeading = (TextView) findViewById(R.id.txtHeading);
        txtHeading.setText("Courses");

        loading.setTitle("Please wait");
        loading.setMessage("Loading courses... ");
        loading.setCancelable(false);
        loading.show();

        //subjectCourses = Course.Table.getCourses( subjectId );
        coursesListView.setAdapter(new ListAdapter<Course>(this, subjectCourses));
		coursesListView.setOnItemClickListener(listViewHandler);
		
		loading.dismiss();
	}
	
	
	private final OnItemClickListener listViewHandler = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			Object o = coursesListView.getItemAtPosition(position);
			Course course = (Course) o;
			
			
			nextActivity = new Intent(ctx, AddCourseSelectionActivity.class);
			nextActivity.putExtra("com.squattingsasquatches.courseId", course.getId());
			startActivity(nextActivity);
			
		}
	};
}
