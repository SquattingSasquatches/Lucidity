package com.squattingsasquatches.lucidity.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.squattingsasquatches.lucidity.InternalReceiver;
import com.squattingsasquatches.lucidity.ListAdapter;
import com.squattingsasquatches.lucidity.R;
import com.squattingsasquatches.lucidity.objects.Course;
import com.squattingsasquatches.lucidity.objects.Subject;
import com.squattingsasquatches.lucidity.objects.University;
import com.squattingsasquatches.lucidity.objects.User;

public class SelectCourseActivity extends LucidityActivity {

	/* UI */
	private ProgressDialog loading;
	private ListView coursesListView;

	/* Misc */
	private ArrayList<Course> subjectCourses;
	private int subjectId;
	private String subjectPrefix;
	private int uniId;
	private InternalReceiver subjectsCoursesView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generic_list);

		subjectCourses = new ArrayList<Course>();
		coursesListView = (ListView) findViewById(R.id.ListContainer);
		loading = new ProgressDialog(this);

		subjectId = getIntent().getIntExtra("subjectId", -1);
		subjectPrefix = getIntent().getStringExtra("subjectPrefix");
		uniId = User.getStoredUniversityId();

		TextView txtHeading = (TextView) findViewById(R.id.txtHeading);
		txtHeading.setText("Choose a Course");

		loading.setTitle("Please wait");
		loading.setMessage("Loading available courses... ");
		loading.setCancelable(false);
		loading.show();

		subjectsCoursesView = new InternalReceiver() {
			@Override
			public void update(JSONArray data) {
				SelectCourseActivity.this.displayCourses(data);
			}
		};
		subjectsCoursesView.addParam("uni_id", uniId);
		subjectsCoursesView.addParam("subject_id", subjectId);

		Log.i("sel", uniId + " | " + subjectId);

		remoteDB.addReceiver("uni.courses.view", subjectsCoursesView);
		remoteDB.execute("uni.courses.view");
	}

	public void displayCourses(JSONArray data) {
		subjectCourses.clear();

		int resultLength = data.length();

		for (int i = 0; i < resultLength; ++i) {
			try {
				JSONObject course = data.getJSONObject(i);
				subjectCourses.add(new Course(course.getInt("course_id"),
						course.getInt("course_number"), course
								.getString("course_name"), new Subject(
								subjectId, subjectPrefix),
						new University(uniId)));
			} catch (JSONException e) {
				Log.d("getCoursesCallback", "JSON error");
			}
		}

		coursesListView
				.setAdapter(new ListAdapter<Course>(this, subjectCourses));
		coursesListView.setOnItemClickListener(listViewHandler);

		loading.dismiss();
	}

	private final OnItemClickListener listViewHandler = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			Object o = coursesListView.getItemAtPosition(position);
			Course course = (Course) o;

			nextActivity = new Intent(SelectCourseActivity.this,
					SelectSectionActivity.class);
			nextActivity.putExtra("courseId", course.getId());
			startActivity(nextActivity);
		}
	};
}
