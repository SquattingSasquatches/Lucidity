package com.squattingsasquatches.lucidity;

public class Subject {
	
	private int id;
	private String name;
	private String prefix;
	
	public Subject() {
		this(0);
	}
	
	public Subject(int id) {
		this(id, "");
	}
	
	public Subject(String prefix) {
		this(0, "", prefix);
	}
	
	public Subject(int id, String prefix) {
		this(id, "", prefix);
	}
	
	public Subject(int id, String name, String prefix) {
		this.setId(id);
		this.setName(name);
		this.setPrefix(prefix);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
}
