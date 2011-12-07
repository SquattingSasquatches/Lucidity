package com.squattingsasquatches.lucidity;

import java.util.Date;

public class Course extends DataItem {
	
	private int courseNum;
	private Date startDate;
	private Date endDate;
	private University uni;
	private Subject subject;
	
	public Course() {
		this(-1, 0);
	}
	
	public Course(int courseNum) {
		this(-1, courseNum);
	}
	
	public Course(int id, int courseNum) {
		this(id, courseNum, new Subject());
	}
	
	public Course(int id, String name) {
		this(id, 0, name, new Date(0), new Date(0), new Subject(), new University());
	}
	
	public Course(int id, int courseNum, String name, Subject subject, University uni) {
		this(id, courseNum, name, new Date(0), new Date(0), subject, uni);
	}
	
	public Course(int id, int courseNum, Subject subject) {
		this(id, courseNum, "", new Date(0), new Date(0), subject, new University());
	}
	
	public Course(int id, int courseNum, String name, Date startDate, Date endDate, Subject subject, University uni) {
		super(id, name);
		this.uni = uni;
		this.courseNum = courseNum;
		this.startDate = startDate;
		this.endDate = endDate;
		this.subject = subject;
	}

	public University getUni() {
		return uni;
	}

	public void setUni(University uni) {
		this.uni = uni;
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
		if (getCourseNum() > 0)
			return getSubject().getPrefix() + " " + getCourseNum() + " - " + super.name;
		return super.name;
	}
	
	@Override
	public boolean equals(Object o) {
		Course c = (Course) o;
		return equals(c.getSubject().getPrefix(), c.getCourseNum());
	}
	
	public boolean equals(String courseTitle) {
		return toString().equalsIgnoreCase(courseTitle);
	}
	
	public boolean equals(String subject, int courseNum) {
		return equalsIgnoreCase(subject, String.valueOf(courseNum));
	}
	
	public boolean equalsIgnoreCase(String subject, String courseNum) {
		return subject.toLowerCase().equals(getSubject().getPrefix().toLowerCase()) && courseNum.equals(String.valueOf(getCourseNum())); 
	}
}
