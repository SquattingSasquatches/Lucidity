package com.squattingsasquatches.lucidity;

public class StudentCourse {

	private int userId;
	private int courseId;
	
	public StudentCourse() {
		this( 0, 0 );
	}
	
	public StudentCourse(int userId, int courseId) {
		this.setUserId(userId);
		this.setCourseId(courseId);
	}
	public int getCourseId(){ return courseId; };
	public int getUserId() { return userId; };
	public void setCourseId( int i ) { courseId = i; };
	public void setUserId( int i ) { userId = i; };
	
	public class Table
	{
		public static final String NAME = "student_courses";
		
		public class Fields
		{
			public static final String IS_VERIFIED = "is_verified";
			public static final String COURSE_ID = "course_id";
			public static final String STUDENT_ID = "student_id";
		}
	}
}