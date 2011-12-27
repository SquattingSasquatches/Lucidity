package com.squattingsasquatches.lucidity;

import java.util.ArrayList;

import com.squattingsasquatches.lucidity.objects.ExtendedDataItem;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ExtendedListAdapter<E extends ExtendedDataItem> extends ListAdapter<E> {
	
	public ExtendedListAdapter(Context context, ArrayList<E> items) {
		super(context, items, R.layout.extended_list_item);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(itemLayout, null);
			holder = new ViewHolder();
			holder.txtName = (TextView) convertView.findViewById(R.id.txtItemName);
			holder.txtItemInfo1 = (TextView) convertView.findViewById(R.id.txtItemInfoLine1);
			holder.txtItemInfo2 = (TextView) convertView.findViewById(R.id.txtItemInfoLine2);
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
		holder.txtItemInfo1.setText(items.get(position).getItemInfo1());
		holder.txtItemInfo2.setText(items.get(position).getItemInfo2());

		return convertView;
	}

	static class ViewHolder {
		TextView txtName;
		TextView txtItemInfo1;
		TextView txtItemInfo2;
		ImageView imgIcon;
	}
}