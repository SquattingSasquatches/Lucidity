package com.squattingsasquatches.lucidity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LocalDBAdapter {
	
	// db table and key constants (maybe move to Codes.java)
	private static final String DB_NAME = "lucidity";
	private static final String USER_TABLE = "user";
	private static final String COURSES_TABLE = "courses";
	private static final String KEY_ID = "_id";
	private static final String KEY_UNI_ID = "uni_id";
	private static final String KEY_NAME = "name";
	private static final String KEY_C2DM_ID = "c2dm_id";
	private static final String KEY_COURSE_NUMBER = "course_number";
	private static final String KEY_SUBJECT_ID = "subject_id";
	private static final String KEY_PROFESSORS_ID = "professors_id";
	private static final String KEY_START_DATE = "start_date";
	private static final String KEY_END_DATE = "end_date";
	private static final String KEY_C2DM_IS_REGISTERED = "c2dm_is_registered";
	private static final String KEY_C2DM_LAST_CHECK = "c2dm_last_check";
	
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
	public long saveUserData(User user) {
		ContentValues userData = new ContentValues();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		userData.put(KEY_ID, user.getId());
		userData.put(KEY_NAME, user.getName());
		userData.put(KEY_UNI_ID, user.getUniId());
		userData.put(KEY_C2DM_ID, user.getC2dmRegistrationId());
		userData.put(KEY_C2DM_IS_REGISTERED, 1);
		userData.put(KEY_C2DM_LAST_CHECK, dateFormat.format(new Date()));
		return db.insert(USER_TABLE, null, userData);
	}
	
	// get User's data
	public long getUserData()  {
		if (db == null) open();
		return db.insert(USER_TABLE, null, null); //TODO: this
	}
	
	// get User's id
	public int getUserId() {
		if (db == null) open();
		String[] columns = {KEY_ID};
		Cursor result = db.query(USER_TABLE, columns, null, null, null, null, null);
		result.moveToFirst();
		return result.getInt(0);
	}
	
	public int getUserUniId() {
		if (db == null) open();
		String[] columns = {KEY_UNI_ID};
		Cursor result = db.query(USER_TABLE, columns, null, null, null, null, null);
		result.moveToFirst();
		return result.getInt(0);
	}
	
	// save user course info to local DB
	public long saveCourseInfo(ArrayList<Course> courses) {
		if (db == null) open();
		ContentValues courseInfo = new ContentValues();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (Course c : courses) {
			courseInfo.put(KEY_COURSE_NUMBER, c.getCourseNum());
			courseInfo.put(KEY_SUBJECT_ID, c.getSubject().getId());
			courseInfo.put(KEY_UNI_ID, c.getUni().getId());
			courseInfo.put(KEY_NAME, c.getName());
			courseInfo.put(KEY_START_DATE, dateFormat.format(c.getStartDate()));
			courseInfo.put(KEY_END_DATE, dateFormat.format(c.getEndDate()));
		}
		return db.insert(COURSES_TABLE, null, courseInfo);
	}
	
	public JSONArray getCourses() {
		if (db == null) open();
		JSONArray courses = new JSONArray();
		Cursor result = db.query(USER_TABLE, null, null, null, null, null, null);
		result.moveToFirst();
		while (result.isAfterLast() == false) {
            try {
				courses.put(new JSONObject("{\"id\": " + result.getInt(0) + ", \"" + KEY_PROFESSORS_ID + "\": " + result.getInt(1) + ", \"" + KEY_NAME + "\": " + result.getString(2) + ", \"" + KEY_START_DATE + "\": " + result.getString(3) + ", \"" + KEY_END_DATE + "\": " + result.getString(4)));
			} catch (JSONException e) {
				Log.d("getCourses()", "JSON error");
			} finally {
				result.moveToNext();
			}    
        }
		result.close();
		return courses;
	}
	
	public class DBHelper extends SQLiteOpenHelper {
		
		private static final int DB_VERSION = 1;
		private static final String DB_CREATE = 
										"create table " +
												USER_TABLE + 
												"(" + KEY_ID + " NUMERIC not null, " +
													  KEY_NAME + " TEXT not null, " +
													  KEY_UNI_ID + " NUMERIC not null, " +
													  KEY_C2DM_ID + " TEXT not null, " +
													  KEY_C2DM_IS_REGISTERED + " NUMERIC not null, " +
													  KEY_C2DM_LAST_CHECK + " TEXT not null); " +
										"create table " +
												COURSES_TABLE +
												" (" + KEY_ID + " INTEGER PRIMARY KEY not null, " +
													   KEY_COURSE_NUMBER + " NUMERIC not null, " +
													   KEY_SUBJECT_ID + " NUMERIC not null, " +
													   KEY_UNI_ID + " NUMERIC not null, " +
													   KEY_START_DATE + " TEXT not null, " +
													   KEY_END_DATE + " TEXT not null);";
		
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