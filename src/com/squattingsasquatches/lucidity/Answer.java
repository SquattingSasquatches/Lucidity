package com.squattingsasquatches.lucidity;

public class Answer extends DataItem {
	
	private int questionId;
	
	public Answer() {
		this(-1);
	}
	
	public Answer(int id) {
		this(id, "");
	}
	
	public Answer(int id, String name) {
		this(id, name, -1);
	}
	
	public Answer(int id, String name, int questionId) {
		super(id, name);
		this.setQuestionId(questionId);
	}
	
	@Override
	public boolean equals(Object o) {
		Answer guess = (Answer) o;
		return this.name.equals(guess.getName());
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

}
