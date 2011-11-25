package com.squattingsasquatches.lucidity;

public class Subject {
	
	private int id;
	private String longName;
	private String shortName;
	
	public Subject() {
		this(0);
	}
	
	public Subject(int id) {
		this(id, "");
	}
	
	public Subject(int id, String shortName) {
		this(id, "", shortName);
	}
	
	public Subject(int id, String longName, String shortName) {
		this.setId(id);
		this.setLongName(longName);
		this.setShortName(shortName);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLongName() {
		return longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
}
