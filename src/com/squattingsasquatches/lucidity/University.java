package com.squattingsasquatches.lucidity;

public class University extends DataItem {
	
	public University() {
		this(-1);
	}
	
	public University(int id) {
		this(id, "");
	}
	
	public University(int id, String name) {
		super(id, name);
	}
	
}
