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

public class CourseMenuStudent extends Activity implements RemoteResultReceiver.Receiver {
	
	/* DBs */
	private RemoteDBAdapter remoteDB;
	private LocalDBAdapter localDB;
	
	/* UI */
	private ProgressDialog loading;
	private ListView coursesListView;
	
	/* Misc */
	private Intent nextActivity;
	private ArrayList<Course> userCourses;
	private int userId;
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
        
        boolean updateCourses = getIntent().getExtras().getBoolean("updateCourses", false);
        
        ctx = this;
        userCourses = new ArrayList<Course>();
        coursesListView = (ListView) findViewById(R.id.ListContainer);
        loading = new ProgressDialog(this);
        localDB = new LocalDBAdapter(this).open();
        remoteDB = new RemoteDBAdapter(this);
        remoteDB.setReceiver(this);
        userId = getIntent().getIntExtra("com.squattingsasquatches.userId", -1);
        
        loading.setTitle("Please wait");
        loading.setMessage("Loading your saved courses... ");
        loading.setCancelable(false);
        loading.show();
        
        if (updateCourses) {
	        remoteDB.setAction("user.courses.view");
	        remoteDB.addParam("user_id", localDB.getUserId());
	        remoteDB.execute(Codes.GET_USER_COURSES);
        } else {
        	getCoursesCallback(localDB.getCourses());
        }
	}
	
	public void attachCourseOnClickListener() {
		userCourses.add(new Course(0, "Add Courses"));
		coursesListView.setAdapter(new CourseListAdapter(this, userCourses));
		coursesListView.setOnItemClickListener(listViewHandler);
		loading.dismiss();
	}
	
	public void getCoursesCallback(JSONArray result) {
		// populate coursesListView with courses
		userCourses.clear();
		
		int resultLength = result.length();
		
		for (int i = 0; i < resultLength; ++i) {
			try {
				JSONObject course = result.getJSONObject(i);
				userCourses.add(new Course(course.getInt(LocalDBAdapter.KEY_ID),
										course.getInt(LocalDBAdapter.KEY_COURSE_NUMBER),
										course.getString(LocalDBAdapter.KEY_NAME),
										new Date(course.getString(LocalDBAdapter.KEY_START_DATE)),
										new Date(course.getString(LocalDBAdapter.KEY_END_DATE)),
										new Subject(course.getInt(LocalDBAdapter.KEY_SUBJECT_ID)),
										new University(course.getInt(LocalDBAdapter.KEY_UNI_ID))));
			} catch (JSONException e) {
				Log.d("getCoursesCallback", "JSON error");
			}
		}
		
		attachCourseOnClickListener();
	}
	
	/* calls the designated callback */
    public void doCallback(int callbackCode, JSONArray result) {
    	if (callbackCode == Codes.GET_USER_COURSES)
    		getCoursesCallback(result);
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
			Object o = coursesListView.getItemAtPosition(position);
			Course course = (Course) o;
			
			switch (course.getId()) {
				case -1:
					// reload courses
					loading.show();
					userCourses.clear();
					remoteDB.execute();
					break;
				case 0:
					// Add a Course
					// Start SubjectsActivity
					nextActivity = new Intent(ctx, SubjectsActivity.class);
					nextActivity.putExtra("com.squattingsasquatches.userId", userId);
					startActivity(nextActivity);
					break;
				default:
					// load selected course and start Course activity
					break;
			}
		}
	};
	
	/* NOT NEEDED FOR THIS ACTIVITY ANYMORE BUT COULD STILL USE IT TO VALIDATE UNIVERSITIES IN SPLASH
	class ValidateStarter implements OnFocusChangeListener {
		
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                ((AutoCompleteTextView) v).performValidation();
            }
        }
        
    };
	
	class ACValidator implements AutoCompleteTextView.Validator {

		public CharSequence fixText(CharSequence invalidText) {
			return "";
		}

		public boolean isValid(CharSequence text) {
			String courseTitle = text.toString();
			Iterator<Course> it = availableCourses.iterator();
			
			// Check that the user hasn't already entered this course
			int count = 0;
			
			for (int i = 0; i < 3; ++i) {
	    		if (courseTitle.equals(acNewCourses.get(i).getText().toString()))
	    			++count;
	    		
	    		if (count > 1) return false; // 1 for the text just entered
			}
			
			// Check entered course against valid courses for the user's university	
			while (it.hasNext()) {
				if (((Course) it.next()).equals(courseTitle.toString()))
					return true;
			}
			
			return false; // Course doesn't exist
		}
		
	};*/
}
