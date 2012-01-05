package com.squattingsasquatches.lucidity;

import java.util.ArrayList;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.squattingsasquatches.lucidity.objects.DataItem;

public class AutoCompleteArrayAdapter<E extends DataItem> extends
		ArrayAdapter<E> implements Filterable {

	private ArrayList<E> matchingItems;
	final private ArrayList<E> allItems;

	@SuppressWarnings("unchecked")
	public AutoCompleteArrayAdapter(Context ctx, int viewId,
			ArrayList<E> matchingItems) {
		super(ctx, viewId, matchingItems);
		this.allItems = (ArrayList<E>) matchingItems.clone();
		this.matchingItems = (ArrayList<E>) matchingItems.clone();
	}

	@Override
	public int getCount() {
		return this.matchingItems.size();
	}

	@Override
	public Filter getFilter() {
		final Filter filter = new Filter() {
			@SuppressWarnings("unchecked")
			@Override
			public String convertResultToString(Object resultValue) {
				return ((E) resultValue).toString();
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				if (constraint != null) {
					final ArrayList<E> tmpAllData = AutoCompleteArrayAdapter.this.allItems;
					final ArrayList<E> tmpDataShown = (ArrayList<E>) AutoCompleteArrayAdapter.this.matchingItems
							.clone();

					tmpDataShown.clear();

					for (int i = 0; i < tmpAllData.size(); i++)
						if (tmpAllData.get(i).toString().toLowerCase()
								.contains(constraint.toString().toLowerCase()))
							tmpDataShown.add(tmpAllData.get(i));

					final FilterResults filterResults = new FilterResults();
					filterResults.values = tmpDataShown;
					filterResults.count = tmpDataShown.size();
					return filterResults;
				} else
					return new FilterResults();
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence contraint,
					FilterResults results) {
				if (results != null && results.count > 0) {
					AutoCompleteArrayAdapter.this.matchingItems = (ArrayList<E>) results.values;
					AutoCompleteArrayAdapter.this.notifyDataSetChanged();
				}
			}
		};

		return filter;
	}

	@Override
	public E getItem(int i) {
		return this.matchingItems.get(i);
	}
}