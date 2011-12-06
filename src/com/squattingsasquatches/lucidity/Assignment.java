package com.squattingsasquatches.lucidity;

import java.util.Date;

public class Assignment extends ExtendedDataItem{
	
	private int doc_id;
	private String description;
	private Date due_date;

	public Assignment(int id, String name) {
		this(id, name, 0, "", null);
	}
	
	public Assignment(int id, String name, int d_id, String desc, Date due) {
		super(id, name);
		
		this.setDescription(desc);
		this.setDocId(d_id);
		this.setDueDate(due);
		if(this.getDueDate() != null){
			this.setItemInfo1("Due Date: " + due_date.getDay() + "/" + due_date.getMonth() + "/" + due_date.getYear());
		}
		if(doc_id != 0){
			this.setItemInfo2("Document Available!");
		}
		
	}
	
	public void setDocId(int d){
		doc_id = d;
	}
	
	public int getDocId(){
		return doc_id;
	}
	
	public void setDescription(String s){
		description = s;
	}
	
	public String getDescription(){
		return description;
	}

	public void setDueDate(Date d){
		due_date = d;
	}
	
	public Date getDueDate(){
		return due_date;
	}
	
	@Override
	public String toString() {
		return super.getName();
	}
	
}
