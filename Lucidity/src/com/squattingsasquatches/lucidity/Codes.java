package com.squattingsasquatches.lucidity;

public class Codes {
	/* PHP results */
	public static final int NOT_A_JSON_ARRAY = -2;
	public static final int DATABASE_ERROR = -1;
	public static final int SUCCESS = 0;
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
	public static final int REGISTER = 54;
	public static final int GET_COURSES = 55;
	
	
	private Codes() {}
}