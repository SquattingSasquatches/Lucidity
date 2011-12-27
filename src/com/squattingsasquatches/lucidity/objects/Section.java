package com.squattingsasquatches.lucidity.objects;

import com.squattingsasquatches.lucidity.objects.User.Keys;



public class Section extends ExtendedDataItem {
	
	private Course course;
	private User professor;
	private String location;
	private String days;
	private String startTime;
	private String endTime;
	private int isVerified;

	public Section(int id, String name) {
		this(id, name, new Course(), new User(), "", "", "", "", 0);
	}
	
	public Section(int id, String name, Course course, User professor, String location, String days, String startTime, String endTime) {
		this(id, name, course, professor, location, days, startTime, endTime, 0);
	}
	
	public Section(int id, String name, Course course, User professor, String location, String days, String startTime, String endTime, int isVerified) {
		super(id, name);
		this.setCourse(course);
		this.setProfessor(professor);
		this.setLocation(location);
		this.setDays(days);
		this.setStartTime(startTime);
		this.setEndTime(endTime);
		this.setIsVerified(isVerified);
		this.setItemInfo1("Instructor: " + professor.toString());
		this.setItemInfo2("Meets: " + days + " at " + startTime + " - " + endTime);
	}
	
	public Course getCourse() {
		return course;
	}
	
	public void setCourse(Course course) {
		this.course = course;
	}
	
	public User getProfessor() {
		return professor;
	}
	
	public void setProfessor(User professor) {
		this.professor = professor;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public int getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(int isVerified) {
		this.isVerified = isVerified;
	}
	
	@Override
	public String toString() {
		if (getCourse().getCourseNum() > 0)
			return getCourse().getSubject().getPrefix() + " " + getCourse().getCourseNum() + "-" + getName();
		return getName();
	}

	public static final class Keys
	{
		public static final String id 				= 	"id";
		public static final String name 			= 	"section_name";
		public static final String courseId 		= 	"course_id";
		public static final String courseNumber 	= 	"course_number";
		public static final String sectionNumber 	= 	"section_number";
		public static final String subjectPrefix 	= 	"subject_prefix";
		public static final String professorId 		= 	"professor_id";
		public static final String professorName 	= 	"professor_name";
		public static final String days 			= 	"days";
		public static final String location 		= 	"location";
		public static final String startTime 		= 	"start_time";
		public static final String endTime 			= 	"end_time";
		public static final String verified 		= 	"verified";
		public static final String checkedIn 		= 	"checked_in";
	}
	
	public static final String tableName = "sections";
	
	public static final String schema = tableName + 
												" (" + Keys.id + " INTEGER not null, " +
													Keys.name + " TEXT not null, " +
													Keys.subjectPrefix + " TEXT not null, " +
													Keys.courseId + " INTEGER not null, " +
													Keys.courseNumber + " TEXT not null, " +
													Keys.sectionNumber + " TEXT not null, " +
													Keys.professorId + " INTEGER not null, " +
													Keys.professorName + " TEXT not null, " +
													Keys.days + " TEXT not null, " +
													Keys.location + " TEXT not null, " +
													Keys.startTime + " TEXT not null, " +
													Keys.endTime + " TEXT not null, " +
													Keys.verified + " INTEGER not null);";
	
}
