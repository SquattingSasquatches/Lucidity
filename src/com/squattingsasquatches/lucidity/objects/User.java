package com.squattingsasquatches.lucidity.objects;


import android.content.ContentValues;
import android.database.Cursor;

import com.squattingsasquatches.lucidity.LucidityDatabase;


public class User extends DataItem {
	
	private University uni;
	private String deviceId;
	
	private String c2dmRegistrationId;
	private int c2dmIsRegistered;
	private String c2dmLastCheck;
	
	private double longitude, latitude;
	
	public User() {
		this("");
	}
	
	public User(String name) {
		this(-1, name);
	}
	
	public User(int id, String name) {
		this(id, name, new University(), "", "");
	}
	
	public User(int id, String name, University uni, String deviceId, String c2dmRegistrationId) {
		super(id, name);
		this.setUniversity(uni);
		this.setDeviceId(deviceId);
		this.setC2dmRegistrationId(c2dmRegistrationId);
	}

	public User(int id, String name, University uni, String deviceId, double longitude, double latitude, String c2dmRegistrationId, int c2dmIsRegistered, String c2dmLastCheck ) {
		this(id, name, uni, deviceId, c2dmRegistrationId);
		this.longitude = longitude;
		this.latitude = latitude;
		this.c2dmIsRegistered = c2dmIsRegistered;
		this.c2dmLastCheck = c2dmLastCheck;
		
	}
	public User(Cursor result)
	{
		this( result.getInt(0), result.getString(1), new University( result.getInt(2) ), result.getString(3), result.getDouble(4), result.getDouble(5), result.getString(6), result.getInt(7), result.getString(8) );
		
	}

	public University getUniversity() {
		return uni;
	}

	public void setUniversity(University uni) {
		this.uni = uni;
	}

	public String getC2dmRegistrationId() {
		return c2dmRegistrationId;
	}

	public void setC2dmRegistrationId(String c2dmRegistrationId) {
		this.c2dmRegistrationId = c2dmRegistrationId;
	}

	
	// Only gets the first user, since there should only be one.
	public static User get()
	{
		Cursor result = LucidityDatabase.db.query(tableName, null, null, null, null, null, null);
		result.moveToFirst();
		return new User( result );
	}

	public static int getId()
	{
		return get().id;
	}
	public static int getUniversityId()
	{
		return get().uni.id;
	}
	public static String getDeviceId()
	{
		return get().deviceId;
	}
	public static void update( User user )
	{
		ContentValues values = new ContentValues();
		values.put(Keys.id, user.id);
		values.put(Keys.name, user.name);
		values.put(Keys.universityId, user.uni.id);
		values.put(Keys.c2dmId, user.c2dmRegistrationId);
		values.put(Keys.c2dmIsRegistered, user.c2dmIsRegistered);
		values.put(Keys.c2dmLastCheck, user.c2dmLastCheck);
		values.put(Keys.deviceId, user.deviceId);
		values.put(Keys.longitude, user.longitude);
		values.put(Keys.latitude, user.latitude);
		LucidityDatabase.db.update(tableName, values, "id = ?", new String[]{ String.valueOf( user.id )  } );
	}
	public static void insert( User user )
	{
		ContentValues values = new ContentValues();
		values.put(Keys.id, user.id);
		values.put(Keys.name, user.name);
		values.put(Keys.universityId, user.uni.id);
		values.put(Keys.c2dmId, user.c2dmRegistrationId);
		values.put(Keys.c2dmIsRegistered, user.c2dmIsRegistered);
		values.put(Keys.c2dmLastCheck, user.c2dmLastCheck);
		values.put(Keys.deviceId, user.deviceId);
		values.put(Keys.longitude, user.longitude);
		values.put(Keys.latitude, user.latitude);
		LucidityDatabase.db.insert(tableName, null, values);
	}
	public static void delete( int id )
	{
		LucidityDatabase.db.delete(tableName, "id = ?", new String[]{Keys.id});
	}
	@Override
	public String toString() {
		return name;
	}
	public static final class Keys
	{
		public static final String id 					= 	"id";
		public static final String name 				= 	"name";
		public static final String universityId 		= 	"uni_id";
		public static final String deviceId				= 	"device_id";
		public static final String longitude			= 	"longitude";
		public static final String latitude				= 	"latitude";
		public static final String c2dmId 				= 	"c2dm_id";
		public static final String c2dmIsRegistered		= 	"c2dm_is_registered";
		public static final String c2dmLastCheck		= 	"c2dm_last_check";
	}
	
	public static final String tableName = "user";
	
	public static final String schema = tableName + 
												" (" + Keys.id + " INTEGER not null, " +
												Keys.name + " TEXT not null, " +
												Keys.universityId + " INTEGER not null, " +
												Keys.deviceId + " INTEGER not null, " +
												Keys.longitude + " NUMERIC not null, " +
												Keys.latitude + " NUMERIC not null, " +
												Keys.c2dmId + " TEXT not null, " +
												Keys.c2dmIsRegistered + " INTEGER not null, " +
												Keys.c2dmLastCheck + " TEXT not null); ";

	public University getUni() {
		return uni;
	}

	public void setUni(University uni) {
		this.uni = uni;
	}

	public int getC2dmIsRegistered() {
		return c2dmIsRegistered;
	}

	public void setC2dmIsRegistered(int c2dmIsRegistered) {
		this.c2dmIsRegistered = c2dmIsRegistered;
	}

	public String getC2dmLastCheck() {
		return c2dmLastCheck;
	}

	public void setC2dmLastCheck(String c2dmLastCheck) {
		this.c2dmLastCheck = c2dmLastCheck;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public static String getTablename() {
		return tableName;
	}

	public static String getSchema() {
		return schema;
	}
}
