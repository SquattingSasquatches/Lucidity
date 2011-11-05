package com.squattingsasquatches;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class CourseMenuStudent extends Activity {
	
	private ListView coursesListView;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_menu_student);
        
        coursesListView = (ListView) findViewById(R.id.coursesListView);
        
        // populate coursesListView with courses
        // "Add a Course" as last view
	}
	
}
