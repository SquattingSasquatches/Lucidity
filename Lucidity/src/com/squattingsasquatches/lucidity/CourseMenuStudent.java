package com.squattingsasquatches.lucidity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class CourseMenuStudent extends Activity implements RemoteResultReceiver.Receiver {
	
	/* DBs */
	private RemoteDBAdapter remoteDB;
	private LocalDBAdapter localDB;
	
	/* UI */
	private ProgressDialog loading;
	private AlertDialog.Builder addCourseBuilder;
	private AlertDialog addCourseDialog;
	private View addCourseDialogLayout;
	private LayoutInflater dialogInflater;
	private LinearLayout newCourses;
	private ListView coursesListView;
	private ArrayList<AutoCompleteTextView> acNewCourses;
	
	/* Misc */
	private CourseArrayAdapter courseAdapter;
	private ArrayList<Course> userCourses;
	private ArrayList<Course> availableCourses;
	private ArrayList<Course> coursesToAdd;
	private int userId;	
	
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
        setContentView(R.layout.course_menu_student);
        
        boolean updateCourses = getIntent().getExtras().getBoolean("updateCourses", false);
        
        userCourses = new ArrayList<Course>();
        availableCourses = new ArrayList<Course>();
        coursesToAdd = new ArrayList<Course>();
        coursesListView = (ListView) findViewById(R.id.coursesListView);
        acNewCourses = new ArrayList<AutoCompleteTextView>();
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
	
	public void initializeCourseOnClickListener() {
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
		
		initializeCourseOnClickListener();
	}
	
	public void loadCoursesCallback(JSONArray result) {
    	JSONObject courseJSON;
    	
    	dialogInflater = getLayoutInflater();
    	addCourseDialogLayout = dialogInflater.inflate(R.layout.add_course, (ViewGroup) findViewById(R.id.addCourseDialog));
    	    	
    	int resultLength = result.length();
    	
    	addCourseBuilder = new AlertDialog.Builder(this);
    	addCourseBuilder
    				.setTitle(R.string.add_courses)
    				.setIcon(R.drawable.plus)
    				.setView(addCourseDialogLayout)
    				.setNegativeButton(R.string.cancel, cancelListener)
    				.setPositiveButton(R.string.save_new_courses, saveCoursesListener);
    	
    	newCourses = (LinearLayout) addCourseDialogLayout.findViewById(R.id.addCourseDialog);
    	
    	// Add the default 3 ACTV's to our ArrayList
    	for (int i = 0; i < 3; ++i)
    		acNewCourses.add((AutoCompleteTextView) newCourses.getChildAt(i));
    	
    	// Populate 'courses' with the courses available at the user's university
    	for (int i = 0; i < resultLength; ++i) {
    		try {
    			courseJSON = result.getJSONObject(i);
    			availableCourses.add(new Course(courseJSON.getInt("course_id"),
										courseJSON.getInt("course_number"),
										new Subject(courseJSON.getInt("subject_id"), courseJSON.getString("subject_name"))));
			} catch (JSONException e) {
				Log.d("loadCourses", "error loading courses");
			}
    	}
    	
    	courseAdapter = new CourseArrayAdapter(this, R.layout.ac_list_item, availableCourses);
    	
    	for (int i = 0; i < 3; ++i) {
    		acNewCourses.get(i).setAdapter(courseAdapter);
    		acNewCourses.get(i).setValidator(new ACValidator());
    		acNewCourses.get(i).setOnFocusChangeListener(new ValidateStarter());
    		acNewCourses.get(i).setOnItemClickListener(itemClickListener);
    	}
    	
    	addCourseDialog = addCourseBuilder.create();
    	addCourseDialog.show();
    	
    	loading.dismiss();
    }
	
	public void addCoursesCallback(JSONArray result) {
		localDB.saveCourseInfo(coursesToAdd);
		getCoursesCallback(localDB.getCourses());
	}	
	
	/* calls the designated callback */
    public void doCallback(int callbackCode, JSONArray result) {
    	if (callbackCode == Codes.GET_USER_COURSES)
    		getCoursesCallback(result);
    	else if (callbackCode == Codes.LOAD_COURSES)
    		loadCoursesCallback(result);
    	else if (callbackCode == Codes.ADD_COURSES)
    		addCoursesCallback(result);
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
	
	private final OnItemClickListener itemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			for (int i = 0; i < 3; ++i) {
				if (acNewCourses.get(i).isFocused()) {
					acNewCourses.get(i).clearFocus();
					if (i >= coursesToAdd.size())
						coursesToAdd.add(availableCourses.get(position));
					else
						coursesToAdd.set(i, availableCourses.get(position));
				}
			}
		}
	};
	
	private final OnClickListener cancelListener = new OnClickListener() {
		public void onClick(DialogInterface dialog, int position) {
			dialog.cancel();
		}
	};
	
	private final OnClickListener saveCoursesListener = new OnClickListener() {
		public void onClick(DialogInterface dialog, int position) {
			loading.setMessage("Saving selected courses...");
			loading.show();
			
			int numAdding = coursesToAdd.size();
			String courseIds = "";
			
			for (int i = 0; i < numAdding; ++i) {
				courseIds += coursesToAdd.get(i).getId() + ",";
			}
			
			remoteDB.setAction("user.course.add");
			remoteDB.addParam(Codes.KEY_USER_ID, userId);
			remoteDB.addParam(Codes.KEY_COURSE_IDS, courseIds);
			remoteDB.execute(Codes.ADD_COURSES);
			//loading.dismiss();
		}
	};
	
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
		
	};
}
