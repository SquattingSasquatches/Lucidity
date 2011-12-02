package com.squattingsasquatches.lucidity;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/* could possibly make this generic by replacing Subject with a generic and .getPrefix with a .toString() method */

public class SubjectListAdapter extends BaseAdapter {
	
	 private ArrayList<Subject> subjects;
	 
	 private LayoutInflater mInflater;

	 public SubjectListAdapter(Context context, ArrayList<Subject> subjects) {
		 this.subjects = subjects;
		 this.mInflater = LayoutInflater.from(context);
	 }

	 public int getCount() {
		 return subjects.size();
	 }

	 public Subject getItem(int position) {
		 return subjects.get(position);
	 }

	 public long getItemId(int position) {
		 return position;
	 }

	 public View getView(int position, View convertView, ViewGroup parent) {
		 ViewHolder holder;
		 if (convertView == null) {
			 convertView = mInflater.inflate(R.layout.simple_list_item, null);
			 holder = new ViewHolder();
			 holder.txtSubjectPrefix = (TextView) convertView.findViewById(R.id.txtItemName);
			 holder.imgAction = (ImageView) convertView.findViewById(R.id.imgAction);
			 
			 //If this is the last item in the list, change the arrow to a plus. (Add a Course)
			 if (getCount() == (getItemId(position) + 1))
				 holder.imgAction.setImageResource(R.drawable.plus);
			 
			 convertView.setTag(holder);
		 } else {
			 holder = (ViewHolder) convertView.getTag();
		 }
	  
		 holder.txtSubjectPrefix.setText(subjects.get(position).getPrefix());

		 return convertView;
	}

	static class ViewHolder {
		TextView txtSubjectPrefix;
		ImageView imgAction;
	}
}