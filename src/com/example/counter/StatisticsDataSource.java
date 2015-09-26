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
	}

	/**
	 * Opens connection and updates the category in use
	 * id_category is set to the current category: getCurrentCategory(), 
	 */
	public void open() throws SQLException{
		this.db = this.db_helper.getWritableDatabase();
		this.id_category = getCategoryInUse();
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
	 * @param rating 
	 */
	public void saveCount(Date date,int id_category, int rating)
	{
		Debug.log("INSERT INTO Counts (date,id_category,rating) VALUES ('"+date.getTime()+"',"+id_category+","+rating+")");
		this.db.execSQL("INSERT INTO Counts (date,id_category,rating) VALUES ('"+date.getTime()+"',"+id_category+","+rating+")");
	}
	/**
	 * Stores a count
	 * @param date when the count was given
	 * @param id_category id of the category which counted
	 */
	public void saveCount(Date date,int id_category)
	{
		saveCount(date,id_category,0);
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
	 * Returns the category selected in the database
	 * */
	public int getCategoryInUse(){
		int result = 0;
		String sql;

		sql = "Select id_category from CategoryInUse";
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
		Debug.log("Cursor to Count");
		Count count = new Count();
		count.setDate(cursor.getLong(0));
		count.setCategoryId(cursor.getInt(1));

		return count;
	}

	private Category cursorToCategory(Cursor cursor) {
		Debug.log("Cursor to Category");
		Category cat = new Category();
		cat.setId(cursor.getInt(0));
		cat.setName(cursor.getString(1));

		return cat;
	}

	/** returns an array containing all categories
	 */
	public ArrayList<Category> getArrayCategories() {

		ArrayList<Category> array_categories = new ArrayList<Category>();

		Cursor cursor = db.rawQuery("Select * from Categories",null);
		Debug.log("Select * from Categories");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Category cat = cursorToCategory(cursor);
			array_categories.add(cat);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return array_categories;
	}

	/** Stores a new category in the database
	 */
	public void createCategory(Category cat)
	{
		Debug.log("INSERT INTO Categories (name) VALUES ('"+cat.getName()+"')");
		this.db.execSQL("INSERT INTO Categories (name) VALUES ('"+cat.getName()+"')");
	}

	/** Updates an existing category
	*/
	public void updateCategory(Category cat) {
		Debug.log("UPDATE Categories SET name = '"+cat.getName()+"' where _id = "+cat.getId());
		this.db.execSQL("UPDATE Categories SET name = '"+cat.getName()+"' where _id = "+cat.getId());
		
	}

	/** Sets the category in use
	 */
	public void setCategoryInUse(Category cat)
	{
		Debug.log("Update CategoryInUse SET id_category = "+cat.getId());
		this.db.execSQL("Update CategoryInUse SET id_category = "+cat.getId());
	}

	/** Deletes an existing category
	*/
	public void deleteCategory(Category cat) {
		Debug.log("DELETE FROM Categories WHERE _id ="+cat.getId());
		this.db.execSQL("DELETE FROM Categories WHERE _id ="+cat.getId());
	}

	/** reset the counts for a category
	*/
	public void resetCategory(Category cat) {
		Debug.log("DELETE FROM Counts WHERE category_id ="+cat.getId());
		this.db.execSQL("DELETE FROM Counts WHERE _id ="+cat.getId());
	}
}
