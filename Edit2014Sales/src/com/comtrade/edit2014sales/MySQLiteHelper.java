package com.comtrade.edit2014sales;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {
	public static final String TABLE_ARTIKLI = "artikli";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_BARKOD = "barkod";
	public static final String COLUMN_NAZIV = "naziv";
	public static final String COLUMN_CIJENA = "cijena";
	
	private static final String DATABASE_NAME = "artikli.db";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE = "create table "
		      + TABLE_ARTIKLI + "(" + COLUMN_ID
		      + " integer primary key autoincrement, " + COLUMN_BARKOD
		      + " integer, " + COLUMN_NAZIV + " varchar(50), " + COLUMN_CIJENA 
		      + " float);"; 
	
	public MySQLiteHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTIKLI);
	    onCreate(db);
	}

}
