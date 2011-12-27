package com.squattingsasquatches.lucidity;

import java.util.ArrayList;

import com.squattingsasquatches.lucidity.objects.DataItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter<E extends DataItem> extends BaseAdapter {
	
	 protected ArrayList<E> items;
	 protected LayoutInflater mInflater;
	 protected int itemLayout;
	 
	 public ListAdapter(Context context, ArrayList<E> items) {
		 this(context, items, R.layout.simple_list_item);
	 }

	 public ListAdapter(Context context, ArrayList<E> items, int itemLayout) {
		 this.items = items;
		 this.mInflater = LayoutInflater.from(context);
		 this.itemLayout = itemLayout;
	 }

	 @Override
	public int getCount() {
		 return items.size();
	 }

	 @Override
	public E getItem(int position) {
		 return items.get(position);
	 }

	 @Override
	public long getItemId(int position) {
		 return position;
	 }

	 @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 ViewHolder holder;
		 if (convertView == null) {
			 convertView = mInflater.inflate(itemLayout, null);
			 holder = new ViewHolder();
			 holder.txtName = (TextView) convertView.findViewById(R.id.txtItemName);
			 holder.imgIcon = (ImageView) convertView.findViewById(R.id.imgAction);
			 
			 //If this item has id -1, change the arrow to a plus. (Add a Course)
			 if (items.get(position).getId() == -1)
				 holder.imgIcon.setImageResource(R.drawable.plus);
			 else if (items.get(position).getId() == -2)
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