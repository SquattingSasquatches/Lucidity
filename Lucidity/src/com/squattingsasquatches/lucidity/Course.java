package com.squattingsasquatches.lucidity;

import java.util.Date;
import java.util.ArrayList;

public class Course {
	
	private int id;
	private int courseNum;
	private String name;
	private Date startDate;
	private Date endDate;
	private University uni;
	private Subject subject;
	
	public Course() {
		this(0, 0);
	}
	
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
		return getSubject().getPrefix() + " " + getCourseNum();
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
	public class Table
	{
		public String NAME = "courses";
		
		public class Fields
		{
			public static final String ID = "id";
			public static final String UNI_ID = "uni_id";
			public static final String NAME = "name";
			public static final String START_DATE = "start_date";
			public static final String END_DATE = "end_date";
		}
		public Course getCourse( int id )
		{
			return new Course();
		}
		public ArrayList<Course> getCourses()
		{
			return new ArrayList<Course>();
		}
	}
}
