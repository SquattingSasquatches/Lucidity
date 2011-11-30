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

public class SubjectsActivity extends Activity implements RemoteResultReceiver.Receiver {

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
			remoteDB.unregisterReceiver();
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
        remoteDB.setReceiver(this);
        userId = getIntent().getIntExtra("com.squattingsasquatches.userId", -1);
        
        TextView txtHeading = (TextView) findViewById(R.id.txtHeading);
        txtHeading.setText("Subjects");
        
        loading.setTitle("Please wait");
        loading.setMessage("Loading subjects... ");
        loading.setCancelable(false);
        loading.show();
        
        remoteDB.setAction("uni.subjects.view");
        remoteDB.addParam("uni_id", localDB.getUserUniId());
        remoteDB.execute(Codes.LOAD_UNI_COURSES);
	}
	
	public void loadSubjectsCallback(JSONArray result) {
		uniSubjects.clear();
		
		int resultLength = result.length();
		
		for (int i = 0; i < resultLength; ++i) {
			try {
				JSONObject subject = result.getJSONObject(i);
				Log.i("WTF", subject.getString(LocalDBAdapter.KEY_SUBJECT_NAME));
				uniSubjects.add(new Subject(subject.getInt(LocalDBAdapter.KEY_ID),
											//subject.getString(LocalDBAdapter.KEY_SUBJECT_NAME),
											subject.getString(LocalDBAdapter.KEY_SUBJECT_PREFIX)
											)
								);
			} catch (JSONException e) {
				Log.d("loadSubjectsCallback", "JSON error");
			}
		}
		subjectsListView.setAdapter(new SubjectListAdapter(this, uniSubjects));
		subjectsListView.setOnItemClickListener(listViewHandler);
		loading.dismiss();
	}
	
	/* calls the designated callback */
    public void doCallback(int callbackCode, JSONArray result) {
    	if (callbackCode == Codes.LOAD_UNI_COURSES)
    		loadSubjectsCallback(result);
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
			Object o = subjectsListView.getItemAtPosition(position);
			Subject subject = (Subject) o;
			
			int selected = subject.getId();
			
			Log.d("uni click", selected+"");
			// load courses with subject
		}
	};

}
