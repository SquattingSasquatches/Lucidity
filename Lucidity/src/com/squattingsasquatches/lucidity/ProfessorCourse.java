package com.squattingsasquatches.lucidity;

public class ProfessorCourse {

	private int professorId;
	private int courseId;
	
	public ProfessorCourse() {
		this( 0, 0 );
	}
	
	public ProfessorCourse(int userId, int courseId) {
		this.setProfessorId(professorId);
		this.setCourseId(courseId);
	}
	public int getCourseId(){ return courseId; };
	public int getProfessorId() { return professorId; };
	public void setCourseId( int i ) { courseId = i; };
	public void setProfessorId( int i ) { professorId = i; };
	
	public class Table
	{
		public static final String NAME = "professor_courses";
		
		public class Fields
		{
			public static final String PROFESSOR_ID = "professor_id";
			public static final String COURSE_ID = "course_id";
		}
	}
}