package com.squattingsasquatches.lucidity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
		this.onCreate(db);
	}

}
