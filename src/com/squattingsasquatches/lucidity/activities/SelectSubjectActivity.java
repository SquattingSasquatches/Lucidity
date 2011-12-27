package com.squattingsasquatches.lucidity.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.squattingsasquatches.lucidity.InternalReceiver;
import com.squattingsasquatches.lucidity.ListAdapter;
import com.squattingsasquatches.lucidity.R;
import com.squattingsasquatches.lucidity.R.id;
import com.squattingsasquatches.lucidity.R.layout;
import com.squattingsasquatches.lucidity.objects.Subject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class SelectSubjectActivity extends LucidityActivity {

	/* UI */
	private ProgressDialog loading;
	private ListView subjectsListView;
	
	/* Misc */
	private ArrayList<Subject> uniSubjects;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generic_list);
		
		uniSubjects = new ArrayList<Subject>();
		subjectsListView = (ListView) findViewById(R.id.ListContainer);
        loading = new ProgressDialog(this);
        
        TextView txtHeading = (TextView) findViewById(R.id.txtHeading);
        txtHeading.setText("Choose a Subject");
        
        loading.setTitle("Please wait");
        loading.setMessage("Loading available subjects... ");
        loading.setCancelable(false);
        loading.show();
        
        InternalReceiver uniSubjectsView = new InternalReceiver(){
			@Override
			public void update( JSONArray data ){
				SelectSubjectActivity.this.loadSubjectsCallback( data );
			}
		};
		uniSubjectsView.addParam("uni_id", localDB.getUserUniId());
        
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
		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			Object o = subjectsListView.getItemAtPosition(position);
			Subject subject = (Subject) o;
			
			nextActivity = new Intent(SelectSubjectActivity.this, SelectCourseActivity.class);
			nextActivity.putExtra("subjectId", subject.getId());
			nextActivity.putExtra("subjectPrefix", subject.getPrefix());
			startActivity(nextActivity);
		}
	};

}
