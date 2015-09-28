package com.example.counter;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class CategoriesListViewAdapter extends BaseAdapter {
	private ArrayList<Category> list = new ArrayList<Category>();
	private Context context;

	public CategoriesListViewAdapter(ArrayList<Category> list, Context context) {
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return list.get(position).getId();
	}

	@Override
	public View getView(final int position, View convert_view, final ViewGroup parent)
	{
		View view = convert_view;

		//if convert_view is null we inflate it
		if(view==null)
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.category_list_row,null);
		}

		Category cat = (Category) getItem(position);

		if(cat != null)
		{
			final EditText edittxt = (EditText) view.findViewById(R.id.edittxt_row_category_name);
			TextView txt = (TextView) view.findViewById(R.id.txt_row_category_name);
			Button btn_edit = (Button) view.findViewById(R.id.btn_row_category_edit);
			Button btn_delete = (Button) view.findViewById(R.id.btn_row_category_delete);
			Button btn_reset = (Button) view.findViewById(R.id.btn_row_category_reset);

			edittxt.setText(cat.getName());
			txt.setText(cat.getName());

			//OnClick edit
			btn_edit.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v)
			{
				editName(v,position);
			}
			});

			//OnClick delete 
			btn_delete.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v)
			{
				deleteRow(position);
			}
			});

			//OnClick reset 
			btn_reset.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v)
			{
				resetCategory(position);
			}
			});

			txt.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Debug.log("call on txtview onClickListener: Stablish as selected category");
					selectCategory(position);
				}

			});

			//once is shown we want it to request focus

			edittxt.post(new Runnable() {
				public void run() {
					edittxt.requestFocusFromTouch();
					edittxt.selectAll();
					InputMethodManager lManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE); 
					lManager.showSoftInput(edittxt, 0);
				}
			});	
		}

		return view;
	}

	/** switches between the TextView and the EditText
	 * if EditText is shown also updates the category
	 * @param View v
	 * @param int position
	 */
	public void editName(View v, int position)
	{
		Debug.log("Edit name");
		View row_view = (View) v.getParent();
		EditText edittxt_name = (EditText) row_view.findViewById(R.id.edittxt_row_category_name);
		TextView txt_name = (TextView) row_view.findViewById(R.id.txt_row_category_name);
		Category cat = list.get(position);

		//if not yet editable
		if(!edittxt_name.isShown())
		{
			edittxt_name.setVisibility(View.VISIBLE);
			txt_name.setVisibility(View.GONE);
			v.setBackgroundResource(android.R.drawable.ic_menu_save);
		}else{
			String name = edittxt_name.getText().toString();
			if(name != "")
			{
				edittxt_name.setVisibility(View.GONE);
				txt_name.setVisibility(View.VISIBLE);
				txt_name.setText(name);
				v.setBackgroundResource(android.R.drawable.ic_menu_edit);
				//Todo: save name
				cat.setName(name);
				if(this.context instanceof CategoriesActivity){
					((CategoriesActivity)this.context).updateCategory(cat);
				}
			}
		}

		notifyDataSetChanged();
	}

	/** deletes the category
	 * @param position
	 */
	public void deleteRow(final int position)
	{
		final Context context = this.context;
		final ArrayList<Category> list = this.list;

		//alert dialog
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		// set title
		alertDialogBuilder.setTitle("Delete Category");

		// set dialog message
		alertDialogBuilder
			.setMessage("Delete category?")
			.setCancelable(false)
			.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {

					Debug.log("Delete Row");
					Category cat = list.get(position);
					if(context instanceof CategoriesActivity){
						((CategoriesActivity)context).deleteCategory(cat);
					}

					list.remove(position);
					notifyDataSetChanged();
				}
			})
		.setNegativeButton("No",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing
				dialog.cancel();
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();	

	}

	/** resets the counts for the category selected
	 * @param position
	 */
	public void resetCategory(final int position)
	{
		final Context context = this.context;

		//alert dialog
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		// set title
		alertDialogBuilder.setTitle("Reset Category");

		// set dialog message
		alertDialogBuilder
			.setMessage("This will delete all the counts for the category.\r\nAre you sure?")
			.setCancelable(false)
			.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {

					Debug.log("Reset Category");
					Category cat = list.get(position);
					if(context instanceof CategoriesActivity){
						((CategoriesActivity)context).resetCategory(cat);
					}
					notifyDataSetChanged();
				}
			})
		.setNegativeButton("No",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing
				dialog.cancel();
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();	
		
	}

	/** selects the category as in use
	 * @param position
	 */
	public void selectCategory(final int position)
	{
		final Context context = this.context;

		//alert dialog
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		// set title
		alertDialogBuilder.setTitle("Select Category");

		// set dialog message
		alertDialogBuilder
			.setMessage("Select category?")
			.setCancelable(false)
			.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {

					Debug.log("Select Category");
					Category cat = list.get(position);
					if(context instanceof CategoriesActivity){
						((CategoriesActivity)context).selectCategory(cat);
					}
				}
			})
		.setNegativeButton("No",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing
				dialog.cancel();
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();	

	}

	public View getViewByPosition(int pos, ListView listView) {
		final int firstListItemPosition = listView.getFirstVisiblePosition();
		final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

		if (pos < firstListItemPosition || pos > lastListItemPosition ) {
			return listView.getAdapter().getView(pos, null, listView);
		} else {
			final int childIndex = pos - firstListItemPosition;
			return listView.getChildAt(childIndex);
		}
	}
}
