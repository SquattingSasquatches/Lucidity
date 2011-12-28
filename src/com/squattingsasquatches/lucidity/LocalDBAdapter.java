package com.squattingsasquatches.lucidity;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LocalDBAdapter {

	private Context ctx;
	private SQLiteDatabase db;

	private DBHelper dbHelper;

	public LocalDBAdapter(Context ctx) {
		this.ctx = ctx;
	}

	public void close() {
		dbHelper.close();
	}

	public LocalDBAdapter open() throws SQLException {
		Log.i("open()", "opening SQLDatabase...");
		dbHelper = new DBHelper(ctx);
		db = dbHelper.getWritableDatabase();
		return this;
	}

}