package com.squattingsasquatches.lucidity;

import java.util.ArrayList;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

public class AutoCompleteArrayAdapter<E extends DataItem> extends ArrayAdapter<E> implements Filterable {

	private ArrayList<E> allItems, matchingItems;

	@SuppressWarnings("unchecked")
	public AutoCompleteArrayAdapter(Context ctx, int viewId, ArrayList<E> allItems) {
		super(ctx, viewId, allItems);
		this.allItems = allItems;
		this.matchingItems = (ArrayList<E>) allItems.clone();
	}

	@Override
    public int getCount() {
        return allItems.size();
    }

    @Override
    public E getItem(int i) {
        return allItems.get(i);
    }
    
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
        	@SuppressWarnings("unchecked")
			@Override
            public String convertResultToString(Object resultValue) {
            	return ((E)(resultValue)).toString();
            }
        	
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
            	if (constraint != null) {
					ArrayList<E> tmpAllData = allItems;
					ArrayList<E> tmpDataShown = matchingItems;

					tmpDataShown.clear();  

					for (int i = 0; i < tmpAllData.size(); i++)	{
						if (tmpAllData.get(i).toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
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