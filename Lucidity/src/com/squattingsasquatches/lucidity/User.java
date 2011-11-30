package com.squattingsasquatches.lucidity;

public class User {
	
	private int id;
	private String name;
	private int uniId;
	private String c2dmRegistrationId;
	private String deviceId;
	
	public User() {
		this(0, "", 0, "", "");
	}
	
	public User(int id, String name, int uniId, String c2dmRegistrationId, String deviceId) {
		this.setId(id);
		this.setName(name);
		this.setUniId(uniId);
		this.setC2dmRegistrationId(c2dmRegistrationId);
		this.setDeviceId(deviceId);
	}

	public int getId() {
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

	public int getUniId() {
		return uniId;
	}

	public void setUniId(int uniId) {
		this.uniId = uniId;
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
	public class Table
	{
		public static final String NAME = "users";
		
		public class Fields
		{
			public static final String ID = "id";
			public static final String NAME = "name";
			public static final String UNI_ID = "uni_id";
			public static final String C2DM_ID = "c2dm_id";
		}
	}
}
