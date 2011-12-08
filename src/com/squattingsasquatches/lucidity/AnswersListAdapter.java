package com.squattingsasquatches.lucidity;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AnswersListAdapter<E extends DataItem> extends BaseAdapter {
	
	 protected ArrayList<E> items;
	 protected LayoutInflater mInflater;

	 public AnswersListAdapter(Context context, ArrayList<E> items) {
		 this.items = items;
		 this.mInflater = LayoutInflater.from(context);
	 }

	 public int getCount() {
		 return items.size();
	 }

	 public E getItem(int position) {
		 return items.get(position);
	 }

	 public long getItemId(int position) {
		 return position;
	 }

	 public View getView(int position, View convertView, ViewGroup parent) {
		 ViewHolder holder;
		 if (convertView == null) {
			 convertView = mInflater.inflate(R.layout.answers_list_item, null);
			 holder = new ViewHolder();
			 holder.txtName = (TextView) convertView.findViewById(R.id.txtItemName);
			 holder.imgIcon = (ImageView) convertView.findViewById(R.id.imgAction);
			 
			 //If this item has id -1, change the arrow to a plus. (Add a Course)
			 if (items.get(position).getId() == -1)
				 holder.imgIcon.setImageResource(R.drawable.plus);
			 if (items.get(position).getId() == -2)
				 holder.imgIcon.setVisibility(View.GONE);
			 
			 convertView.setTag(holder);
		 } else {
			 holder = (ViewHolder) convertView.getTag();
		 }
	  
		 holder.txtName.setText(items.get(position).toString());

		 return convertView;
	}

	static class ViewHolder {
		TextView txtName;
		ImageView imgIcon;
	}
}