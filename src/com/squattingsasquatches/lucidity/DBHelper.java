package com.squattingsasquatches.lucidity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.squattingsasquatches.lucidity.objects.Section;
import com.squattingsasquatches.lucidity.objects.University;
import com.squattingsasquatches.lucidity.objects.User;

public class DBHelper extends SQLiteOpenHelper {

	private static final int DB_VERSION = 1;

	public DBHelper(Context ctx) {
		super(ctx, Config.DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i("onCreate", "Making tables!");
		db.execSQL("create table " + User.schema);
		db.execSQL("create table " + Section.schema);
		// db.execSQL(DB_CREATE_GPS_TABLE);
		db.execSQL("create table " + University.schema);
		Log.i("onCreate", "Made tables!");
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		Log.i("onOpen", "Making tables!");
		db.execSQL("create table if not exists " + University.schema);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + User.schema + ";");
		db.execSQL("DROP TABLE IF EXISTS " + Section.schema + ";");
		// db.execSQL("DROP TABLE IF EXISTS " + GPS_TABLE + ";");
		db.execSQL("DROP TABLE IF EXISTS " + University.schema + ";");
		onCreate(db);
	}
}
