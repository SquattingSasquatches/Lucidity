package com.squattingsasquatches.lucidity;

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
}
