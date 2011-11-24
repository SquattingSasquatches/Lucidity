package com.squattingsasquatches.lucidity;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

public class CourseMenuStudent extends Activity implements RemoteResultReceiver.Receiver {
	
	private ListView coursesListView;
	private RemoteDBAdapter remoteDB;
	private LocalDBAdapter localDB;
	private String deviceID;
	private ArrayList<Course> courses;
	private ProgressDialog loading;
	private Context ctx;
	
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
        
        ctx = this;
        
        boolean updateCourses = getIntent().getExtras().getBoolean("updateCourses", false);
        
        courses = new ArrayList<Course>();
        coursesListView = (ListView) findViewById(R.id.coursesListView);
        loading = new ProgressDialog(this);
        localDB = new LocalDBAdapter(this);
        remoteDB = new RemoteDBAdapter(this);
        remoteDB.setReceiver(this);
        deviceID = getIntent().getStringExtra("com.squattingsasquatches.deviceID");
        
        loading.setTitle("Please wait");
        loading.setMessage("Loading your courses... ");
        loading.setOnCancelListener(dialogCancelListener);
        loading.show();
        
        if (updateCourses) {
	        remoteDB.setAction("user.courses.view");
	        remoteDB.addParam("device_id", deviceID);
	        remoteDB.execute(Codes.GET_USER_COURSES);
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
	
	public void loadCoursesCallback(JSONArray result) {
		Dialog addCourse = new Dialog(ctx, R.style.AddCourseTheme);		
    	ArrayAdapter<String> adapter;
    	ArrayList<String> subjects = new ArrayList<String>();
    	ArrayList<String> courseNums = new ArrayList<String>();
    	JSONObject course;
    	int resultLength = result.length();
    	
    	/* TODO: view available courses remote query
    	 * 
    	 * select subject_id from uni_subjects where uni_id = $uni_id
    	 * foreach $subject_id : 
    	 * 		select short_name from subjects where id = $SID
    	 * 		select course_number, id from courses where subject_id = $SID
    	 * 
    	 * should probably combine into single SQL statement
    	 * 
    	 * example of expected returned JSON ~> {short_name: "MATH", course_number:350, course_id: 23}
    	 */
    	
    	addCourse.setContentView(R.layout.add_course);
    	
    	AutoCompleteTextView acTxtSubject = (AutoCompleteTextView) addCourse.findViewById(R.id.acSubject);
    	AutoCompleteTextView acTxtCourseNum = (AutoCompleteTextView) addCourse.findViewById(R.id.acCourseNumber);
    	
    	for (int i = 0; i < resultLength; ++i) {
    		try {
    			course = result.getJSONObject(i);
    			subjects.add(course.getString("short_name"));
    			courseNums.add(course.getString("course_number"));
    			courseNums.add(course.getString("course_id"));
			} catch (JSONException e) {
				Log.d("loadUniversities", "error loading univerisites");
			}
    	}
    	
    	adapter = new ArrayAdapter<String>(this, R.layout.ac_list_item, subjects.toArray(new String[subjects.size()]));
    	acTxtSubject.setAdapter(adapter);
    	
    	adapter = new ArrayAdapter<String>(this, R.layout.ac_list_item, courseNums.toArray(new String[courseNums.size()]));
    	acTxtCourseNum.setAdapter(adapter);
    	
		addCourse.show();
    	loading.dismiss();
    }
	
	/* calls the designated callback */
    public void doCallback(int callbackCode, JSONArray result) {
    	if (callbackCode == Codes.GET_USER_COURSES)
    		getCoursesCallback(result);
    	else if (callbackCode == Codes.LOAD_COURSES)
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
					// Load possible courses from server
					loading.setMessage("Loading courses...");
					loading.show();
					remoteDB.setAction("subjects.view");
					remoteDB.addParam("uni", localDB.getUserUniId());
					remoteDB.execute(Codes.LOAD_COURSES);
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
