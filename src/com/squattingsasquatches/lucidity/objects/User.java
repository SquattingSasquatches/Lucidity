package com.squattingsasquatches.lucidity.objects;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import android.location.Location;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squattingsasquatches.lucidity.LucidityDatabase;

public class User extends DataItem {

	public User(int id, String name, University uni, String deviceId,
			String c2dmRegistrationId, int c2dmIsRegistered,
			String c2dmLastCheck, double longitude, double latitude) {
		super(id, name);
		this.uni = uni;
		this.deviceId = deviceId;
		this.c2dmRegistrationId = c2dmRegistrationId;
		this.c2dmIsRegistered = c2dmIsRegistered;
		this.c2dmLastCheck = c2dmLastCheck;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	private University uni;
	private String deviceId = "";

	private String c2dmRegistrationId = "";
	private int c2dmIsRegistered = 0;
	private String c2dmLastCheck = "";

	private double longitude, latitude;

	public User() {
		this("");
	}

	public User(String name) {
		this.name = name;
	}

	public User(int id, String name) {
		this(id, name, new University(), "", "");
	}

	public User(int id, String name, University uni, String deviceId,
			String c2dmRegistrationId) {
		super(id, name);
		this.setUniversity(uni);
		Log.i("User", "uni id: " + uni.getId());
		this.deviceId = deviceId;
		this.setC2dmRegistrationId(c2dmRegistrationId);
	}

	public User(int id, String name, University uni, String deviceId,
			double longitude, double latitude, String c2dmRegistrationId,
			int c2dmIsRegistered, String c2dmLastCheck) {
		this(id, name, uni, deviceId, c2dmRegistrationId);
		this.longitude = longitude;
		this.latitude = latitude;
		this.c2dmIsRegistered = c2dmIsRegistered;
		this.c2dmLastCheck = c2dmLastCheck;

	}

	public User(Cursor c) {
		this(c.getInt(c.getColumnIndex(Keys.id)), c.getString(c
				.getColumnIndex(Keys.name)), new University(c.getInt(c
				.getColumnIndex(Keys.universityId))), c.getString(c
				.getColumnIndex(Keys.deviceId)), c.getDouble(c
				.getColumnIndex(Keys.longitude)), c.getDouble(c
				.getColumnIndex(Keys.latitude)), c.getString(c
				.getColumnIndex(Keys.c2dmId)), c.getInt(c
				.getColumnIndex(Keys.c2dmIsRegistered)), c.getString(c
				.getColumnIndex(Keys.c2dmLastCheck)));
	}

	public void load() {
		User u = User.get();
		this.setC2dmIsRegistered(u.getC2dmIsRegistered());
		this.setC2dmLastCheck(u.getC2dmLastCheck());
		this.setC2dmRegistrationId(u.getC2dmRegistrationId());
		this.setDeviceId(u.getDeviceId());
		this.setId(u.getId());
		this.setLatitude(u.getLatitude());
		this.setLongitude(u.getLongitude());
		this.setName(u.getName());

		University uni = University.get(u.getUniversity().getId());
		this.setUniversity(uni);

	}

	public static boolean exists() {
		return (User.get() != null);
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
	public static User get() {
		Cursor result = LucidityDatabase.db().query(User.tableName, null, null,
				null, null, null, null);

		if (!result.moveToFirst()) {
			result.close();
			return null;
		}

		result.moveToFirst();

		User u = new User(result);
		result.close();
		return u;

	}

	public static int getStoredId() {
		User u = User.get();
		if (u != null)
			return u.getId();
		return 0;
	}

	public static int getStoredUniversityId() {
		User u = User.get();
		if (u != null) {
			return u.getUniversity().getId();
		}
		return 0;
	}

	public static String getStoredDeviceId() {
		User u = User.get();
		if (u != null)
			return u.getDeviceId();
		return "";
	}

	public static void setStoredLocation(Location loc) {
		ContentValues data = new ContentValues();
		data.put(Keys.latitude, loc.getLatitude());
		data.put(Keys.longitude, loc.getLongitude());
		LucidityDatabase.db().update(tableName, data, Keys.id + " = ?",
				new String[] { String.valueOf(User.getStoredId()) });

	}

	public static void setStoredLatitude(double latitude) {
		ContentValues data = new ContentValues();
		data.put(Keys.latitude, latitude);
		LucidityDatabase.db().update(tableName, data, Keys.id + " = ?",
				new String[] { String.valueOf(User.getStoredId()) });
	}

	public static void setStoredLongitude(double longitude) {
		ContentValues data = new ContentValues();
		data.put(Keys.longitude, longitude);
		LucidityDatabase.db().update(tableName, data, Keys.id + " = ?",
				new String[] { String.valueOf(User.getStoredId()) });
	}

	public static void setStoredLocation(double latitude, double longitude) {
		ContentValues data = new ContentValues();
		data.put(Keys.latitude, latitude);
		data.put(Keys.longitude, longitude);
		LucidityDatabase.db().update(tableName, data, Keys.id + " = ?",
				new String[] { String.valueOf(User.getStoredId()) });
	}

	public static void save(User user) {

		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Log.i("User save()", "Saving...");

		// If the user exists, don't insert!
		if (userExists()) {
			User u = get();

			Log.i("User save()", "User exists.");
			if (u.getId() != user.getId()) {
				LucidityDatabase.db().delete(User.tableName, Keys.id + " = ?",
						new String[] { String.valueOf(user.getId()) });
				Log.i("User save()",
						"User has different id. Deleting/Inserting...");

				user.setC2dmIsRegistered(1);
				user.setC2dmLastCheck(dateFormat.format(new Date()));
				User.insert(user);
				return;

			} else {
				Log.i("User save()", "Updating...");

				user.setC2dmIsRegistered(1);
				user.setC2dmLastCheck(dateFormat.format(new Date()));
				User.update(user);
				return;
			}
		}

		// Doesn't exist? Insert away!
		Log.i("User save()", "User not found! Inserting...");

		user.setC2dmIsRegistered(1);
		user.setC2dmLastCheck(dateFormat.format(new Date()));
		User.insert(user);
		return;
	}

	public static boolean userExists() {
		Cursor result = LucidityDatabase.db().query(tableName, null, null,
				null, null, null, null);

		boolean test = (result.getCount() > 0);
		result.close();
		return test;
	}

	public static void update(User user) {
		ContentValues values = new ContentValues();
		values.put(Keys.name, user.getName());
		values.put(Keys.universityId, user.getUniversity().getId());
		values.put(Keys.c2dmId, user.getC2dmRegistrationId());
		values.put(Keys.c2dmIsRegistered, user.getC2dmIsRegistered());
		values.put(Keys.c2dmLastCheck, user.getC2dmLastCheck());
		values.put(Keys.deviceId, user.getDeviceId());
		values.put(Keys.longitude, user.getLongitude());
		values.put(Keys.latitude, user.getLatitude());
		LucidityDatabase.db().update(tableName, values, "id = ?",
				new String[] { String.valueOf(user.id) });
	}

	public static void insert(User user) {
		ContentValues values = new ContentValues();
		values.put(Keys.id, user.getId());
		values.put(Keys.name, user.getName());
		values.put(Keys.universityId, user.getUniversity().getId());
		values.put(Keys.c2dmId, user.getC2dmRegistrationId());
		values.put(Keys.c2dmIsRegistered, user.getC2dmIsRegistered());
		values.put(Keys.c2dmLastCheck, user.getC2dmLastCheck());
		values.put(Keys.deviceId, user.getDeviceId());
		values.put(Keys.longitude, user.getLongitude());
		values.put(Keys.latitude, user.getLatitude());
		LucidityDatabase.db().insert(tableName, null, values);
	}

	public static void delete(int id) {
		LucidityDatabase.db().delete(tableName, "id = ?",
				new String[] { Keys.id });
	}

	public static String toJSON() {
		Type t = new TypeToken<User>() {
		}.getType();
		Gson g = new Gson();
		return g.toJson(get(), t);
	}

	public static String toJSON(User user) {
		Type t = new TypeToken<User>() {
		}.getType();
		Gson g = new Gson();
		return g.toJson(user, t);
	}

	@Override
	public String toString() {
		return name;
	}

	public static final class Keys {
		public static final String id = "id";
		public static final String name = "name";
		public static final String universityId = "uni_id";
		public static final String deviceId = "device_id";
		public static final String longitude = "longitude";
		public static final String latitude = "latitude";
		public static final String c2dmId = "c2dm_id";
		public static final String c2dmIsRegistered = "c2dm_is_registered";
		public static final String c2dmLastCheck = "c2dm_last_check";
	}

	public static final String tableName = "user";

	public static final String schema = tableName + " (" + Keys.id
			+ " INTEGER not null, " + Keys.name + " TEXT not null, "
			+ Keys.universityId + " INTEGER not null, " + Keys.deviceId
			+ " INTEGER not null, " + Keys.longitude + " NUMERIC not null, "
			+ Keys.latitude + " NUMERIC not null, " + Keys.c2dmId
			+ " TEXT not null, " + Keys.c2dmIsRegistered
			+ " INTEGER not null, " + Keys.c2dmLastCheck + " TEXT not null); ";

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

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

}
