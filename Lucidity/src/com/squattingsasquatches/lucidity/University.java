package com.squattingsasquatches.lucidity;

public class University {
	
	private int id;
	private String name;
	
	public University() {
		this(0, "");
	}
	
	public University(int id) {
		this(id, "");
	}
	
	public University(int id, String name) {
		this.setId(id);
		this.setName(name);
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
	
	@Override
	public String toString() {
		return getName();
	}
	public class Table
	{
		public static final String NAME = "unis";
		
		public class Fields
		{
			public static final String ID = "id";
			public static final String NAME = "name";
		}
	}
}
