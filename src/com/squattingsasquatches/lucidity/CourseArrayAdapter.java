package com.squattingsasquatches.lucidity;

import java.util.ArrayList;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

public class CourseArrayAdapter extends ArrayAdapter<Course> implements Filterable {

	private ArrayList<Course> allCourses, matchingCourses;
	
	@SuppressWarnings("unchecked")
	public CourseArrayAdapter(Context ctx, int viewId, ArrayList<Course> allCourses) {
		super(ctx, viewId, allCourses);
		this.allCourses = allCourses;
		this.matchingCourses = (ArrayList<Course>) allCourses.clone();
	}

	@Override
    public int getCount() {
        return allCourses.size();
    }

    @Override
    public Course getItem(int i) {
        return allCourses.get(i);
    }
    
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
        	@Override
            public String convertResultToString(Object resultValue) {
            	return ((Course)(resultValue)).toString();
            }
        	
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
            	if (constraint != null) {
					ArrayList<Course> tmpAllData = allCourses;
					ArrayList<Course> tmpDataShown = matchingCourses;
					 
					tmpDataShown.clear();  
					
					for (int i = 0; i < tmpAllData.size(); i++)	{
						if (tmpAllData.get(i).toString().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
							tmpDataShown.add(tmpAllData.get(i));
						}
					}

					FilterResults filterResults = new FilterResults();
					filterResults.values = tmpDataShown;
					filterResults.count = tmpDataShown.size();
					return filterResults;
                } else {
                	return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence contraint, FilterResults results) {
                if (results != null && results.count > 0) {
                	notifyDataSetChanged();
                }
            }
        };
        
        return filter;
    }
}
