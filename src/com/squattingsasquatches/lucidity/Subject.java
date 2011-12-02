package com.squattingsasquatches.lucidity;

public class Subject extends DataItem {
	
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
		super(id, name);
		this.setPrefix(prefix);
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	@Override
	public String toString() {
		return this.prefix + " - " + super.name;
	}
}
