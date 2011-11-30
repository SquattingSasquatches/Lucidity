package com.squattingsasquatches.lucidity;

public class UserDevice {
	private int userId;
	private int deviceId;
	
	public UserDevice() {
		this( 0, 0 );
	}
	
	public UserDevice(int userId, int deviceId) {
		this.setUserId(userId);
		this.setDeviceId(deviceId);
	}
	public int getDeviceId(){ return deviceId; };
	public int getUserId() { return userId; };
	public void setDeviceId( int i ) { deviceId = i; };
	public void setUserId( int i ) { userId = i; };
	
	
	
	public class Table
	{
		public static final String NAME = "user_devices";
		
		public class Fields
		{
			public static final String USER_ID = "user_id";
			public static final String DEVICE_ID = "device_id";
		}
		public UserDevice getUserDevice( int id )
		{
			return new UserDevice();
		}
	}
}
