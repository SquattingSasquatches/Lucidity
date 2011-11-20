package com.squattingsasquatches.lucidity;

import org.json.JSONArray;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalDBAdapter {
	
	// db table and key constants (maybe move to Codes.java)
	private static final String DB_NAME = "lucidity";
	private static final String USER_TABLE = "user";
	private static final String COURSES_TABLE = "courses";
	private static final String KEY_ID = "_id";
	private static final String KEY_NAME = "name";
	private static final String KEY_C2DM_ID = "c2dm_id";
	private static final String KEY_UNI_ID = "uni_id";
	private static final String KEY_START_DATE = "start_date";
	private static final String KEY_END_DATE = "end_date";
	
	private Context ctx;
	private SQLiteDatabase db;
	private DBHelper dbHelper;
	
	public LocalDBAdapter(Context ctx) {
		this.ctx = ctx;
	}

	public LocalDBAdapter open() throws SQLException {
		dbHelper = new DBHelper(ctx);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}
	
	// save user data to local DB
	public long saveUserData(int id, String name, String c2dm_id) {
		ContentValues userData = new ContentValues();
		userData.put(KEY_ID, id);
		userData.put(KEY_NAME, name);
		userData.put(KEY_C2DM_ID, c2dm_id);
		return db.insert(USER_TABLE, null, userData);
	}
	
	// save user course info to local DB
	public long saveCourseInfo(JSONArray courses) {
		ContentValues courseInfo = new ContentValues();
		// iterate over courses and fill courseInfo
		return db.insert(COURSES_TABLE, null, courseInfo);
	}
	
	public class DBHelper extends SQLiteOpenHelper {
		
		private static final int DB_VERSION = 1;
		private static final String DB_CREATE = 
										"create table " + USER_TABLE + " (" + KEY_ID + " NUMERIC not null, " + KEY_NAME + " TEXT not null, " + KEY_C2DM_ID + " TEXT not null);" +
										"create table " + COURSES_TABLE + " (" + KEY_ID + " INTEGER PRIMARY KEY not null, " + KEY_UNI_ID + " NUMERIC not null, " + KEY_NAME + " TEXT not null, " + KEY_START_DATE + " TEXT not null, " + KEY_END_DATE + " TEXT not null);";
		
		public DBHelper(Context ctx) {
			super(ctx, DB_NAME, null, DB_VERSION);
		}
	
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DB_CREATE);
		}
	
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE + "; DROP TABLE IF EXISTS " + COURSES_TABLE + ";");
			onCreate(db);
		}
	}
}
