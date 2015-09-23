/**
 * 
 */
package com.example.counter;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * @author vivo
 *
 */
public class CategoriesActivity extends ListActivity {

	private StatisticsDataSource statistics;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Debug.log("init categories activity");
		statistics = new StatisticsDataSource(this.getApplicationContext());
		setContentView(R.layout.activity_categories);

		ArrayList<Category> arraylist_categories = statistics.getArrayCategories();
		/*
		String array_categories[] = new String[arraylist_categories.size()];

		for(int i=0; i<arraylist_categories.size(); i++)
		{
			Category cat = arraylist_categories.get(i);
			array_categories[i] = cat.getName();
		}
		*/

		CategoriesListViewAdapter adapter = new CategoriesListViewAdapter(arraylist_categories, this);
		setListAdapter(adapter);
	}

	/** saves a new category
	 */
	public void saveCategory(View v)
	{
		Debug.log("Activity runs saveCategory");
		Category cat = new Category();
		TextView txt_category = (TextView) this.findViewById(R.id.txt_category);
		String name = txt_category.getText().toString();
		cat.setName(name);
		statistics.saveCategory(cat);
	}

}
