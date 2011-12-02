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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CoursesActivity extends Activity implements RemoteResultReceiver.Receiver {
	/* DBs */
	private RemoteDBAdapter remoteDB;
	private LocalDBAdapter localDB;
	
	/* UI */
	private ProgressDialog loading;
	private ListView CoursesListView;
	
	/* Misc */
	private Intent nextActivity;
	private ArrayList<Course> uniCourses;
	private int subjectId;
	private Context ctx;
	
	@Override
	public void onPause() {
		super.onPause();
		if (remoteDB != null)
			remoteDB.unregisterReceiver();
		if (localDB != null)
			localDB.close();
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generic_list);
		
		ctx = this;
		uniCourses = new ArrayList<Course>();
		CoursesListView = (ListView) findViewById(R.id.ListContainer);
        loading = new ProgressDialog(this);
        localDB = new LocalDBAdapter(this).open();
        remoteDB = new RemoteDBAdapter(this);
        remoteDB.setReceiver(this);
        subjectId = getIntent().getIntExtra("com.squattingsasquatches.subjectId", -1);
        
        TextView txtHeading = (TextView) findViewById(R.id.txtHeading);
        txtHeading.setText("Choose a Course");
        
        loading.setTitle("Please wait");
        loading.setMessage("Loading Courses... ");
        loading.setCancelable(false);
        loading.show();
        
        remoteDB.setAction("uni.courses.view");
        remoteDB.addParam("subject_id", subjectId);
        remoteDB.execute(Codes.LOAD_UNI_COURSES);
	}
	
	public void loadCoursesCallback(JSONArray result) {
		uniCourses.clear();
		
		int resultLength = result.length();
		
		for (int i = 0; i < resultLength; ++i) {
			try {
				JSONObject Course = result.getJSONObject(i);
				uniCourses.add(new Course(Course.getInt(Codes.KEY_COURSE_ID), Course.getString(Codes.KEY_COURSE_NUMBER)));
			} catch (JSONException e) {
				Log.d("getCoursesCallback", "JSON error");
			}
		}
		
		CoursesListView.setAdapter(new CourseListAdapter(this, uniCourses));
		CoursesListView.setOnItemClickListener(listViewHandler);
		loading.dismiss();
	}
	
	/* calls the designated callback */
    public void doCallback(int callbackCode, JSONArray result) {
    	if (callbackCode == Codes.LOAD_UNI_COURSES)
    		loadCoursesCallback(result);
    	else
    		Log.d("WTF", "How'd you get here?");
    }
	
	public void onReceiveResult(int resultCode, Bundle resultData) {
		// result from remote PHP query
		Log.i("onReceiveResult", String.valueOf(resultCode));
		
		if (resultCode == Codes.REMOTE_QUERY_COMPLETE) {
			String result = resultData.getString("result");
			int callbackCode = resultData.getInt("callback");
			if (result != null) {
				try {
					doCallback(callbackCode, new JSONArray(result));
				} catch (JSONException e) {
					Log.e("onReceiveResult", "error with JSONArray");
				}
			}
		} else {
			// bads!
		}
	}
	
	private final OnItemClickListener listViewHandler = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			Object o = CoursesListView.getItemAtPosition(position);
			Course Course = (Course) o;
			
			int selected = Course.getId();
			
			Log.d("uni click", selected+"");
			// load courses with Course
		}
	};

}