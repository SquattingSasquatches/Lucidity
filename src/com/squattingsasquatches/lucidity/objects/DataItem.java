package com.squattingsasquatches.lucidity.objects;

import java.util.ArrayList;

import com.squattingsasquatches.lucidity.LucidityDatabase;
import com.squattingsasquatches.lucidity.objects.University.Keys;

import android.database.Cursor;

public class DataItem {
	
	protected int id;
	protected String name;
	public String tableName;
	
	public DataItem(int id, String name) {
		this.setId(id);
		this.setName(name);
	}

	public DataItem(Cursor result)
	{
		this.id = result.getInt(0);
		this.name = result.getString(1) ;
	}
	private int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	@Override
	public String toString() {
		return getName();
	}

}
