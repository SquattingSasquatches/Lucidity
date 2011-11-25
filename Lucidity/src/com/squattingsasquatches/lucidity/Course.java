package com.squattingsasquatches.lucidity;

import java.util.Date;

public class Course {
	
	private int id;
	private int courseNum;
	private String name;
	private Date startDate;
	private Date endDate;
	private University uni;
	private Subject subject;
	
	public Course(int id, int courseNum) {
		this(id, courseNum, new Subject());
	}
	
	public Course(int id, String name) {
		this(id, 0, name, new Date(0), new Date(0), new Subject(), new University());
	}
	
	public Course(int id, int courseNum, Subject subject) {
		this(id, courseNum, "", new Date(0), new Date(0), subject, new University());
	}
	
	public Course(int id, int courseNum, String name, Date startDate, Date endDate, Subject subject, University uni) {
		this.id = id;
		this.uni = uni;
		this.courseNum = courseNum;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.subject = subject;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public University getUni() {
		return uni;
	}

	public void setUni(University uni) {
		this.uni = uni;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public int getCourseNum() {
		return courseNum;
	}

	public void setCourseNum(int courseNum) {
		this.courseNum = courseNum;
	}
	
	@Override
	public String toString() {
		return getSubject().getShortName() + " " + getCourseNum();
	}
}
