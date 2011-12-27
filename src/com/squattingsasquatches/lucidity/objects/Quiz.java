package com.squattingsasquatches.lucidity.objects;

import java.util.ArrayList;

import android.text.format.Time;

public class Quiz extends DataItem {
	
	private int duration;
	private Time startTime;
	private ArrayList<Question> questions;
	private ArrayList<Answer> studentAnswers;
	
	public Quiz() {
		this(-1);
	}
	
	public Quiz(int id) {
		this(id, "");
	}
	
	public Quiz(int id, String name) {
		this(id, name, 0, new Time(), new ArrayList<Question>());
	}
	
	public Quiz(int id, String name, int duration, Time startTime, ArrayList<Question> questions) {
		super(id, name);
		this.setDuration(duration);
		this.setStartTime(startTime);
		this.setQuestions(questions);
		setStudentAnswers(new ArrayList<Answer>());
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public ArrayList<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(ArrayList<Question> questions) {
		this.questions = questions;
	}
	
	public boolean addQuestion(Question question) {
		return questions.add(question);
	}
	
	public Question getQuestion(int i) {
		return questions.get(i);
	}

	public ArrayList<Answer> getStudentAnswers() {
		return studentAnswers;
	}

	public void setStudentAnswers(ArrayList<Answer> studentAnswers) {
		this.studentAnswers = studentAnswers;
	}
	
}
