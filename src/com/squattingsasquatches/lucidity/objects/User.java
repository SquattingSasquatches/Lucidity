package com.squattingsasquatches.lucidity.objects;


public class User extends DataItem {
	
	private University uni;
	private String c2dmRegistrationId;
	private String deviceId;
	
	public User() {
		this("");
	}
	
	public User(String name) {
		this(-1, name);
	}
	
	public User(int id, String name) {
		this(id, name, new University(), "", "");
	}
	
	public User(int id, String name, University uni, String c2dmRegistrationId, String deviceId) {
		super(id, name);
		this.setUniversity(uni);
		this.setC2dmRegistrationId(c2dmRegistrationId);
		this.setDeviceId(deviceId);
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

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
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
		public static final String c2dmId 				= 	"c2dm_id";
		public static final String c2dmIsRegistered		= 	"c2dm_is_registered";
		public static final String c2dmLastCheck		= 	"c2dm_last_check";
		public static final String deviceId				= 	"device_id";
		public static final String longitude			= 	"longitude";
		public static final String latitude				= 	"latitude";
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
}
