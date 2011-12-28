package com.squattingsasquatches.lucidity.objects;

import java.util.ArrayList;

import com.squattingsasquatches.lucidity.LucidityDatabase;
import android.database.Cursor;
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
	public University(Cursor result)
	{
		super( result.getInt(0), result.getString(1) );
		this.manualFlag = result.getInt(2);
	}
	public int getManualFlag()
	{
		return this.manualFlag;
	}
	public void setManualFlag( int flag )
	{
		this.manualFlag = flag;
	}
	public static University get(int id)
	{
		Cursor result = LucidityDatabase.db.query(tableName, null, "id = ?", new String[]{Keys.id}, null, null, null);
		return new University( result );
	}
	public static ArrayList<University> getAll()
	{
		ArrayList<University> universities = new ArrayList<University>();
		Cursor result = LucidityDatabase.db.query(tableName, null, null, null, null, null, null);
		
		if (result.getCount() == 0)
			return universities;
		
		while (!result.isAfterLast()) {
			universities.add(new University(result));
			result.moveToNext();
		}
		
		return universities;
	}
	public static void update( University university )
	{
		ContentValues values = new ContentValues();
		values.put("id", university.id);
		values.put("name", university.name);
		values.put("manualFlag", university.manualFlag);
		LucidityDatabase.db.update(tableName, values, "id = ?", new String[]{ String.valueOf( university.id )  } );
	}
	public static void insert( University university )
	{
		ContentValues values = new ContentValues();
		values.put("id", university.id);
		values.put("name", university.name);
		values.put("manualFlag", university.manualFlag);
		LucidityDatabase.db.insert(tableName, null, values);
	}
	public static void delete( int id )
	{
		LucidityDatabase.db.delete(tableName, "id = ?", new String[]{Keys.id});
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

	public static String getTablename() {
		return tableName;
	}

	public static String getSchema() {
		return schema;
	}
	
}
