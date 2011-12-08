package com.squattingsasquatches.lucidity;

import java.util.ArrayList;

public class Question extends DataItem {
	
	private ArrayList<Answer> answers;
	
	public Question() {
		this(-1);
	}
	
	public Question(int id) {
		this(id, "");
	}

	public Question(int id, String name) {
		this(id, name, new ArrayList<Answer>());
	}
	
	public Question(int id, String name, ArrayList<Answer> answers) {
		super(id, name);
		this.setAnswers(answers);
	}

	public ArrayList<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(ArrayList<Answer> answers) {
		this.answers = answers;
	}
	
	public boolean addAnswer(Answer answer) {
		return answers.add(answer);
	}
	
	public Answer getAnswer(int i) {
		return this.answers.get(i);
	}

}
