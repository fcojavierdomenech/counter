package com.example.counter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class StatisticsDataSource {
	private DatabaseHelper db_helper;
	private SQLiteDatabase db;
	//keeps the current category, last selected
	private int id_category;

	/**
	 * Initialices the dbHelper
	 */
	public StatisticsDataSource(Context context) {
		this.db_helper = new DatabaseHelper(context);
		this.open();
		//by default current category is 1, pointing to the default category
		this.id_category = getCategoryInUse();
		//fixme: id_category should be set to the current category: getCurrentCategory(), 
		//which returns the current category in use
	}

	/**
	 * Opens connection
	 */
	public void open() throws SQLException{
		this.db = this.db_helper.getWritableDatabase();
	}

	/**
	 * closes connection
	 */
	public void close() throws SQLException{
		this.db.close();
	}

	/**
	 * Changes the category id to a new one
	 */
	public void changeCategory(int id_category) {
		this.id_category = id_category;
	}

	/**
	 * Stores a count
	 * @param date when the count was given
	 * @param id_category id of the category which counted
	 */
	public void saveCount(Date date,int id_category)
	{
		Debug.log("INSERT INTO Counts (date,id_category) VALUES ('"+date.getTime()+"',"+id_category+")");
		this.db.execSQL("INSERT INTO Counts (date,id_category) VALUES ('"+date.getTime()+"',"+id_category+")");
	}

	/** 
	 * Lists all the counts for a specified category 
	 * @param category
	 * */
	public Vector<String> listCounts(int id_category){
		Vector<String> result = new Vector<String>();

		Cursor cursor = db.rawQuery("Select date from Counts where _id = "+id_category,null);
		System.out.println(
				"Select date from Counts where _id = "+id_category
				);

		result.add(cursor.getString(0));
		cursor.close();

		return result;
	}

	/** 
	 * Lists all the counts for a specified category 
	 * @param category
	 * */
	public int getTotalCounts(){
		int result = 0;
		String sql;

		sql = "Select count(*) from Counts where id_category = "+this.id_category;
		Debug.log(sql);

		Cursor cursor = db.rawQuery(sql,null);
		Debug.log(cursor);

		if(cursor.moveToFirst())
			result = cursor.getInt(0);
		cursor.close();

		return result;
	}

	/** 
	 * Returns the category marked as 'in_use' in the database
	 * */
	public int getCategoryInUse(){
		int result = 0;
		String sql;

		sql = "Select _id from Categories where in_use = 1";
		Debug.log(sql);

		Cursor cursor = db.rawQuery(sql,null);
		DatabaseUtils.dumpCursor(cursor);

		if(cursor.moveToFirst())
			result = cursor.getInt(0);
		cursor.close();

		return result;
	}


	/** 
	 * Lists all the counts for the current category
	 * */
	public Vector<String> listCounts(){
		return listCounts(this.id_category);
	}

	/** 
	 * Returns the current category in use
	 * */
	public int getCategory(){
		return this.id_category;
	}

	public List<Count> getAllCounts() {
		List<Count> counts = new ArrayList<Count>();

		Cursor cursor = db.rawQuery("Select date from Counts where _id = "+this.getCategory(),null);
		System.out.println("Select date from Counts where _id = "+this.getCategory());

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Count count = cursorToCount(cursor);
			counts.add(count);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return counts;
	}

	private Count cursorToCount(Cursor cursor) {
		Count count = new Count();
		count.setDate(cursor.getLong(0));
		count.setCategoryId(cursor.getInt(1));

		return count;
	}

}
