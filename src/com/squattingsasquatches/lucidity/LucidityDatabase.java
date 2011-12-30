package com.squattingsasquatches.lucidity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LucidityDatabase {

	public static SQLiteDatabase db;
	private static SQLiteOpenHelper dbHelper;

	public LucidityDatabase(Context context) {
		dbHelper = new DBHelper(context);
		db = dbHelper.getWritableDatabase();
		db.setLockingEnabled(true);
	}

	public static SQLiteDatabase db() {
		return dbHelper.getWritableDatabase();
	}

	public static void close() {
		dbHelper.close();
	}
}
