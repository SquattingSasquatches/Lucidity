package com.squattingsasquatches.lucidity;

import java.util.Date;

public class Course {
	
	private int id;
	private int professorsId;
	private String name;
	private Date startDate;
	private Date endDate;
	
	public Course(int id, String name) {
		this(id, 0, name, new Date(0), new Date(0));
	}
	
	public Course(int id, int professorsId, String name, Date startDate, Date endDate) {
		this.id = id;
		this.professorsId = professorsId;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProfessorsID() {
		return professorsId;
	}

	public void setProfessorsID(int professorsId) {
		this.professorsId = professorsId;
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
}
