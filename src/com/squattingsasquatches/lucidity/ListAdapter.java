package com.squattingsasquatches.lucidity;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squattingsasquatches.lucidity.objects.DataItem;

public class ListAdapter<E extends DataItem> extends BaseAdapter {

	static class ViewHolder {
		ImageView imgIcon;
		TextView txtName;
	}

	protected int itemLayout;
	protected ArrayList<E> items;

	protected LayoutInflater mInflater;

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
		return this.items.size();
	}

	@Override
	public E getItem(int position) {
		return this.items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = this.mInflater.inflate(this.itemLayout, null);
			holder = new ViewHolder();
			holder.txtName = (TextView) convertView
					.findViewById(R.id.txtItemName);
			holder.imgIcon = (ImageView) convertView
					.findViewById(R.id.imgAction);

			// If this item has id -1, change the arrow to a plus. (Add a
			// Course)
			if (this.items.get(position).getId() == -1)
				holder.imgIcon.setImageResource(R.drawable.plus);
			else if (this.items.get(position).getId() == -2)
				holder.imgIcon.setVisibility(View.GONE);

			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		holder.txtName.setText(this.items.get(position).toString());

		return convertView;
	}
}