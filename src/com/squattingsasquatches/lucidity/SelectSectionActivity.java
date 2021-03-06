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
import android.widget.Toast;

public class SelectSectionActivity extends Activity {

	/* DBs */
	private RemoteDBAdapter remoteDB;
	private LocalDBAdapter localDB;
	
	/* UI */
	private ProgressDialog loading;
	private ListView sectionsListView;
	
	/* Misc */
	private Intent nextActivity;
	private ArrayList<Section> courseSections;
	private int courseId;
	
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
		
		/* DB */
		remoteDB = new RemoteDBAdapter(this);
		localDB = new LocalDBAdapter(this).open();
		
		/* UI */
		loading = new ProgressDialog(this);
		loading.setTitle("Please wait");
        loading.setMessage("Loading available subjects... ");
        loading.setCancelable(false);
        loading.show();
        
        TextView txtHeading = (TextView) findViewById(R.id.txtHeading);
        txtHeading.setText("Choose a Section");
        
        /* Misc */
		courseSections = new ArrayList<Section>();
		sectionsListView = (ListView) findViewById(R.id.ListContainer);
		courseId = getIntent().getIntExtra("com.squattingsasquatches.courseId", -1);
        
        InternalReceiver courseSectionsView = new InternalReceiver(){
			public void update( JSONArray data ){
				SelectSectionActivity.this.loadSectionsCallback( data );
			}
		};
		courseSectionsView.addParam("course_id", courseId);
        
        remoteDB.addReceiver("course.sections.view", courseSectionsView);
        remoteDB.execute("course.sections.view");
	}
	
	public void loadSectionsCallback(JSONArray data) {
		courseSections.clear();
		
		int resultLength = data.length();
		
		for (int i = 0; i < resultLength; ++i) {
			try {
				JSONObject section = data.getJSONObject(i);

				courseSections.add(new Section(
										section.getInt("id"),
										section.getString("section_name"),
										new Course(section.getInt("course_id"),
												   section.getInt("course_number"),
												   new Subject(section.getString("subject_prefix"))),
										new User(section.getString("professor_name")),
										section.getString("location"),
										section.getString("days"),
										section.getString("start_time"),
										section.getString("end_time")
										));
			} catch (JSONException e) {
				Log.d("loadSections", "JSON error");
			}
		}
		
		sectionsListView.setAdapter(new ExtendedListAdapter<Section>(this, courseSections));
		sectionsListView.setOnItemClickListener(listViewHandler);
		loading.dismiss();
	}
	
	public void registerSectionCallback(JSONArray data) {
		if (!data.isNull(0)) {
			Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_LONG).show();
			nextActivity = new Intent(SelectSectionActivity.this, CourseMenuActivity.class);
			nextActivity.putExtra("com.squattingsasquatches.userId", localDB.getUserId());
			nextActivity.putExtra("com.squattingsasquatches.updateCourses", true);
			nextActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(nextActivity);
			finish();
		} else {
			Log.e("RegisterSectionCallback", "Error while registering");
		}
	}
	
	private final OnItemClickListener listViewHandler = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			Object o = sectionsListView.getItemAtPosition(position);
			Section section = (Section) o;
			
			//register user with section, return to CourseMenuActivity
			InternalReceiver sectionRegister = new InternalReceiver(){
				public void update( JSONArray data ){
					SelectSectionActivity.this.registerSectionCallback( data );
				}
			};
			sectionRegister.addParam("section_id", section.getId());
			sectionRegister.addParam("user_id", localDB.getUserId());
			
			Log.e("registering", section.getId() + " | " + localDB.getUserId());
	        
	        remoteDB.addReceiver("user.section.register", sectionRegister);
	        remoteDB.execute("user.section.register");
		}
	};

}
