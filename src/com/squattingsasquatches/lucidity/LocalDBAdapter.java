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
	
	// db table and key constants
	private static final String DB_NAME = "lucidity";
	private static final String USER_TABLE = "user";
	private static final String SECTIONS_TABLE = "sections";
	
	public static final String KEY_ID = "id";
	public static final String KEY_UNI_ID = "uni_id";
	public static final String KEY_COURSE_ID = "course_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_C2DM_ID = "c2dm_id";
	public static final String KEY_SECTION_NUMBER = "section_number";
	public static final String KEY_SUBJECT_ID = "subject_id";
	public static final String KEY_SUBJECT_PREFIX = "subject_prefix";
	public static final String KEY_PROFESSOR_ID = "professor_id";
	public static final String KEY_SECTION_ID = "section_id";
	public static final String KEY_START_TIME = "start_time";
	public static final String KEY_END_TIME = "end_time";
	public static final String KEY_C2DM_IS_REGISTERED = "c2dm_is_registered";
	public static final String KEY_C2DM_LAST_CHECK = "c2dm_last_check";
	public static final String KEY_PROFESSOR_NAME = "professor_name";
	public static final String KEY_COURSE_NUMBER = "course_number";
	public static final String KEY_VERIFIED = "is_verified";
	public static final String KEY_DAYS = "days";
	public static final String KEY_LOCATION = "location";
	
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
		userData.put(KEY_UNI_ID, user.getUniversity().getId());
		userData.put(KEY_C2DM_ID, user.getC2dmRegistrationId());
		userData.put(KEY_C2DM_IS_REGISTERED, 1);
		userData.put(KEY_C2DM_LAST_CHECK, dateFormat.format(new Date()));
		return db.insert(USER_TABLE, null, userData);
	}
	
	// get User's data
	public long getUserData()  {
		return db.insert(USER_TABLE, null, null); //TODO: this
	}
	
	// get User's id
	public int getUserId() {
		String[] columns = {KEY_ID};
		Cursor result = db.query(USER_TABLE, columns, null, null, null, null, null);
		result.moveToFirst();
		int userId = result.getInt(0);
		result.close();
		return userId;
	}
	
	public int getUserUniId() {
		String[] columns = {KEY_UNI_ID};
		Cursor result = db.query(USER_TABLE, columns, null, null, null, null, null);
		result.moveToFirst();
		int uniId = result.getInt(0);
		result.close();
		return uniId;
	}
	
	public long saveSectionInfo(Section section) {
		ArrayList<Section> wrapper = new ArrayList<Section>();
		wrapper.add(section);
		return saveSectionInfo(wrapper);
	}
	
	// save user section info to local DB
	public long saveSectionInfo(ArrayList<Section> sections) {
		ContentValues sectionInfo = new ContentValues();
		String[] columns = {KEY_SECTION_ID};
		long result = -1;
		for (Section s : sections) {
			String[] args = {String.valueOf(s.getId())};
			Cursor sectionExists = db.query(SECTIONS_TABLE, columns, KEY_SECTION_ID + " = ?", args, null, null, null, null);
			if (sectionExists.getCount() == 0) {
				Log.i("saveSectionInfo", "Saving section_id " + s.getId());
				sectionInfo = new ContentValues();
				sectionInfo.put(KEY_SECTION_ID, s.getId());
				sectionInfo.put(KEY_SUBJECT_PREFIX, s.getCourse().getSubject().getPrefix());
				sectionInfo.put(KEY_COURSE_NUMBER, s.getCourse().getCourseNum());
				sectionInfo.put(KEY_SECTION_NUMBER, s.getName());
				sectionInfo.put(KEY_COURSE_ID, s.getCourse().getId());
				sectionInfo.put(KEY_PROFESSOR_ID, s.getProfessor().getId());
				sectionInfo.put(KEY_PROFESSOR_NAME, s.getProfessor().getName());
				sectionInfo.put(KEY_DAYS, s.getDays());
				sectionInfo.put(KEY_LOCATION, s.getLocation());
				sectionInfo.put(KEY_START_TIME, s.getStartTime());
				sectionInfo.put(KEY_END_TIME, s.getEndTime());
				sectionInfo.put(KEY_VERIFIED, s.getIsVerified());
				result = db.insert(SECTIONS_TABLE, null, sectionInfo);
				Log.i("result", result+"");
			}
		}
		return result;
	}
	
	public JSONArray getSections() {
		JSONArray sections = new JSONArray();
		Cursor result = db.query(SECTIONS_TABLE, null, null, null, null, null, null);
		
		if (!result.moveToFirst())
			return sections;
		
		while (!result.isAfterLast()) {
            try {
            	Log.i("json", "{\"" +
								KEY_SECTION_ID + "\": \"" + result.getInt(0) + "\", \"" +
								KEY_SUBJECT_PREFIX + "\": \"" + result.getString(1) + "\", \"" +
								KEY_COURSE_NUMBER + "\": \"" + result.getInt(2) + "\", \"" +
								KEY_SECTION_NUMBER + "\": \"" + result.getString(3) + "\", \"" +
								KEY_COURSE_ID + "\": \"" + result.getInt(4) + "\", \"" +
								KEY_PROFESSOR_ID + "\": \"" + result.getInt(5) + "\", \"" +
								KEY_PROFESSOR_NAME + "\": \"" + result.getString(6) + "\", \"" +
								KEY_DAYS + "\": \"" + result.getString(8) + "\", \"" +
								KEY_LOCATION + "\": \"" + result.getString(7) + "\", \"" +
								KEY_START_TIME + "\": \"" + result.getString(9) + "\", \"" +
							    KEY_END_TIME + "\": \"" + result.getString(10) + "\", \"" +
							    KEY_VERIFIED + "\": \"" + result.getInt(11) + "\"}");
				sections.put(
						new JSONObject("{\"" +
									KEY_SECTION_ID + "\": \"" + result.getInt(0) + "\", \"" +
									KEY_SUBJECT_PREFIX + "\": \"" + result.getString(1) + "\", \"" +
									KEY_COURSE_NUMBER + "\": \"" + result.getInt(2) + "\", \"" +
									KEY_SECTION_NUMBER + "\": \"" + result.getString(3) + "\", \"" +
									KEY_COURSE_ID + "\": \"" + result.getInt(4) + "\", \"" +
									KEY_PROFESSOR_ID + "\": \"" + result.getInt(5) + "\", \"" +
									KEY_PROFESSOR_NAME + "\": \"" + result.getString(6) + "\", \"" +
									KEY_DAYS + "\": \"" + result.getString(8) + "\", \"" +
									KEY_LOCATION + "\": \"" + result.getString(7) + "\", \"" +
									KEY_START_TIME + "\": \"" + result.getString(9) + "\", \"" +
								    KEY_END_TIME + "\": \"" + result.getString(10) + "\", \"" +
								    KEY_VERIFIED + "\": \"" + result.getInt(11) + "\"}"));
			} catch (JSONException e) {
				Log.d("getSections()", "JSON error");
			} finally {
				result.moveToNext();
			}    
        }
		result.close();
		return sections;
	}
	
	public class DBHelper extends SQLiteOpenHelper {
		
		private static final int DB_VERSION = 1;
		private static final String DB_CREATE_USERS_TABLE = 
										"create table " +
												USER_TABLE + 
												" (" + KEY_ID + " INTEGER not null, " +
													   KEY_NAME + " TEXT not null, " +
													   KEY_UNI_ID + " INTEGER not null, " +
													   KEY_C2DM_ID + " TEXT not null, " +
													   KEY_C2DM_IS_REGISTERED + " INTEGER not null, " +
													   KEY_C2DM_LAST_CHECK + " TEXT not null); ";
		private static final String DB_CREATE_SECTIONS_TABLE = 
										"create table " +
												SECTIONS_TABLE +
												" (" + KEY_SECTION_ID + " INTEGER not null, " +
													   KEY_SUBJECT_PREFIX + " TEXT not null, " +
													   KEY_COURSE_NUMBER + " TEXT not null, " +
													   KEY_SECTION_NUMBER + " TEXT not null, " +
													   KEY_COURSE_ID + " INTEGER not null, " +
													   KEY_PROFESSOR_ID + " INTEGER not null, " +
													   KEY_PROFESSOR_NAME + " TEXT not null, " +
													   KEY_DAYS + " TEXT not null, " +
													   KEY_LOCATION + " TEXT not null, " +
													   KEY_START_TIME + " TEXT not null, " +
													   KEY_END_TIME + " TEXT not null, " +
													   KEY_VERIFIED + " INTEGER not null);";
		
		public DBHelper(Context ctx) {
			super(ctx, DB_NAME, null, DB_VERSION);
		}
	
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DB_CREATE_USERS_TABLE);
			db.execSQL(DB_CREATE_SECTIONS_TABLE);
		}
	
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE + ";");
			db.execSQL("DROP TABLE IF EXISTS " + SECTIONS_TABLE + ";");
			onCreate(db);
		}
	}
}