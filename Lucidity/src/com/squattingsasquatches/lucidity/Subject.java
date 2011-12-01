package com.squattingsasquatches.lucidity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
	public static final class Table
	{
		public static final String NAME = "subjects";
		
		public class Fields
		{
			public static final String ID = "id";
			public static final String UNI_ID = "uni_id";
			public static final String PREFIX = "prefix";
			public static final String NAME = "name";
		}
		public static final Subject getSubject( int id )
		{
			PHPClient client = new PHPClient();
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("action", "user.course.get.php");
			params.put(Course.Table.Fields.ID, String.valueOf(id));
			
			return new Subject();
		}
		public static final ArrayList<Subject> getSubjects( int uni_id )
		{
			PHPClient client = new PHPClient();
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("action", "uni.subjects.view");
			params.put(Course.Table.Fields.UNI_ID, String.valueOf(uni_id));
			
			JSONArray response = client.execute(params);
			ArrayList<Subject> subjects = new ArrayList<Subject>();
			JSONObject subject;
			for( int i = 0; i < response.length(); i++)
			{
				try {
					subject = response.getJSONObject(i);
					subjects.add(new Subject(subject.getInt(Subject.Table.Fields.ID),
											subject.getString(Subject.Table.Fields.NAME),
											subject.getString(Subject.Table.Fields.PREFIX)));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return subjects;
		}
	}
}
