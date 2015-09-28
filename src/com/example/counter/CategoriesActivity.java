/**
 * 
 */
package com.example.counter;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author vivo
 *
 */
public class CategoriesActivity extends Activity {

	private StatisticsDataSource statistics;
	private ArrayList<Category> arraylist_categories;
	private CategoriesListViewAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Debug.log("init categories activity");
		setContentView(R.layout.activity_categories);
		statistics = new StatisticsDataSource(this.getApplicationContext());

		arraylist_categories = statistics.getArrayCategories();

		adapter = new CategoriesListViewAdapter(arraylist_categories, this);

		ListView list_view = (ListView) this.findViewById(android.R.id.list);
		list_view.setAdapter(adapter);

		//Scrolling on top
		scrollMyListViewToPos(0);

		//on top of listview
		//ListView list_view = (ListView) this.findViewById(android.R.id.list);
		//ListView list_view = getListView();
		list_view.setSelectionAfterHeaderView();

		//final ListView list_view = (ListView) this.findViewById(android.R.id.list);
		//list_view.setSelectionAfterHeaderView();;
		//list_view.smoothScrollToPosition(0);
	}

	/*
	@Override
	protected void onListItemClick(ListView list_view, View view, int position, long id)
	{
		super.onListItemClick(list_view, view, position, id);
		//Object o = getListAdapter().getItem(position);
		Debug.log("onListItemClick");
		view.setSelected(true);
	}
	*/

	/** saves a new category
	 */
	public void createCategory(View v)
	{
		Debug.log("Activity runs updateCategory");
		Category cat = new Category();
		TextView txt_category = (TextView) this.findViewById(R.id.txt_category);
		String name = txt_category.getText().toString();
		if(name != "")
		{
			cat.setName(name);
			statistics.createCategory(cat);
			
			//added dinamically to the listView
			arraylist_categories.add(cat);
			adapter.notifyDataSetChanged();

			int pos = adapter.getCount() - 1;
			scrollMyListViewToPos(pos);
		}
	}


	/** updates an existing category
	 * @param cat Category
	*/
	public void updateCategory(Category cat)
	{
		Debug.log("CategoriesActivity runs updateCategory");
		statistics.updateCategory(cat);
	}


	/** resets category count
	 * this means all counts of this category will be reset
	 * @param cat Category
	*/
	public void resetCategory(Category cat)
	{
		Debug.log("CategoriesActivity runs resetCategory");
		statistics.resetCategory(cat);
	}

	/** resets category count
	 * this means all counts of this category will be reset
	 * it ends the current activity
	 * @param cat Category
	 */
	public void selectCategory(Category cat)
	{
		Debug.log("CategoriesActivity runs selectCategory");
		statistics.selectCategory(cat);
		finish();
	}

	/** deletes an existing category
	 * @param cat Category
	*/
	public void deleteCategory(Category cat)
	{
		Debug.log("CategoriesActivity runs deleteCategory");
		statistics.deleteCategory(cat);
	}

	/** Scrolls the list view to the position given
	 */
	private void scrollMyListViewToPos(final int pos) {
		Debug.log("Scrolling to position "+pos);
		final ListView list_view = (ListView) this.findViewById(android.R.id.list);
		list_view.post(new Runnable() {
			@Override
			public void run() {
				// Select the last row so it will scroll into view...
				//list_view.setSelection(pos);
				list_view.smoothScrollToPosition(pos);
			}
		});
	}
}
