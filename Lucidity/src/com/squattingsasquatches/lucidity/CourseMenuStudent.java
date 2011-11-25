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
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class CourseMenuStudent extends Activity implements RemoteResultReceiver.Receiver {
	
	/* DBs */
	private RemoteDBAdapter remoteDB;
	private LocalDBAdapter localDB;
	
	/* UI */
	private ProgressDialog loading;
	private Dialog addCourse;
	private Button btnSaveCourses;
	private Button btnAddCourse;
	private LinearLayout newCourses;
	private ListView coursesListView;
	private ArrayList<AutoCompleteTextView> acNewCourses;
	
	/* Misc */
	private CourseArrayAdapter courseAdapter;
	private ArrayList<Course> courses;
	private Context ctx;
	private int userId;	
	
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
        acNewCourses = new ArrayList<AutoCompleteTextView>();
        loading = new ProgressDialog(this);
        localDB = new LocalDBAdapter(this).open();
        remoteDB = new RemoteDBAdapter(this);
        remoteDB.setReceiver(this);
        userId = getIntent().getIntExtra("com.squattingsasquatches.userId", -1);
        
        loading.setTitle("Please wait");
        loading.setMessage("Loading your saved courses... ");
        loading.setOnCancelListener(dialogCancelListener);
        loading.show();
        
        if (updateCourses) {
	        remoteDB.setAction("user.courses.view");
	        remoteDB.addParam("user_id", localDB.getUserId());
	        remoteDB.execute(Codes.GET_USER_COURSES);
        } else {
        	getCoursesCallback(localDB.getCourses());
        }
	}
	
	public void getCoursesCallback(JSONArray result) {
		// populate coursesListView with courses
		loading.dismiss();
		courses.clear();
		
		int resultLength = result.length();
		
		for (int i = 0; i < resultLength; ++i) {
			try {
				JSONObject course = result.getJSONObject(i);
				courses.add(new Course(course.getInt("id"),
										course.getInt("course_num"),
										course.getString("name"),
										new Date(course.getString("start_date")),
										new Date(course.getString("end_date")),
										new Subject(course.getInt("subject_id")),
										new University(course.getInt("uni_id"))));
			} catch (JSONException e) {
				Log.d("getCoursesCallback", "JSON error");
			}
		}
		
		initializeCourseOnClickListener();
	}
	
	public void initializeCourseOnClickListener() {
		courses.add(new Course(0, "Add Courses"));
		coursesListView.setAdapter(new CourseListAdapter(this, courses));
		coursesListView.setOnItemClickListener(listViewHandler);
	}
	
	public void loadCoursesCallback(JSONArray result) {
    	JSONObject courseJSON;
    	ArrayList<Course> courses = new ArrayList<Course>();
    	
    	
    	int resultLength = result.length();
    	
    	addCourse = new Dialog(ctx, R.style.AddCourseTheme);
    	addCourse.setContentView(R.layout.add_course);
    	
    	newCourses = (LinearLayout) addCourse.findViewById(R.id.newCourses);
    	btnAddCourse = (Button) addCourse.findViewById(R.id.btnAddCourse);
    	btnSaveCourses = (Button) addCourse.findViewById(R.id.btnSaveNewCourses);
    	
    	// Add the default 3 ACTV's to our ArrayList
    	for (int i = 0; i < 3; ++i)    	
    		acNewCourses.add((AutoCompleteTextView) newCourses.getChildAt(i));
    	
    	// Populate 'courses' with the courses available at the user's university
    	for (int i = 0; i < resultLength; ++i) {
    		try {
    			courseJSON = result.getJSONObject(i);
    			courses.add(new Course(courseJSON.getInt("course_id"),
										courseJSON.getInt("course_number"),
										new Subject(courseJSON.getInt("subject_id"), courseJSON.getString("subject_name"))));
			} catch (JSONException e) {
				Log.d("loadCourses", "error loading courses");
			}
    	}
    	
    	courseAdapter = new CourseArrayAdapter(this, R.layout.ac_list_item, courses);
    	
    	for (int i = 0; i < 3; ++i) {
    		acNewCourses.get(i).setAdapter(courseAdapter);
    		acNewCourses.get(i).setOnItemClickListener(courseNumListener);
    	}
    	
    	btnAddCourse.setOnClickListener(addCourseTxtViewListener);
    	btnSaveCourses.setOnClickListener(saveCoursesListener);
    	
		addCourse.show();
		addCourse.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
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
	
	private final OnItemClickListener courseNumListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> a, View v, int position, long id) {
			Log.d("selected", ((Course) a.getItemAtPosition(position)).getId() +"");
			
		}
	};
	
	private final OnClickListener addCourseTxtViewListener = new OnClickListener() {
		public void onClick(View v) {
			if (acNewCourses.size() < 5) {
				LayoutParams layout = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				AutoCompleteTextView acNewCourse = new AutoCompleteTextView(ctx);
				
				acNewCourse.setLayoutParams(layout);
				acNewCourse.setHint("...");
				acNewCourse.setThreshold(1);
				acNewCourse.setAdapter(courseAdapter);
				acNewCourse.setOnItemClickListener(courseNumListener);
				
				acNewCourses.add(acNewCourse);
				
				newCourses.addView(acNewCourse);
			} else {
				Toast.makeText(ctx, "You can only add 5 courses at a time.", Toast.LENGTH_LONG).show(); // Just because
			}
		}
	};
	
	private final OnClickListener saveCoursesListener = new OnClickListener() {
		public void onClick(View v) {
			loading.setMessage("Saving selected course...");
			addCourse.dismiss();
			loading.show();
			
			remoteDB.setAction("user.course.add");
			remoteDB.addParam(Codes.KEY_USER_ID, userId);
			//remoteDB.addParam(Codes.KEY_COURSE_ID, v.get)
			
			loading.dismiss();
		}
	};
	
	private final OnItemClickListener listViewHandler = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> a, View v, int position, long id) {
			Object o = coursesListView.getItemAtPosition(position);
			Course course = (Course) o;
			
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
					loading.setMessage("Loading available courses...");
					loading.show();
					remoteDB.setAction("uni.courses.view");
					Log.d("uni_id", userId + "");
					remoteDB.addParam("uni_id", localDB.getUserUniId());
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
			if (remoteDB.stopService()) {
				courses.clear();
				courses.add(new Course(-1, "Load My Courses"));
				initializeCourseOnClickListener();
			}
		}
	};
}
