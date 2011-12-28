package com.squattingsasquatches.lucidity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LucidityDatabase {

	public static SQLiteDatabase db;
	private SQLiteOpenHelper dbHelper;

	public LucidityDatabase(Context context) {
		dbHelper = new DBHelper(context);
		db = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
}
