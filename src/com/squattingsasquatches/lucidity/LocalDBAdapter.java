package com.squattingsasquatches.lucidity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.squattingsasquatches.lucidity.objects.Section;
import com.squattingsasquatches.lucidity.objects.University;
import com.squattingsasquatches.lucidity.objects.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

public class LocalDBAdapter {
	
	// db table and key constants
	private static final String DB_NAME = "lucidity";
	
	
	private Context ctx;
	private SQLiteDatabase db;
	private DBHelper dbHelper;
	
	public LocalDBAdapter(Context ctx) {
		this.ctx = ctx;
	}

	public LocalDBAdapter open() throws SQLException {
		Log.i("open()", "opening SQLDatabase...");
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
		userData.put(User.Keys.id, user.getId());
		userData.put(User.Keys.name, user.getName());
		userData.put(User.Keys.universityId, user.getUniversity().getId());
		userData.put(User.Keys.c2dmId, user.getC2dmRegistrationId());
		userData.put(User.Keys.c2dmIsRegistered, 1);
		userData.put(User.Keys.c2dmLastCheck, dateFormat.format(new Date()));
		return db.insert(User.tableName, null, userData);
	}
	
	// get User's data
	public long getUserData()  {
		return db.insert(User.tableName, null, null); //TODO: this
	}
	
	// get User's id
	public int getUserId() {
		String[] columns = {User.Keys.id};
		Cursor result = db.query(User.tableName, columns, null, null, null, null, null);
		result.moveToFirst();
		int userId = result.getInt(0);
		result.close();
		return userId;
	}
	
	public int getUserUniId() {
		String[] columns = {User.Keys.universityId};
		Cursor result = db.query(User.tableName, columns, null, null, null, null, null);
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
		String[] columns = {Section.Keys.id};
		long result = -1;
		for (Section s : sections) {
			String[] args = {String.valueOf(s.getId())};
			Cursor sectionExists = db.query(Section.tableName, columns, Section.Keys.id + " = ?", args, null, null, null, null);
			if (sectionExists.getCount() == 0) {
				Log.i("saveSectionInfo", "Saving section_id " + s.getId());
				sectionInfo = new ContentValues();
				sectionInfo.put(Section.Keys.id, s.getId());
				sectionInfo.put(Section.Keys.name, s.getName());
				sectionInfo.put(Section.Keys.subjectPrefix, s.getCourse().getSubject().getPrefix());
				sectionInfo.put(Section.Keys.courseId, s.getCourse().getId());
				sectionInfo.put(Section.Keys.courseNumber, s.getCourse().getCourseNum());
				sectionInfo.put(Section.Keys.professorId, s.getProfessor().getId());
				sectionInfo.put(Section.Keys.professorName, s.getProfessor().getName());
				sectionInfo.put(Section.Keys.days, s.getDays());
				sectionInfo.put(Section.Keys.location, s.getLocation());
				sectionInfo.put(Section.Keys.startTime, s.getStartTime());
				sectionInfo.put(Section.Keys.endTime, s.getEndTime());
				sectionInfo.put(Section.Keys.verified, s.getIsVerified());
				result = db.insert(Section.tableName, null, sectionInfo);
				Log.i("result", result+"");
			}
		}
		return result;
	}
	
	
	// save user section info to local DB
	public void saveUniversities(ArrayList<University> universities) {
		ContentValues universityInfo = new ContentValues();
		String[] columns = {University.Keys.id};
		for (University u : universities) {
				
				String[] args = {String.valueOf(u.getId())};
				
				universityInfo = new ContentValues();
				universityInfo.put(University.Keys.id, u.getId());
				universityInfo.put(University.Keys.name, u.getName());
				universityInfo.put(University.Keys.manualFlag, u.getManualFlag());
				
				//if( result.getInt(2) == 1 )
				// TODO: Handle manual duplicates.
				
				Cursor result = db.query(University.tableName, columns, University.Keys.id + " = ?", args, null, null, null, null);
				
				if ( result.getCount() == 0 )
					db.insert(University.tableName, null, universityInfo);
				else
					db.update(University.tableName, universityInfo, University.Keys.id + " = ?", args);
		}
	}
	public JSONArray getUniversities() {
		JSONArray universities = new JSONArray();
		Cursor result = db.query(University.tableName, null, null, null, null, null, null);
		
		if (!result.moveToFirst())
			return universities;
		
		while (!result.isAfterLast()) {
            try {
				universities.put(
						new JSONObject("{\"" +
									University.Keys.id + "\": \"" + result.getInt(0) + "\", \"" +
									University.Keys.name + "\": \"" + result.getString(1) + "\", \"" +
									University.Keys.manualFlag + "\": \"" + result.getInt(2) + "\"}"));
			} catch (JSONException e) {
				Log.d("getUniversities()", "JSON error");
			} finally {
				result.moveToNext();
			}    
        }
		result.close();
		return universities;
	}	
	
	public JSONArray getSections() {
		JSONArray sections = new JSONArray();
		Cursor result = db.query(Section.tableName, null, null, null, null, null, null);
		
		if (!result.moveToFirst())
			return sections;
		
		while (!result.isAfterLast()) {
            try {
				sections.put(
						new JSONObject("{\"" +
									Section.Keys.id + "\": \"" + result.getInt(0) + "\", \"" +
									Section.Keys.subjectPrefix + "\": \"" + result.getString(1) + "\", \"" +
									Section.Keys.courseNumber + "\": \"" + result.getInt(2) + "\", \"" +
									Section.Keys.name + "\": \"" + result.getString(3) + "\", \"" +
									Section.Keys.courseId + "\": \"" + result.getInt(4) + "\", \"" +
									Section.Keys.professorId + "\": \"" + result.getInt(5) + "\", \"" +
									Section.Keys.professorName + "\": \"" + result.getString(6) + "\", \"" +
									Section.Keys.days + "\": \"" + result.getString(8) + "\", \"" +
									Section.Keys.location + "\": \"" + result.getString(7) + "\", \"" +
									Section.Keys.startTime + "\": \"" + result.getString(9) + "\", \"" +
									Section.Keys.endTime + "\": \"" + result.getString(10) + "\", \"" +
									Section.Keys.verified + "\": \"" + result.getInt(11) + "\"}"));
			} catch (JSONException e) {
				Log.d("getSections()", "JSON error");
			} finally {
				result.moveToNext();
			}    
        }
		result.close();
		return sections;
	}
	
	public int getCourseNumber(int sectionId) {
		Cursor result = db.query(Section.tableName, new String[] {Section.Keys.courseNumber}, Section.Keys.id + " = ?", new String[] {String.valueOf(sectionId)}, null, null, null);
		result.moveToFirst();
		int courseNum = result.getInt(0);
		result.close();
		return courseNum;
	}
	
	public String getCoursePrefix(int sectionId) {
		Cursor result = db.query(Section.tableName, new String[] {Section.Keys.subjectPrefix}, Section.Keys.id + " = ?", new String[] {String.valueOf(sectionId)}, null, null, null);
		result.moveToFirst();
		String subjectPrefix = result.getString(0);
		result.close();
		return subjectPrefix;
	}
	
	public Location getLocation(String provider) {
		Cursor result = db.query(User.tableName, new String[] {User.Keys.latitude, User.Keys.longitude}, null, null, null, null, null);
		result.moveToFirst();
		Location loc = new Location(provider);
		loc.setLatitude(result.getDouble(0));
		loc.setLongitude(result.getDouble(1));
		result.close();
		return loc;
	}
	
	public long saveLocation(Location loc) {
		ContentValues gpsData = new ContentValues();
		//ContentValues sectionData = new ContentValues();
		//gpsData.put(KEY_CHECKED_IN, 0);
		//sectionData.put(Section.Keys.checkedIn, 1);
		gpsData.put(User.Keys.latitude, loc.getLatitude());
		gpsData.put(User.Keys.longitude, loc.getLongitude());
		return db.insert(User.tableName, null, gpsData);
	}
	
	public long saveCheckedIn(boolean checkedIn, int sectionId) {
		ContentValues sectionData = new ContentValues();
		int c = 0;
		if (checkedIn) c = 1;
		sectionData.put(Section.Keys.checkedIn, c);
		return db.update(Section.tableName, sectionData, Section.Keys.id + " = ?", new String[]{String.valueOf(sectionId)});
	}
	
	public boolean isCheckedIn(int sectionId) {
		Cursor result = db.query(Section.tableName, new String[] {Section.Keys.checkedIn}, null, null, null, null, null);
		result.moveToFirst();
		boolean checkedIn = false;
		if (result.getInt(0) == 1)
			checkedIn = true;
		result.close();
		return checkedIn;
	}
	
	public class DBHelper extends SQLiteOpenHelper {
		
		private static final int DB_VERSION = 1;
		
		public DBHelper(Context ctx) {
			super(ctx, DB_NAME, null, DB_VERSION);
		}
		
		@Override
		public void onOpen(SQLiteDatabase db)
		{
			Log.i("onOpen", "Making tables!");
			db.execSQL("create table if not exists " + University.schema);
		}
		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.i("onCreate", "Making tables!");
			db.execSQL("create table " + User.schema);
			db.execSQL("create table " + Section.schema);
			//db.execSQL(DB_CREATE_GPS_TABLE);
			db.execSQL("create table " + University.schema);
			Log.i("onCreate", "Made tables!");
		}
	
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + User.schema + ";");
			db.execSQL("DROP TABLE IF EXISTS " + Section.schema + ";");
			//db.execSQL("DROP TABLE IF EXISTS " + GPS_TABLE + ";");
			db.execSQL("DROP TABLE IF EXISTS " + University.schema + ";");
			onCreate(db);
		}
	}
}