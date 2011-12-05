package com.squattingsasquatches.lucidity;

import android.text.format.Time;

public class Section extends DataItem {
	
	private Course course;
	private User professor;
	private String location;
	private String days;
	private Time startTime;
	private Time endTime;

	public Section(int id, String name) {
		this(id, name, new Course(), new User(), "", "", new Time(), new Time());
	}
	
	public Section(int id, String name, Course course, User professor, String location, String days, Time startTime, Time endTime) {
		super(id, name);
		this.setCourse(course);
		this.setProfessor(professor);
		this.setLocation(location);
		this.setDays(days);
		this.setStartTime(startTime);
		this.setEndTime(endTime);
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

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}
	
	@Override
	public String toString() {
		return getCourse().getSubject().getPrefix() + " " + getCourse().getCourseNum() + "-" + getName();
	}
}
