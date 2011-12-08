package com.squattingsasquatches.lucidity;

public class Answer extends DataItem {
	
	public Answer() {
		this(-1);
	}
	
	public Answer(int id) {
		this(id, "");
	}
	
	public Answer(int id, String name) {
		super(id, name);
	}
	
	@Override
	public boolean equals(Object o) {
		Answer guess = (Answer) o;
		return this.name.equals(guess.getName());
	}

}
