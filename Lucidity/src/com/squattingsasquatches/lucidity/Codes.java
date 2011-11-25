package com.squattingsasquatches.lucidity;

public class Codes {
	/* General */
	public static final int ERROR = -1;
	public static final int SUCCESS = 0;
	
	/* PHP results */
	public static final int NOT_A_JSON_ARRAY = -2;
	public static final int DATABASE_ERROR = -1;
	public static final int NO_USER_ID_FOUND = 1;
	public static final int NO_USER_ID_SUPPLIED = 2;
	public static final int NO_DEVICE_ID_FOUND = 3;
	public static final int NO_DEVICE_ID_SUPPLIED = 4;
	public static final int NO_COURSE_ID_FOUND = 5;
	public static final int NO_COURSE_ID_SUPPLIED = 6;
	public static final int USER_NOT_PROFESSOR_OF_COURSE = 7;
	public static final int NO_STUDENT_COURSE_PAIR_FOUND = 8;
	public static final int NO_NAME_SUPPLIED = 9;
	public static final int STUDENT_ALREADY_EXISTS = 10;
	public static final int DEVICE_ID_ALREADY_EXISTS = 11;
	public static final int COURSE_ALREADY_REGISTERED = 12;
	public static final int NO_STUDENT_ID_SUPPLIED = 13;
	public static final int NO_LECTURE_HALL_SUPPLIED = 14;
	
	
	/* App Callbacks */
	public static final int REMOTE_QUERY_COMPLETE = 50;
	public static final int REMOTE_QUERY_ERROR = 51;
	public static final int NO_CALLBACK = 52;
	public static final int LOGIN = 53;
	public static final int LOAD_UNIVERSITIES = 54;
	public static final int REGISTER = 55;
	public static final int GET_USER_COURSES = 56;
	public static final int LOAD_COURSES = 57;
	
	/* String constants */
	public static final String KEY_RESULT = "result";
	public static final String KEY_CALLBACK = "callback";
	public static final String KEY_C2DM_RESULT = "c2dm_result";
	public static final String KEY_C2DM_ID = "c2dm_id";
	public static final String KEY_DEVICE_ID = "deviceID";
	public static final String KEY_USERS_NAME = "usersName";
	public static final String KEY_USER_ID = "user_id";
	
	private Codes() {} // don't allow instantiation of this class
}
