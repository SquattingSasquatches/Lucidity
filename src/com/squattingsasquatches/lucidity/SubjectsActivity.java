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

public class SubjectsActivity extends Activity {

	/* DBs */
	private RemoteDBAdapter remoteDB;
	private LocalDBAdapter localDB;
	
	/* UI */
	private ProgressDialog loading;
	private ListView subjectsListView;
	
	/* Misc */
	private Intent nextActivity;
	private ArrayList<Subject> uniSubjects;
	private int userId;
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
		uniSubjects = new ArrayList<Subject>();
		subjectsListView = (ListView) findViewById(R.id.ListContainer);
        loading = new ProgressDialog(this);
        localDB = new LocalDBAdapter(this).open();
        remoteDB = new RemoteDBAdapter(this);
        userId = getIntent().getIntExtra("com.squattingsasquatches.userId", -1);
        
        TextView txtHeading = (TextView) findViewById(R.id.txtHeading);
        txtHeading.setText("Choose a Subject");
        
        loading.setTitle("Please wait");
        loading.setMessage("Loading subjects... ");
        loading.setCancelable(false);
        loading.show();
        
        InternalReceiver uniSubjectsView = new InternalReceiver(){
			public void update( JSONArray data ){
				SubjectsActivity.this.loadSubjectsCallback( data );
			}
		};
		uniSubjectsView.params.put("uni_id", Integer.toString(localDB.getUserUniId()));
        
        remoteDB.addReceiver("uni.subjects.view", uniSubjectsView);
        remoteDB.execute("uni.subjects.view");
	}
	
	public void loadSubjectsCallback(JSONArray result) {
		uniSubjects.clear();
		
		int resultLength = result.length();
		
		for (int i = 0; i < resultLength; ++i) {
			try {
				JSONObject subject = result.getJSONObject(i);
				uniSubjects.add(new Subject(
										subject.getInt("subject_id"),
										subject.getString("subject_name"),
										subject.getString("subject_prefix")
										));
			} catch (JSONException e) {
				Log.d("getCoursesCallback", "JSON error");
			}
		}
		
		subjectsListView.setAdapter(new ListAdapter<Subject>(this, uniSubjects));
		subjectsListView.setOnItemClickListener(listViewHandler);
		loading.dismiss();
	}
	
	private final OnItemClickListener listViewHandler = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			Object o = subjectsListView.getItemAtPosition(position);
			Subject subject = (Subject) o;
			
			int selected = subject.getId();
			
			Log.d("uni click", selected+"");
			// load courses with subject
		}
	};

}
