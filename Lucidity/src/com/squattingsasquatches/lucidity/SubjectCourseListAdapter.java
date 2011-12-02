package com.squattingsasquatches.lucidity;

import java.util.ArrayList;

import com.squattingsasquatches.lucidity.CourseListAdapter.ViewHolder;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class SubjectCourseListAdapter implements ListAdapter {
	
	private ArrayList<Course> courses = new ArrayList<Course>();

	private LayoutInflater mInflater;
	 
	public SubjectCourseListAdapter(Context context, ArrayList<Course> courses) {
		 this.courses = courses;
		 this.mInflater = LayoutInflater.from(context);
	 }
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getItemViewType(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		 if (convertView == null) {
			 convertView = mInflater.inflate(R.layout.simple_list_item, null);
			 holder = new ViewHolder();
			 holder.txtCourseName = (TextView) convertView.findViewById(R.id.txtItemName);
			 holder.imgAction = (ImageView) convertView.findViewById(R.id.imgAction);
			 
			 //If this is the last item in the list, change the arrow to a plus. (Add a Course)
			 if (getCount() == (getItemId(position) + 1))
				 holder.imgAction.setImageResource(R.drawable.plus);
			 
			 convertView.setTag(holder);
		 } else {
			 holder = (ViewHolder) convertView.getTag();
		 }
	  
		 holder.txtCourseName.setText(courses.get(position).getName() + " " + courses.get(position).getCourseNum());

		 return convertView;
	}

	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return false;
	}

}
