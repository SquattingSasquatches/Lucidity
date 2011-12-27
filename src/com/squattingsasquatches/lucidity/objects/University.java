package com.squattingsasquatches.lucidity.objects;

import com.squattingsasquatches.lucidity.LucidityDatabase;
import com.squattingsasquatches.lucidity.activities.LucidityActivity;
import android.content.ContentValues;


public class University extends DataItem {
	
	private int manualFlag = 0;
	
	public University() {
		this(-1);
	}
	
	public University(int id) {
		this(id, "");
	}
	
	public University(int id, String name) {
		super(id, name);
	}
	
	public University(int id, String name, int manualFlag) {
		super(id, name);
		this.manualFlag = manualFlag;
	}
	public int getManualFlag()
	{
		return this.manualFlag;
	}
	public void setManualFlag( int flag )
	{
		this.manualFlag = flag;
	}
	public void getUniversityById(int id)
	{
		LucidityDatabase.db.query(tableName, new String[]{Keys.id}, "id = ?", null, null, null, null);
	}
	public void getAllUniversities()
	{
		LucidityDatabase.db.query(tableName, null, null, null, null, null, null);
	}
	public void insertUniversity()
	{
		ContentValues values = new ContentValues();
		LucidityDatabase.db.insert(tableName, null, values);
	}
	public static final class Keys
	{
		public static final String id 				= 	"id";
		public static final String name 			= 	"name";
		public static final String manualFlag 		= 	"manualFlag";
	}

	public static final String tableName = "universities";
	
	public static final String schema = tableName + 
			" (" + 	Keys.id + " INTEGER not null, " +
					Keys.name + " TEXT not null, " +
					Keys.manualFlag + " INTEGER not null); ";
	
}
