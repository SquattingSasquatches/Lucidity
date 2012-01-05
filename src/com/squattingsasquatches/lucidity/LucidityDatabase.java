package com.squattingsasquatches.lucidity;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.squattingsasquatches.lucidity.objects.Section;
import com.squattingsasquatches.lucidity.objects.University;
import com.squattingsasquatches.lucidity.objects.User;

public class LucidityDatabase extends SQLiteOpenHelper {
	private static final int DB_VERSION = 1;

	private static LucidityDatabase instance;
	private static SQLiteDatabase db;
	private static Context context;

	public LucidityDatabase(Context ctx) {
		super(ctx, Config.DB_NAME, null, DB_VERSION);
		LucidityDatabase.context = ctx;

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + User.schema);
		db.execSQL("create table " + Section.schema);
		db.execSQL("create table " + University.schema);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		db.execSQL("create table if not exists " + University.schema);
		db.execSQL("create table if not exists " + User.schema);
		db.execSQL("create table if not exists " + Section.schema);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + User.tableName + ";");
		db.execSQL("DROP TABLE IF EXISTS " + Section.tableName + ";");
		db.execSQL("DROP TABLE IF EXISTS " + University.tableName + ";");
		onCreate(db);
	}

	// public static SQLiteDatabase db;
	// private static SQLiteOpenHelper dbHelper;

	// public LucidityDatabase(Context context) {
	// dbHelper = new DBHelper(context);
	// db = dbHelper.getWritableDatabase();
	// // db.setLockingEnabled(true);
	// }

	// public static SQLiteDatabase db() {
	// return dbHelper.getWritableDatabase();
	// }

	// public static void close() {
	// // dbHelper.close();
	// }
	@Override
	public synchronized void close() {
		if (instance != null)
			db.close();
	}

	public static synchronized LucidityDatabase getInstance(Context context) {
		if (instance == null) {
			instance = new LucidityDatabase(context);
			db = instance.getWritableDatabase();
		}

		return instance;
	}

	public static synchronized SQLiteDatabase db() throws SQLException {
		if (instance == null) {
			instance = new LucidityDatabase(context);
		}
		return instance.getWritableDatabase();
	}
}
