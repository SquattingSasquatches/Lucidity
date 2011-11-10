package com.squattingsasquatches;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CourseListAdapter extends BaseAdapter {
	
	 private ArrayList<Course> courses;
	 
	 private LayoutInflater mInflater;

	 public CourseListAdapter(Context context, ArrayList<Course> courses) {
		 this.courses = courses;
		 this.mInflater = LayoutInflater.from(context);
	 }

	 public int getCount() {
		 return courses.size();
	 }

	 public Course getItem(int position) {
		 return courses.get(position);
	 }

	 public long getItemId(int position) {
		 return position;
	 }

	 public View getView(int position, View convertView, ViewGroup parent) {
		 ViewHolder holder;
		 if (convertView == null) {
			 convertView = mInflater.inflate(R.layout.course_list_item, null);
			 holder = new ViewHolder();
			 holder.txtCourseName = (TextView) convertView.findViewById(R.id.txtCourseName);

			 convertView.setTag(holder);
		 } else {
			 holder = (ViewHolder) convertView.getTag();
		 }
	  
		 holder.txtCourseName.setText(courses.get(position).getName());

		 return convertView;
	}

	static class ViewHolder {
		TextView txtCourseName;
	}
}