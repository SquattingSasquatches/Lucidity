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

public class ListAdapter<E extends DataItem> extends BaseAdapter {
	
	 private ArrayList<E> items;
	 
	 private LayoutInflater mInflater;

	 public ListAdapter(Context context, ArrayList<E> items) {
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
			 convertView = mInflater.inflate(R.layout.simple_list_item, null);
			 holder = new ViewHolder();
			 holder.txtName = (TextView) convertView.findViewById(R.id.txtItemName);
			 holder.imgIcon = (ImageView) convertView.findViewById(R.id.imgAction);
			 
			 //If this is the last item in the list, change the arrow to a plus. (Add a Course)
			 if (items.get(position).getId() == -1)
				 holder.imgIcon.setImageResource(R.drawable.plus);
			 
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