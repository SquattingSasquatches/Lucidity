package com.squattingsasquatches.lucidity;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
			 convertView = mInflater.inflate(R.layout.simple_list_item, null);
			 holder = new ViewHolder();
			 holder.txtCourseName = (TextView) convertView.findViewById(R.id.txtItemName);
			 holder.imgAction = (ImageView) convertView.findViewById(R.id.imgAction);
			 
			 //If this is the "Add a Course" item, change the arrow to a plus.
			 if (courses.get(position).getId() == 0)
				 holder.imgAction.setImageResource(R.drawable.plus);
			 
			 convertView.setTag(holder);
		 } else {
			 holder = (ViewHolder) convertView.getTag();
		 }
	  
		 holder.txtCourseName.setText(courses.get(position).getName());

		 return convertView;
	}

	static class ViewHolder {
		TextView txtCourseName;
		ImageView imgAction;
	}
}