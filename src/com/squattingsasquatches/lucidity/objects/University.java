package com.squattingsasquatches.lucidity.objects;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squattingsasquatches.lucidity.LucidityDatabase;
import com.squattingsasquatches.lucidity.UniversityXMLHandler;

public class University extends DataItem {

	private int manualFlag = 0;
	private String serverAddress;
	private int serverPort = 80;

	public University() {
		this(-1);
	}

	public University(int id) {
		this(id, "");
	}

	public University(int id, String name) {
		super(id, name);
	}

	public University(int id, String name, int manualFlag) {
		super(id, name);
		this.manualFlag = manualFlag;
	}

	public University(String id, String name, String serverAddress,
			String serverPort, int manualFlag) {
		super();
		this.id = Integer.parseInt(id);
		this.name = name;
		this.serverAddress = serverAddress;
		this.serverPort = Integer.parseInt(serverPort);
		this.manualFlag = manualFlag;
	}

	public University(Cursor c) {
		super(c.getInt(c.getColumnIndex(Keys.id)), c.getString(c
				.getColumnIndex(Keys.name)));
		this.manualFlag = c.getInt(c.getColumnIndex(Keys.manualFlag));
		this.serverAddress = c.getString(c.getColumnIndex(Keys.serverAddress));
		this.serverPort = c.getInt(c.getColumnIndex(Keys.serverPort));

	}

	public University(int id, String name, String serverAddress,
			int serverPort, int manualFlag) {

		this.id = id;
		this.name = name;
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		this.manualFlag = manualFlag;
	}

	public int getManualFlag() {
		return this.manualFlag;
	}

	public void setManualFlag(int flag) {
		this.manualFlag = flag;
	}

	// Loads default packaged university server listing. Used when server
	// listing is empty.
	public static ArrayList<University> loadDefault(Context context)
			throws SAXException, ParserConfigurationException, IOException {

		Log.i("loadDefault()", "Loading default universities...");

		InputSource is = new InputSource(context.getAssets().open(
				"universities.xml"));

		// create the factory
		SAXParserFactory factory = SAXParserFactory.newInstance();

		// create a parser
		SAXParser parser = factory.newSAXParser();

		// create the reader (scanner)
		XMLReader xmlreader = parser.getXMLReader();

		final UniversityXMLHandler UXMLHandler = new UniversityXMLHandler();

		xmlreader.setContentHandler(UXMLHandler);

		xmlreader.parse(is);

		Log.i("loadDefault()", "Done parsing.");

		new Thread(new Runnable() {
			public void run() {
				// Transaction required since user can kill app during this
				// insertion process.
				LucidityDatabase.db().beginTransaction();
				University.insert((ArrayList<University>) UXMLHandler
						.getUniversities().clone());
				LucidityDatabase.db().setTransactionSuccessful();
				LucidityDatabase.db().endTransaction();
			}
		}).start();

		Log.i("loadDefault()", "Done.");
		return UXMLHandler.getUniversities();
	}

	public static boolean isEmpty() {
		return getAll().isEmpty();
	}

	public static University get(int id) {
		Cursor result = LucidityDatabase.db().query(tableName, null,
				Keys.id + " = ?", new String[] { String.valueOf(id) }, null,
				null, null);

		if (!result.moveToFirst()) {
			result.close();
			return null;
		}

		University u = new University(result);
		result.close();
		return u;
	}

	public static ArrayList<University> getAll() {
		ArrayList<University> universities = new ArrayList<University>();
		Cursor result = LucidityDatabase.db().query(tableName, null, null,
				null, null, null, null);

		if (!result.moveToFirst()) {
			result.close();
			return universities;
		}

		while (!result.isAfterLast()) {
			universities.add(new University(result));
			result.moveToNext();
		}
		result.close();
		return universities;
	}

	public static void update(University university) {
		ContentValues values = new ContentValues();
		values.put(Keys.name, university.getName());
		values.put(Keys.manualFlag, university.getManualFlag());
		values.put(Keys.serverAddress, university.getServerAddress());
		values.put(Keys.serverPort, university.getServerPort());
		LucidityDatabase.db().update(tableName, values, "id = ?",
				new String[] { String.valueOf(university.getId()) });
	}

	public static void insert(University university) {

		ContentValues values = new ContentValues();
		values.put(Keys.id, university.getId());
		values.put(Keys.name, university.getName());
		values.put(Keys.manualFlag, university.getManualFlag());
		values.put(Keys.serverAddress, university.getServerAddress());
		values.put(Keys.serverPort, university.getServerPort());
		LucidityDatabase.db().insert(tableName, null, values);
	}

	public static void insert(ArrayList<University> universities) {
		for (University u : universities) {
			insert(u);
		}
	}

	public static void delete(int id) {
		LucidityDatabase.db().delete(tableName, "id = ?",
				new String[] { Keys.id });
	}

	public static String toJSON(int id) {
		Type t = new TypeToken<University>() {
		}.getType();
		Gson g = new Gson();
		return g.toJson(get(id), t);
	}

	public static String toJSON(University university) {
		Type t = new TypeToken<University>() {
		}.getType();
		Gson g = new Gson();
		return g.toJson(university, t);
	}

	public static String getAllJSON() {
		Type t = new TypeToken<ArrayList<University>>() {
		}.getType();
		Gson g = new Gson();
		return g.toJson(getAll(), t);
	}

	public static final class Keys {
		public static final String id = "id";
		public static final String name = "name";
		public static final String manualFlag = "manualFlag";
		public static final String serverAddress = "serverAddress";
		public static final String serverPort = "serverPort";
	}

	public static final String tableName = "universities";

	public static final String schema = tableName + " (" + Keys.id
			+ " INTEGER not null, " + Keys.name + " TEXT not null, "
			+ Keys.manualFlag + " INTEGER not null, " + Keys.serverAddress
			+ " TEXT not null, " + Keys.serverPort + " INTEGER not null ); ";

	public static String getTablename() {
		return tableName;
	}

	public static String getSchema() {
		return schema;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

}
