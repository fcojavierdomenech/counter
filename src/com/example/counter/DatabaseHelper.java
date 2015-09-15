package com.example.counter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Fco.Javier Domenech
 *
 * This class will create and handle the database where will be stored all the data related to the counts done.
 *
 */
@SuppressLint("NewApi")
public class DatabaseHelper extends SQLiteOpenHelper{

	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public DatabaseHelper(Context context) {
		super(context, "statistics", null, 1);
	}

	/* Creates the database if doesn't exist
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		//Create table Categories
		db.execSQL(
				"Create table Categories ("+
				"_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
				"name VARCHAR(20),"+
				"in_use INTEGER DEFAULT 0"+
				")"
			  );
		
		//Create table Counts 
		db.execSQL(
				"Create table Counts ("+
				"_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
				"date DATE,"+
				"id_category INTEGER,"+
			        "FOREIGN KEY (id_category) REFERENCES Categories(_id) "+
				"ON DELETE CASCADE"+
				")"
			  );

		db.execSQL(
				"INSERT INTO Categories (name,in_use) VALUES ('default',1)"
			  );

	}

	/* Modifies the database if it's already installed the app on a previous version
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
	}
}

