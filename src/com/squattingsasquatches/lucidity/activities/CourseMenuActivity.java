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

import com.squattingsasquatches.lucidity.InternalReceiver;
import com.squattingsasquatches.lucidity.ListAdapter;
import com.squattingsasquatches.lucidity.R;
import com.squattingsasquatches.lucidity.objects.Course;
import com.squattingsasquatches.lucidity.objects.Section;
import com.squattingsasquatches.lucidity.objects.Subject;
import com.squattingsasquatches.lucidity.objects.User;

public class CourseMenuActivity extends LucidityActivity {

	/* UI */
	private ProgressDialog loading;
	private ListView coursesListView;

	/* Misc */
	private ArrayList<Section> userSections;
	private int userId;
	private boolean updateCourses;

	InternalReceiver getCourses;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generic_list);

		updateCourses = getIntent().getBooleanExtra("updateCourses", false);

		userSections = new ArrayList<Section>();
		coursesListView = (ListView) findViewById(R.id.ListContainer);
		loading = new ProgressDialog(this);

		userId = getIntent().getIntExtra("userId", -1);

		// Receivers
		getCourses = new InternalReceiver() {
			@Override
			public void update(JSONArray data) {
				CourseMenuActivity.this.displayCourses(data);
			}
		};
		getCourses.addParam("user_id", userId);

		remoteDB.addReceiver("user.courses.view", getCourses);

		loading.setTitle("Please wait");
		loading.setMessage("Loading your saved courses... ");
		loading.setCancelable(false);
		loading.show();

		if (updateCourses) {
			remoteDB.execute("user.courses.view");
		} else {
			displayCourses(new JSONArray(Section.getAll()));
		}
	}

	public void attachCourseOnClickListener() {
		userSections.add(new Section(-1, "Add a Course"));
		coursesListView
				.setAdapter(new ListAdapter<Section>(this, userSections));
		coursesListView.setOnItemClickListener(listViewHandler);
		loading.dismiss();
	}

	public void displayCourses(JSONArray data) {
		userSections.clear();

		int resultLength = data.length();

		for (int i = 0; i < resultLength; ++i) {
			try {
				JSONObject section = data.getJSONObject(i);

				userSections
						.add(new Section(
								section.getInt("section_id"),
								section.getString("section_number"),
								new Course(
										section.getInt(Section.Keys.courseId),
										section.getInt(Section.Keys.courseNumber),
										new Subject(
												section.getString(Section.Keys.subjectPrefix))),
								new User(
										section.getInt(Section.Keys.professorId),
										section.getString(Section.Keys.professorName)),
								section.getString(Section.Keys.days), section
										.getString(Section.Keys.location),
								section.getString(Section.Keys.startTime),
								section.getString(Section.Keys.endTime),
								section.getInt("is_verified"), section
										.getInt(Section.Keys.checkedIn)));
			} catch (JSONException e) {
				Log.d("getCoursesCallback", "JSON error");
				e.printStackTrace();
			}
		}

		if (updateCourses && resultLength > 0)
			Section.insert(userSections);

		attachCourseOnClickListener();
	}

	private final OnItemClickListener listViewHandler = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			Object o = coursesListView.getItemAtPosition(position);
			Section section = (Section) o;

			switch (section.getId()) {
			case -1:
				// Add a Course
				// Start SubjectsActivity
				nextActivity = new Intent(CourseMenuActivity.this,
						SelectSubjectActivity.class);
				nextActivity.putExtra("userId", userId);
				startActivity(nextActivity);
				break;
			default:
				// load selected course and start CourseHome activity
				nextActivity = new Intent(CourseMenuActivity.this,
						CourseHomeActivity.class);
				nextActivity.putExtra("sectionId", section.getId());
				startActivity(nextActivity);
				break;
			}
		}
	};
}
