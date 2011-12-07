package com.squattingsasquatches.lucidity;


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
	
}
