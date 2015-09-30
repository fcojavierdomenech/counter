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
	 * Returns the name of a category
	 * @param id_category
	 * */
	public String getCategoryName(int id_category){
		String result = "";
		String sql;

		sql = "Select name from Categories where _id="+id_category;
		Debug.log(sql);

		Cursor cursor = db.rawQuery(sql,null);
		DatabaseUtils.dumpCursor(cursor);

		if(cursor.moveToFirst())
			result = cursor.getString(0);
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

	/** returns a list of counts for all categories for a specific date
	 */
	public List<Count> getAllCategoriesCountsByDate(long date) {
		//ToDo
		List<Count> counts = new ArrayList<Count>();

		Cursor cursor = db.rawQuery("Select date,id_category from Counts where id_category = "+this.getCategory(),null);
		System.out.println("Select date,id_category from Counts where id_category = "+this.getCategory());

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

	/** returns a lists of counts grouped per day for all categories
	 */
	public List<GroupCount> getAllCategoriesCountsGroupedByDate() {
		List<GroupCount> counts = new ArrayList<GroupCount>();

		//Cursor cursor = db.rawQuery("select strftime('%d-%m-%Y',  date/1000,'unixepoch') as date, id_category,count(*) AS counts_per_day from Counts group by strftime('%d-%m-%Y', date/1000,'unixepoch'),id_category",null);
		//System.out.println("select strftime('%d-%m-%Y',  date/1000,'unixepoch') as date, id_category,count(*) AS counts_per_day from Counts group by strftime('%d-%m-%Y', date/1000,'unixepoch'),id_category");
		Cursor cursor = db.rawQuery("select date, id_category,count(*) AS counts_per_day from Counts group by strftime('%d-%m-%Y', date/1000,'unixepoch'),id_category",null);
		System.out.println("select date, id_category,count(*) AS counts_per_day from Counts group by strftime('%d-%m-%Y', date/1000,'unixepoch'),id_category");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			GroupCount count = cursorToGroupCount(cursor);
			counts.add(count);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return counts;
	}

	/** returns a list of counts for the current category for a specific date 
	*/
	public List<Count> getCurrentCategoryCountsByDate() {
		//ToDo
		List<Count> counts = new ArrayList<Count>();

		Cursor cursor = db.rawQuery("Select date,id_category from Counts where id_category = "+this.getCategory(),null);
		System.out.println("Select date,id_category from Counts where id_category = "+this.getCategory());

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

	/** returns a lists of counts grouped per day for the current category
	*/
	public List<GroupCount> getCurrentCategoryCountsGroupedByDate() {
		List<GroupCount> counts = new ArrayList<GroupCount>();

		Cursor cursor = db.rawQuery("select strftime('%d-%m-%Y',  date/1000,'unixepoch') as date, id_category,count(*) AS counts_per_day from Counts where id_category = "+this.getCategory()+" group by strftime('%d-%m-%Y', date/1000,'unixepoch')",null);
		System.out.println("select strftime('%d-%m-%Y',  date/1000,'unixepoch') as date, id_category,count(*) AS counts_per_day from Counts where id_category = "+this.getCategory()+" group by strftime('%d-%m-%Y', date/1000,'unixepoch')");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			GroupCount count = cursorToGroupCount(cursor);
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


	private GroupCount cursorToGroupCount(Cursor cursor) {
		Debug.log("Cursor to GroupCount");
		GroupCount count = new GroupCount();
		count.setDate(cursor.getLong(0));
		count.setCategoryId(cursor.getInt(1));
		count.setCounts(cursor.getInt(2));

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
		Debug.log("DELETE FROM Counts WHERE id_category ="+cat.getId());
		this.db.execSQL("DELETE FROM Counts WHERE id_category ="+cat.getId());
	}

	/** select the counts for a category
	*/
	public void selectCategory(Category cat) {
		Debug.log("UPDATE CategoryInUse set id_category ="+cat.getId());
		this.db.execSQL("UPDATE CategoryInUse set id_category ="+cat.getId());
	}
}
