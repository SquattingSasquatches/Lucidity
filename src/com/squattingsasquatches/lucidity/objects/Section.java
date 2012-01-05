package com.squattingsasquatches.lucidity.objects;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squattingsasquatches.lucidity.LucidityDatabase;

public class Section extends ExtendedDataItem {

	public static final class Keys {
		public static final String checkedIn = "checked_in";
		public static final String courseId = "course_id";
		public static final String courseNumber = "course_number";
		public static final String days = "days";
		public static final String endTime = "end_time";
		public static final String id = "id";
		public static final String location = "location";
		public static final String name = "section_name";
		public static final String professorId = "professor_id";
		public static final String professorName = "professor_name";
		public static final String sectionNumber = "section_number";
		public static final String startTime = "start_time";
		public static final String subjectPrefix = "subject_prefix";
		public static final String verified = "verified";
	}

	public static final String tableName = "sections";
	public static final String schema = tableName + " (" + Keys.id
			+ " INTEGER not null, " + Keys.name + " TEXT not null, "
			+ Keys.subjectPrefix + " TEXT not null, " + Keys.courseId
			+ " INTEGER not null, " + Keys.courseNumber + " TEXT not null, "
			+ Keys.sectionNumber + " TEXT not null, " + Keys.professorId
			+ " INTEGER not null, " + Keys.professorName + " TEXT not null, "
			+ Keys.days + " TEXT not null, " + Keys.location
			+ " TEXT not null, " + Keys.startTime + " TEXT not null, "
			+ Keys.endTime + " TEXT not null, " + Keys.verified
			+ " INTEGER not null);";

	public static void delete(int id) {
		LucidityDatabase.db().delete(tableName, "id = ?",
				new String[] { Keys.id });
	}

	public static void setStoredCheckedIn(boolean checkedIn, int id) {
		ContentValues data = new ContentValues();
		int c = 0;
		if (checkedIn)
			c = 1;
		data.put(Keys.checkedIn, c);
		LucidityDatabase.db().update(tableName, data, Keys.id + " = ?",
				new String[] { String.valueOf(id) });

	}

	public static int getStoredCheckedIn(int id) {
		return get(id).getCheckedIn();
	}

	public static Section get(int id) {
		Cursor result = LucidityDatabase.db().query(tableName, null, "id = ?",
				new String[] { Keys.id }, null, null, null);
		return new Section(result);
	}

	public static ArrayList<Section> getAll() {
		ArrayList<Section> sections = new ArrayList<Section>();
		Cursor result = LucidityDatabase.db().query(tableName, null, null,
				null, null, null, null);

		if (!result.moveToFirst()) {
			result.close();
			return sections;
		}

		while (!result.isAfterLast()) {
			sections.add(new Section(result));
			result.moveToNext();
		}

		result.close();
		return sections;
	}

	public static void insert(ArrayList<Section> sections) {
		for (Section s : sections) {
			insert(s);
		}
	}

	public static String toJSON(int id) {
		Type t = new TypeToken<Section>() {
		}.getType();
		Gson g = new Gson();
		return g.toJson(get(id), t);
	}

	public static String toJSON(Section section) {
		Type t = new TypeToken<Section>() {
		}.getType();
		Gson g = new Gson();
		return g.toJson(section, t);
	}

	public static String getAllJSON() {
		Type t = new TypeToken<ArrayList<Section>>() {
		}.getType();
		Gson g = new Gson();
		return g.toJson(getAll(), t);
	}

	public static void insert(Section section) {
		ContentValues values = new ContentValues();
		values.put(Keys.id, section.getId());
		values.put(Keys.name, section.getName());
		values.put(Keys.courseId, section.getCourse().getId());
		values.put(Keys.courseNumber, section.getCourse().getCourseNum());
		values.put(Keys.sectionNumber, section.getSectionNumber());
		values.put(Keys.subjectPrefix, section.getCourse().getSubject()
				.getPrefix());
		values.put(Keys.professorId, section.getProfessor().getId());
		values.put(Keys.professorName, section.getProfessor().getName());
		values.put(Keys.days, section.getDays());
		values.put(Keys.location, section.getLocation());
		values.put(Keys.startTime, section.getStartTime());
		values.put(Keys.endTime, section.getEndTime());
		values.put(Keys.verified, section.getIsVerified());
		values.put(Keys.checkedIn, section.getCheckedIn());
		LucidityDatabase.db().insert(tableName, null, values);
	}

	public static void update(Section section) {
		ContentValues values = new ContentValues();
		values.put(Keys.name, section.getName());
		values.put(Keys.courseId, section.getCourse().getId());
		values.put(Keys.courseNumber, section.getCourse().getCourseNum());
		values.put(Keys.sectionNumber, section.getSectionNumber());
		values.put(Keys.subjectPrefix, section.getCourse().getSubject()
				.getPrefix());
		values.put(Keys.professorId, section.getProfessor().getId());
		values.put(Keys.professorName, section.getProfessor().getName());
		values.put(Keys.days, section.getDays());
		values.put(Keys.location, section.getLocation());
		values.put(Keys.startTime, section.getStartTime());
		values.put(Keys.endTime, section.getEndTime());
		values.put(Keys.verified, section.getIsVerified());
		values.put(Keys.checkedIn, section.getCheckedIn());
		LucidityDatabase.db().update(tableName, values, "id = ?",
				new String[] { String.valueOf(section.getId()) });
	}

	private int checkedIn;

	private Course course;

	private String days;

	// public Section(int id, String name, Course course, User professor, String
	// location, String days, String startTime, String endTime) {
	// this(id, name, course, professor, location, days, startTime, endTime,
	// sectionNumber, 0, 0);
	// }

	private String endTime;
	private int isVerified;
	private String location;

	private User professor;

	private String sectionNumber;

	private String startTime;

	public Section(Cursor c) {
		this(c.getInt(c.getColumnIndex(Keys.id)), c.getString(c
				.getColumnIndex(Keys.name)), new Course(c.getInt(c
				.getColumnIndex(Keys.courseId)), c.getInt(c
				.getColumnIndex(Keys.courseNumber))), new User(c.getInt(c
				.getColumnIndex(Keys.professorId)), c.getString(c
				.getColumnIndex(Keys.professorName))), c.getString(c
				.getColumnIndex(Keys.location)), c.getString(c
				.getColumnIndex(Keys.days)), c.getString(c
				.getColumnIndex(Keys.startTime)), c.getString(c
				.getColumnIndex(Keys.endTime)), c.getInt(c
				.getColumnIndex(Keys.verified)), c.getInt(c
				.getColumnIndex(Keys.checkedIn)));
	}

	public Section(int id, String name) {
		this(id, name, new Course(), new User(), "", "", "", "", 0, 0);
	}

	public Section(int id, String name, Course course, User professor,
			String location, String days, String startTime, String endTime,
			int isVerified, int checkedIn) {
		super(id, name);
		this.setCourse(course);
		this.setProfessor(professor);
		this.setLocation(location);
		this.setDays(days);
		this.setStartTime(startTime);
		this.setEndTime(endTime);
		this.setIsVerified(isVerified);
		this.setItemInfo1("Instructor: " + professor.toString());
		this.setItemInfo2("Meets: " + days + " at " + startTime + " - "
				+ endTime);
	}

	public Section(int id, String name, Course course, User professor,
			String location, String days, String startTime, String endTime,
			String sectionNumber, int isVerified, int checkedIn) {
		super(id, name);
		this.course = course;
		this.professor = professor;
		this.location = location;
		this.days = days;
		this.startTime = startTime;
		this.endTime = endTime;
		this.sectionNumber = sectionNumber;
		this.isVerified = isVerified;
		this.checkedIn = checkedIn;
	}

	public int getId() {
		return id;
	}

	public int getCheckedIn() {
		return checkedIn;
	}

	public Course getCourse() {
		return course;
	}

	public String getDays() {
		return days;
	}

	public String getEndTime() {
		return endTime;
	}

	public int getIsVerified() {
		return isVerified;
	}

	public String getLocation() {
		return location;
	}

	public User getProfessor() {
		return professor;
	}

	public String getSectionNumber() {
		return sectionNumber;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setCheckedIn(int checkedIn) {
		this.checkedIn = checkedIn;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setIsVerified(int isVerified) {
		this.isVerified = isVerified;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setProfessor(User professor) {
		this.professor = professor;
	}

	public void setSectionNumber(String sectionNumber) {
		this.sectionNumber = sectionNumber;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	@Override
	public String toString() {
		if (getCourse().getCourseNum() > 0)
			return getCourse().getSubject().getPrefix() + " "
					+ getCourse().getCourseNum() + "-" + getName();
		return getName();
	}

}
